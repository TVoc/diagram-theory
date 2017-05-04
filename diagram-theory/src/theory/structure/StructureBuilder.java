package theory.structure;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

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
		StringBuilder stringDomain = new StringBuilder();
		Set<String> ranStrings = new HashSet<String>();
		
		while (ranStrings.size() < Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getStringFactor()))
		{
			String ran = RandomStringUtils.random(20, true, true);
			
			if (ranStrings.contains(ran))
			{
				continue;
			}
			
			if (ranStrings.size() == 0)
			{
				stringDomain.append("\"" + ran + "\"");
			}
			else
			{
				stringDomain.append(", \"" + ran + "\"");
			}
			
			ranStrings.add(ran);
		}
		
		StringBuilder floatDomain = new StringBuilder();
		
		double ele = 0;
		int i = 0;
		while (i < Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getFloatFactor()))
		{
			if (i == 0)
			{
				floatDomain.append(ele);
			}
			else
			{
				floatDomain.append(", " + ele);
			}
			ele += 0.5;
			i++;
		}
		
		return OutputConvenienceFunctions.insertTabsNewLine("structure struct : V {", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("Object = { 1.." + this.getFactors().getNumObjects() + "}", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedStrings = { " + stringDomain.toString() + " }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedInt = { 1.." + (int) Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getIntFactor()) + " }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("LimitedFloat = { " + floatDomain.toString() + " }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel() + 1);
	}
}
