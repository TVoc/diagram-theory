package theory.theory.sequencediagrams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import parser.XMLParser;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import data.sequencediagrams.SDPoint;

// TODO update for more complex combined fragment handling
// TODO add causes from return points
// TODO overhaul SDPoint progression based on Prev(Time) : Time?

public class CheckpointBuilder
{
	public CheckpointBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		
		this.nonStandardPoints = new ArrayList<SDPoint>();
		this.returnPoints = new ArrayList<SDPoint>();
		this.checkpoints = new TreeMap<SDPoint, String>();
		
		this.nonStandardPoints.add(new SDPoint("finished", -1, false));
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final List<SDPoint> nonStandardPoints;
	
	private final List<SDPoint> getNonStandardPoints() 
	{
		return this.nonStandardPoints;
	}
	
	private final List<SDPoint> returnPoints;
	
	private final List<SDPoint> getReturnPoints()
	{
		return this.returnPoints;
	}
	
	private final Map<SDPoint, String> checkpoints;
	
	private final Map<SDPoint, String> getCheckpoints()
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

		this.getCheckpoints().put(frag.getSdIf(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdIf() + ") <- SDPointAt(t, " + (frag.getSdIf().offset(-1)) + ") & " + ifGuardQuantifiers.toString() + ifGuardBuilder.toString() + frag.getIfGuard() + ").");
		this.getCheckpoints().put(frag.getSdThen(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdThen() + ") <- SDPointAt(t, " + (frag.getSdIf().offset(-1)) + ") & " + thenGuardQuantifiers.toString() + thenGuardBuilder.toString() + frag.getThenGuard() + ").");
		this.getCheckpoints().put(frag.getSdExit(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdExit() + ") <- SDPointAt(t, " + (frag.getSdThen().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdExit().offset(-1)) + ").");
		
		this.getNonStandardPoints().add(frag.getSdIf().offset(-1));
		this.getNonStandardPoints().add(frag.getSdThen().offset(-1));
		
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
		
		this.getCheckpoints().put(frag.getSdStart(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdStart() + ") <- (SDPointAt(t, " + (frag.getSdStart().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdEnd().offset(-1)) + ")) & " + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ").");
		this.getCheckpoints().put(frag.getSdEnd(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdEnd() + ") <- (SDPointAt(t, " + (frag.getSdStart().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdEnd().offset(-1)) + ")) & ~(" + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ")).");
		
		this.getNonStandardPoints().add(frag.getSdStart().offset(-1));
		this.getNonStandardPoints().add(frag.getSdEnd().offset(-1));
		
		return this;
	}
	
	public CheckpointBuilder handleCallPoint(Message call, SeqDiagramStore store)
	{
		this.getNonStandardPoints().add(call.getSDPoint());
		this.getNonStandardPoints().add(store.getLastMessageForDiagram(call.getDiagramName()).getSDPoint());
		
		this.getReturnPoints().add(store.getLastMessageForDiagram(call.getDiagramName()).getSDPoint());
		
		Message callMsg = store.getCallMessage(call);
		
		this.getCheckpoints().put(callMsg.getSDPoint(), "! t [Time] : C_SDPointAt(Next(t), " + callMsg.getSDPoint() + ") <- SDPointAt(t, " + call.getSDPoint() + ").");
		
		return this;
	}
	
//	public CheckpointBuilder handleCallPoint(Message callFrom, Message callTo, SeqDiagramStore store)
//	{
//		this.getNonStandardPoints().add
//	}
	
	public String build()
	{
		StringBuilder toReturn = new StringBuilder();
		
		StringBuilder general = new StringBuilder("! t [Time] s [SDPoint] : C_SDPointAt(Next(t), NextSD(s)) <- SDPointAt(t, s)");
		
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
		
		toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		if (! this.getReturnPoints().isEmpty())
		{
			StringBuilder returnCauses = new StringBuilder("! t [Time] s [SDPoint] : C_SDPoint(Next(t), s) <- ReturnPoint(t, CurrentStackLevel(t), s) & (");
			
			Iterator<SDPoint> it = this.getReturnPoints().iterator();
			
			while (it.hasNext())
			{
				SDPoint returnPoint = it.next();
				
				returnCauses.append("SDPointAt(t, " + returnPoint + ")");
				
				if (it.hasNext())
				{
					returnCauses.append(" | ");
				}
				else
				{
					returnCauses.append(").");
				}
			}
			
			toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(returnCauses.toString(), this.getTabLevel()));
		}
		
		return toReturn.toString();
	}
}
