package data.sequencediagrams;

import java.util.Optional;

public class Message
{
	public Message(String content, int sdPoint, boolean isReturn, Optional<String> fromId, Optional<String> toId) throws IllegalArgumentException
	{
		if (content == null)
		{
			throw new IllegalArgumentException("content cannot be null");
		}
		if (sdPoint < 1)
		{
			throw new IllegalArgumentException("sdPoint cannot be smaller than 1");
		}
		if (fromId == null)
		{
			throw new IllegalArgumentException("fromId cannot be null");
		}
		if (toId == null)
		{
			throw new IllegalArgumentException("toId cannot be null");
		}
		
		this.content = content;
		this.sdPoint = sdPoint;
		this.fromId = fromId;
		this.toId = toId;
		this.isReturn = isReturn;
	}
	
	private final String content;
	
	private final int sdPoint;
	
	private final Optional<String> fromId;
	
	private final Optional<String> toId;
	
	private final boolean isReturn;

	public String getContent()
	{
		return this.content;
	}

	public int getSdPoint()
	{
		return this.sdPoint;
	}

	public Optional<String> getFromId()
	{
		return this.fromId;
	}
	
	public TempVar getFrom(TempVarContext context) throws IllegalStateException, IllegalArgumentException
	{
		if (! this.getFromId().isPresent())
		{
			throw new IllegalStateException("fromId not present");
		}
		
		return context.resolveTempVar(this.getFromId().get());
	}

	public Optional<String> getToId()
	{
		return this.toId;
	}
	
	public TempVar getTo(TempVarContext context) throws IllegalStateException, IllegalArgumentException
	{
		if (! this.getToId().isPresent())
		{
			throw new IllegalStateException("toId not present");
		}
		
		return context.resolveTempVar(this.getToId().get());
	}
	
	public boolean isReturn()
	{
		return this.isReturn;
	}
}
