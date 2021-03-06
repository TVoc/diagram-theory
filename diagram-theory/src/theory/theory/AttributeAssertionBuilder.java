package theory.theory;

import data.classdiagrams.DataUnit;
import data.classdiagrams.PrimitiveType;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class AttributeAssertionBuilder
{
	public AttributeAssertionBuilder(int tabLevel)
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
	
	public AttributeAssertionBuilder addAttribute(DataUnit attribute, String className, DiagramStore store)
	{
		String predicateName = className + attribute.getName();
		
		
		
		if (! PrimitiveType.isPrimitiveType(attribute.getTypeName(store)))
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! o x : " + predicateName + "(o, x) => (StaticClass("
					+ className + ", o) & ClassOf(" + attribute.getTypeName(store) + ", x)).", this.getTabLevel()));
		}
		else
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! o x : " + predicateName + "(o, x) => StaticClass("
					+ className + ", o).", this.getTabLevel()));
		}
		
		if (attribute.getMultiplicity().getLowerBound() == 0 && attribute.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getLowerBound() == 0)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! o : (? t : StaticClass(t, o) & t = "
				+ className + ") => (" + "#{x : " + predicateName + "(o, x)} =< "
					+ (int) attribute.getMultiplicity().getUpperBound() + ").", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! o : StaticClass(" + className + ", o) => ("
					+ (int) attribute.getMultiplicity().getLowerBound()
					+ " =< #{x : " + predicateName + "(o, x)}).", this.getTabLevel()));
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
			return this;
		}
		
		if (attribute.getMultiplicity().getLowerBound() == attribute.getMultiplicity().getUpperBound())
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! o : StaticClass(" + className + ", o) => ?"
					+ (int) attribute.getMultiplicity().getLowerBound()
						+ " x : " + predicateName + "(o, x).", this.getTabLevel()));
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
				return this;
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"! o : StaticClass("
				+ className + ", o) => (" + (int) attribute.getMultiplicity().getLowerBound() + " =< #{x : " + predicateName + "(o, x)} & #{x : "
				+ predicateName + "(o, x)} =< " + (int) attribute.getMultiplicity().getUpperBound() + ").", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
