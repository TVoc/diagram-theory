package theory;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

public abstract class OutputConvenienceFunctions
{
	public static final int TAB_SIZE = 4;
	
	public static String insertTabs(int tabLevel)
	{
		return StringUtils.repeat(" ", TAB_SIZE * tabLevel);
	}
	
	public static String insertTabsIn(String in, int tabLevel)
	{
		return insertTabs(tabLevel) + in;
	}
	
	public static String insertTabsNewLine(String in, int tabLevel)
	{
		return insertTabsIn(in, tabLevel) + System.lineSeparator();
	}
	
	public static String insertTabsBlankLine(int tabLevel)
	{
		return insertTabsNewLine("", tabLevel);
	}
	
	public static String primitiveTypeToLogicType(String primitiveType) throws IllegalArgumentException
	{
		switch (primitiveType)
		{
			case "bool":
				return primitiveType;
			case "int":
				return "LimitedInt";
			case "short":
				return "LimitedInt";
			case "long":
				return "LimitedInt";
			case "byte":
				return "LimitedInt";
			case "void":
				return "void";
			case "double":
				return "LimitedFloat";
			case "float":
				return "LimitedFloat";
			case "char":
				return primitiveType;
			case "string":
				return "LimitedString";
			default:
				throw new IllegalArgumentException("primitiveType was not a primitive type");
		}
	}
}
