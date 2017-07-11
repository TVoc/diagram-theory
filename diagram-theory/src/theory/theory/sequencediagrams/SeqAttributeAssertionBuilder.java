package theory.theory.sequencediagrams;

import data.classdiagrams.DataUnit;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class SeqAttributeAssertionBuilder
{
	public SeqAttributeAssertionBuilder(int tabLevel)
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
	
	public SeqAttributeAssertionBuilder addAttribute(DataUnit attribute, String className, DiagramStore store)
	{
		String predicateName = className + attribute.getName();
		
		if (attribute.getMultiplicity().getLowerBound() == 0 && attribute.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getLowerBound() == 0)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + className + "] : #{ v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : I_" + predicateName + "(x, v)} =< "
					+ (int) attribute.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + className + "] : #{ v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : " + predicateName + "(t, x, v)} =< "
					+ (int) attribute.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + className + "] : #{ v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : I_" + predicateName + "(x, v)} >= "
					+ (int) attribute.getMultiplicity().getLowerBound() + ".", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + className + "] : #{ v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : " + predicateName + "(t, x, v)} >= "
					+ (int) attribute.getMultiplicity().getLowerBound() + ".", this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getLowerBound() == attribute.getMultiplicity().getUpperBound())
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + className + "] : ?"
					+ (int) attribute.getMultiplicity().getLowerBound() + " v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : I_" + predicateName + "(x, v).", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + className + "] : ?"
					+ (int) attribute.getMultiplicity().getLowerBound() + " v ["
					+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : " + predicateName + "(t, x, v).", this.getTabLevel()));
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
				return this;
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + className + "] : " + (int) attribute.getMultiplicity().getLowerBound() + " =< #{ v ["
				+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : I_" + predicateName + "(x, v)} =< "
				+ (int) attribute.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + className + "] : " + (int) attribute.getMultiplicity().getLowerBound() + " =< #{ v ["
				+ OutputConvenienceFunctions.toIDPType(attribute.getType(), store) + "] : " + predicateName + "(t, x, v)} =< "
				+ (int) attribute.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
