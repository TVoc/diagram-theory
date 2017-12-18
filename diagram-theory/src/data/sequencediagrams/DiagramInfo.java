package data.sequencediagrams;

import java.util.List;
import java.util.Optional;

public class DiagramInfo
{
	public DiagramInfo(String name, TempVar callObject, Optional<List<TempVar>> parameters, Optional<TempVar> returnValue) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (callObject == null)
		{
			throw new IllegalArgumentException("callObject cannot be null");
		}
		if (parameters == null)
		{
			throw new IllegalArgumentException("parameters cannot be null");
		}
		if (returnValue == null)
		{
			throw new IllegalArgumentException("returnValue cannot be null");
		}
		
		this.name = name;
		this.callObject = callObject;
		this.parameters = parameters;
		this.returnValue = returnValue;
	}
	
	private final String name;
	
	private final TempVar callObject;
	
	private final Optional<List<TempVar>> parameters;
	
	public String getName()
	{
		return this.name;
	}
	
	public TempVar getCallObject()
	{
		return this.callObject;
	}
	
	public Optional<List<TempVar>> getParameters()
	{
		return this.parameters;
	}
	
	private final Optional<TempVar> returnValue;
	
	public Optional<TempVar> getReturnValue()
	{
		return this.returnValue;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callObject == null) ? 0 : callObject.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((returnValue == null) ? 0 : returnValue.hashCode());
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
		DiagramInfo other = (DiagramInfo) obj;
		if (callObject == null)
		{
			if (other.callObject != null)
				return false;
		}
		else if (!callObject.equals(other.callObject))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (parameters == null)
		{
			if (other.parameters != null)
				return false;
		}
		else if (!parameters.equals(other.parameters))
			return false;
		if (returnValue == null)
		{
			if (other.returnValue != null)
				return false;
		}
		else if (!returnValue.equals(other.returnValue))
			return false;
		return true;
	}
}
