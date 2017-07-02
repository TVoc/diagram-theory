package data.classdiagrams;

/**
 * Used when specifying a type for an attribute or a parameter. A type can
 * either be a primitive type (e.g. int, bool, string) or one of the types
 * defined in the UML Class Diagram.
 * 
 * 
 * @author Thomas
 *
 */
public interface Type
{

	public String getID();
	
	/**
	 * Resolve this type using the given TypeContext and give its name
	 * 
	 * @param context
	 * @return The type's true name according to context
	 * @throws NoSuchTypeException
	 * 		context cannot resolve this type
	 */
	public String getTypeName(TypeContext context) throws NoSuchTypeException;
	
	/**
	 * Resolve this type using the given TypeContext and give its corresponding Class object
	 * 
	 * @param context
	 * @return	The Class object belonging to this type
	 * @throws NoSuchTypeException
	 * 		context cannot resolve this type
	 */
	public Class getType(TypeContext context) throws NoSuchTypeException;

}
