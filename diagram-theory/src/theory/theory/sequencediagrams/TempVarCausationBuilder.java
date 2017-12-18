package theory.theory.sequencediagrams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

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
			TempVar assigned = store.resolveTempVar(store.getMessage(message.getSdPoint().getSequenceNumber()).getContent());
			
			return this.handleGetStatement(message, false, store, assigned, message.getContent());
		}
		else // either a return message or a procedure call
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
			// TODO split into assignment call and procedure call
		}
		else // of type "one + two"
		{
			return handleArithStatement(message, store, assigned, rhs);
		}
	}

	private TempVarCausationBuilder handleGetStatement(Message message, boolean immediate, SeqDiagramStore store, TempVar assigned,
			String rhs)
	{
		String[] attrSpec = rhs.replaceAll("get","").split("By");
		String toGetClassName = attrSpec[0].split("\\(")[0];
		TempVar getFromT = message.getTo(store);
		Class getFrom = store.getClassByName(message.getTo(store).getType().getTypeName(store));
		Optional<DataUnit> maybeAttr = getFrom.getAttributeByName(StringUtils.uncapitalize(toGetClassName));
		String causesTime = immediate ? "t" : "Next(t)";
		
		if (maybeAttr.isPresent())
		{
			String toAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(maybeAttr.get().getType(), store)
				+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(" + causesTime + ", x) <- SDPointAt(t, " + message.getSdPoint()
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
		+ message.getSdPoint() + ") & " + makeGetterPredicate;
		
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
			+ message.getSdPoint() + ") & " + quantifiers.toString() + assertion.toString();
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
