package theory.vocabulary.sequencediagrams;

import data.classdiagrams.TypeContext;
import data.sequencediagrams.TempVar;
import theory.OutputConvenienceFunctions;

public class TempVarBuilder
{
	public TempVarBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final StringBuilder stringBuilder;
	
	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public void addTempVar(TempVar tempVar, TypeContext context)
	{
		String[] predicateNames = OutputConvenienceFunctions.tempVarPredicateNames(tempVar);
		String typeName = tempVar.getType().getTypeName(context);
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(predicateNames[0] + "(Time, " + typeName + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(predicateNames[1] + "(" + typeName + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(predicateNames[2] + "(Time, " + typeName + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
