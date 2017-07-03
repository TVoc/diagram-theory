package theory.theory.sequencediagrams;

import theory.SeqDiagramStore;

public class CausationBuilder
{
	public CausationBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		this.classVariableCausationBuilder = new ClassVariableCausationBuilder();
		this.checkpointBuilder = new CheckpointBuilder(tabLevel, store);
		this.tempVarCausationBuilder = new TempVarCausationBuilder();
	}
	
	private final int tabLevel;
	
	private final ClassVariableCausationBuilder classVariableCausationBuilder;
	
	private final CheckpointBuilder checkpointBuilder;
	
	private final TempVarCausationBuilder tempVarCausationBuilder;
}
