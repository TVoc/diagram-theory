package data;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Represents one single end of an association in a UML Class Diagram.
 * 
 * @author Thomas
 *
 */
public class AssociationEnd
{

	/**
	 * 
	 * @param theClass
	 * @param roleName
	 * @param multiplicity
	 * @throws IllegalArgumentException
	 *             theClass == null || roleName == null || roleName.equals("")
	 *             || multiplicity == null
	 */
	public AssociationEnd(UserDefinedType theClass, Optional<String> roleName, Optional<Multiplicity> multiplicity)
			throws IllegalArgumentException
	{
		if (theClass == null)
		{
			throw new IllegalArgumentException("theClass cannot be null");
		}
		if (roleName == null)
		{
			throw new IllegalArgumentException("roleName cannot be null");
		}
		if (roleName.isPresent() && roleName.equals(""))
		{
			throw new IllegalArgumentException("roleName cannot be empty");
		}
		if (multiplicity == null)
		{
			throw new IllegalArgumentException("multiplicity cannot be null");
		}

		this.theClass = theClass;
		this.roleName = roleName;
		this.multiplicity = multiplicity;
	}

	private final UserDefinedType theClass;

	/**
	 * 
	 * @param context
	 * @return The name of this association end's type
	 * @throws NoSuchTypeException
	 *             The given type context cannot resolve this association end's
	 *             type
	 */
	public String getType(TypeContext context) throws NoSuchTypeException
	{
		return this.theClass.getTypeName(context);
	}

	private final Optional<String> roleName;

	public boolean hasRoleName()
	{
		return this.internalGetRoleName().isPresent();
	}

	private Optional<String> internalGetRoleName()
	{
		return this.roleName;
	}

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             ! this.hasRoleName()
	 */
	public String getRoleName() throws IllegalStateException
	{
		try
		{
			return this.internalGetRoleName().get();
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(e);
		}
	}

	/**
	 * If multiplicity is absent, then 1..1 is assumed.
	 */
	private final Optional<Multiplicity> multiplicity;

	private Optional<Multiplicity> internalGetMultiplicity()
	{
		return this.multiplicity;
	}

	public Multiplicity getMultiplicity()
	{
		return this.internalGetMultiplicity().orElse(Multiplicity.EXACTLY_ONE);
	}

}
