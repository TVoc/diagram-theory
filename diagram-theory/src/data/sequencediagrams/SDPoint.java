package data.sequencediagrams;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SDPoint implements Comparable<SDPoint>
{
	public static final SDPoint FINISHED = new SDPoint("finished", -1, false);
	public static final SDPoint DUMMY_1 = new SDPoint("dummy", 1, false);
	public static final SDPoint DUMMY_2 = new SDPoint("dummy", 2, false);
	public static final SDPoint DUMMY_3 = new SDPoint("dummy", 3, false);
	
	public SDPoint(String diagramName, int sequenceNumber, boolean post) throws IllegalArgumentException
	{
		if (diagramName == null)
		{
			throw new IllegalArgumentException("diagramName cannot be null");
		}
		
		this.diagramName = diagramName;
		this.sequenceNumber = sequenceNumber;
		this.post = post;
	}
	
	private final String diagramName;
	
	private final int sequenceNumber;
	
	private final boolean post;
	
	public String getDiagramName()
	{
		return this.diagramName;
	}
	
	public int getSequenceNumber()
	{
		return this.sequenceNumber;
	}
	
	public boolean isPost()
	{
		return this.post;
	}
	
	public SDPoint offset(int offset, boolean post)
	{
		return new SDPoint(this.getDiagramName(), this.getSequenceNumber() - offset, post);
	}
	
	public SDPoint offset(int offset)
	{
		return this.offset(offset, false);
	}
	
	public String toString()
	{
		if (this.getDiagramName().equals("finished"))
		{
			return this.getDiagramName();
		}
		return this.isPost() ? this.getDiagramName() + "_" + this.getSequenceNumber() + "post" : this.getDiagramName() + "_" + this.getSequenceNumber();
	}

	@Override
	public int compareTo(SDPoint o)
	{
		if (! this.getDiagramName().equals(o.getDiagramName()))
		{
			return this.getDiagramName().compareTo(o.getDiagramName());
		}
		
		if (this.getSequenceNumber() == o.getSequenceNumber())
		{
			return Boolean.compare(this.isPost(), o.isPost()); // TODO evaluate effect of reversing order
		}
		
		return Integer.compare(this.getSequenceNumber(), o.getSequenceNumber());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagramName == null) ? 0 : diagramName.hashCode());
		result = prime * result + (post ? 1231 : 1237);
		result = prime * result + sequenceNumber;
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
		SDPoint other = (SDPoint) obj;
		if (diagramName == null)
		{
			if (other.diagramName != null)
				return false;
		}
		else if (!diagramName.equals(other.diagramName))
			return false;
		if (post != other.post)
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}
}
