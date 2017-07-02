package theory.theory;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationEnd;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;
import theory.vocabulary.VocabularyAssociationBuilder;

public class AssociationAssertionBuilder
{
	public AssociationAssertionBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.stringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private final StringBuilder stringBuilder;

	private int getTabLevel()
	{
		return this.tabLevel;
	}

	private StringBuilder getStringBuilder()
	{
		return this.stringBuilder;
	}
	
	public AssociationAssertionBuilder addAssociation(Association association, DiagramStore store)
	{
		String predicateName = VocabularyAssociationBuilder.makePredicateName(association, store);
		String tuple = this.makeTuple(association.getNbOfEnds());
		StringBuilder allQuantifiers = new StringBuilder("! ");
		StringBuilder typeAssertion = new StringBuilder("(");
		StringBuilder multAssertions = new StringBuilder();
		
		int i = 0;
		
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		
		while (assEnds.hasNext())
		{
			AssociationEnd ele = assEnds.next();
			
			// add to type assertion
			String theO = "o" + (i+1);
			allQuantifiers.append(theO + " ");
			typeAssertion.append("(StaticClass(" + ele.getTypeName(store) + ", " + theO + "))");
			if (assEnds.hasNext())
			{
				typeAssertion.append(" & ");
			}
			Optional<String> multAssertion = this.multiplicityAssertion(association.getAssociationEnds(), ele,
					predicateName, tuple, (i+1), this.makeQuantifiers(association.getNbOfEnds(), i), store);
			if (multAssertion.isPresent())
			{
				multAssertions.append(multAssertion.get());
			}
			
			i++;
		}
		
		allQuantifiers.append(": ");
		typeAssertion.append(")");
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(allQuantifiers + predicateName + tuple + " => " + typeAssertion + ".", this.getTabLevel()));
		this.getStringBuilder().append(multAssertions);
		
		return this;
	}
	
	private Optional<String> multiplicityAssertion(Set<AssociationEnd> assEnds, AssociationEnd assEnd, String predicateName, String tuple, int assEndNumber, String quantifiers, DiagramStore store)
	{
		String typeAssertion = this.makeTypeAssertion(assEnds, assEnd, store);
		
		if (assEnd.getMultiplicity().getLowerBound() == 0 && assEnd.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			return Optional.empty();
		}
		
		if (assEnd.getMultiplicity().getLowerBound() == 0)
		{
			return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + typeAssertion + "(#{o" + assEndNumber + " : " + predicateName + tuple
					+ "} =< " + (int) assEnd.getMultiplicity().getUpperBound() + ").", this.getTabLevel()));
		}
		
		if (assEnd.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + typeAssertion + "(" + (int) assEnd.getMultiplicity().getLowerBound()
					+ " =< #{o" + assEndNumber + " : " + predicateName + tuple + "}).", this.getTabLevel()));
		}
		
		if (assEnd.getMultiplicity().getLowerBound() == assEnd.getMultiplicity().getUpperBound())
		{
			return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + typeAssertion + "?" + (int) assEnd.getMultiplicity().getLowerBound()
					+ " o" + assEndNumber + " : " + predicateName + tuple + ".", this.getTabLevel()));
		}
		
		return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + typeAssertion + "(" + (int) assEnd.getMultiplicity().getLowerBound()
				+ " =< #{o" + assEndNumber + " : " + predicateName + tuple + "} & "
				+ "#{o" + assEndNumber + " : " + predicateName + tuple
				+ "} =< " + (int) assEnd.getMultiplicity().getUpperBound() + ").", this.getTabLevel()));
	}
	
	private String makeTuple(int numMembers)
	{
		StringBuilder tuple = new StringBuilder("(");
		
		for (int i = 0; i < numMembers; i++)
		{
			String theO = "o" + (i+1);
			if (i < numMembers - 1)
			{
				tuple.append(theO + ",");
			}
			else tuple.append(theO);
		}
		
		return tuple.append(")").toString();
	}
	
	private String makeQuantifiers(int numMembers, int skip)
	{
		StringBuilder quantifiers = new StringBuilder("! ");
		
		for (int i = 0; i < numMembers; i++)
		{
			if (i == skip)
			{
				continue;
			}
			
			quantifiers.append("o" + (i+1) + " ");
		}
		
		return quantifiers.append(": ").toString();
	}
	
	private String makeTypeAssertion(Set<AssociationEnd> assEnds, AssociationEnd skip, DiagramStore store)
	{
		
		StringBuilder toReturn = new StringBuilder("(");
		
		Iterator<AssociationEnd> iterator = assEnds.iterator();
		
		int i = 0;
		
		while (iterator.hasNext())
		{
			AssociationEnd ele = iterator.next();
			
			String theO = "o" + (i+1);
			
			if (ele.equals(skip))
			{
				i++;
				continue;
			}
			
			toReturn.append("(StaticClass(" + ele.getTypeName(store) + ", " + theO + ")) & ");
			
			i++;
		}
		
		if (toReturn.substring(toReturn.length() - 3).equals((" & ")))
		{
			toReturn.delete(toReturn.length() - 3, toReturn.length());
		}
		
		return toReturn.append(") => ").toString();
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
