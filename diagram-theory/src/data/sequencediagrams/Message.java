package data.sequencediagrams;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Message implements Comparable<Message>
{
	public static final Pattern getterSetternPattern = Pattern.compile("(get|set).*");
	
	public Message(String content, String id, int sdPoint, boolean isReturn, Optional<String> fromName, Optional<String> toName,
			String diagramName, boolean post) throws IllegalArgumentException
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
		
		this.content = content.replaceAll("\\s*", "");
		this.id = id;
		this.sdPoint = new SDPoint(diagramName, sdPoint, post);
		this.fromName = fromName;
		this.toName = toName;
		this.fragment = Optional.empty();
		this.isReturn = isReturn;
		this.diagramName = diagramName;
	}
	
	public Message(String content, String id, int sdPoint, boolean isReturn, Optional<String> fromName, Optional<String> toName,
			String diagramName, boolean post, CombinedFragment fragment) throws IllegalArgumentException
	{
		this(content, id, sdPoint, isReturn, fromName, toName, diagramName, post);
		
		if (fragment == null)
		{
			throw new IllegalArgumentException("fragment cannot be null");
		}
		
		this.fragment = Optional.of(fragment);
	}
	
	private final String content;
	
	private final String id;
	
	private final SDPoint sdPoint;
	
	private final Optional<String> fromName;
	
	private final Optional<String> toName;
	
	private final boolean isReturn;
	
	private final String diagramName;
	
	private Optional<CombinedFragment> fragment;

	public String getContent()
	{
		return this.content;
	}
	
	public String getId()
	{
		return this.id;
	}

	public SDPoint getSDPoint()
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
		return this.fragment;
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
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((diagramName == null) ? 0 : diagramName.hashCode());
		result = prime * result + ((fromName == null) ? 0 : fromName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isReturn ? 1231 : 1237);
		result = prime * result + ((sdPoint == null) ? 0 : sdPoint.hashCode());
		result = prime * result + ((toName == null) ? 0 : toName.hashCode());
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
		if (content == null)
		{
			if (other.content != null)
				return false;
		}
		else if (!content.equals(other.content))
			return false;
		if (diagramName == null)
		{
			if (other.diagramName != null)
				return false;
		}
		else if (!diagramName.equals(other.diagramName))
			return false;
		if (fromName == null)
		{
			if (other.fromName != null)
				return false;
		}
		else if (!fromName.equals(other.fromName))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (isReturn != other.isReturn)
			return false;
		if (sdPoint == null)
		{
			if (other.sdPoint != null)
				return false;
		}
		else if (!sdPoint.equals(other.sdPoint))
			return false;
		if (toName == null)
		{
			if (other.toName != null)
				return false;
		}
		else if (!toName.equals(other.toName))
			return false;
		return true;
	}

	@Override
	public int compareTo(Message arg0)
	{
		return this.getSDPoint().compareTo(arg0.getSDPoint());
	}
	
	public String toString()
	{
		return "Content: " + this.getContent() + "; SDPoint: " + this.getSDPoint().toString();
	}
}
