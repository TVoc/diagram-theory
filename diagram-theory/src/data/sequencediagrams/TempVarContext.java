package data.sequencediagrams;

public interface TempVarContext
{
	public boolean hasTempVar(String id);
	
	public TempVar resolveTempVar(String id) throws IllegalArgumentException;
}
