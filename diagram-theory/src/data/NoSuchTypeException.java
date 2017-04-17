package data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * This exception occurs when a TypeContext cannot resolve a given Type.
 * 
 * @author Thomas
 *
 */
public class NoSuchTypeException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3163278769725839317L;
	
	public NoSuchTypeException(String message)
	{
		super(message);
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

}
