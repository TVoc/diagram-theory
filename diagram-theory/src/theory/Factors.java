package theory;

public class Factors
{

	public Factors(int numObjects, double stringFactor, double intFactor, double floatFactor)
	{
		this.numObjects = numObjects;
		this.stringFactor = stringFactor;
		this.intFactor = intFactor;
		this.floatFactor = floatFactor;
	}
	
	private final int numObjects;
	private final double stringFactor;
	private final double intFactor;
	private final double floatFactor;
	
	public int getNumObjects()
	{
		return this.numObjects;
	}
	public double getStringFactor()
	{
		return this.stringFactor;
	}
	public double getIntFactor()
	{
		return this.intFactor;
	}
	public double getFloatFactor()
	{
		return this.floatFactor;
	}
}
