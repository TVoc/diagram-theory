package theory.vocabulary;

import java.util.List;
import java.util.Optional;

import data.classdiagrams.DataUnit;
import data.classdiagrams.Operation;
import data.classdiagrams.PrimitiveType;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class ClassOperationBuilder
{
	public ClassOperationBuilder(int tabLevel)
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
	
	public ClassOperationBuilder addOperation(Operation operation, String className, DiagramStore store)
	{
		StringBuilder predicateArguments = new StringBuilder("(Object,");
		
		Optional<List<DataUnit>> parameters = operation.getAllParameters();
		
		if (parameters.isPresent())
		{
			for (DataUnit ele : parameters.get())
			{
				String typeName = ele.getTypeName(store);
				if (PrimitiveType.isPrimitiveType(typeName))
				{
					predicateArguments.append(OutputConvenienceFunctions.primitiveTypeToLogicType(typeName) + ",");
				}
				else
				{
					predicateArguments.append("Object,");
				}
			}
		}
		
		if (PrimitiveType.isPrimitiveType(operation.getResultType().getTypeName(store)))
		{
			predicateArguments.append(OutputConvenienceFunctions.primitiveTypeToLogicType(operation.getResultType().getTypeName(store)) + ")");
		}
		else
		{
			predicateArguments.append("Object)");
		}
		
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(className + operation.getName()
			+ predicateArguments.toString(), this.getTabLevel()));
		
		return this;
	}
	
	public String build()
	{
		return this.getStringBuilder().toString();
	}
}
