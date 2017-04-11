package data;

/**
 * When given a type, a TypeContext attempts to resolve it to a type's true
 * name.
 * 
 * @author Thomas
 *
 */
public interface TypeContext
{

	/**
	 * 
	 * @param type
	 * @return The type's true name
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public String resolveName(Type type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	The Class object corresponding to type
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public Class resolve(Type type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	This type context can resolve type
	 */
	public boolean canResolve(Type type);

}
