package data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Expresses operation parameters and return types of the sort Class<T>
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
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

}
