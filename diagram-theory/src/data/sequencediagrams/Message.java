package data.sequencediagrams;

import java.util.Optional;

public class Message implements Comparable<Message>
{
	public Message(String content, String id, int sdPoint, boolean isReturn, Optional<String> fromName, Optional<String> toName) throws IllegalArgumentException
	{
		if (content == null)
		{
			throw new IllegalArgumentException("content cannot be null");
		}
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
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
		this.id = id;
		this.sdPoint = sdPoint;
		this.fromName = fromName;
		this.toName = toName;
		this.fragment = Optional.empty();
		this.isReturn = isReturn;
	}
	
	private final String content;
	
	private final String id;
	
	private final int sdPoint;
	
	private final Optional<String> fromName;
	
	private final Optional<String> toName;
	
	private Optional<CombinedFragment> fragment;
	
	private final boolean isReturn;

	public String getContent()
	{
		return this.content;
	}
	
	public String getId()
	{
		return this.id;
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
	
	public Optional<CombinedFragment> getFragment()
	{
		return this.getFragment();
	}
	
	public void setFragment(Optional<CombinedFragment> fragment) throws IllegalArgumentException
	{
		if (fragment == null)
		{
			throw new IllegalArgumentException("fragment cannot be null");
		}
		
		this.fragment = fragment;
	}
	
	public boolean isReturn()
	{
		return this.isReturn;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Message other = (Message) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Message arg0)
	{
		return Integer.compare(this.getSdPoint(), arg0.getSdPoint());
	}
}
