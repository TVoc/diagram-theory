package theory.theory.sequencediagrams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		else // either a return message or a procedure call TODO handle procedure call
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
			return handleGetStatement(message, true, store, assigned, rhs);
		}
		else if (store.isCallPoint(message))
		{
			this.makeCallObjectSentence(message, store);
			this.processCallArguments(message, store);
			return this.handleReturnAssignment(message, store);
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
		String[] attrSpec = rhs.replaceAll("get","").split("By");
		String toGetClassName = attrSpec[0].split("\\(")[0];
		TempVar getFromT = message.getTo(store);
		Class getFrom = store.getClassByName(message.getTo(store).getType().getTypeName(store));
		Optional<DataUnit> maybeAttr = getFrom.getAttributeByName(StringUtils.uncapitalize(toGetClassName));
		String causesTime = immediate ? "t" : "Next(t)";
		
		if (maybeAttr.isPresent())
		{
			String toAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(maybeAttr.get().getType(), store)
				+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(" + causesTime + ", x) <- SDPointAt(t, " + message.getSDPoint()
				+ ") & (? o [" + getFrom.getName() + "] : " + OutputConvenienceFunctions.singleTempVarPredicateName(getFromT)
				+ "(t, o) & " + getFrom.getName() + maybeAttr.get().getName() + "(t, o, x)).";
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
		
		String toAppend = " ! t [Time] " + assigned.getName() + " [" + OutputConvenienceFunctions.toIDPType(assigned.getType(), store)
		+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(" + causesTime + ", " + assigned.getName() + ") <- SDPointAt(t, "
		+ message.getSDPoint() + ") & " + makeGetterPredicate;
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
		return this;
	}
	
	private String makeGetterPredicate(TempVar assigned, TempVar getFrom, String toGetClassName, String predicateName,
				int fromPos, int toPos, boolean by, SeqDiagramStore store)
	{
		StringBuilder toReturn = new StringBuilder("(? " + getFrom.getName() + " [" + getFrom.getType().getTypeName(store) + "] : "
				+ OutputConvenienceFunctions.singleTempVarPredicateName(getFrom) + "(t, " + getFrom.getName() + ") & ");
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
		
		StringBuilder quantifiers = new StringBuilder("(? ");
		StringBuilder assertion = new StringBuilder(": ");
		
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i].equals(""))
			{
				continue;
			}
			
			TempVar var = store.resolveTempVar(parts[i]);
			
			quantifiers.append(var.getName() + " [" + OutputConvenienceFunctions.toIDPType(var.getType(), store) + "] ");
			assertion.append(OutputConvenienceFunctions.singleTempVarPredicateName(var) + "(t, " + var.getName() + ")"
					+ " & ");
		}
		
		assertion.append(message.getContent() + ").");
		
		String toAppend = "! t [Time] " + assigned.getName() + " [" + OutputConvenienceFunctions.toIDPType(assigned.getType(), store)
			+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(t, " + assigned.getName() + ") <- SDPointAt(t, "
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
