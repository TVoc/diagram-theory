package data.sequencediagrams;

import java.util.Optional;

public class Message
{
	public Message(String content, int sdPoint, boolean isReturn, Optional<String> fromName, Optional<String> toName) throws IllegalArgumentException
	{
		if (content == null)
		{
			throw new IllegalArgumentException("content cannot be null");
		}
		if (sdPoint < 1)
		{
			throw new IllegalArgumentException("sdPoint cannot be smaller than 1");
		}
		if (fromName == null)
		{
			throw new IllegalArgumentException("fromId cannot be null");
		}
		if (toName == null)
		{
			throw new IllegalArgumentException("toId cannot be null");
		}
		
		this.content = content;
		this.sdPoint = sdPoint;
		this.fromName = fromName;
		this.toName = toName;
		this.isReturn = isReturn;
	}
	
	private final String content;
	
	private final int sdPoint;
	
	private final Optional<String> fromName;
	
	private final Optional<String> toName;
	
	private final boolean isReturn;

	public String getContent()
	{
		return this.content;
	}

	public int getSdPoint()
	{
		return this.sdPoint;
	}

	public Optional<String> getFromName()
	{
		return this.fromName;
	}
	
	public TempVar getFrom(TempVarContext context) throws IllegalStateException, IllegalArgumentException
	{
		if (! this.getFromName().isPresent())
		{
			throw new IllegalStateException("fromId not present");
		}
		
		return context.resolveTempVar(this.getFromName().get());
	}

	public Optional<String> getToName()
	{
		return this.toName;
	}
	
	public TempVar getTo(TempVarContext context) throws IllegalStateException, IllegalArgumentException
	{
		if (! this.getToName().isPresent())
		{
			throw new IllegalStateException("toId not present");
		}
		
		return context.resolveTempVar(this.getToName().get());
	}
	
	public boolean isReturn()
	{
		return this.isReturn;
	}
}
