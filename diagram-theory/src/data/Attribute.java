package data;

import java.util.Optional;

public class Attribute {
	
	/**
	 * Creates a new attribute with the given name and type
	 * 
	 * @param name
	 * @param type
	 * @param multiplicity
	 * @throws IllegalArgumentException
	 */
	public Attribute(String name, String type, Optional<Multiplicity> multiplicity) throws IllegalArgumentException
	{
		
	}
	
	private final String name;
	private final String type;
	
	public String getName()
	{
		return this.name;
	}
	
	public String getType()
	{
		return this.type;
	}

}
