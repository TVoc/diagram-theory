package theory.theory.sequencediagrams;

import data.classdiagrams.TypeContext;
import data.sequencediagrams.TempVar;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import theory.OutputConvenienceFunctions;

public class StateAxiomBuilder
{
	public StateAxiomBuilder(int tabLevel)
	{
		this.tabLevel = tabLevel;
		this.tempVarStringBuilder = new StringBuilder();
		this.classStringBuilder = new StringBuilder();
	}
	
	private final int tabLevel;
	
	private final StringBuilder tempVarStringBuilder;
	
	public int getTabLevel()
	{
		return this.tabLevel;
	}

	private StringBuilder getTempVarStringBuilder()
	{
		return this.tempVarStringBuilder;
	}
	
	private final StringBuilder classStringBuilder;
	
	private StringBuilder getClassStringBuilder()
	{
		return this.classStringBuilder;
	}
	
	public StateAxiomBuilder addTempVar(TempVar tempVar, TypeContext context)
	{
		String[] predicates = OutputConvenienceFunctions.tempVarPredicateNames(tempVar);
		String typeName = OutputConvenienceFunctions.toIDPType(tempVar.getType(), context);
		
		this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + typeName + "] : " + predicates[0] + "(Start, x) <- " + predicates[1] + "(x).", this.getTabLevel() + 1));
		this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + typeName + "] : " + predicates[0] + "(t, x) <- " + predicates[2] + "(t, x).", this.getTabLevel() + 1));
		this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + typeName + "] : " + predicates[0] + "(Next(t), x) <- " + predicates[0] + "(t, x) & ~( ? x1 [" + typeName + "] : " + predicates[2] + "(t, x1) & ~(x = x1)).", this.getTabLevel() + 1));
		this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1));
		
		return this;
	}
	
	public StateAxiomBuilder addClass(Class clazz, TypeContext context)
	{
		if (clazz.getAllAttributes().isPresent())
		{
			for (DataUnit attr : clazz.getAllAttributes().get())
			{
				String typeName = OutputConvenienceFunctions.toIDPType(attr.getType(), context);
				String baseName = clazz.getName() + attr.getName();
				
				this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! x [" + clazz.getName() + "] y [" + typeName + "] : " + baseName + "(Start, x, y) <- I_" + baseName + "(x, y).", this.getTabLevel() + 1));
				this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + clazz.getName() + "] y [" + typeName + "] : " + baseName + "(t, x, y) <- C_" + baseName + "(t, x, y).", this.getTabLevel() + 1));
				this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine("! t [Time] x [" + clazz.getName() + "] y [" + typeName + "] : " + baseName + "(Next(t), x, y) <- " + baseName + "(t, x, y) & ~Cn_" + baseName + "(t, x, y).", this.getTabLevel() + 1));
				this.getTempVarStringBuilder().append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1));
			}
		}
		
		return this;
	}
	
	public String build()
	{
		return OutputConvenienceFunctions.insertTabsNewLine("! s [SDPoint] : SDPointAt(Start, s) <- I_SDPoint(s).", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("! t [Time] s [SDPoint] : SDPointAt(Next(t), s) <- C_SDPoint(Next(t), s).", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsNewLine("! t [Time] s [SDPoint] : SDPointAt(Next(t), s) <- SDPointAt(t, s) & ~(? s1 [SDPoint] : C_SDPoint(Next(t), s1)).", this.getTabLevel() + 1)
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
				+ this.getTempVarStringBuilder().toString()
				+ OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel() + 1)
				+ this.getClassStringBuilder().toString();
	}
}
