package theory;

public class SeqFactors extends Factors
{

	public SeqFactors(int numObjects, double stringFactor, double intFactor, double floatFactor, int timeSteps)
	{
		super(numObjects, stringFactor, intFactor, floatFactor);
		
		this.timeSteps = timeSteps;
	}
	
	private final int timeSteps;
	
	public int getTimeSteps()
	{
		return this.timeSteps;
	}
}
