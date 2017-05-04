package theory.theory;

import theory.DiagramStore;
import theory.OutputConvenienceFunctions;
import data.Association;
import data.Class;
import data.DataUnit;
import data.Generalization;
import data.Operation;

public class TheoryBuilder
{
	public TheoryBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.classAssertionBuilder = new ClassAssertionBuilder(tabLevel + 1);
		this.hierarchyAssertionBuilder = new HierarchyAssertionBuilder(tabLevel + 1);
		this.attributeAssertionBuilder = new AttributeAssertionBuilder(tabLevel + 1);
		this.operationAssertionBuilder = new OperationAssertionBuilder(tabLevel + 1);
		this.associationAssertionBuilder = new AssociationAssertionBuilder(tabLevel + 1);
	}
	
	private final int tabLevel;
	
	private final ClassAssertionBuilder classAssertionBuilder;
	private final HierarchyAssertionBuilder hierarchyAssertionBuilder;
	private final AttributeAssertionBuilder attributeAssertionBuilder;
	private final OperationAssertionBuilder operationAssertionBuilder;
	private final AssociationAssertionBuilder associationAssertionBuilder;

	private int getTabLevel()
	{
		return this.tabLevel;
	}

	private ClassAssertionBuilder getClassAssertionBuilder()
	{
		return this.classAssertionBuilder;
	}

	private HierarchyAssertionBuilder getHierarchyAssertionBuilder()
	{
		return this.hierarchyAssertionBuilder;
	}

	private AttributeAssertionBuilder getAttributeAssertionBuilder()
	{
		return this.attributeAssertionBuilder;
	}

	private OperationAssertionBuilder getOperationAssertionBuilder()
	{
		return this.operationAssertionBuilder;
	}

	private AssociationAssertionBuilder getAssociationAssertionBuilder()
	{
		return this.associationAssertionBuilder;
	}
	
	public String buildTheory(DiagramStore store)
	{
		for (Class ele : store.getClasses().values())
		{
			this.getClassAssertionBuilder().addClassAssertion(ele.getName());
			
			if (ele.getAllAttributes().isPresent())
			{
				for (DataUnit attribute : ele.getAllAttributes().get())
				{
					this.getAttributeAssertionBuilder().addAttribute(attribute, ele.getName(), store);
				}
			}
			
			if (ele.getAllOperations().isPresent())
			{
				for (Operation operation : ele.getAllOperations().get())
				{
					this.getOperationAssertionBuilder().addOperation(operation, ele.getName(), store);
				}
			}
		}
		
		for (Generalization generalization : store.getGeneralizations())
		{
			this.getHierarchyAssertionBuilder().addGeneralization(generalization, store);
		}
		
		for (Association association : store.getAssociations())
		{
			this.getAssociationAssertionBuilder().addAssociation(association, store);
		}
		
		return OutputConvenienceFunctions.insertTabsNewLine("theory T:V {", this.getTabLevel())
			//	+ this.getClassAssertionBuilder().build()
				+ this.getHierarchyAssertionBuilder().build()
				+ this.getAttributeAssertionBuilder().build()
				+ this.getOperationAssertionBuilder().build()
				+ this.getAssociationAssertionBuilder().build()
				+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel())
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel());
	}
}
