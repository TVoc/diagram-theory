package data.sequencediagrams;

public class AltCombinedFragment
{
	public AltCombinedFragment(String ifGuard, String thenGuard, int sdIf, int sdThen, int sdExit) throws IllegalArgumentException
	{
		if (ifGuard == null)
		{
			throw new IllegalArgumentException("ifGuard cannot be null");
		}
		if (thenGuard == null)
		{
			throw new IllegalArgumentException("thenGuard cannot be null");
		}
		if (sdIf < 1)
		{
			throw new IllegalArgumentException("sdIf cannot be smaller than 1");
		}
		if (sdThen <= sdIf)
		{
			throw new IllegalArgumentException("sdThen cannot be less than or equal to sdIf");
		}
		if (sdExit <= sdThen)
		{
			throw new IllegalArgumentException("sdExit cannot be less than or equal to sdThen");
		}
		
		this.ifGuard = ifGuard;
		this.thenGuard = thenGuard;
		this.sdIf = sdIf;
		this.sdThen = sdThen;
		this.sdExit = sdExit;
	}
	
	private final String ifGuard;
	
	private final String thenGuard;
	
	private final int sdIf;
	
	private final int sdThen;
	
	private final int sdExit;

	public String getIfGuard()
	{
		return this.ifGuard;
	}

	public String getThenGuard()
	{
		return this.thenGuard;
	}
	
	public int getSdIf()
	{
		return this.sdIf;
	}

	public int getSdThen()
	{
		return this.sdThen;
	}

	public int getSdExit()
	{
		return this.sdExit;
	}
}
