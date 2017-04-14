package data;

/**
 * Represents a primitive type such as int, bool or string.
 * 
 * 
 * @author Thomas
 *
 */
public enum PrimitiveType implements Type
{

	BOOLEAN("bool"), BYTE("byte"), CHAR("char"), DOUBLE("double"), FLOAT("float"), INTEGER("int"), LONG("long"), SHORT(
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
	 * 		type does not correspond with a primitive type
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

}
