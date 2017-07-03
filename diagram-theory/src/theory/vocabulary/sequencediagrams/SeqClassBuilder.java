package theory.vocabulary.sequencediagrams;

import data.classdiagrams.Class;
import data.classdiagrams.Generalization;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;;

public class SeqClassBuilder
{
	public SeqClassBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final StringBuilder stringBuilder;
	
	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public SeqClassBuilder addClass(Class clazz, SeqDiagramStore store)
	{
		String line = "type " + clazz.getName();
		
		for (Generalization generalization : store.getGeneralizations())
		{
			String subType = generalization.getSubtype(store);
			
			if (subType.equals(clazz.getName()))
			{
				line = line + " isa " + generalization.getSupertype(store);
				break;
			}
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(line, this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
