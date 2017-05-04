package theory.theory;

import java.util.Iterator;
import java.util.Optional;

import data.Association;
import data.AssociationEnd;
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
		StringBuilder tuple = new StringBuilder("(o1, ");
		StringBuilder allQuantifiers = new StringBuilder("! o1 ");
		
		for (int i = 1; i < association.getNbOfEnds(); i++)
		{
			allQuantifiers.append("o" + (i+1) + " ");
			if (i < association.getNbOfEnds() - 1)
			{
				tuple.append("o" + (i+1) + ", ");
			}
			else
			{
				tuple.append("o" + (i+1));
			}
		}
		
		allQuantifiers.append(": ");
		tuple.append(")");
		
		int i = 1;
		
		Iterator<AssociationEnd> assEnds = association.getAssociationEnds().iterator();
		
		while (assEnds.hasNext())
		{
			AssociationEnd ele = assEnds.next();
			
			StringBuilder quantifiers = new StringBuilder("! ");
			
			for (int j = 1; j <= association.getNbOfEnds(); j++)
			{
				if (i == j)
				{
					continue;
				}
				
				quantifiers.append("o" + j + " ");
			}
			
			quantifiers.append(": ");
			
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(allQuantifiers + predicateName + tuple
					+ " => ? t : StaticClass(t, o" + i + ") & t = " + ele.getTypeName(store) + ".", this.getTabLevel()));
			
			Optional<String> multiplicityAssertion = this.multiplicityAssertion(ele, predicateName, tuple.toString(), i, quantifiers.toString());
			
			if (multiplicityAssertion.isPresent())
			{
				this.getStringBuilder().append(multiplicityAssertion.get());
			}
			
			i++;
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return this;
	}
	
	private Optional<String> multiplicityAssertion(AssociationEnd assEnd, String predicateName, String tuple, int assEndNumber, String quantifiers)
	{
		if (assEnd.getMultiplicity().getLowerBound() == 0 && assEnd.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			return Optional.empty();
		}
		
		if (assEnd.getMultiplicity().getLowerBound() == 0)
		{
			return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + "#{o" + assEndNumber + " : " + predicateName + tuple
					+ "} =< " + (int) assEnd.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
		}
		
		if (assEnd.getMultiplicity().getUpperBound() == Double.POSITIVE_INFINITY)
		{
			return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + (int) assEnd.getMultiplicity().getLowerBound()
					+ " =< #{o" + assEndNumber + " : " + predicateName + tuple + "}.", this.getTabLevel()));
		}
		
		return Optional.of(OutputConvenienceFunctions.insertTabsNewLine(quantifiers + (int) assEnd.getMultiplicity().getLowerBound()
				+ " =< #{o" + assEndNumber + " : " + predicateName + tuple + "} & "
				+ "#{o" + assEndNumber + " : " + predicateName + tuple
				+ "} =< " + (int) assEnd.getMultiplicity().getUpperBound() + ".", this.getTabLevel()));
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
