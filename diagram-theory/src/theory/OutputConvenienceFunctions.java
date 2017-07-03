package theory;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import data.classdiagrams.PrimitiveType;
import data.classdiagrams.Type;
import data.classdiagrams.TypeContext;
import data.sequencediagrams.TempVar;

import org.apache.commons.lang3.StringEscapeUtils;

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
	
	public static String toIDPOperators(String in)
	{
		return StringEscapeUtils.unescapeXml(in).replaceAll("<=", "=<").replaceAll("==", "=");
	}
	
	public static String toIDPType(Type type, TypeContext context)
	{
		String typeName = type.getTypeName(context);
		
		if (PrimitiveType.isPrimitiveType(typeName))
		{
			return primitiveTypeToLogicType(typeName);
		}
		
		return typeName;
	}
	
	public static String[] tempVarPredicateNames(TempVar tempVar)
	{
		String[] toReturn = new String[3];
		
		String capitalized = singleTempVarPredicateName(tempVar);
		
		toReturn[0] = capitalized;
		toReturn[1] = "I_" + capitalized;
		toReturn[2] = "C_" + capitalized;
		
		return toReturn;
	}
	
	public static String singleTempVarPredicateName(TempVar tempVar)
	{
		return WordUtils.capitalize(tempVar.getName()) + "T";
	}
}
