package data;

import java.util.regex.Pattern;

//TODO implement
public final class MultiplicityFactory
{
	private static final String MULTIPLICITY_REGEX = "Unspecified|[0-9]*(\\.\\.|\\.)([0-9]*|\\*)";
	
	private MultiplicityFactory()
	{
		
	}
	
	/**
	 * 
	 * @param multiplicitySpec
	 * 		String of the form m..n or m.n
	 * @param isOrdered
	 * @param isUnique
	 * @return	A multiplicity object matching the given arguments
	 * @throws IllegalArgumentException
	 * 		multiplicitySpec == null || ! isValidMultiplicitySpecialization(multiplicitySpec)
	 * 		|| isOrdered == null || isUnique == null
	 */
	public static Multiplicity parseMultiplicity(String multiplicitySpec, String isOrdered, String isUnique) throws IllegalArgumentException
	{
		if (multiplicitySpec == null)
		{
			throw new IllegalArgumentException("multiplicitySpec cannot be null");
		}
		if (! isValidMultiplicitySpecification(multiplicitySpec))
		{
			throw new IllegalArgumentException("multiplicitySpec does not represent a valid multiplicity");
		}
		if (isOrdered == null)
		{
			throw new IllegalArgumentException("isOrdered cannot be null");
		}
		if (isUnique == null)
		{
			throw new IllegalArgumentException("isUnique cannot be null");
		}
		
		double[] bounds;
		
		if (multiplicitySpec.contains(".."))
		{
			bounds = parseBounds(multiplicitySpec.split("\\.\\."));
		}
		else if (multiplicitySpec.contains("."))
		{
			bounds = parseBounds(multiplicitySpec.split("\\."));
		}
		else
		{
			bounds = new double[]
					{
							1,1
					};
		}
		
		return new Multiplicity(bounds[0], bounds[1], Boolean.parseBoolean(isOrdered), Boolean.parseBoolean(isUnique));
	}
	
	/**
	 * 
	 * @param parts
	 * @return	result[0] is lowerBound; result[1] is upperBound
	 */
	private static double[] parseBounds(String[] parts)
	{
		double[] toReturn = new double[2];
		
		toReturn[0] = Double.parseDouble(parts[0]);
		toReturn[1] = parts[1].equals("*") ? Double.POSITIVE_INFINITY : Double.parseDouble(parts[1]);
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param multiplicitySpec
	 * @return	multiplicitySpec matches the regex Unspecified|[0-9]*(..|.)([0-9]*|\*)
	 */
	public static boolean isValidMultiplicitySpecification(String multiplicitySpec)
	{
		return Pattern.matches(MULTIPLICITY_REGEX, multiplicitySpec);
	}
}
