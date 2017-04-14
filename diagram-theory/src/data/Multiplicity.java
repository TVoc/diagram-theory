package data;

import java.util.Optional;

/**
 * Represents the concept of multiplicity in UML Class Diagrams
 * 
 * @author Thomas
 *
 */
public class Multiplicity
{

	public static final Multiplicity EXACTLY_ONE = new Multiplicity(1, 1, false, false);
	public static final Multiplicity OPTIONAL = new Multiplicity(0, 1, false, false);

	/**
	 * Creates a new multiplicity object with the given lower and upper bounds.
	 * 
	 * @param lowerBound
	 *            The lower bound of this multiplicity
	 * @param upperBound
	 *            The upper bound of this multiplicity
	 * @param isOrdered
	 *            Only relevant if this multiplicity is greater than one.
	 *            Indicates if a total ordering is enforced.
	 * @param isUnique
	 *            Only relevant if this multiplicity is greater than one.
	 *            Indicates if uniqueness if enforced.
	 * @throws IllegalArgumentException
	 *             lowerBound < 0 || upperBound < 0 || lowerBound > upperBound
	 */
	Multiplicity(double lowerBound, double upperBound, boolean isOrdered, boolean isUnique)
			throws IllegalArgumentException
	{
		if (lowerBound < 0)
		{
			throw new IllegalArgumentException("lowerBound cannot be negative");
		}
		if (upperBound < 0)
		{
			throw new IllegalArgumentException("upperBound cannot be negative");
		}
		if (lowerBound > upperBound)
		{
			throw new IllegalArgumentException("lowerBound cannot be greater than upperBound");
		}

		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		
		if (upperBound > 1)
		{
			this.collectionProperties = Optional.of(new CollectionProperties(isOrdered, isUnique));
		}
		else
		{
			this.collectionProperties = Optional.empty();
		}
	}

	/**
	 * Initializes a new multiplicity with the given bounds such that: !
	 * (new.isOrdered() || new.isUnique())
	 * 
	 * @throws IllegalArgumentException
	 *             See {@link #Multiplicity(double, double, boolean, boolean)}
	 */
	Multiplicity(double lowerBound, double upperBound) throws IllegalArgumentException
	{
		this(lowerBound, upperBound, false, false);
	}

	private final double lowerBound;
	private final double upperBound;

	public double getLowerBound()
	{
		return this.lowerBound;
	}

	public double getUpperBound()
	{
		return this.upperBound;
	}

	/**
	 * 
	 * @return This multiplicity expresses that it may be the case that no
	 *         element is present
	 */
	public boolean isOptional()
	{
		return this.lowerBound == 0;
	}

	/**
	 * 
	 * @return This multiplicity expresses that there may be more than one
	 *         element present
	 */
	public boolean isCollection()
	{
		return this.upperBound > 1;
	}

	private final Optional<CollectionProperties> collectionProperties;
	
	private Optional<CollectionProperties> getCollectionProperties()
	{
		return this.collectionProperties;
	}
	
	/**
	 * 
	 * @return	If this multiplicity represents a collection, then determine what type of collection it is
	 * @throws IllegalStateException
	 * 		! this.isCollection()
	 */
	public String getCollectionType() throws IllegalStateException
	{
		if (! this.isCollection())
		{
			throw new IllegalStateException("multiplicity does not represent a collection");
		}
		
		return this.getCollectionProperties().get().getCollectionName();
	}

	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collectionProperties == null) ? 0 : collectionProperties.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lowerBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(upperBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Multiplicity other = (Multiplicity) obj;
		if (collectionProperties == null)
		{
			if (other.collectionProperties != null)
				return false;
		}
		else if (!collectionProperties.equals(other.collectionProperties))
			return false;
		if (Double.doubleToLongBits(lowerBound) != Double.doubleToLongBits(other.lowerBound))
			return false;
		if (Double.doubleToLongBits(upperBound) != Double.doubleToLongBits(other.upperBound))
			return false;
		return true;
	}

	/**
	 * 
	 * @param description
	 * @return	A Multiplicity object that corresponds to the specified description
	 * @throws IllegalArgumentException
	 * 		! (description.equals("Set") || description.equals("Bag") || description.equals("Sequence")
	 * 			|| description.equals("List") || description.equals("OrderedSet")) || description.equals("Optional")
	 */
	public static Multiplicity descriptionToMultiplicity(String description) throws IllegalArgumentException
	{
		switch (description)
		{
			case "Set":
				return new Multiplicity(0, Double.POSITIVE_INFINITY, false, true);
			case "Bag":
				return new Multiplicity(0, Double.POSITIVE_INFINITY, false, false);
			case "Sequence":
				return new Multiplicity(0, Double.POSITIVE_INFINITY, true, false);
			case "OrderedSet":
				return new Multiplicity(0, Double.POSITIVE_INFINITY, true, true);
			case "List":
				return new Multiplicity(0, Double.POSITIVE_INFINITY, false, false);
			case "Optional":
				return OPTIONAL;
			default:
				throw new IllegalArgumentException("collectionType not recognized");
		}
	}

	private class CollectionProperties
	{
		CollectionProperties(boolean isOrdered, boolean isUnique)
		{
			this.isOrdered = isOrdered;
			this.isUnique = isUnique;
		}
		
		private final boolean isOrdered;
		private final boolean isUnique;

		public boolean isOrdered()
		{
			return this.isOrdered;
		}

		public boolean isUnique()
		{
			return this.isUnique;
		}
		
		public String getCollectionName()
		{
			if (! this.isOrdered() && ! this.isUnique())
			{
				return "Bag";
			}
			else if (! this.isOrdered() && this.isUnique())
			{
				return "Set";
			}
			else if (this.isOrdered() && ! this.isUnique())
			{
				return "Sequence";
			}
			else // (this.isOrdered && this.isUnique)
			{
				return "OrderedSet";
			}
		}
	}
}
