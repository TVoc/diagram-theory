package data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClassBuilder
{

	/**
	 * Initializes a new class builder with a name for the new class
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 * 		name == null || name.equals("")
	 */
	public ClassBuilder(String name) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		
		this.name = name;
		this.attributes = new HashSet<DataUnit>();
		this.operations = new HashSet<Operation>();
	}
	
	/**
	 * Initializes a new class builder with an empty name for the new class
	 */
	public ClassBuilder()
	{
		this.name = "";
		this.attributes = new HashSet<DataUnit>();
		this.operations = new HashSet<Operation>();
	}
	
	private String name;
	
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 
	 * @param name
	 * 		The new class's name
	 * @throws IllegalArgumentException
	 * 		name == null || name.equals("")
	 */
	public ClassBuilder setName(String name) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		
		this.name = name;
		return this;
	}
	
	private Set<DataUnit> attributes;

	private Set<DataUnit> internalGetAttributes()
	{
		return this.attributes;
	}

	/**
	 * 
	 * @return An unmodifiable view of the new class's attributes so far
	 */
	public Set<DataUnit> getAttributes()
	{
		return Collections.unmodifiableSet(this.internalGetAttributes());
	}
	
	/**
	 * 
	 * @param attribute
	 * @throws IllegalArgumentException
	 * 		attribute == null
	 */
	public ClassBuilder addAttribute(DataUnit attribute) throws IllegalArgumentException
	{
		if (attribute == null)
		{
			throw new IllegalArgumentException("attribute cannot be null");
		}
		
		this.internalGetAttributes().add(attribute);
		
		return this;
	}
	
	/**
	 * Removes the given attribute, if present.
	 * 
	 * @param attribute
	 */
	public ClassBuilder removeAttribute(DataUnit attribute)
	{
		this.internalGetAttributes().remove(attribute);
		
		return this;
	}
	
	public ClassBuilder clearAttributes()
	{
		this.internalGetAttributes().clear();
		
		return this;
	}
	
	private Set<Operation> operations;
	
	private Set<Operation> internalGetOperations()
	{
		return this.operations;
	}
	
	/**
	 * 
	 * @return An unmodifiable view of this class's operations so far
	 */
	public Set<Operation> getOperations()
	{
		return Collections.unmodifiableSet(this.internalGetOperations());
	}
	
	/**
	 * 
	 * @param operation
	 * @throws IllegalArgumentException
	 * 		operation == null
	 */
	public ClassBuilder addOperation(Operation operation) throws IllegalArgumentException
	{
		if (operation == null)
		{
			throw new IllegalArgumentException("operation cannot be null");
		}
		
		this.internalGetOperations().add(operation);
		
		return this;
	}
	
	/**
	 * Removes the given operation, if present.
	 * 
	 * @param operation
	 */
	public ClassBuilder removeOperation(Operation operation)
	{
		this.internalGetOperations().remove(operation);
		
		return this;
	}
	
	public ClassBuilder clearOperations()
	{
		this.internalGetOperations().clear();
		
		return this;
	}
	
	/**
	 * Prepare this builder for reuse.
	 */
	private void reset()
	{
		this.name = "";
		this.attributes = new HashSet<DataUnit>();
		this.operations = new HashSet<Operation>();
	}
	
	/**
	 * Initializes a new class with the current name, attributes and operations. Afterwards, this builder may be reused to construct a different class.
	 * 
	 * @return	A new class with the current name, attributes and operations.
	 * @throws IllegalStateException
	 * 		this.getName().equals("")
	 */
	public Class build() throws IllegalStateException
	{
		try
		{
			Class toReturn = new Class(this.getName(), this.internalGetAttributes(), this.internalGetOperations());
			this.reset();
			return toReturn;
		}
		catch(IllegalArgumentException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
