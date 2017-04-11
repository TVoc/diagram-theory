package data;

/**
 * Represents a generalization in a UML Class Diagram
 * 
 * @author Thomas
 *
 */
public class Generalization
{
	/**
	 * 
	 * @param superType
	 *            The type that is subclassed
	 * @param subType
	 *            The type that is a subclass of the super type
	 * @throws IllegalArgumentException
	 *             superType == null || subType == null
	 * 
	 */
	public Generalization(UserDefinedType superType, UserDefinedType subType) throws IllegalArgumentException
	{
		if (superType == null)
		{
			throw new IllegalArgumentException("superType cannot be null");
		}
		if (subType == null)
		{
			throw new IllegalArgumentException("subType cannot be null");
		}

		this.superType = superType;
		this.subType = subType;
	}

	/**
	 * The type that is a subclass of the superclass
	 */
	private final UserDefinedType subType;

	private UserDefinedType internalGetSubtype()
	{
		return this.subType;
	}

	/**
	 * 
	 * @return This generalization's subtype's true name
	 * @throws NoSuchTypeException
	 *             The given type context cannot resolve this generalization's
	 *             subtype
	 */
	public String getSubtype(TypeContext context) throws NoSuchTypeException
	{
		return this.internalGetSubtype().getType(context);
	}

	/**
	 * The type that is subclassed
	 */
	private final UserDefinedType superType;

	private UserDefinedType internalGetSupertype()
	{
		return this.superType;
	}

	/**
	 * 
	 * @param context
	 * @return This generalization's supertype's true name
	 * @throws NoSuchTypeException
	 *             The given type context cannot resolve this generalization's
	 *             supertype
	 */
	public String getSupertype(TypeContext context) throws NoSuchTypeException
	{
		return this.internalGetSupertype().getType(context);
	}
}
