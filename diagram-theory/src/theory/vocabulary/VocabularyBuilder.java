package theory.vocabulary;

import java.util.Map.Entry;

import theory.DiagramStore;
import theory.OutputConvenienceFunctions;
import data.Association;
import data.Class;
import data.DataUnit;
import data.Operation;

public class VocabularyBuilder
{
	public VocabularyBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
//		this.classPredicateBuilder = new ClassPredicateBuilder(tabLevel + 1);
		this.classAttributeBuilder = new ClassAttributeBuilder(tabLevel + 1);
		this.classOperationBuilder = new ClassOperationBuilder(tabLevel + 1);
		this.vocabularyAssociationBuilder = new VocabularyAssociationBuilder(tabLevel + 1);
		this.classObjectBuilder = new ClassObjectBuilder(tabLevel + 1);
	}
	
	private final int tabLevel;
	
	private int getTabLevel()
	{
		return this.tabLevel;
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
		String general = OutputConvenienceFunctions.insertTabsNewLine("vocabulary V {", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedInt isa int", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedFloat isa int", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type LimitedString isa string", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type bool constructed from { true, false }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type void constructed from { void }", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type ClassObject", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("type Object", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("RuntimeClass(ClassObject, Object)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("StaticClass(ClassObject, Object)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("IsDirectSupertypeOf(ClassObject, ClassObject)", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("IsSupertypeOf(ClassObject, ClassObject)", this.getTabLevel() + 1);
//				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1);
		
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
		
		return general 
//			+ this.getClassPredicateBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
//			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getClassObjectBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getClassAttributeBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getClassOperationBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ this.getVocabularyAssociationBuilder().build() + OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
			+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel());
	}
}
