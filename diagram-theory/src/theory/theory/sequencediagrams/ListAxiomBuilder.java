package theory.theory.sequencediagrams;

import java.util.Iterator;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationEnd;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;
import theory.vocabulary.VocabularyBuilder;
import theory.vocabulary.sequencediagrams.SeqAssociationBuilder;

public class ListAxiomBuilder
{
	public ListAxiomBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.definitionBuilder = new StringBuilder();
		this.theorySentenceBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final StringBuilder definitionBuilder;
	
	private StringBuilder getDefitionBuilder()
	{
		return this.definitionBuilder;
	}
	
	private final StringBuilder theorySentenceBuilder;
	
	private StringBuilder getTheorySentenceBuilder()
	{
		return this.theorySentenceBuilder;
	}
	
	public ListAxiomBuilder addAssociation(Association association, SeqDiagramStore store)
	{
		if (association.getNbOfEnds() != 2)
		{
			return this;
		}
		
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		AssociationEnd first = assEnds.next();
		AssociationEnd second = assEnds.next();
		
		if (first.getMultiplicity().isCollection())
		{
			
		}
		if (second.getMultiplicity().isCollection())
		{
			
		}
		
		return this;
	}
	
	private void addListAxioms(Association association, AssociationEnd from, AssociationEnd to, SeqDiagramStore store)
	{
		String[] symbols = SeqAssociationBuilder.constructListSymbols(from, to, store);
		
		String fromType = from.getTypeName(store);
		String toType = to.getTypeName(store);
		
		this.getDefitionBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"! f [" + fromType + "] to [" + toType + "] : " + symbols[1] + "(f, 0) <- " + symbols[0] + "(f) = to.", this.getTabLevel()));
		this.getDefitionBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"! f [" + fromType + "] i [Index] to [" + toType + "] : " + symbols[1] + "(f, i) <- "
				+ "( ? to1 [" + toType + "] : " + symbols[1] + "(f, (i-1)) = to1 & "
				+ symbols[2] + "(f, to1) = to.", this.getTabLevel()));
		
		String predicateName = VocabularyAssociationBuilder.makePredicateName(association, store);
		
		if (association.getAssociationEnds().iterator().next().getTypeName(store).equals(fromType))
		{
			this.getTheorySentenceBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
					"! f [" + fromType + "] to [" + toType + "] : " + predicateName + "(f, to) => (? i [Index] : "
					+ symbols[1] + "(f, i) = to).", this.getTabLevel()));
		}
		else
		{
			this.getTheorySentenceBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
					"! to [" + toType + "] f [" + fromType + "] : " + predicateName + "(to, f) => (? i [Index] : "
					+ symbols[1] + "(f, i) = to).", this.getTabLevel()));
		}
	}
	
	public String buildDefinitions()
	{
		return this.getDefitionBuilder().toString();
	}
	
	public String buildTheorySentences()
	{
		return this.getTheorySentenceBuilder().toString();
	}
}
