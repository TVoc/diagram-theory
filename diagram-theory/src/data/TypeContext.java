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
	 */
	public String resolve(Type type);
	
	/**
	 * This type context can resolve the given type.
	 * 
	 * @param type
	 * @return
	 */
	public boolean canResolve(Type type);

}
