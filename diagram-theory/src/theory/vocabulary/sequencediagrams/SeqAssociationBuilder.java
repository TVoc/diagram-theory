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
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
