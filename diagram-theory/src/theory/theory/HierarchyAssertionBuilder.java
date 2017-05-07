package theory.theory;

import data.Generalization;
import theory.DiagramStore;
import theory.OutputConvenienceFunctions;

public class HierarchyAssertionBuilder
{
	public HierarchyAssertionBuilder(int tabLevel)
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
	
	public HierarchyAssertionBuilder addGeneralization(Generalization generalization, DiagramStore store)
	{
		String superType = generalization.getSupertype(store);
		String subType = generalization.getSubtype(store);
		this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(
				"! x y : IsDirectSupertypeOf(x, y) <- x = "
				+ superType + " & y = " + subType + ".", this.getTabLevel() + 1));
		
		return this;
	}
	
	public String build()
	{
		if (this.getStringBuilder().length() == 0)
		{
			return new StringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel())
					+ OutputConvenienceFunctions.insertTabsNewLine("! x y : IsDirectSupertypeOf(x, y) <- false.", this.getTabLevel() + 1)
					+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsNewLine("! o : ?1 x : RuntimeClass(x, o).", this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsNewLine("! x y : IsSupertypeOf(x, y) <- IsDirectSupertypeOf(x, y).", this.getTabLevel() + 1))
					.append(OutputConvenienceFunctions.insertTabsNewLine("! x y : IsSupertypeOf(y, x) <- ? z : IsSupertypeOf(y, z) & IsSupertypeOf(z, x).", this.getTabLevel() + 1))
					.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1))
					.append(OutputConvenienceFunctions.insertTabsNewLine("! x o : StaticClass(x, o) <- RuntimeClass(x, o).", this.getTabLevel() + 1))
					.append(OutputConvenienceFunctions.insertTabsNewLine("! x y o: StaticClass(y, o) <- RuntimeClass(x, o) & IsSupertypeOf(y, x).", this.getTabLevel() + 1))
					.append(OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
					.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
					.toString();
		}
		return new StringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel())
									+ this.getStringBuilder().toString()
									+ OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsNewLine("! o : ?1 x : RuntimeClass(x, o).", this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsNewLine("{", this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsNewLine("! x y : IsSupertypeOf(x, y) <- IsDirectSupertypeOf(x, y).", this.getTabLevel() + 1))
								.append(OutputConvenienceFunctions.insertTabsNewLine("! x y : IsSupertypeOf(y, x) <- ? z : IsSupertypeOf(y, z) & IsSupertypeOf(z, x).", this.getTabLevel() + 1))
								.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1))
								.append(OutputConvenienceFunctions.insertTabsNewLine("! x o : StaticClass(x, o) <- RuntimeClass(x, o).", this.getTabLevel() + 1))
								.append(OutputConvenienceFunctions.insertTabsNewLine("! x y o: StaticClass(y, o) <- RuntimeClass(x, o) & IsSupertypeOf(y, x).", this.getTabLevel() + 1))
								.append(OutputConvenienceFunctions.insertTabsNewLine("}", this.getTabLevel()))
								.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()))
								.toString();
	}
}
