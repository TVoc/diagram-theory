package data;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a class read from a UML Class Diagram.
 * 
 * @author Thomas
 *
 */
public class Class {
	
	/**
	 * Creates a new Class with the given id.
	 * 
	 * @param id
	 * 		id of the new class
	 * @throws IllegalArgumentException
	 * 		id.equals("") || id == null
	 */
	public Class(String id) throws IllegalArgumentException
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
	
	public String getID()
	{
		return this.id;
	}
	
	private Optional<Set<Attribute>> attributes;
	
	/**
	 * 
	 * @return
	 * An unmodifiable view of this class's attributes
	 */
	public Optional<Set<Attribute>> getAllAttributes()
	{
		if (! this.getAttributes().isPresent())
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(Collections.unmodifiableSet(this.getAttributes().get()));
		}
	}
	
	private Optional<Set<Attribute>> getAttributes()
	{
		return this.attributes;
	}
	
	private Optional<Set<Operation>> operations;
	
	/**
	 * 
	 * @return
	 * An unmodifiable view of this class's operations
	 */
	public Optional<Set<Operation>> getAllOperations()
	{
		if (! this.getOperations().isPresent())
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(Collections.unmodifiableSet(this.getOperations().get()));
		}
	}
	
	private Optional<Set<Operation>> getOperations()
	{
		return this.operations;
	}
}
