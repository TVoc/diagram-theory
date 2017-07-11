package theory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.Generalization;
import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.sequencediagrams.TempVarContext;

public class SeqDiagramStore extends DiagramStore implements TempVarContext
{

	SeqDiagramStore(Map<String, Class> classes, Map<String, Class> classesByName, Set<Association> associations, Set<Generalization> generalizations, Map<String,TempVar> tempVars
			, List<Message> messages, List<AltCombinedFragment> altCombinedFragments, List<LoopCombinedFragment> loopCombinedFragments)
			throws IllegalArgumentException
	{
		super(classes, classesByName, associations, generalizations);
		
		if (tempVars == null)
		{
			throw new IllegalArgumentException("tempVars cannot be null");
		}
		if (messages == null)
		{
			throw new IllegalArgumentException("messages cannot be null");
		}
		if (altCombinedFragments == null)
		{
			throw new IllegalArgumentException("altCombinedFragments cannot be null");
		}
		if (loopCombinedFragments == null)
		{
			throw new IllegalArgumentException("loopCombinedFragments cannot be null");
		}
		
		this.tempVars = tempVars;
		this.messages = messages;
		this.altCombinedFragments = altCombinedFragments;
		this.loopCombinedFragments = loopCombinedFragments;
	}
	
	private final Map<String,TempVar> tempVars;
	
	private Map<String,TempVar> internalGetTempVars()
	{
		return this.tempVars;
	}
	
	public Map<String,TempVar> getTempVars()
	{
		return Collections.unmodifiableMap(this.internalGetTempVars());
	}
	
	public boolean hasTempVar(String name)
	{
		return this.internalGetTempVars().containsKey(name);
	}
	
	@Override
	public TempVar resolveTempVar(String id) throws IllegalArgumentException
	{
		if (! this.hasTempVar(id))
		{
			throw new IllegalArgumentException("name: " + id + " not present");
		}
		
		return this.internalGetTempVars().get(id);
	}
	
	private final List<Message> messages;
	
	private List<Message> internalGetMessages()
	{
		return this.messages;
	}
	
	public List<Message> getMessages()
	{
		return Collections.unmodifiableList(this.internalGetMessages());
	}
	
	public Message getMessage(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetMessages().get(index);
	}
	
	private final List<AltCombinedFragment> altCombinedFragments;
	
	private List<AltCombinedFragment> internalGetAltCombinedFragments()
	{
		return this.altCombinedFragments;
	}
	
	public List<AltCombinedFragment> getAltCombinedFragments()
	{
		return Collections.unmodifiableList(this.internalGetAltCombinedFragments());
	}
	
	public AltCombinedFragment getAltCombinedFragment(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetAltCombinedFragments().get(index);
	}
	
	private final List<LoopCombinedFragment> loopCombinedFragments;
	
	private List<LoopCombinedFragment> internalGetLoopCombinedFragments()
	{
		return this.loopCombinedFragments;
	}
	
	public List<LoopCombinedFragment> getLoopCombinedFragments()
	{
		return Collections.unmodifiableList(this.internalGetLoopCombinedFragments());
	}
	
	public LoopCombinedFragment getLoopCombinedFragment(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetLoopCombinedFragments().get(index);
	}
}
