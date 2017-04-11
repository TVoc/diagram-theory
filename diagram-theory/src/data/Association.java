package data;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

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
}
