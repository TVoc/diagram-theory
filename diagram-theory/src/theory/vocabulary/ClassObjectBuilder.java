package theory.vocabulary;

import theory.OutputConvenienceFunctions;

public class ClassObjectBuilder
{
	
	public ClassObjectBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}

	private final StringBuilder stringBuilder;
	private final int tabLevel;
	
	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	public ClassObjectBuilder addClass(String className)
	{
		if (this.getStringBuilder().length() == 0)
		{
			this.getStringBuilder().append(className);
		}
		else
		{
			this.getStringBuilder().append(", " + className);
		}
		
		return this;
	}
	
	public String build()
	{
		return OutputConvenienceFunctions.insertTabs(this.getTabLevel()) + "type ClassObject constructed from { " + this.getStringBuilder().toString() + " }"; 
	}
	
}
