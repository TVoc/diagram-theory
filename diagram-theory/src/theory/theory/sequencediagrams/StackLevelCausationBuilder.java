package theory.theory.sequencediagrams;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.sequencediagrams.Message;
import data.sequencediagrams.SDPoint;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;

public class StackLevelCausationBuilder
{
	public StackLevelCausationBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		this.callPoints = new TreeSet<SDPoint>();
		this.returnPoints = new TreeSet<SDPoint>();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final Set<SDPoint> callPoints;
	
	private Set<SDPoint> getCallPoints()
	{
		return this.callPoints;
	}
	
	private final Set<SDPoint> returnPoints;
	
	private Set<SDPoint> getReturnPoints()
	{
		return this.returnPoints;
	}
	
	public StackLevelCausationBuilder handleCallPoint(Message call, SeqDiagramStore store)
	{
		this.getCallPoints().add(call.getSDPoint());
		this.getReturnPoints().add(store.getLastMessageForDiagram(call.getDiagramName()).getSDPoint());
		
		return this;
	}
	
	public String build()
	{
		StringBuilder inc = new StringBuilder("! t [Time] st [StackLevel] : C_CurrentStackLevel(Next(t), st) <- (CurrentStackLevel(t) = (st - 1)) & (");
		
		Iterator<SDPoint> callPoints = this.getCallPoints().iterator();
		
		while (callPoints.hasNext())
		{
			SDPoint callPoint = callPoints.next();
			inc.append("SDPointAt(t, " + callPoint + ")");
			
			if (callPoints.hasNext())
			{
				inc.append(" | ");
			}
			else
			{
				inc.append(").");
			}
		}
		
		StringBuilder dec = new StringBuilder("! t [Time] st [StackLevel] : C_CurrentStackLevel(Next(t), st) <- (CurrentStackLevel(t) = (st + 1)) & (");
		
		Iterator<SDPoint> returnPoints = this.getReturnPoints().iterator();
		
		while (returnPoints.hasNext())
		{
			SDPoint returnPoint = returnPoints.next();
			dec.append("SDPointAt(t, " + returnPoint + ")");
			
			if (returnPoints.hasNext())
			{
				dec.append(" | ");
			}
			else
			{
				dec.append(").");
			}
		}
		
		return OutputConvenienceFunctions.insertTabsNewLine(inc.toString(), this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine(dec.toString(), this.getTabLevel());
	}
}
