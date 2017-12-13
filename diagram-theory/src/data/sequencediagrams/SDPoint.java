package data.sequencediagrams;

public class SDPoint implements Comparable<SDPoint>
{
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
			return Boolean.compare(this.isPost(), o.isPost());
		}
		
		return Integer.compare(this.getSequenceNumber(), o.getSequenceNumber());
	}
}
