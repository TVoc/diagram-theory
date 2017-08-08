package theory.theory.sequencediagrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.TempVar;
import parser.XMLParser;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;

public class CheckpointBuilder
{
	public CheckpointBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		
		this.nonStandardPoints = new ArrayList<Integer>();
		this.checkpoints = new TreeMap<Integer, String>();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final List<Integer> nonStandardPoints;
	
	private final List<Integer> getNonStandardPoints() 
	{
		return this.nonStandardPoints;
	}
	
	private final Map<Integer, String> checkpoints;
	
	private final Map<Integer, String> getCheckpoints()
	{
		return this.checkpoints;
	}
	
	public CheckpointBuilder handleAltCombinedFragment(AltCombinedFragment frag, SeqDiagramStore store)
	{
		StringBuilder ifGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder ifGuardBuilder = new StringBuilder(": ");
		StringBuilder thenGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder thenGuardBuilder = new StringBuilder(": ");
		
		String[] ifGuardParts = frag.getIfGuard().split(XMLParser.TEMPVAR_SEPARATOR);
		
		for (String part : ifGuardParts)
		{
			if (part.equals("") || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}
			
			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);
			
			ifGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			ifGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}
		
		String[] thenGuardParts = frag.getThenGuard().split(XMLParser.TEMPVAR_SEPARATOR);
		
		for (String part : thenGuardParts)
		{
			if (part.equals("") || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}
			
			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);
			
			thenGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			thenGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}

		this.getCheckpoints().put(frag.getSdIf(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdIf() + ") <- SDPointAt(t, " + (frag.getSdIf() - 1) + ") & " + ifGuardQuantifiers.toString() + ifGuardBuilder.toString() + frag.getIfGuard() + ").");
		this.getCheckpoints().put(frag.getSdThen(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdThen() + ") <- SDPointAt(t, " + (frag.getSdIf() - 1) + ") & " + thenGuardQuantifiers.toString() + thenGuardBuilder.toString() + frag.getThenGuard() + ").");
		this.getCheckpoints().put(frag.getSdExit(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdExit() + ") <- SDPointAt(t, " + (frag.getSdThen() - 1) + ") | SDPointAt(t, " + (frag.getSdExit() - 1) + ").");
		
		this.getNonStandardPoints().add(frag.getSdIf() - 1);
		this.getNonStandardPoints().add(frag.getSdThen() - 1);
		
		return this;
	}
	
	public CheckpointBuilder handleLoopCombinedFragment(LoopCombinedFragment frag, SeqDiagramStore store)
	{
		StringBuilder loopGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder loopGuardBuilder = new StringBuilder(": ");
		
		String[] loopGuardParts = frag.getGuard().split(XMLParser.TEMPVAR_SEPARATOR);
		
		for (String part : loopGuardParts)
		{
			if (part.equals(""))
			{
				continue;
			}
			
			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);
			
			loopGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			loopGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}
		
		this.getCheckpoints().put(frag.getSdStart(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdStart() + ") <- (SDPointAt(t, " + (frag.getSdStart() - 1) + ") | SDPointAt(t, " + (frag.getSdEnd() - 1) + ")) & " + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ").");
		this.getCheckpoints().put(frag.getSdEnd(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdEnd() + ") <- (SDPointAt(t, " + (frag.getSdStart() - 1) + ") | SDPointAt(t, " + (frag.getSdEnd() - 1) + ")) & ~(" + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ")).");
		
		this.getNonStandardPoints().add(frag.getSdStart() - 1);
		this.getNonStandardPoints().add(frag.getSdEnd() - 1);
		
		return this;
	}
	
	public String build()
	{
		StringBuilder toReturn = new StringBuilder();
		
		StringBuilder general = new StringBuilder("! t [Time] s [SDPoint] : C_SDPointAt(Next(t), (s+1)) <- SDPointAt(t, s)");
		
		if (! this.getNonStandardPoints().isEmpty())
		{
			general.append("& ~(");
			
			for (int i = 0; i < this.getNonStandardPoints().size(); i++)
			{
				if (i == this.getNonStandardPoints().size() - 1)
				{
					general.append("(s = " + this.getNonStandardPoints().get(i) + ")).");
				}
				else
				{
					general.append("(s = " + this.getNonStandardPoints().get(i) + ") | ");
				}
			}
		}
		else
		{
			general.append(".");
		}
		
		toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(general.toString(), this.getTabLevel()));
		
		for (String ele : this.getCheckpoints().values())
		{
			toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(ele, this.getTabLevel()));
		}
		
		toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(tabLevel));
		
		return toReturn.toString();
	}
}
