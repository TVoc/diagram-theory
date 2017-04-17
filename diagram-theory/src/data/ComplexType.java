package data;

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

}
