package data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents an operation in a UML Class Diagram
 * 
 * @author Thomas
 *
 */
public class Operation
{

	/**
	 * 
	 * @param name
	 * @param resultType
	 * @param resultType
	 * @param parameters
	 * @throws IllegalArgumentException
	 *             name == null || name.equals("") || multiplicity == null ||
	 *             parameters == null || parameters.isEmpty()
	 */
	public Operation(String name, DataUnit resultType,
			Optional<List<DataUnit>> parameters) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		if (resultType == null)
		{
			throw new IllegalArgumentException("resultType cannot be null");
		}
		if (parameters == null)
		{
			throw new IllegalArgumentException("parameters cannot be null");
		}
		if (parameters.isPresent() && parameters.get().isEmpty())
		{
			throw new IllegalArgumentException("parameters cannot be empty");
		}

		this.name = name;
		this.resultType = resultType;
		this.parameters = parameters;
	}

	/**
	 * The name of this operation.
	 */
	private final String name;

	/**
	 * The type of the result of an invocation.
	 */
	private final DataUnit resultType;

	public String getName()
	{
		return this.name;
	}

	public DataUnit getResultType()
	{
		return this.resultType;
	}

	/**
	 * Represents the parameters of an operation.
	 */
	private final Optional<List<DataUnit>> parameters;

	private Optional<List<DataUnit>> getParameters()
	{
		return this.parameters;
	}

	/**
	 * 
	 * @return An unmodifiable view of this operation's parameters
	 */
	public Optional<List<DataUnit>> getAllParameters()
	{
		if (!parameters.isPresent())
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(Collections.unmodifiableList(this.getParameters().get()));
		}
	}
}
