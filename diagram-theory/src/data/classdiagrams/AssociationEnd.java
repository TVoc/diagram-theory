package data.classdiagrams;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
	
	private UserDefinedType getTheClass()
	{
		return this.theClass;
	}

	/**
	 * 
	 * @param context
	 * @return The name of this association end's type
	 * @throws NoSuchTypeException
	 *             The given type context cannot resolve this association end's
	 *             type
	 */
	public String getTypeName(TypeContext context) throws NoSuchTypeException
	{
		return this.getTheClass().getTypeName(context);
	}
	
	/**
	 * 
	 * @param context
	 * @return	The class object corresponding to this association end's type
	 * @throws NoSuchTypeException
	 * 		The given type context cannot resolve this association end's type
	 */
	public Class getType(TypeContext context) throws NoSuchTypeException
	{
		return this.getTheClass().getType(context);
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
	
	public boolean isCollection()
	{
		return this.getMultiplicity().isCollection();
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
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((theClass == null) ? 0 : theClass.hashCode());
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
		AssociationEnd other = (AssociationEnd) obj;
		if (multiplicity == null)
		{
			if (other.multiplicity != null)
				return false;
		}
		else if (!multiplicity.equals(other.multiplicity))
			return false;
		if (roleName == null)
		{
			if (other.roleName != null)
				return false;
		}
		else if (!roleName.equals(other.roleName))
			return false;
		if (theClass == null)
		{
			if (other.theClass != null)
				return false;
		}
		else if (!theClass.equals(other.theClass))
			return false;
		return true;
	}
}
