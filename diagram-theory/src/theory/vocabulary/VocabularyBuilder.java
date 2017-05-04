package theory.vocabulary;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;

import theory.DiagramStore;
import theory.Factors;
import theory.OutputConvenienceFunctions;
import data.Association;
import data.Class;
import data.DataUnit;
import data.Operation;

public class VocabularyBuilder
{
	public VocabularyBuilder(int tabLevel, Factors factors)
	{
		this.tabLevel = tabLevel;
//		this.classPredicateBuilder = new ClassPredicateBuilder(tabLevel + 1);
		this.classAttributeBuilder = new ClassAttributeBuilder(tabLevel + 1);
		this.classOperationBuilder = new ClassOperationBuilder(tabLevel + 1);
		this.vocabularyAssociationBuilder = new VocabularyAssociationBuilder(tabLevel + 1);
		this.classObjectBuilder = new ClassObjectBuilder(tabLevel + 1);
		this.factors = factors;
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
	}
	
	private final Factors factors;
	
	private Factors getFactors()
	{
		return this.factors;
	}

//	private final ClassPredicateBuilder classPredicateBuilder;
	private final ClassAttributeBuilder classAttributeBuilder;
	private final ClassOperationBuilder classOperationBuilder;
	private final VocabularyAssociationBuilder vocabularyAssociationBuilder;
	private final ClassObjectBuilder classObjectBuilder;
	
//	private ClassPredicateBuilder getClassPredicateBuilder()
//	{
//		return classPredicateBuilder;
//	}

	private ClassAttributeBuilder getClassAttributeBuilder()
	{
		return classAttributeBuilder;
	}

	private ClassOperationBuilder getClassOperationBuilder()
	{
		return classOperationBuilder;
	}

	private VocabularyAssociationBuilder getVocabularyAssociationBuilder()
	{
		return vocabularyAssociationBuilder;
	}
	
	private ClassObjectBuilder getClassObjectBuilder()
	{
		return this.classObjectBuilder;
	}

	public String build(DiagramStore store)
	{
			//	+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1);
		
		for (Entry<String,Class> ele : store.getClasses().entrySet())
		{
//			this.getClassPredicateBuilder().addClassPredicate(ele.getValue().getName());
			this.getClassObjectBuilder().addClass(ele.getValue().getName());
			
			if (ele.getValue().getAllAttributes().isPresent())
			{
				for (DataUnit attribute : ele.getValue().getAllAttributes().get())
				{
					this.getClassAttributeBuilder().addAttribute(attribute, ele.getValue().getName(), store);
				}
			}
			
			if (ele.getValue().getAllOperations().isPresent())
			{
				for (Operation operation : ele.getValue().getAllOperations().get())
				{
					this.getClassOperationBuilder().addOperation(operation, ele.getValue().getName(), store);
				}
			}
		}
		
		for (Association association : store.getAssociations())
		{
			this.getVocabularyAssociationBuilder().addAssociation(association, store);
		}
		
		StringBuilder stringDomain = new StringBuilder();
		Set<String> ranStrings = new HashSet<String>();
		
		while (ranStrings.size() < Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getStringFactor()))
		{
			String ran = RandomStringUtils.random(20, true, true);
			
			if (ranStrings.contains(ran))
			{
				continue;
			}
			
			if (ranStrings.size() == 0)
			{
				stringDomain.append("\"" + ran + "\"");
			}
			else
			{
				stringDomain.append("; \"" + ran + "\"");
			}
			
			ranStrings.add(ran);
		}
		
		StringBuilder floatDomain = new StringBuilder();
		
		double ele = 0;
		int i = 0;
		while (i < Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getFloatFactor()))
		{
			if (i == 0)
			{
				floatDomain.append(ele);
			}
			else
			{
				floatDomain.append("; " + ele);
			}
			ele += 0.5;
			i++;
		}
		
		String general = OutputConvenienceFunctions.insertTabsNewLine("vocabulary V {", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedInt = { 1.." + (int) Math.ceil((double) this.getFactors().getNumObjects() * this.getFactors().getIntFactor()) + " } isa int", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedFloat = { " + floatDomain.toString() + " } isa float", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedString  = { " + stringDomain.toString() + " } isa string", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type bool constructed from { true, false }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type void constructed from { void }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type Object", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine(this.getClassObjectBuilder().build(), this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("RuntimeClass(ClassObject, Object)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("StaticClass(ClassObject, Object)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("IsDirectSupertypeOf(ClassObject, ClassObject)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("IsSupertypeOf(ClassObject, ClassObject)", this.getTabLevel() + 1);
		
		return general 
//			+ this.getClassPredicateBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
//			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getClassAttributeBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getClassOperationBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getVocabularyAssociationBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel())
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel());
	}
}
