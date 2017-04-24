package theory.vocabulary;

import theory.OutputConvenienceFunctions;

public class ClassPredicateBuilder
{
	public ClassPredicateBuilder(int tabLevel)
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
	
	public ClassPredicateBuilder addClassPredicate(String className)
	{
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(className + "(ClassObject)", this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
