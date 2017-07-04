package theory.vocabulary.sequencediagrams;

import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import data.sequencediagrams.TempVar;

public class SeqVocabularyBuilder
{
	public SeqVocabularyBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		
		seqClassBuilder = new SeqClassBuilder(tabLevel + 1);
		tempVarBuilder = new TempVarBuilder(tabLevel + 1);
		seqClassAttributeBuilder = new SeqClassAttributeBuilder(tabLevel + 1);
		seqAssociationBuilder = new SeqAssociationBuilder(tabLevel + 1);
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final SeqClassBuilder seqClassBuilder;
	
	private final TempVarBuilder tempVarBuilder;
	
	private final SeqClassAttributeBuilder seqClassAttributeBuilder;
	
	private final SeqAssociationBuilder seqAssociationBuilder;
	
	public SeqClassBuilder getSeqClassBuilder()
	{
		return this.seqClassBuilder;
	}

	public TempVarBuilder getTempVarBuilder()
	{
		return this.tempVarBuilder;
	}

	public SeqClassAttributeBuilder getSeqClassAttributeBuilder()
	{
		return this.seqClassAttributeBuilder;
	}

	public SeqAssociationBuilder getSeqAssociationBuilder()
	{
		return this.seqAssociationBuilder;
	}

	public String build(SeqDiagramStore store)
	{
		for (Class ele : store.getClasses().values())
		{
			this.getSeqClassBuilder().addClass(ele, store);
			
			if (ele.getAllAttributes().isPresent())
			{
				for (DataUnit attr : ele.getAllAttributes().get())
				{
					this.getSeqClassAttributeBuilder().addAttribute(attr, ele.getName(), store);
				}
			}
		}
		
		for (TempVar ele : store.getTempVars().values())
		{
			this.getTempVarBuilder().addTempVar(ele, store);
		}
		
		for (Association ele : store.getAssociations())
		{
			this.getSeqAssociationBuilder().addAssociation(ele, store);
		}
		
		return OutputConvenienceFunctions.insertTabsNewLine("LTCvocabulary V {", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("type Time isa nat", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("Start: Time", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("partial Next(Time) : Time", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type SDPoint = { 1.." + store.getMessages().size() + 1, this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("SDPointAt(Time,SDPoint)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("I_SDPointAt(SDPoint)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("C_SDPointAt(Time,SDPoint)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedInt isa int", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedFloat isa float", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedString isa string", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type bool", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type void", this.getTabLevel() + 1)
				+ this.getSeqClassBuilder().build()
				+ this.getTempVarBuilder().build()
				+ this.getSeqClassAttributeBuilder().build()
				+ this.getSeqAssociationBuilder().build();
	}
}
