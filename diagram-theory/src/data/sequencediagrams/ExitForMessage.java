package data.sequencediagrams;

import java.util.Collections;
import java.util.Map;

public class ExitForMessage
{
	public ExitForMessage(Message message, Map<Message, String> exitTos) throws IllegalArgumentException
	{
		if (message == null)
		{
			throw new IllegalArgumentException("message cannot be null");
		}
		if (exitTos == null)
		{
			throw new IllegalArgumentException("exitTos cannot be null");
		}
		
		this.message = message;
		this.exitTos = exitTos;
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
}
