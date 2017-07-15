package theory.structure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import theory.DiagramStore;
import theory.OutputConvenienceFunctions;
import theory.SeqFactors;
import data.classdiagrams.Class;
import data.classdiagrams.Generalization;

public class SeqStructureBuilder
{
	public final double FLOAT_STEP = 0.5;
	
	public SeqStructureBuilder(int tabLevel, SeqFactors factors, Set<String> tempVarNames)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder(OutputConvenienceFunctions.insertTabsNewLine("structure S:V {", tabLevel));
		this.factors = factors;
		
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine("Time = { 0.." + factors.getTimeSteps() + " }", tabLevel + 1));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine("Start = 0", tabLevel + 1));
		
		StringBuilder nextBuilder = new StringBuilder("Next = { ");
		
		for (int i = 0; i < factors.getTimeSteps(); i++)
		{
			if (i == (factors.getTimeSteps() - 1))
			{
				nextBuilder.append(i + "->" + (i+1) + " }");
			}
			else
			{
				nextBuilder.append(i + "->" + (i+1) + "; ");
			}
		}
		
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine(nextBuilder.toString(), tabLevel + 1));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsBlankLine(tabLevel));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine("I_SDPointAt = { 1 }", tabLevel + 1));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsBlankLine(tabLevel));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine("LimitedInt = { " + (int) (- factors.getNumObjects() * factors.getIntFactor() * 0.5 - 1)
				+ ".." + (int) (factors.getNumObjects() * factors.getIntFactor() * 0.5 + 1) + " }", tabLevel + 1));
		
		StringBuilder floatBuilder = new StringBuilder("LimitedFloat = { 0.0; ");
		
		double abs = 0.5;
		
		for (int i = 1; i < factors.getNumObjects() * factors.getFloatFactor(); i += 2)
		{
			floatBuilder.append(abs + "; " + (-abs));
			
			if (i >= (factors.getNumObjects() * factors.getFloatFactor()) - 2)
			{
				floatBuilder.append("}");
			}
			else
			{
				floatBuilder.append("; ");
			}
		}
		
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine(floatBuilder.toString(), tabLevel + 1));
		
		StringBuilder stringDomainBuilder = new StringBuilder("LimitedString = { ");
		Set<String> generated = new HashSet<String>();
		
		for (int i = 0; i < factors.getNumObjects() * factors.getStringFactor(); i++)
		{
			boolean newString = false;
			
			while (! newString)
			{
				String ran = RandomStringUtils.random(20, true, true);
				
				if (generated.contains(ran))
				{
					continue;
				}
				
				generated.add(ran);
				
				if ((i == (factors.getNumObjects() * factors.getStringFactor()) - 1) & tempVarNames.isEmpty())
				{
					System.out.println("reached");
					stringDomainBuilder.append("\"" + ran + "\" }");
				}
				else
				{
					stringDomainBuilder.append("\"" + ran + "\"; ");
				}
				
				newString = true;
			}
		}
		
		Iterator<String> tempVarIterator = tempVarNames.iterator();
		
		while (tempVarIterator.hasNext())
		{
			String ele = tempVarIterator.next();
			
			if (tempVarIterator.hasNext())
			{
				stringDomainBuilder.append("\"" + ele + "\"; ");
			}
			else
			{
				stringDomainBuilder.append("\"" + ele + "\"} ");
			}
		}
		
		stringBuilder.append(OutputConvenienceFunctions.insertTabsNewLine(stringDomainBuilder.toString(), tabLevel + 1));
		stringBuilder.append(OutputConvenienceFunctions.insertTabsBlankLine(tabLevel + 1));
	}
	
	private final int tabLevel;
	
	private final StringBuilder stringBuilder;
	
	private final SeqFactors factors;

	public int getTabLevel()
	{
		return this.tabLevel;
	}

	public StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public SeqFactors getFactors()
	{
		return this.factors;
	}
	
	public SeqStructureBuilder processClasses(DiagramStore store)
	{	
		for (Class clazz : store.getClasses().values())
		{
			StringBuilder classLine = new StringBuilder(clazz.getName() + " = { ");
					
			for (int i = 0; i < this.getFactors().getNumObjects(); i++)
			{
				if (i == this.getFactors().getNumObjects() - 1)
				{
					classLine.append(clazz.getName() + (i+1));
				}
				else
				{
					classLine.append(clazz.getName() + (i+1) + "; ");
				}
			}
			
			for (Generalization generalization : store.getGeneralizations())
			{
				if (generalization.getSupertype(store).equals(clazz.getName()))
				{
					for (int i = 0; i < this.getFactors().getNumObjects(); i++)
					{
						classLine.append("; " + generalization.getSubtype(store) + (i+1));
					}
				}
			}
			
			classLine.append("}");
			
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(classLine.toString(), this.getTabLevel() + 1));
		}
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString() + OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel());
	}
}
