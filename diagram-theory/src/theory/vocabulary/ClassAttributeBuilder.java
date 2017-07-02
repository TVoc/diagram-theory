package theory.vocabulary;

import data.classdiagrams.DataUnit;
import data.classdiagrams.PrimitiveType;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class ClassAttributeBuilder
{
	public ClassAttributeBuilder(int tabLevel)
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
	
	public ClassAttributeBuilder addAttribute(DataUnit attribute, String className, DiagramStore store)
	{
		String typeName = attribute.getTypeName(store);
		if (PrimitiveType.isPrimitiveType(typeName))
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(className + attribute.getName() + "(Object,"
					+ OutputConvenienceFunctions.primitiveTypeToLogicType(typeName) + ")", this.getTabLevel()));
		}
		else
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(className + attribute.getName() + "(Object,Object)", this.getTabLevel()));
		}
		
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
