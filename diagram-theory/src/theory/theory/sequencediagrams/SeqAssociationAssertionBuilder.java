package theory.theory.sequencediagrams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationEnd;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;

public class SeqAssociationAssertionBuilder
{
	public SeqAssociationAssertionBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private final StringBuilder stringBuilder;

	public int getTabLevel()
	{
		return this.tabLevel;
	}

	public StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public SeqAssociationAssertionBuilder addAssociation(Association assoc, SeqDiagramStore store)
	{	
		String predicateName = VocabularyAssociationBuilder.makePredicateName(assoc, store);
		String tuple = this.makeTuple(assoc);
		List<String> allQuantifiers = this.allQuantifiers(assoc, store);
		
		int i = 0;
		
		for (AssociationEnd ele : assoc.getAssociationEnds())
		{
			if (ele.getMultiplicity().getLowerBound() == 0 && ele.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
			{
				i++;
				continue;
			}
			else if (ele.getMultiplicity().getLowerBound() == 0)
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(this.flatten(allQuantifiers, i)
						+ "#{ x" + (i+1) + " [" + ele.getTypeName(store) + "] : " + predicateName + tuple + "} =< " + (int) ele.getMultiplicity().getUpperBound() + ".",
						this.getTabLevel()));
			}
			else if (ele.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(this.flatten(allQuantifiers, i)
						+ "#{ x" + (i+1) + " [" + ele.getTypeName(store) + "] : " + predicateName + tuple + "} >= " + (int) ele.getMultiplicity().getLowerBound() + ".",
						this.getTabLevel()));
			}
			else if (ele.getMultiplicity().getLowerBound() == ele.getMultiplicity().getUpperBound())
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(this.flatten(allQuantifiers, i)
						+ "?" + (int) ele.getMultiplicity().getLowerBound() + " x" + (i+1) + " [" + ele.getTypeName(store) + "] : "
						+ predicateName + tuple + ".", 
						this.getTabLevel()));
			}
			else
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(this.flatten(allQuantifiers, i)
						+ (int) ele.getMultiplicity().getLowerBound() + " =< #{ x" + (i+1) + " [" + ele.getTypeName(store) + "] : " + predicateName + tuple + "} =< " + (int) ele.getMultiplicity().getUpperBound() + ".",
						this.getTabLevel()));
			}
			
			i++;
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return this;
	}
	
	private List<String> allQuantifiers(Association assoc, SeqDiagramStore store)
	{
		List<String> toReturn = new ArrayList<String>();
		
		int i = 1;
		
		for (AssociationEnd ele : assoc.getAssociationEnds())
		{
			toReturn.add("x" + (i++) + " [" + ele.getTypeName(store) + "]");
		}
		
		return toReturn;
	}
	
	private String flatten(List<String> quantifiers)
	{
		StringBuilder toReturn = new StringBuilder("! ");
		
		Iterator<String> iterator = quantifiers.iterator();
		
		while (iterator.hasNext())
		{
			String ele = iterator.next();
			
			if (iterator.hasNext())
			{
				toReturn.append(ele + " ");
			}
			else
			{
				toReturn.append(ele + " : ");
			}
		}
		
		return toReturn.toString();
	}
	
	private String flatten(List<String> quantifiers, int skip)
	{
		// NOTE: skip is zero-based
		StringBuilder toReturn = new StringBuilder("! ");
		
		for(int i = 0; i < quantifiers.size(); i++)
		{
			if ((i == (skip - 1) && i == (quantifiers.size() - 2))
					|| (i == (quantifiers.size() - 1)))
			{
				toReturn.append(quantifiers.get(i) + " : ");
				break;
			}
			if (i == skip)
			{
				continue;
			}
			
			toReturn.append(quantifiers.get(i) + " ");
		}
		
		return toReturn.toString();
	}
	
	private String makeTuple(Association association)
	{
		StringBuilder toReturn = new StringBuilder("(");
		
		for (int i = 0; i < association.getNbOfEnds(); i++)
		{
			if (i == (association.getNbOfEnds() - 1))
			{
				toReturn.append("x" + (i+1) + ")");
			}
			else
			{
				toReturn.append("x" + (i+1) + ", ");
			}
		}
		
		return toReturn.toString();
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
