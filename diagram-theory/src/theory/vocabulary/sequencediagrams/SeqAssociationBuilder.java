package theory.vocabulary.sequencediagrams;

import java.util.Iterator;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationEnd;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;

public class SeqAssociationBuilder
{
	public SeqAssociationBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final StringBuilder stringBuilder;
	
	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public SeqAssociationBuilder addAssociation(Association association, SeqDiagramStore store)
	{
		String predicateName = VocabularyAssociationBuilder.makePredicateName(association, store);
		
		StringBuilder predicate = new StringBuilder(predicateName + "(");
		
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		
		while (assEnds.hasNext())
		{
			AssociationEnd assEnd = assEnds.next();
			
			if (assEnds.hasNext())
			{
				predicate.append(assEnd.getTypeName(store) + ", ");
			}
			else
			{
				predicate.append(assEnd.getTypeName(store) + ")");
			}
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(predicate.toString(), this.getTabLevel()));
		
		if (association.getNbOfEnds() == 2)
		{
			return this.addListSymbols(association, store);
		}
		
		return this;
	}
	
	private SeqAssociationBuilder addListSymbols(Association association, SeqDiagramStore store)
	{
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		AssociationEnd first = assEnds.next();
		AssociationEnd second = assEnds.next();
		
		if (first.getMultiplicity().isCollection())
		{
			this.addListSymbolsFromAssociation(second, first, store);
		}
		if (second.getMultiplicity().isCollection())
		{
			this.addListSymbolsFromAssociation(first, second, store);
		}
		
		return this;
	}
	
	private void addListSymbolsFromAssociation(AssociationEnd from, AssociationEnd to, SeqDiagramStore store)
	{
		String fromName = from.getTypeName(store);
		String toName = to.getTypeName(store);
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"partial " + fromName + "First" + toName + "(" + fromName + ")", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"partial " + fromName + "Get" + toName + "(" + fromName + ", LimitedInt)", this.getTabLevel()));
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"partial " + fromName + "Next" + toName + "(" + fromName + ", LimitedInt)", this.getTabLevel()));
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param store
	 * @return
	 * 		return[0] = fromName + "First" + toName
	 * 		return[1] = fromName + "Get" + toName
	 * 		return[2] = fromName + "Next" + toName
	 */
	public static final String[] constructListSymbols(AssociationEnd from, AssociationEnd to, SeqDiagramStore store)
	{
		String[] toReturn = new String[3];
		
		String fromName = from.getTypeName(store);
		String toName = to.getTypeName(store);
		
		toReturn[0] = fromName + "First" + toName;
		toReturn[1] = fromName + "Get" + toName;
		toReturn[2] = fromName + "Next" + toName;
		
		return toReturn;
	}
}
