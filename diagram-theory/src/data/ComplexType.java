package data;

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
	 * @param id
	 * @param multiplicity
	 * @throws IllegalArgumentException
	 * 		id == null || id.equals("") || multiplicity == null
	 */
	public ComplexType(String id, Multiplicity multiplicity) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (id.equals(""))
		{
			throw new IllegalArgumentException("id cannot be empty");
		}
		if (multiplicity == null)
		{
			throw new IllegalArgumentException("multiplicity cannot be null");
		}
		
		this.id = id;
		this.multiplicity = multiplicity;
	}
	
	private final String id;
	
	public String getID()
	{
		return this.id;
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

}
