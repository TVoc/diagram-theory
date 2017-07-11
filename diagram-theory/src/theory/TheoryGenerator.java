package theory;

import java.io.FileWriter;
import java.io.IOException;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.classdiagrams.Association;
import data.classdiagrams.Class;
import theory.structure.SeqStructureBuilder;
import theory.structure.StructureBuilder;
import theory.theory.TheoryBuilder;
import theory.vocabulary.VocabularyBuilder;
import theory.vocabulary.sequencediagrams.SeqVocabularyBuilder;

public class TheoryGenerator
{
	public void generateTheory(DiagramStore store, String outputPath, Factors factors) throws IOException
	{
		FileWriter file = new FileWriter(outputPath, false);
		
		VocabularyBuilder vocBuilder = new VocabularyBuilder(0, factors);
		TheoryBuilder theoryBuilder = new TheoryBuilder(0);
		StructureBuilder structureBuilder = new StructureBuilder(0, factors);
		
		file.write(vocBuilder.build(store));
		file.write(theoryBuilder.buildTheory(store));
		file.write(structureBuilder.build());
		file.write(System.lineSeparator());
		file.write("procedure main() {" + System.lineSeparator());
		file.write(OutputConvenienceFunctions.insertTabsNewLine("print(modelexpand(T,thestruct)[1])", 1));
		file.write("}");
		
		
		file.close();
	}
	
	public void generateLTCTheory(SeqDiagramStore store, String outputPath, SeqFactors factors) throws IOException
	{
		FileWriter file = new FileWriter(outputPath, false);
		
		theory.theory.sequencediagrams.TheoryBuilder theoryBuilder = new theory.theory.sequencediagrams.TheoryBuilder(0, store);
		SeqVocabularyBuilder vocBuilder = new SeqVocabularyBuilder(0);
		SeqStructureBuilder structBuilder = new SeqStructureBuilder(0, factors);
		
		structBuilder.processClasses(store);
		
		for (Message message : store.getMessages())
		{
			theoryBuilder.handleMessage(message, store);
		}
		
		for (AltCombinedFragment frag : store.getAltCombinedFragments())
		{
			theoryBuilder.handleAltCombinedFragment(frag, store);
		}
		
		for (LoopCombinedFragment frag : store.getLoopCombinedFragments())
		{
			theoryBuilder.handleLoopCombinedFragment(frag, store);
		}
		
		for (TempVar tempVar : store.getTempVars().values())
		{
			theoryBuilder.addTempVar(tempVar, store);
		}
		
		for (Class clazz : store.getClasses().values())
		{
			theoryBuilder.addClass(clazz, store);
		}
		
		for (Association assoc : store.getAssociations())
		{
			theoryBuilder.addAssociation(assoc, store);
		}
		
		file.write("include<LTC>" + System.lineSeparator() + System.lineSeparator());
		file.write(vocBuilder.build(store));
		file.write(theoryBuilder.build());
		file.write(structBuilder.build());		
		file.write("procedure main() {" + System.lineSeparator());
		file.write(OutputConvenienceFunctions.insertTabsNewLine("print(modelexpand(T,thestruct)[1])", 1));
		file.write("}");
		
		file.close();
	}
}
