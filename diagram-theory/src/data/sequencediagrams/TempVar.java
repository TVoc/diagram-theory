package data.sequencediagrams;

import data.classdiagrams.DataUnit;

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
	public TempVar(DataUnit type, int sdPoint) throws IllegalArgumentException
	{
		if (type == null)
		{
			throw new IllegalArgumentException("type cannot be null");
		}
		if (sdPoint < 1)
		{
			throw new IllegalArgumentException("sdPoint cannot be smaller than 1");
		}
		
		this.type = type;
		this.sdPoint = sdPoint;
	}
	
	private final DataUnit type;
	
	private final int sdPoint;

	public DataUnit getType()
	{
		return this.type;
	}

	/**
	 * 
	 * @return	The point in the sequence diagram where this temp var is initialized
	 */
	public int getSdPoint()
	{
		return this.sdPoint;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + sdPoint;
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
		if (sdPoint != other.sdPoint)
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
