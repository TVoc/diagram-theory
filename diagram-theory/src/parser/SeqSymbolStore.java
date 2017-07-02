package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.sequencediagrams.TempVarContext;

public class SeqSymbolStore extends SymbolStore implements TempVarContext
{
	public SeqSymbolStore()
	{
		super();
		
		idsToNames = new HashMap<String,String>();
		tempVars = new HashMap<String,TempVar>();
		messages = new ArrayList<Message>();
		idsToMessages = new HashMap<String,Message>();
		altCombinedFragments = new ArrayList<AltCombinedFragment>();
		loopCombinedFragments = new ArrayList<LoopCombinedFragment>();
	}
	
	private final Map<String,String> idsToNames;
	
	private Map<String,String> internalGetIdsToNames()
	{
		return this.idsToNames;
	}
	
	public Map<String,String> getIdsToNames()
	{
		return Collections.unmodifiableMap(this.internalGetIdsToNames());
	}
	
	public boolean hasId(String id)
	{
		return this.internalGetIdsToNames().containsKey(id);
	}
	
	public String getNameOf(String id) throws IllegalArgumentException
	{
		if (! this.hasId(id))
		{
			throw new IllegalArgumentException("id not present");
		}
		
		return this.internalGetIdsToNames().get(id);
	}
	
	public void addName(String id, String name) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		
		this.internalGetIdsToNames().put(id, name);
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
		if (! this.hasId(id))
		{
			throw new IllegalArgumentException("id not present");
		}
		
		return this.internalGetTempVars().get(this.internalGetIdsToNames().get(id));
	}
	
	public void addTempVar(String name, TempVar temp) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (temp == null)
		{
			throw new IllegalArgumentException("temp cannot be null");
		}
		
		this.internalGetTempVars().put(name, temp);
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
	
	public void addMessage(Message message) throws IllegalArgumentException
	{
		if (message == null)
		{
			throw new IllegalArgumentException("message cannot be null");
		}
		
		this.internalGetMessages().add(message);
	}
	
	private final Map<String, Message> idsToMessages;
	
	private Map<String, Message> internalGetIdsToMessages()
	{
		return this.idsToMessages;
	}
	
	public Map<String, Message> getIdsToMessages()
	{
		return Collections.unmodifiableMap(this.internalGetIdsToMessages());
	}
	
	public boolean hasMessage(String id)
	{
		return this.internalGetIdsToMessages().containsKey(id);
	}
	
	public Message idToMessage(String id) throws IllegalArgumentException
	{
		if (! this.hasMessage(id))
		{
			throw new IllegalArgumentException("id not present");
		}
		
		return this.internalGetIdsToMessages().get(id);
	}
	
	public void addIdToMessage(String id, Message message) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (message == null)
		{
			throw new IllegalArgumentException("message cannot be null");
		}
		
		this.internalGetIdsToMessages().put(id, message);
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
