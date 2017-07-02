package theory.theory;

import data.classdiagrams.Operation;
import data.classdiagrams.PrimitiveType;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class OperationAssertionBuilder
{
	public OperationAssertionBuilder(int tabLevel)
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
	
	public OperationAssertionBuilder addOperation(Operation operation, String className, DiagramStore store)
	{
		String predicateName = className + operation.getName();
		StringBuilder quantifiers = new StringBuilder("! o ");
		StringBuilder tuple = new StringBuilder("(o, ");
		StringBuilder multClassAssertion = new StringBuilder("(StaticClass(" + className + ", o)");
		
		if (operation.getAllParameters().isPresent())
		{
			for (int i = 0; i < operation.getAllParameters().get().size(); i++)
			{
				String theP = "p" + (i+1);
				quantifiers.append(theP + " ");
				tuple.append(theP + ", ");
				if (! PrimitiveType.isPrimitiveType(operation.getAllParameters().get().get(i).getTypeName(store)))
				{
					multClassAssertion.append(" & (StaticClass(" + operation.getAllParameters().get().get(i).getTypeName(store) + ", " + theP + "))");
				}
			}
		}
		
		String altQuantifiers = "(?1 r : ";
		String multQuantifiers = quantifiers.toString() + ": ";
		
		quantifiers.append("r : ");
		tuple.append("r)");
		
		
		if (! PrimitiveType.isPrimitiveType(operation.getResultType().getTypeName(store)))
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(quantifiers.toString() + predicateName
					+ tuple.toString() + " => " + multClassAssertion + " & (StaticClass(" + operation.getResultType().getTypeName(store) + ", r))).", this.getTabLevel()));

		}
		else
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(quantifiers.toString() + predicateName
					+ tuple.toString() + " => " + multClassAssertion + ").", this.getTabLevel()));
		}
		
		// TODO accommodate optional result and more than one result
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(multQuantifiers + multClassAssertion + ") => " + altQuantifiers + predicateName
				+ tuple.toString() + ").", this.getTabLevel()));
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
