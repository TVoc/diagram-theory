package theory.vocabulary;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationEnd;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class VocabularyAssociationBuilder
{
	public VocabularyAssociationBuilder(int tabLevel)
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
	
	public VocabularyAssociationBuilder addAssociation(Association association, DiagramStore store)
	{
		String predicateName = makePredicateName(association, store);
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(predicateName
				+ "(" + StringUtils.repeat("Object,", association.getNbOfEnds() - 1) + "Object)", this.getTabLevel()));
		
		return this;
	}

	public static String makePredicateName(Association association, DiagramStore store)
	{
		StringBuilder predicateName = new StringBuilder();
		
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		
		while (assEnds.hasNext())
		{
			AssociationEnd ele = assEnds.next();
			predicateName.append(ele.getTypeName(store));
			
			if (assEnds.hasNext())
			{
				predicateName.append("and");
			}
		}
		
		return predicateName.toString();
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
