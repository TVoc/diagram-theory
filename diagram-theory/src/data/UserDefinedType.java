package data;

/**
 * This represents the occurrence of a type defined by a UML Class Diagram as an
 * attribute or operation parameter.
 * 
 * @author Thomas
 *
 */
public class UserDefinedType implements Type
{

	/**
	 * 
	 * @param id
	 * @throws IllegalArgumentException
	 *             id == null || id.equals("id")
	 */
	public UserDefinedType(String id) throws IllegalArgumentException
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

	/**
	 * The TypeContext will use this id to retrieve the type's true name.
	 */
	private final String id;

	@Override
	public String getType(TypeContext context) throws NoSuchTypeException
	{
		return context.resolve(this);
	}

}
