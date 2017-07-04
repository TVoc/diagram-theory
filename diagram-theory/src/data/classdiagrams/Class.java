package data.classdiagrams;

import java.util.Optional;
import java.util.Set;

//TODO: provide support for multiple type parameters like in Map<K,V>?
public interface Class
{

	public String getName();

	/**
	 * 
	 * @return An unmodifiable view of this class's attributes
	 */
	public Optional<Set<DataUnit>> getAllAttributes();
	
	/**
	 * 
	 * @param name
	 * @return	The attribute identified by the given name, if it exists
	 */
	public Optional<DataUnit> getAttributeByName(String name);

	/**
	 * 
	 * @return An unmodifiable view of this class's operations
	 */
	public Optional<Set<Operation>> getAllOperations();

}