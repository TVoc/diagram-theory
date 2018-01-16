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
	
	public String getNameAsAssignee()
	{
		return this.name + "_new";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempVar other = (TempVar) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		}
		else if (!type.equals(other.type))
			return false;
		return true;
	}
}
