package data;

import java.util.Optional;
import java.util.Set;

public enum PrimitiveClass implements Class
{
	BOOLEAN("bool"), BYTE("byte"), CHAR("char"), DOUBLE("double"),
	FLOAT("float"), INTEGER("int"), LONG("long"), SHORT("short"),
	STRING("string"), VOID("void");

	PrimitiveClass(String name) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty.");
		}
		
		this.name = name;
	}
	
	private final String name;
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Optional<Set<DataUnit>> getAllAttributes()
	{
		return Optional.empty();
	}

	@Override
	public Optional<Set<Operation>> getAllOperations()
	{
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param type
	 * @return	The PrimitiveType with the same name as the given type
	 * @throws IllegalArgumentException
	 * 		! isPrimitiveType(type)
	 */
	public static PrimitiveClass getType(String type) throws IllegalArgumentException
	{
		for (PrimitiveClass ele : PrimitiveClass.values())
		{
			if (type.equals(ele.getName()))
			{
				return ele;
			}
		}
		
		throw new IllegalArgumentException(type + " did not correspond to a primitive class");
	}
	
	/**
	 * 
	 * @param name
	 * @return	name is the name of one of the primitive type objects
	 */
	public static boolean isPrimitiveClass(String name)
	{
		for (PrimitiveClass ele : PrimitiveClass.values())
		{
			if (name.equals(ele.getName()))
			{
				return true;
			}
		}
		
		return false;
	}
	
}
