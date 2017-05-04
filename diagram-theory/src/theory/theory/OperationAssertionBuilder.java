package theory.theory;

import data.DataUnit;
import data.Operation;
import data.PrimitiveType;
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
		StringBuilder multClassAssertion = new StringBuilder("((?t : StaticClass(t, o) & t = " + className + ")");
		
		if (operation.getAllParameters().isPresent())
		{
			for (int i = 0; i < operation.getAllParameters().get().size(); i++)
			{
				String theP = "p" + (i+1);
				quantifiers.append(theP + " ");
				tuple.append(theP + ", ");
				multClassAssertion.append(" & (?t : StaticClass(t, " + theP + ") & t = " + operation.getAllParameters().get().get(i).getTypeName(store) + ")");
			}
		}
		
		String altQuantifiers = "(?1 r : ";
		String multQuantifiers = quantifiers.toString() + ": ";
		
		quantifiers.append("r : ");
		tuple.append("r)");
		multClassAssertion.append(") => ");
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(quantifiers.toString() + predicateName
						+ tuple.toString() + " => ? t : StaticClass(t, o) & t = " + className + ".", this.getTabLevel()));
		
		if (operation.getAllParameters().isPresent())
		{
			int pCounter = 1;
			
			for (DataUnit ele : operation.getAllParameters().get())
			{
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(quantifiers.toString() + predicateName
						+ tuple.toString() + " => ? t : StaticClass(t, p" + pCounter + ") & t = " + ele.getTypeName(store) + ".", this.getTabLevel()));
				pCounter++;
			}
		}
		
		if (! PrimitiveType.isPrimitiveType(operation.getResultType().getTypeName(store)))
		{
			this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(quantifiers.toString() + predicateName
					+ tuple.toString() + " => ? t : StaticClass(t, r) & t = " + operation.getResultType().getTypeName(store) + ".", this.getTabLevel()));

		}

		// TODO accommodate optional result and more than one result
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(multQuantifiers + multClassAssertion + altQuantifiers + predicateName
				+ tuple.toString() + ").", this.getTabLevel()));
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
