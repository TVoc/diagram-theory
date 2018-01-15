package theory.theory.sequencediagrams;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.sequencediagrams.Message;
import data.sequencediagrams.SDPoint;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;

public class ReturnPointCausationBuilder
{
	public ReturnPointCausationBuilder(int tabLevel, SeqDiagramStore store)
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
	
	public ReturnPointCausationBuilder handleCallPoint(Message call, SeqDiagramStore store)
	{
		this.getCallPoints().add(call.getSDPoint());
		this.getReturnPoints().add(store.getLastMessageForDiagram(call.getDiagramName()).getSDPoint());
		
		return this;
	}
	
	public ReturnPointCausationBuilder handleReturnMessage(Message returnMessage, SeqDiagramStore store)
	{
		this.getReturnPoints().add(returnMessage.getSDPoint());
		
		return this;
	}
	
	public String build()
	{
		StringBuilder toReturn = new StringBuilder();
		
		for (SDPoint callPoint : this.getCallPoints())
		{
			toReturn.append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] st [StackLevel] : C_ReturnPoint(Next(t), st, " 
					+ callPoint + "post) <- (CurrentStackLevel(t) = (st-1)) & SDPointAt(t, " + callPoint + ").", this.getTabLevel()));
		}
		
		StringBuilder uncause = new StringBuilder("! t [Time] st [StackLevel] sd [SDPoint] : Cn_ReturnPoint(Next(t), st, sd) <- (CurrentStackLevel(t) = st) & ReturnPoint(t, st, sd) & (");
		
		Iterator<SDPoint> returnPoints = this.getReturnPoints().iterator();
		
		while (returnPoints.hasNext())
		{
			SDPoint returnPoint = returnPoints.next();
			
			uncause.append("SDPointAt(t, " + returnPoint + ")");
			
			if (returnPoints.hasNext())
			{
				uncause.append(" | ");
			}
			else
			{
				uncause.append(").");
			}
		}
		
		toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(uncause.toString(), this.getTabLevel()));
		toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return toReturn.toString();
	}
}
