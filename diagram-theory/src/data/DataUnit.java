package data;

import java.util.Optional;

/**
 * Represents either an attribute of a class or a parameter of an operation.
 * 
 * @author Thomas
 *
 */
public class DataUnit
{

	/**
	 * Creates a new data unit with the given name and type
	 * 
	 * @param name
	 * @param type
	 * @param multiplicity
	 * @throws IllegalArgumentException
	 *             name == null || name.equals("") || type == null ||
	 *             type.equals("") || multiplicity == null
	 */
	public DataUnit(String name, Type type, Optional<Multiplicity> multiplicity) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		if (type == null)
		{
			throw new IllegalArgumentException("type cannot be null");
		}
		if (type.equals(""))
		{
			throw new IllegalArgumentException("type cannot be empty");
		}
		if (multiplicity == null)
		{
			throw new IllegalArgumentException("multiplicity cannot be null");
		}

		this.name = name;
		this.type = type;
		this.multiplicity = multiplicity;
	}

	/**
	 * The name of this attribute or parameter.
	 */
	private final String name;

	/**
	 * The type of this attribute or parameter.
	 */
	private final Type type;

	public String getName()
	{
		return this.name;
	}

	private Type getType()
	{
		return this.type;
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @throws NoSuchTypeException
	 *             The given type context cannot resolve this data unit's type
	 */
	public String getType(TypeContext context) throws NoSuchTypeException
	{
		return this.getType().getType(context);
	}

	/**
	 * Expresses how many elements this data unit may contain. If multiplicity
	 * is absent, then 1..1 is assumed.
	 */
	private final Optional<Multiplicity> multiplicity;

	public Multiplicity getMultiplicity()
	{
		return this.internalGetMultiplicity().orElse(Multiplicity.EXACTLY_ONE);
	}

	private Optional<Multiplicity> internalGetMultiplicity()
	{
		return this.multiplicity;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((multiplicity == null) ? 0 : multiplicity.hashCode());
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
		DataUnit other = (DataUnit) obj;
		if (multiplicity == null)
		{
			if (other.multiplicity != null)
				return false;
		}
		else if (!multiplicity.equals(other.multiplicity))
			return false;
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
