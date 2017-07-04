package theory.theory.sequencediagrams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.DataUnit;
import data.classdiagrams.PrimitiveType;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.vocabulary.VocabularyAssociationBuilder;

public class TempVarCausationBuilder
{
	public static final Pattern GETTER_PARAMTETER = Pattern.compile("\\(\"(.*?)\"\\)");
	
	public TempVarCausationBuilder(int tabLevel)
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
	
	public TempVarCausationBuilder handleMessage(Message message, SeqDiagramStore store)
	{
		if (message.getContent().contains("="))
		{
			return this.handleAssignment(message, store);
		}
	}
	
	private TempVarCausationBuilder handleAssignment(Message message, SeqDiagramStore store)
	{
		String[] parts = message.getContent().split("=");
		
		StringBuilder merge = new StringBuilder();
		
		for (int i = 1; i < parts.length; i++)
		{
			merge.append(parts[i]);
		}
		
		TempVar assigned = store.resolveTempVar(parts[0]);
		String rhs = merge.toString();
		
		if (rhs.contains("get"))
		{
			String[] attrSpec = rhs.replaceAll("get","").split("By");
			String toGetClassName = attrSpec[0];
			TempVar getFromT = message.getTo(store);
			Class getFrom = store.getClassByName(message.getTo(store).getType().getTypeName(store));
			Optional<DataUnit> maybeAttr = getFrom.getAttributeByName(toGetClassName);
			
			if (maybeAttr.isPresent())
			{
				String toAppend = "! t [Time] x [" + OutputConvenienceFunctions.toIDPType(maybeAttr.get().getType(), store)
					+ "] : C_" + OutputConvenienceFunctions.singleTempVarPredicateName(assigned) + "(t, x) <- SDPointAt(t, " + message.getSdPoint()
					+ ") & (? o [" + getFrom.getName() + "] : " + OutputConvenienceFunctions.singleTempVarPredicateName(getFromT)
					+ "(t, o) & " + getFrom.getName() + maybeAttr.get().getName() + "(t, o, x)).";
				this.getStringBuilder().append(OutputConvenienceFunctions.insertTabsNewLine(toAppend, this.getTabLevel()));
				
				return this;
			}
			
			Association assoc = null;
			for (Association ele : store.getAssociations())
			{
				List<String> asList = Arrays.asList(getFrom.getName(), toGetClassName);
				
				if (ele.containsAll(store, asList))
				{
					assoc = ele;
				}
			}
			
			String predicateName = VocabularyAssociationBuilder.makePredicateName(assoc, store);
			int fromPos = assoc.positionOf(store, getFrom.getName());
			int toPos = assoc.positionOf(store, toGetClassName);
		}
	}
}
