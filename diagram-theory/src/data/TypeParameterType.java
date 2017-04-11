package data;

/**
 * Expresses operation parameters and return types of the sort <T extends Type>
 * 
 * @author Thomas
 *
 */
public class TypeParameterType implements Type
{
	/**
	 * 
	 * @param id
	 * @throws IllegalArgumentException
	 * 		id == null || id.equals("")
	 */
	public TypeParameterType(String id) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (id.equals(""))
		{
			throw new IllegalArgumentException("id cannot be empty");
		}
		
		this.id = id;
	}
	
	private final String id;
	
	@Override
	public String getID()
	{
		return this.id;
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
