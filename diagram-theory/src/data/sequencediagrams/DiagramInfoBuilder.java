package data.sequencediagrams;

import java.util.List;
import java.util.Optional;

public class DiagramInfoBuilder {
	
	private String name;
	
	private TempVar callObject;
	
	private Optional<List<TempVar>> parameters = Optional.empty();
	
	private Optional<TempVar> returnValue = Optional.empty();

	public String getName() {
		return name;
	}

	public DiagramInfoBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public TempVar getCallObject() {
		return callObject;
	}

	public DiagramInfoBuilder setCallObject(TempVar callObject) {
		this.callObject = callObject;
		return this;
	}

	public Optional<List<TempVar>> getParameters() {
		return parameters;
	}

	public DiagramInfoBuilder setParameters(Optional<List<TempVar>> parameters) {
		this.parameters = parameters;
		return this;
	}

	public Optional<TempVar> getReturnValue() {
		return returnValue;
	}

	public DiagramInfoBuilder setReturnValue(Optional<TempVar> returnValue) {
		this.returnValue = returnValue;
		return this;
	}
	
	public DiagramInfo build()
	{
		return new DiagramInfo(this.getName(), this.getCallObject(), this.getParameters(), this.getReturnValue());
	}
	
	public void reset()
	{
		this.setName(null);
		this.setCallObject(null);
		this.setParameters(Optional.empty());
		this.setReturnValue(Optional.empty());
	}
}
