package data.classdiagrams;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a class read from a UML Class Diagram.
 * 
 * @author Thomas
 *
 */
public class UserDefinedClass implements Class
{

	/**
	 * Creates a new Class with the given id.
	 * 
	 * @param name
	 *            The name of the new class
	 * @param attributes
	 *            Attributes of the new class. May be null.
	 * @param operations
	 *            Operations of the new class. May be null.
	 * 
	 * @throws IllegalArgumentException
	 *             id.equals("") || id == null || attributes == null ||
	 *             operations == null
	 */
	UserDefinedClass(String name, Set<DataUnit> attributes, Set<Operation> operations) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		if (attributes == null)
		{
			throw new IllegalArgumentException("attributes cannot be null");
		}
		if (operations == null)
		{
			throw new IllegalArgumentException("operations cannot be null");
		}

		this.name = name;

		if (attributes.isEmpty())
		{
			this.attributes = Optional.empty();
		}
		else
		{
			this.attributes = Optional.of(attributes);
		}

		if (operations.isEmpty())
		{
			this.operations = Optional.empty();
		}
		else
		{
			this.operations = Optional.of(operations);
		}
	}

	private final String name;

	/* (non-Javadoc)
	 * @see data.Class#getName()
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	private final Optional<Set<DataUnit>> attributes;

	/* (non-Javadoc)
	 * @see data.Class#getAllAttributes()
	 */
	@Override
	public Optional<Set<DataUnit>> getAllAttributes()
	{
		if (!this.internalGetAttributes().isPresent())
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(Collections.unmodifiableSet(this.internalGetAttributes().get()));
		}
	}
	
	@Override
	public boolean hasAttributesByName(Collection<String> attrNames)
	{
		if (! this.internalGetAttributes().isPresent())
		{
			return false;
		}
		
		Set<String> names = new HashSet<String>();
		
		for (DataUnit attr : this.internalGetAttributes().get())
		{
			names.add(attr.getName());
		}
		
		for (String name : attrNames)
		{
			if (! names.contains(name))
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Optional<DataUnit> getAttributeByName(String name)
	{
		if (! this.internalGetAttributes().isPresent())
		{
			return Optional.empty();
		}
		
		for (DataUnit attr : this.internalGetAttributes().get())
		{
			if (attr.getName().equals(name))
			{
				return Optional.of(attr);
			}
		}
		
		return Optional.empty();
	}

	private Optional<Set<DataUnit>> internalGetAttributes()
	{
		return this.attributes;
	}

	private final Optional<Set<Operation>> operations;

	/* (non-Javadoc)
	 * @see data.Class#getAllOperations()
	 */
	@Override
	public Optional<Set<Operation>> getAllOperations()
	{
		if (!this.internalGetOperations().isPresent())
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(Collections.unmodifiableSet(this.internalGetOperations().get()));
		}
	}

	private Optional<Set<Operation>> internalGetOperations()
	{
		return this.operations;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((operations == null) ? 0 : operations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDefinedClass other = (UserDefinedClass) obj;
		if (attributes == null)
		{
			if (other.attributes != null)
				return false;
		}
		else if (!attributes.equals(other.attributes))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (operations == null)
		{
			if (other.operations != null)
				return false;
		}
		else if (!operations.equals(other.operations))
			return false;
		return true;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
