package data.sequencediagrams;

public class LoopCombinedFragment
{
	public LoopCombinedFragment(String guard, int sdStart, int sdEnd) throws IllegalArgumentException
	{
		if (guard == null)
		{
			throw new IllegalArgumentException("guard cannot be null");
		}
		if (sdStart < 1)
		{
			throw new IllegalArgumentException("sdStart cannot be less than 1");
		}
		if (sdEnd <= sdStart)
		{
			throw new IllegalArgumentException("sdEnd cannot be less than or equal to sdStart");
		}
		
		this.guard = guard;
		this.sdStart = sdStart;
		this.sdEnd = sdEnd;
	}
	
	private final String guard;
	
	private final int sdStart;
	
	private final int sdEnd;

	public String getGuard()
	{
		return this.guard;
	}

	public int getSdStart()
	{
		return this.sdStart;
	}

	public int getSdEnd()
	{
		return this.sdEnd;
	}
}
