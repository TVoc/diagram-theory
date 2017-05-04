package theory.structure;

import theory.Factors;
import theory.OutputConvenienceFunctions;

public class StructureBuilder
{
	public StructureBuilder(int tabLevel, Factors factors)
	{
		this.tabLevel = tabLevel;
		this.factors = factors;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	private final StringBuilder stringBuilder;
	private final Factors factors;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	private Factors getFactors()
	{
		return this.factors;
	}
	
	public String build()
	{	
		return OutputConvenienceFunctions.insertTabsNewLine("structure thestruct : V {", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("Object = { 1.." + this.getFactors().getNumObjects() + "}", this.getTabLevel() + 1)
//				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedString = { " + stringDomain.toString() + " }", this.getTabLevel() + 1)
//				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedInt = { 1.." + (int) Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getIntFactor()) + " }", this.getTabLevel() + 1)
//				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedFloat = { " + floatDomain.toString() + " }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel());
	}
}
