package data.classdiagrams;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AssociationBuilder
{
	public AssociationBuilder()
	{
		this.associationEnds = new HashSet<AssociationEnd>();
	}
	
	private Set<AssociationEnd> associationEnds;
	
	private Set<AssociationEnd> internalGetAssociationEnds()
	{
		return this.associationEnds;
	}
	
	/**
	 * 
	 * @return	An unmodifiable view of the new association's current association ends
	 */
	public Set<AssociationEnd> getAssociationEnds()
	{
		return Collections.unmodifiableSet(this.internalGetAssociationEnds());
	}
	
	/**
	 * 
	 * @param associationEnd
	 * @return
	 * @throws IllegalArgumentException
	 * 		associationEnd == null
	 */
	public AssociationBuilder addAssociationEnd(AssociationEnd associationEnd) throws IllegalArgumentException
	{
		if (associationEnd == null)
		{
			throw new IllegalArgumentException("associationEnd cannot be null");
		}
		
		this.internalGetAssociationEnds().add(associationEnd);
		
		return this;
	}
	
	/**
	 * Removes the given association end, if present.
	 * 
	 * @param associationEnd
	 * @return
	 */
	public AssociationBuilder removeAssociationEnd(AssociationEnd associationEnd)
	{
		this.internalGetAssociationEnds().remove(associationEnd);
		
		return this;
	}
	
	public AssociationBuilder clearAssociationEnds()
	{
		this.internalGetAssociationEnds().clear();
		
		return this;
	}
	
	/**
	 * Prepare this builder for reuse.
	 */
	private void reset()
	{
		this.associationEnds = new HashSet<AssociationEnd>();
	}
	
	/**
	 * 
	 * @return	A new association with the current association ends. This builder may afterwards be reused to construct a different association.
	 * @throws IllegalStateException
	 * 		this.getAssociationEnds().size() < 2
	 */
	public Association build() throws IllegalStateException
	{
		try
		{
			Association toReturn = new Association(this.internalGetAssociationEnds());
			this.reset();
			return toReturn;
		}
		catch(IllegalArgumentException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
