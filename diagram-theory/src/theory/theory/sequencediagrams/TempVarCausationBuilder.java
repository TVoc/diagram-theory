package theory.theory.sequencediagrams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import data.sequencediagrams.DiagramInfo;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import parser.XMLParser;
import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;

public class TempVarCausationBuilder
{
	public static final Pattern GETTER_PARAMETER = Pattern.compile("\\((.*?)\\)");
	public static final String ARGLIST_SEPARATOR = "(\\s)*\\,(\\s)*";
	
	public TempVarCausationBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private final StringBuilder stringBuilder;

	private int getTabLevel()
	{
		return this.tabLevel;
	}

	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public TempVarCausationBuilder handleMessage(Message message, SeqDiagramStore store)
	{
		if (message.getContent().contains("="))
		{
			return this.handleAssignment(message, store);
		}
		else if (message.getContent().split("\\(")[0].contains("get"))
		{
			TempVar assigned = store.resolveTempVar(store.getMessage(message.getSDPoint().getSequenceNumber()).getContent());
			
			return this.handleGetStatement(message, false, store, assigned, message.getContent());
		}
		else if (store.isCallPoint(message))
		{
			this.makeCallObjectSentence(message, store);
			return this.processCallArguments(message, store);
		}
		else // return message
		{
			return this;
		}
	}
	
	private TempVarCausationBuilder handleAssignment(Message message, SeqDiagramStore store)
	{
		String[] parts = message.getContent().split("=");
		
		StringBuilder merge = new StringBuilder();
		
		for (int i = 1; i < parts.length; i++)
		{
			merge.append(parts[i]);
		}
		
		TempVar assigned = store.resolveTempVar(parts[0]);
		String rhs = merge.toString();
		
		if (rhs.contains("get"))
		{
			return handleGetStatement(message, false, store, assigned, rhs); // TODO assess effect of no longer making assignments immediate
		}
		else if (rhs.contains("flipBool"))
		{
			return handleFlipBool(message, store, assigned, rhs);
		}
		else if (store.isCallPoint(message))
		{
			this.makeCallObjectSentence(message, store);
			this.processCallArguments(message, store);
			return this.handleReturnAssignment(message, store);
		}
		else if (rhs.contains("randomInt"))
		{
			return handleRandomInt(message, store, assigned, rhs);
		}
		else // of type "one + two"
		{
			return handleArithStatement(message, store, assigned, rhs);
		}
	}

	private TempVarCausationBuilder handleGetStatement(Message message, boolean immediate, SeqDiagramStore store, TempVar assigned,
			String rhs)
	{
		// TODO accommodate e.g. getNumHeaps
		if (rhs.contains("getNum"))
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
					this.makeGetSizeAxiom(message, store, assigned, rhs), this.getTabLevel()));
			return this;
		}
		
		String[] attrSpec = rhs.replaceAll("get","").split("By");
		String toGetClassName = attrSpec[0].split("\\(")[0];
		TempVar getFromT = message.getTo(store);
		Class getFrom = store.getClassByName(message.getTo(store).getType().getTypeName(store));
		
		Optional<DataUnit> maybeAttr = getFrom.getAttributeByName(StringUtils.uncapitalize(toGetClassName));
		String causesTime = immediate ? "t" : "Next(t)";
		
		if (maybeAttr.isPresent())
		{
			String toAppend = "! t [Time] st [StackLevel] x [" + OutputConvenienceFunctions.toIDPType(maybeAttr.get().getType(), store)
				+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(" + causesTime + ", st, x) <- (CurrentStackLevel(t) = st) & SDPointAt(t, " + message.getSDPoint()
				+ ") & (? o [" + getFrom.getName() + "] : " + OutputConvenienceFunctions.singleTempVarPredicateName(getFromT)
				+ "(t, CurrentStackLevel(t), o) & " + getFrom.getName() + maybeAttr.get().getName() + "(t, o, x)).";
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
			
			return this;
		}
		
		Association assoc = null;
		for (Association ele : store.getAssociations())
		{
			List<String> asList = Arrays.asList(getFrom.getName(), toGetClassName);
			
			if (ele.containsAll(store, asList))
			{
				assoc = ele;
				
				if (assoc.getNbOfEnds() == 2 && assoc.isCollection(toGetClassName, store).orElse(false))
				{
					String index = this.findGetterValue(rhs);
					
					this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
							this.makeListGetAxiom(message, store, assigned, toGetClassName, index), this.getTabLevel()));
					return this;
				}
			}
		}
		
		String predicateName = VocabularyAssociationBuilder.makePredicateName(assoc, store);
		int fromPos = assoc.positionOf(store, getFrom.getName());
		int toPos = assoc.positionOf(store, toGetClassName);
		
		boolean by = attrSpec.length > 1;
		String makeGetterPredicate = this.makeGetterPredicate(assigned, getFromT, toGetClassName, predicateName, fromPos, toPos, by, store);
		
		if (by)
		{
			String classVarName = StringUtils.uncapitalize(attrSpec[1].split("\\(")[0]);
			String getterValue = this.findGetterValue(attrSpec[1]);
			makeGetterPredicate = makeGetterPredicate + toGetClassName + classVarName + "(t, " + assigned.getName() + ", " + getterValue + ")).";
		}		
		
		String toAppend = " ! t [Time] st [StackLevel] " + assigned.getName() + " [" + OutputConvenienceFunctions.toIDPType(assigned.getType(), store)
		+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(" + causesTime + ", st, " + assigned.getName() + ") <- (CurrentStacklevel(t) = st) & SDPointAt(t, "
		+ message.getSDPoint() + ") & " + makeGetterPredicate;
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
		return this;
	}
	
	private String makeGetSizeAxiom(Message message, SeqDiagramStore store, TempVar assigned, String rhs)
	{
		String toGetTypeName = rhs.replaceAll("getNum|\\(\\)", "");
		toGetTypeName = toGetTypeName.substring(0, toGetTypeName.length() - 1);
		
		TempVar getFrom = message.getTo(store);
		
		Association assoc = null;
		for (Association ele : store.getAssociations())
		{
			List<String> asList = Arrays.asList(getFrom.getName(), toGetTypeName);
			
			if (ele.containsAll(store, asList))
			{
				assoc = ele;
			}
		}
		
		return 	"! t [Time] st [StackLevel] a [LimitedInt] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned)
				+ "(Next(t), st, a) <- (CurrentStacklevel(t) = st) & SDPointAt(t, " + message.getSDPoint() + ") & #{ i [LimitedInt] : ? e ["
				+ toGetTypeName + "] : " + VocabularyAssociationBuilder.getListGetterPredicate(getFrom.getType().getTypeName(store), toGetTypeName)
				+ "(" + getFrom.getName() + ", i) = e} = a.";
	}
	
	private String makeListGetAxiom(Message message, SeqDiagramStore store, TempVar assigned, String toGetClassName, String index)
	{
		String assignedType = OutputConvenienceFunctions.toIDPType(assigned.getType(), store);
		String getFromType = OutputConvenienceFunctions.toIDPType(message.getTo(store).getType(), store);
		String predicateName = OutputConvenienceFunctions.singleTempVarPredicateName(assigned);
		String fromPredicateName = OutputConvenienceFunctions.singleTempVarPredicateName(message.getTo(store));
		
		if (OutputConvenienceFunctions.representsInteger(index))
		{
			return "! t [Time] st [StackLevel] e [" + assignedType + "] : C_" + predicateName + "(Next(t), st, e) <- (CurrentStackLevel(t) = st) & SDPointAt(t, " + message.getSDPoint()
			+ ") & ( ? o [" + getFromType + "] : " + fromPredicateName + "(t, o) & " 
			+ VocabularyAssociationBuilder.getListGetterPredicate(getFromType, assignedType) + "(o, " + index + ") = e).";
		}
		else
		{
			TempVar indexVar = store.resolveTempVar(index);
			String indexPredicate = OutputConvenienceFunctions.singleTempVarPredicateName(indexVar);
			
			return "! t [Time] st [StackLevel] e [" + assignedType + "] : C_" + predicateName + "(Next(t), st, e) <- (CurrentStackLevel(t) = st) & SDPointAt(t, " + message.getSDPoint()
			+ ") & ( ? o [" + getFromType + "] i [LimitedInt] : " + fromPredicateName + "(t, o) & "
			+ indexPredicate + "(t, i) & "
			+ VocabularyAssociationBuilder.getListGetterPredicate(getFromType, assignedType) + "(o, i) = e).";
		}
	}
	
	private TempVarCausationBuilder handleRandomInt(Message message, SeqDiagramStore store, TempVar assigned, String rhs)
	{
		String[] bounds = this.getRandomBounds(rhs);
		String lb = OutputConvenienceFunctions.representsInteger(bounds[0]) ? bounds[0] : "lb";
		String ub = OutputConvenienceFunctions.representsInteger(bounds[1]) ? bounds[1] : "ub";
		
		String predicateName = OutputConvenienceFunctions.singleTempVarPredicateName(assigned);
		
		StringBuilder toReturn = new StringBuilder("! t [Time] st [StackLevel] r [LimitedInt] : C_" + predicateName + "(Next(t), st, r) <- (CurrentStackLevel(t) = st) & SDPointAt(t, " + message.getSDPoint()
				+ ") & ");
		StringBuilder quantifiers = new StringBuilder();
		String expression = "r = abs(RandomInt(t)) % (" + ub + " + 1 - " + lb + ") + " + lb;
		
		boolean hasVar = "lb".equals(lb) || "ub".equals(ub);
		
		if (hasVar)
		{
			quantifiers.append("( ? ");
			
			if ("lb".equals(lb))
			{
				quantifiers.append("lb [LimitedInt] ");
			}
			if ("ub".equals(ub))
			{
				quantifiers.append("ub [LimitedInt] ");
			}
			
			quantifiers.append(": ");
			
			if ("lb".equals(lb))
			{
				quantifiers.append(OutputConvenienceFunctions.singleTempVarPredicateName(store.resolveTempVar(bounds[0]))
						+ "(t, lb)");
			}
			if ("ub".equals(ub))
			{
				String toAppend = OutputConvenienceFunctions.singleTempVarPredicateName(store.resolveTempVar(bounds[1]))
						+ "(t, ub)";
				quantifiers.append(("lb".equals(lb) ? " & " : "") + toAppend);
			}
		}
		
		toReturn.append(quantifiers.toString()).append(expression).append(hasVar ? ")." : ".");
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toReturn.toString(), this.getTabLevel()));
		
		return this;
	}
	
	private TempVarCausationBuilder handleFlipBool(Message message, SeqDiagramStore store, TempVar assigned, String rhs)
	{
		String predicateName = OutputConvenienceFunctions.singleTempVarPredicateName(assigned);
		String getPredicateName = OutputConvenienceFunctions.singleTempVarPredicateName(
				store.resolveTempVar(this.findGetterValue(rhs)));
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"! t [Time] st [StackLevel] b [boolean] : C_" + predicateName + "(Next(t), st, b) <- (CurrentStackLevel(t) = st) & SDPointAt(t, " + message.getSDPoint()
				+ ") & (? b1 [boolean] : " + getPredicateName + "(t, b1) & b = flipBool(b1)).", this.getTabLevel()));
		
		return this;
	}
	
	private String[] getRandomBounds(String rhs)
	{
		Matcher m = GETTER_PARAMETER.matcher(rhs);
		m.find();
		return m.group(1).split(ARGLIST_SEPARATOR);
	}
	
	private String makeGetterPredicate(TempVar assigned, TempVar getFrom, String toGetClassName, String predicateName,
				int fromPos, int toPos, boolean by, SeqDiagramStore store)
	{
		StringBuilder toReturn = new StringBuilder("(? " + getFrom.getName() + " [" + getFrom.getType().getTypeName(store) + "] : "
				+ OutputConvenienceFunctions.singleTempVarPredicateName(getFrom) + "(t, st, " + getFrom.getName() + ") & ");
		String byAdd = by ? " & " : ").";
		
		if (fromPos == toPos)
		{
			toReturn.append("(" + predicateName + "(" + assigned.getName() + ", " + getFrom.getName() + ") | " + predicateName + "(" + getFrom.getName()
				+ ", " + assigned.getName() + "))" + byAdd);
		}
		else if (fromPos < toPos)
		{
			toReturn.append(predicateName + "(" + getFrom.getName() + ", " + assigned.getName() + ")" + byAdd);
		}
		else // fromPos > toPos
		{
			 toReturn.append(predicateName + "(" + assigned.getName() + ", " + getFrom.getName() + ")" + byAdd);
		}
		
		return toReturn.toString();
	}
	
	private String findGetterValue(String getterString)
	{
		Matcher m = GETTER_PARAMETER.matcher(getterString);
		m.find();
		return m.group(1);
	}
	
	private TempVarCausationBuilder handleArithStatement(Message message, SeqDiagramStore store, TempVar assigned, String rhs)
	{
		String[] parts = rhs.split(XMLParser.TEMPVAR_SEPARATOR);
		
		StringBuilder quantifiers = new StringBuilder("");
		StringBuilder assertion = new StringBuilder("");
		
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i].equals(""))
			{
				continue;
			}
			
			if (store.hasTempVar(parts[i]))
			{
				TempVar var = store.resolveTempVar(parts[i]);
				
				if ("".equals(quantifiers.toString()))
				{
					quantifiers.append("(? " + var.getName() + " [" + OutputConvenienceFunctions.toIDPType(var.getType(), store) + "] ");
				}
				else
				{
					quantifiers.append(var.getName() + " [" + OutputConvenienceFunctions.toIDPType(var.getType(), store) + "] ");
				}
				if ("".equals(assertion.toString()))
				{
					assertion.append(": " + OutputConvenienceFunctions.singleTempVarPredicateName(var) + "(t, " + var.getName() + ")"
							+ " & ");
				}
				else
				{
					assertion.append(OutputConvenienceFunctions.singleTempVarPredicateName(var) + "(t, " + var.getName() + ")"
							+ " & ");
				}
			}
		}
		
		if (! "".equals(assertion.toString()))
		{
			assertion.append(message.getContent() + ").");
		}
		
		String toAppend = ("".equals(quantifiers.toString()) && "".equals(assertion.toString())) ?
			"! t [Time] st [StackLevel] " + assigned.getName() + " [" + OutputConvenienceFunctions.toIDPType(assigned.getType(), store)
			+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(Next(t), st, " + assigned.getName() + ") <- (CurrentStackLevel(t) = st) & SDPointAt(t, "
			+ message.getSDPoint() + ")."
			:
			"! t [Time] st [StackLevel] " + assigned.getName() + " [" + OutputConvenienceFunctions.toIDPType(assigned.getType(), store)
			+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(Next(t), st, " + assigned.getName() + ") <- (CurrentStackLevel(t) = st) & SDPointAt(t, "
			+ message.getSDPoint() + ") & " + quantifiers.toString() + assertion.toString();
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
		
		return this;
	}
	
	private TempVarCausationBuilder makeCallObjectSentence(Message message, SeqDiagramStore store)
	{
		TempVar messageCallVar = message.getTo(store);
		DiagramInfo diagram = store.getDiagramCallInfo(message);
		TempVar callObject = diagram.getCallObject();
		String callObjectType = OutputConvenienceFunctions.toIDPType(callObject.getType(), store);
		String callObjectTypePred = "C_" + OutputConvenienceFunctions.singleTempVarPredicateName(callObject);
		String messageCallVarPred = OutputConvenienceFunctions.singleTempVarPredicateName(messageCallVar);
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] st [StackLevel] c ["
				+ callObjectType + "] : " + callObjectTypePred + "(Next(t), st, c) <- (CurrentStackLevel(t) = (s-1)) & SDPointAt(t, "
				+ message.getSDPoint() + ") & " + messageCallVarPred + "(t, (s-1), c).", this.getTabLevel()));
		
		return this;
	}
	
	private TempVarCausationBuilder processCallArguments(Message message, SeqDiagramStore store)
	{
		Matcher m = GETTER_PARAMETER.matcher(message.getContent());
		m.find();
		
		if (m.group(1).isEmpty())
		{
			return this;
		}
		
		String[] argStrings = m.group(1).split(ARGLIST_SEPARATOR);
		DiagramInfo diagramInfo = store.getDiagramCallInfo(message);
		List<TempVar> diagramParameters = diagramInfo.getParameters().get();
		
		for (int i = 0; i < argStrings.length; i++)
		{
			String argString = argStrings[i];
			TempVar diagramParameter = diagramParameters.get(i);
			String diagramParameterPred = "C_" + OutputConvenienceFunctions.singleTempVarPredicateName(diagramParameter);
			
			if (store.hasTempVar(argString))
			{
				TempVar argVar = store.resolveTempVar(argString);
				String argVarPred = OutputConvenienceFunctions.singleTempVarPredicateName(argVar);
				String diagramParameterType = OutputConvenienceFunctions.toIDPType(diagramParameter.getType(), store);
				
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] st [StackLevel] p ["
						+ diagramParameterType + "] : " + diagramParameterPred + "(Next(t), st, p) <- (CurrentStackLevel(t) = (st-1)) & SDPointAt(t, "
						+ message.getSDPoint() + ") & " + argVarPred + "(t, (s-1), p).", this.getTabLevel()));
			}
			else
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] st [StackLevel] : "
						+ diagramParameterPred + "(Next(t), st, " + argString + ") <- (CurrentStackLevel(t) = (st-1)) & SDPointAt(t, "
						+ message.getSDPoint() + ").", this.getTabLevel()));
			}
		}
		
		return this;
	}
	
	private TempVarCausationBuilder handleReturnAssignment(Message message, SeqDiagramStore store)
	{
		String tempVarName = message.getContent().split("=")[0].replaceAll("\\s", "");
		TempVar assignedVar = store.resolveTempVar(tempVarName);
		String assignedType = OutputConvenienceFunctions.toIDPType(assignedVar.getType(), store);
		String assignedPred = "C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assignedVar);
		DiagramInfo diagramInfo = store.getDiagramCallInfo(message);
		Message lastMessage = store.getLastMessageForDiagram(diagramInfo.getName());
		TempVar returnValue = store.resolveTempVar(lastMessage.getContent());
		String returnValuePred = OutputConvenienceFunctions.singleTempVarPredicateName(returnValue);
		String SDPost = message.getSDPoint() + "post";
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] st [StackLevel] v ["
				+ assignedType + "] : " + assignedPred + "(Next(t), st, v) <- (CurrentStackLevel(t) = st & SDPointAt(t, "
				+ SDPost + ") & " + returnValuePred + "(t, (st+1), v).", this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
