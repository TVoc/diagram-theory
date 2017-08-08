package data.sequencediagrams;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExitForMessageBuilder
{
	public ExitForMessageBuilder(Message message) throws IllegalArgumentException
	{
		if (message == null)
		{
			throw new IllegalArgumentException("message cannot be null");
		}
		
		this.message = message;
		this.exitTos = new HashMap<Message, String>();
	}
	
	private final Message message;
	
	private final Map<Message, String> exitTos;

	public Message getMessage()
	{
		return this.message;
	}

	private Map<Message, String> internalGetExitTos()
	{
		return this.exitTos;
	}
	
	public Map<Message, String> getExitTos()
	{
		return Collections.unmodifiableMap(this.internalGetExitTos());
	}
	
	public ExitForMessageBuilder putExit(Message message, String guard)
	{
		this.internalGetExitTos().put(message, guard);
		
		return this;
	}
	
	public ExitForMessageBuilder putExits(Map<Message, String> exits)
	{
		this.internalGetExitTos().putAll(exits);
		
		return this;
	}
	
	public ExitForMessage build()
	{
		return new ExitForMessage(this.getMessage(), this.getExitTos());
	}
}
