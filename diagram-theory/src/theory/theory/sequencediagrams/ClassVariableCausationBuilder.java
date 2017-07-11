package theory.theory.sequencediagrams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;

public class ClassVariableCausationBuilder
{
	public ClassVariableCausationBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
		this.classVarDefs = new HashMap<String, ClassVariableDefinition>();
		
		for (Class clazz : store.getClasses().values())
		{
			if (clazz.getAllAttributes().isPresent())
			{
				for (DataUnit attr : clazz.getAllAttributes().get())
				{
					classVarDefs.put(clazz.getName() + attr.getName(), new ClassVariableDefinition(attr, clazz, store));
				}
			}
		}
	}
	
	private final int tabLevel;
	
	private final StringBuilder stringBuilder;

	public int getTabLevel()
	{
		return this.tabLevel;
	}

	public StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	private final Map<String, ClassVariableDefinition> classVarDefs;
	
	private Map<String, ClassVariableDefinition> getClassVarDefs()
	{
		return this.classVarDefs;
	}
	
	public ClassVariableCausationBuilder handleMessage(Message message, SeqDiagramStore store)
	{
		if (message.getContent().contains("set"))
		{
			String toSet = StringUtils.uncapitalize(message.getContent().replaceAll("set", "").split("\\(")[0]);
			TempVar setOf = message.getTo(store);
			Class setClass = store.getClassByName(message.getTo(store).getType().getTypeName(store));
			String predName = setClass.getName() + toSet;
			
			this.getClassVarDefs().get(predName).handleSet(message, store, toSet, predName, setOf, setClass);
		}
		
		return this;
	}
	
	private String findSetterValue(String setterString)
	{
		Matcher m = TempVarCausationBuilder.GETTER_PARAMETER.matcher(setterString);
		m.find();
		return m.group(1);
	}
	
	public String build()
	{
		StringBuilder toReturn = new StringBuilder();
		
		Iterator<ClassVariableDefinition> defs = this.getClassVarDefs().values().iterator();
		
		while (defs.hasNext())
		{
			ClassVariableDefinition ele = defs.next();
			
			toReturn.append(ele.toString());
			
			if (defs.hasNext())
			{
				toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			}
		}
		
		return toReturn.toString();
	}
	
	private class ClassVariableDefinition
	{
		ClassVariableDefinition(DataUnit attr, Class clazz, SeqDiagramStore store)
		{
			String predName = OutputConvenienceFunctions.attributePredicateName(attr, clazz);
			
			this.causeStringBuilder = new StringBuilder("! t [Time] x [" + clazz.getName() + "] v [" + OutputConvenienceFunctions.toIDPType(attr.getType(),  store)
					+ "] : C_" + predName + "(t, x, v) <- false.");
			this.uncauseStringBuilder = new StringBuilder("! t [Time] x [" + clazz.getName() + "] v [" + OutputConvenienceFunctions.toIDPType(attr.getType(),  store)
			+ "] : Cn_" + predName + "(t, x, v) <- false.");
			this.modified = false;
		}
		
		private final StringBuilder causeStringBuilder;
		
		private final StringBuilder uncauseStringBuilder;

		public StringBuilder getCauseStringBuilder()
		{
			return this.causeStringBuilder;
		}

		public StringBuilder getUncauseStringBuilder()
		{
			return this.uncauseStringBuilder;
		}
		
		private boolean modified;
		
		public boolean isModified()
		{
			return this.modified;
		}
		
		public void setModified(boolean modified)
		{
			this.modified = true;
		}
		
		public void handleSet(Message message, SeqDiagramStore store, String toSet, String predName, TempVar setOf, Class setClass)
		{
			if (! this.isModified())
			{
				this.getCauseStringBuilder().delete(0, this.getCauseStringBuilder().length());
				this.getUncauseStringBuilder().delete(0, this.getUncauseStringBuilder().length());
				
				this.setModified(true);
			}

			String setterValue = ClassVariableCausationBuilder.this.findSetterValue(message.getContent());
			
			if (store.hasTempVar(setterValue))
			{
				TempVar temp = store.resolveTempVar(setterValue);
				
				String causeAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(setOf.getType(), store) + "] v ["
						+ OutputConvenienceFunctions.toIDPType(temp.getType(), store) + "] : C_" + predName + "(t, x, v) <- SDPointAt(t, "
						+ message.getSdPoint() + ") & " + OutputConvenienceFunctions.singleTempVarPredicateName(setOf) + "(t, x) & "
						+ OutputConvenienceFunctions.singleTempVarPredicateName(temp) + "(t, v).";
				String uncauseAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(setOf.getType(), store) + "] v ["
						+ OutputConvenienceFunctions.toIDPType(temp.getType(), store) + "] : C_" + predName + "(Next(t), x, v) <- SDPointAt(Next(t), "
						+ message.getSdPoint() + ") & " + OutputConvenienceFunctions.singleTempVarPredicateName(setOf) + "(t, x) & "
						+ predName + "(t, x, v) & ~"
						+ OutputConvenienceFunctions.singleTempVarPredicateName(temp) + "(Next(t), v).";
				
				this.getCauseStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(causeAppend, ClassVariableCausationBuilder.this.getTabLevel()));
				this.getUncauseStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(uncauseAppend, ClassVariableCausationBuilder.this.getTabLevel()));
				
				return;
			}
			
			setterValue = OutputConvenienceFunctions.catchIDPBoolean(setterValue);
			
			String causeAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(setOf.getType(), store) + "] : C_" + predName
					+ "(t, x, " + setterValue + ") <- SDPointAt(t, " + message.getSdPoint() + ") & " + OutputConvenienceFunctions.singleTempVarPredicateName(setOf)
					+ "(t, x).";
			String uncauseAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(setOf.getType(), store) + "] v ["
					+ OutputConvenienceFunctions.toIDPType(setClass.getAttributeByName(toSet).get().getType(), store)
					+ "] : Cn_" + predName
					+ "(Next(t), x, v) <- SDPointAt(Next(t), " + message.getSdPoint() + ") & " + OutputConvenienceFunctions.singleTempVarPredicateName(setOf)
					+ "(t, x) & " + predName + "(t, x, v) & ~(v = " + setterValue + ").";
			
			this.getCauseStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(causeAppend, ClassVariableCausationBuilder.this.getTabLevel()));
			this.getUncauseStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(uncauseAppend, ClassVariableCausationBuilder.this.getTabLevel()));
			
			return;
		}
		
		public String toString()
		{
			return OutputConvenienceFunctions.insertTabsNewLine(this.getCauseStringBuilder().toString(), ClassVariableCausationBuilder.this.getTabLevel() + 1)
					+ OutputConvenienceFunctions.insertTabsNewLine(this.getUncauseStringBuilder().toString(), ClassVariableCausationBuilder.this.getTabLevel() + 1);
		}
	}
}
