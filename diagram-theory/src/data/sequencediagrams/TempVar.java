package data.sequencediagrams;

import data.classdiagrams.DataUnit;
import data.classdiagrams.Type;

/**
 * Either a lifeline or a temporary variable that occurs in a sequence diagram
 * 
 * @author Thomas
 *
 */
public class TempVar
{
	/**
	 * 
	 * @param type
	 * @param sdPoint
	 * @throws IllegalArgumentException
	 * 		type == null || sdPoint < 1
	 */
	public TempVar(Type type, String name) throws IllegalArgumentException
	{
		if (type == null)
		{
			throw new IllegalArgumentException("type cannot be null");
		}
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		
		this.type = type;
		this.name = name;
	}
	
	private final Type type;

	public Type getType()
	{
		return this.type;
	}
	
	private final String name;
	
	public String getName()
	{
		return this.name;
	}
}
