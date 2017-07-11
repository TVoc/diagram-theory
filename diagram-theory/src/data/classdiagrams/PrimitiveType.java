package data.classdiagrams;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a primitive type such as int, bool or string.
 * 
 * 
 * @author Thomas
 *
 */
public enum PrimitiveType implements Type
{

	BOOLEAN("boolean"), BYTE("byte"), CHAR("char"), DOUBLE("double"), FLOAT("float"), INTEGER("int"), LONG("long"), SHORT(
			"short"), STRING("string"), VOID("void");

	private final String name;

	/**
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 *             name == null || name.equals("")
	 */
	PrimitiveType(String name) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}

		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}
	
	@Override
	public String getID()
	{
		return this.getName();
	}

	@Override
	public String getTypeName(TypeContext context)
	{
		return context.resolveName(this);
	}

	@Override
	public Class getType(TypeContext context) throws NoSuchTypeException
	{
		return context.resolve(this);
	}
	
	/**
	 * 
	 * @param type
	 * @return	The PrimitiveType with the same name as the given type
	 * @throws IllegalArgumentException
	 * 		! isPrimitiveType(type)
	 */
	public static PrimitiveType getType(String type) throws IllegalArgumentException
	{
		for (PrimitiveType ele : PrimitiveType.values())
		{
			if (type.equals(ele.getName()))
			{
				return ele;
			}
		}
		
		throw new IllegalArgumentException(type + " did not correspond to a primitive type");
	}
	
	/**
	 * 
	 * @param name
	 * @return	name is the name of one of the primitive type objects
	 */
	public static boolean isPrimitiveType(String name)
	{
		for (PrimitiveType ele : PrimitiveType.values())
		{
			if (name.equals(ele.getName()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

}
