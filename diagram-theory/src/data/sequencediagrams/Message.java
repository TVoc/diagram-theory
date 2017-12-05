package data.sequencediagrams;

import java.util.Optional;
import java.util.regex.Pattern;

public class Message implements Comparable<Message>
{
	public static final Pattern getterSetternPattern = Pattern.compile("(get|set).*");
	
	public Message(String content, String id, double sdPoint, boolean isReturn, Optional<String> fromName, Optional<String> toName, String diagramName) throws IllegalArgumentException
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
		if (diagramName == null)
		{
			throw new IllegalArgumentException("diagramName cannot be null");
		}
		
		this.content = content;
		this.id = id;
		this.sdPoint = sdPoint;
		this.fromName = fromName;
		this.toName = toName;
		this.fragment = Optional.empty();
		this.isReturn = isReturn;
		this.diagramName = diagramName;
	}
	
	private final String content;
	
	private final String id;
	
	private final double sdPoint;
	
	private final Optional<String> fromName;
	
	private final Optional<String> toName;
	
	private final boolean isReturn;
	
	private final String diagramName;

	public String getContent()
	{
		return this.content;
	}
	
	public String getId()
	{
		return this.id;
	}

	public double getSdPoint()
	{
		return this.sdPoint;
	}
	
	public String getFullSDPoint() {
		return this.getDiagramName() + "_" + (int) this.getSdPoint();
	}
	
	public String getSDPointAsCallPoint()
	{
		return this.getDiagramName() + "_" + (this.getSdPoint() + "post");
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
	
	
	
	public String getDiagramName()
	{
		return this.diagramName;
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
		return this.getFullSDPoint().compareTo(arg0.getFullSDPoint());
	}
}
