package data;

import java.util.Optional;
import java.util.Set;

enum PrimitiveClass implements Class
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
	
}
