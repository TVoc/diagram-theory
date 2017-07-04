package data.classdiagrams;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an association between several types defined in the UML Class
 * Diagram
 * 
 * @author Thomas
 *
 */
public class Association implements Iterable<AssociationEnd>
{
	/**
	 * 
	 * @param associationEnds
	 * @throws IllegalArgumentException
	 *             associationEnds == null || associationEnds.size() < 2
	 */
	Association(Set<AssociationEnd> associationEnds) throws IllegalArgumentException
	{
		if (associationEnds == null)
		{
			throw new IllegalArgumentException("associationEnds cannot be null");
		}
		if (associationEnds.size() < 2)
		{
			throw new IllegalArgumentException("associationEnds must have at least two elements");
		}

		this.associationEnds = associationEnds;
	}

	private Set<AssociationEnd> associationEnds;

	private Set<AssociationEnd> internalGetAssociationEnds()
	{
		return this.associationEnds;
	}

	/**
	 * 
	 * @return An unmodifiable view of this association's association ends
	 */
	public Set<AssociationEnd> getAssociationEnds()
	{
		return Collections.unmodifiableSet(this.internalGetAssociationEnds());
	}

	public Iterator<AssociationEnd> iterator()
	{
		return this.getAssociationEnds().iterator();
	}

	/**
	 * 
	 * @return The number of this association's association ends
	 */
	public int getNbOfEnds()
	{
		return this.internalGetAssociationEnds().size();
	}
	
	public Set<String> getClassNames(TypeContext context)
	{
		Set<String> toReturn = new HashSet<String>();
		
		for (AssociationEnd ele : this)
		{
			toReturn.add(ele.getTypeName(context));
		}
		
		return toReturn;
	}
	
	public boolean containsAll(TypeContext context, Collection<String> classes)
	{
		return this.getClassNames(context).containsAll(classes);
	}
	
	public int positionOf(TypeContext context, String className)
	{
		int toReturn = 1;
		
		for (AssociationEnd ele : this)
		{
			if (ele.getTypeName(context).equals(className))
			{
				return toReturn;
			}
			
			toReturn++;
		}
		
		return -1;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
