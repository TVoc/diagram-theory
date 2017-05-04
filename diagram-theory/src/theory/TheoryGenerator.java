package theory;

import java.io.FileWriter;
import java.io.IOException;

import theory.structure.StructureBuilder;
import theory.theory.TheoryBuilder;
import theory.vocabulary.VocabularyBuilder;

public class TheoryGenerator
{
	public void generateTheory(DiagramStore store, String outputPath, Factors factors) throws IOException
	{
		FileWriter file = new FileWriter(outputPath, false);
		
		VocabularyBuilder vocBuilder = new VocabularyBuilder(0);
		TheoryBuilder theoryBuilder = new TheoryBuilder(0);
		StructureBuilder structureBuilder = new StructureBuilder(0, factors);
		
		file.write(vocBuilder.build(store));
		file.write(theoryBuilder.buildTheory(store));
		file.write(structureBuilder.build());
		
		
		file.close();
	}
}
