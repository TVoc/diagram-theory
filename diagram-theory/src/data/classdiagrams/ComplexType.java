package data.classdiagrams;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A type with a multiplicity. Used to express types like List<T> or Optional<T>.
 * 
 * @author Thomas
 *
 */
public class ComplexType implements Type
{
	/**
	 * 
	 * @param type
	 * @param multiplicity
	 * @throws IllegalArgumentException
	 * 		type == null || multiplicity == null
	 */
	public ComplexType(Type type, Multiplicity multiplicity) throws IllegalArgumentException
	{
		if (type == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (type.equals(""))
		{
			throw new IllegalArgumentException("id cannot be empty");
		}
		if (multiplicity == null)
		{
			throw new IllegalArgumentException("multiplicity cannot be null");
		}
		
		this.type = type;
		this.multiplicity = multiplicity;
	}
	
	private final Type type;
	
	public Type getType()
	{
		return this.type;
	}
	
	public String getID()
	{
		return this.getType().getID();
	}
	
	private final Multiplicity multiplicity;
	
	public Multiplicity getMultiplicity()
	{
		return this.multiplicity;
	}

	@Override
	public String getTypeName(TypeContext context) throws NoSuchTypeException
	{
		return context.resolveName(this);
	}

	@Override
	public Class getType(TypeContext context) throws NoSuchTypeException
	{
		return context.resolve(this);
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((multiplicity == null) ? 0 : multiplicity.hashCode());
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
		ComplexType other = (ComplexType) obj;
		if (multiplicity == null)
		{
			if (other.multiplicity != null)
				return false;
		}
		else if (!multiplicity.equals(other.multiplicity))
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
