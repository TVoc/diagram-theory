package theory.theory.sequencediagrams;

import data.sequencediagrams.AltCombinedFragment;
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
	}
	
	private final int tabLevel;
	
	private final ClassVariableCausationBuilder classVariableCausationBuilder;
	
	private final CheckpointBuilder checkpointBuilder;
	
	private final TempVarCausationBuilder tempVarCausationBuilder;

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
	
	public void handleAltCombinedFragment(AltCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCheckpointBuilder().handleAltCombinedFragment(frag, store);
	}
	
	public void handleLoopCombinedFragment(LoopCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCheckpointBuilder().handleLoopCombinedFragment(frag, store);
	}
	
	public void handleMessage(Message message, SeqDiagramStore store)
	{
		this.getClassVariableCausationBuilder().handleMessage(message, store);
		this.getTempVarCausationBuilder().handleMessage(message, store);
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