package data;

/**
 * Used when specifying a type for an attribute or a parameter. A type can either be a primitive type (e.g. int, bool, string) or
 * one of the types defined in the UML Class Diagram.
 * 
 * 
 * @author Thomas
 *
 */
public interface Type
{
	
	/**
	 * Resolve this type using the given TypeContext
	 * 
	 * @param context
	 * @return	The type's true name according to context
	 * @throws NoSuchTypeException
	 * 		context cannot resolve this type
	 */
	public String getType(TypeContext context) throws NoSuchTypeException;

}
