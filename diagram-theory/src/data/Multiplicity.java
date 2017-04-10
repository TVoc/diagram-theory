package data;

/**
 * Represents the concept of multiplicity in UML Class Diagrams
 * 
 * @author Thomas
 *
 */
public class Multiplicity {
	
	/**
	 * Creates a new multiplicity object with the given lower and upper bounds.
	 * 
	 * @param lowerBound
	 * 		The lower bound of this multiplicity
	 * @param upperBound
	 * 		The upper bound of this multiplicity
	 * @throws IllegalArgumentException
	 * 		lowerBound < 0 || upperBound < 0 || lowerBound > upperBound
	 */
	Multiplicity(double lowerBound, double upperBound) throws IllegalArgumentException
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

}
