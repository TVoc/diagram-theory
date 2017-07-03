package theory.vocabulary.sequencediagrams;

import data.classdiagrams.DataUnit;
import data.classdiagrams.PrimitiveType;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;

public class SeqClassAttributeBuilder
{
	public SeqClassAttributeBuilder(int tabLevel)
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
	
	public SeqClassAttributeBuilder addAttribute(DataUnit attribute, String className, SeqDiagramStore store)
	{
		String attrType = attribute.getTypeName(store);
		
		if (PrimitiveType.isPrimitiveType(attrType))
		{
			attrType = OutputConvenienceFunctions.primitiveTypeToLogicType(attrType);
		}
		
		String baseName = className + attribute.getName();
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(baseName + "(Time, " + className + ", " + attrType + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("I_" + baseName + "(" + className + ", " + attrType + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine( "C_" + baseName + "(Time, " + className + ", " + attrType + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("Cn_" + baseName + "(Time, " + className + ", " + attrType + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
