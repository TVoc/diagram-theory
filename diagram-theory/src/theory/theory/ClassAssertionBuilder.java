package theory.theory;

import theory.OutputConvenienceFunctions;

public class ClassAssertionBuilder
{
	public ClassAssertionBuilder(int tabLevel)
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
	
	public ClassAssertionBuilder addClassAssertion(String className)
	{
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("?1 x : " + className + "(x).", this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString() + System.lineSeparator();
	}
}
