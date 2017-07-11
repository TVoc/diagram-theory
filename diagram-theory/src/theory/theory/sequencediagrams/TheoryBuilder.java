package theory.theory.sequencediagrams;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;

public class TheoryBuilder
{
	public TheoryBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.tabLevel = tabLevel;
		this.causationBuilder = new CausationBuilder(tabLevel + 2, store);
		this.stateAxiomBuilder = new StateAxiomBuilder(tabLevel + 2);
		this.seqAttributeAssertionBuilder = new SeqAttributeAssertionBuilder(tabLevel + 2);
		this.seqAssociationAssertionBuilder = new SeqAssociationAssertionBuilder(tabLevel + 2);
	}
	
	private final int tabLevel;
	
	private final CausationBuilder causationBuilder;
	
	private final StateAxiomBuilder stateAxiomBuilder;
	
	private final SeqAttributeAssertionBuilder seqAttributeAssertionBuilder;
	
	private final SeqAssociationAssertionBuilder seqAssociationAssertionBuilder;
	
	public int getTabLevel()
	{
		return this.tabLevel;
	}

	public CausationBuilder getCausationBuilder()
	{
		return this.causationBuilder;
	}

	public StateAxiomBuilder getStateAxiomBuilder()
	{
		return this.stateAxiomBuilder;
	}

	public SeqAttributeAssertionBuilder getSeqAttributeAssertionBuilder()
	{
		return this.seqAttributeAssertionBuilder;
	}

	public SeqAssociationAssertionBuilder getSeqAssociationAssertionBuilder()
	{
		return this.seqAssociationAssertionBuilder;
	}

	public TheoryBuilder handleMessage(Message message, SeqDiagramStore store)
	{
		this.getCausationBuilder().handleMessage(message, store);
		return this;
	}
	
	public TheoryBuilder handleAltCombinedFragment(AltCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCausationBuilder().handleAltCombinedFragment(frag, store);
		return this;
	}
	
	public TheoryBuilder handleLoopCombinedFragment(LoopCombinedFragment frag, SeqDiagramStore store)
	{
		this.getCausationBuilder().handleLoopCombinedFragment(frag, store);
		return this;
	}
	
	public TheoryBuilder addTempVar(TempVar var, SeqDiagramStore store)
	{
		this.getStateAxiomBuilder().addTempVar(var, store);
		return this;
	}
	
	public TheoryBuilder addClass(Class clazz, SeqDiagramStore store)
	{
		this.getStateAxiomBuilder().addClass(clazz, store);
		
		if (clazz.getAllAttributes().isPresent())
		{
			for (DataUnit attr : clazz.getAllAttributes().get())
			{
				this.getSeqAttributeAssertionBuilder().addAttribute(attr, clazz.getName(), store);
			}
		}
		
		return this;
	}
	
	public TheoryBuilder addAssociation(Association assoc, SeqDiagramStore store)
	{
		this.getSeqAssociationAssertionBuilder().addAssociation(assoc, store);
		return this;
	}
	
	public String build()
	{
		return OutputConvenienceFunctions.insertTabsNewLine("theory T:V {", this.getTabLevel())
			+ OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel() + 1)
			+ this.getCausationBuilder().build()
			+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel() + 1)
			+ this.getStateAxiomBuilder().build()
			+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel() + 1)
			+ this.getSeqAttributeAssertionBuilder().build()
			+ this.getSeqAssociationAssertionBuilder().build()
			+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel());
	}
}
