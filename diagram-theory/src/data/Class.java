package data;

import java.util.Optional;
import java.util.Set;

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
	 * @return An unmodifiable view of this class's operations
	 */
	public Optional<Set<Operation>> getAllOperations();

}