package theory.theory.sequencediagrams;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.CombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import theory.SeqDiagramStore;

public class CausationBuilder
{
	public CausationBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		this.classVariableCausationBuilder = new ClassVariableCausationBuilder(tabLevel, store);
		this.checkpointBuilder = new CheckpointBuilder(tabLevel, store);
		this.tempVarCausationBuilder = new TempVarCausationBuilder(tabLevel);
		this.stackLevelCausationBuilder = new StackLevelCausationBuilder(tabLevel, store);
	}
	
	private final int tabLevel;
	
	private final ClassVariableCausationBuilder classVariableCausationBuilder;
	
	private final CheckpointBuilder checkpointBuilder;
	
	private final TempVarCausationBuilder tempVarCausationBuilder;
	
	private final StackLevelCausationBuilder stackLevelCausationBuilder;

	public int getTabLevel()
	{
		return this.tabLevel;
	}

	public ClassVariableCausationBuilder getClassVariableCausationBuilder()
	{
		return this.classVariableCausationBuilder;
	}

	public CheckpointBuilder getCheckpointBuilder()
	{
		return this.checkpointBuilder;
	}

	public TempVarCausationBuilder getTempVarCausationBuilder()
	{
		return this.tempVarCausationBuilder;
	}
	
	public StackLevelCausationBuilder getStackLevelCausationBuilder()
	{
		return this.stackLevelCausationBuilder;
	}
	
	public void processCombinedFragment(CombinedFragment frag, SeqDiagramStore store)
	{
		this.getCheckpointBuilder().processCombinedFragment(frag, store);
	}
	
	@Deprecated
	public void handleAltCombinedFragment(AltCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCheckpointBuilder().handleAltCombinedFragment(frag, store);
	}
	
	@Deprecated
	public void handleLoopCombinedFragment(LoopCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCheckpointBuilder().handleLoopCombinedFragment(frag, store);
	}
	
	public void handleMessage(Message message, SeqDiagramStore store)
	{
		this.getClassVariableCausationBuilder().handleMessage(message, store);
		this.getTempVarCausationBuilder().handleMessage(message, store);
		if (store.isCallPoint(message))
		{
			this.getStackLevelCausationBuilder().handleCallPoint(message, store);
		}
	}
	
	public String build()
	{
		StringBuilder toReturn = new StringBuilder();
		
		toReturn.append(this.getClassVariableCausationBuilder().build());
		toReturn.append(this.getCheckpointBuilder().build());
		toReturn.append(this.getTempVarCausationBuilder().build());
		
		return toReturn.toString();
	}
}
