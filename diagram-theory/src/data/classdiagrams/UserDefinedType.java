package data.classdiagrams;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UserDefinedType other = (UserDefinedType) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}
