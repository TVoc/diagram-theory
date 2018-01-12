package parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.DiagramInfo;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;
import data.sequencediagrams.TempVarContext;
import theory.CallPointExpander;

//TODO processing DiagramInfo objects

public class SeqSymbolStore extends SymbolStore implements TempVarContext, CallPointExpander
{
	public SeqSymbolStore()
	{
		super();
		
		this.idsToNames = new HashMap<String,String>();
		this.tempVars = new HashMap<String,TempVar>();
		this.messages = new ArrayList<Message>();
		this.diagrams = new HashMap<String, DiagramInfo>();
		this.idsToMessages = new HashMap<String,Message>();
		this.callPointsExpanded = false;
		this.callPoints = new TreeMap<Message, Message>();
		this.altCombinedFragments = new ArrayList<AltCombinedFragment>();
		this.loopCombinedFragments = new ArrayList<LoopCombinedFragment>();
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
	public TempVar resolveTempVar(String name) throws IllegalArgumentException
	{
		if (! this.hasTempVar(name))
		{
			throw new IllegalArgumentException("name: " + name + " not present");
		}
		
		return this.internalGetTempVars().get(name);
	}
	
	private final Map<String, DiagramInfo> diagrams;
	
	private Map<String, DiagramInfo> internalGetDiagrams()
	{
		return this.diagrams;
	}
	
	public Map<String, DiagramInfo> getDiagrams()
	{
		return Collections.unmodifiableMap(this.internalGetDiagrams());
	}
	
	public void addDiagramInfo(String diagramName, DiagramInfo info) throws IllegalArgumentException
	{
		if (diagramName == null)
		{
			throw new IllegalArgumentException("diagramName cannot be null");
		}
		if (info == null)
		{
			throw new IllegalArgumentException("info cannot be null");
		}
		
		this.internalGetDiagrams().put(diagramName, info);
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
	
	public void addMessage(Message message) throws IllegalArgumentException, IllegalStateException
	{
		if (message == null)
		{
			throw new IllegalArgumentException("message cannot be null");
		}
		if (this.callPointsExpanded())
		{
			throw new IllegalStateException("this store has already expanded call points");
		}
		
		this.internalGetMessages().add(message);
	}
	
	public void addMessages(Collection<Message> messages) throws IllegalArgumentException, IllegalStateException
	{
		if (messages == null)
		{
			throw new IllegalArgumentException("messages cannot be null");
		}
		if (this.callPointsExpanded())
		{
			throw new IllegalStateException("this store has already expanded call points");
		}
		
		this.internalGetMessages().addAll(messages);
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
		if (this.callPointsExpanded())
		{
			throw new IllegalStateException("this store has already expanded call points");
		}
		
		this.internalGetIdsToMessages().put(id, message);
	}
	
	private boolean callPointsExpanded;
	
	private boolean callPointsExpanded()
	{
		return this.callPointsExpanded;
	}
	
	private void setCallPointsExpanded(boolean callPointsExpanded)
	{
		this.callPointsExpanded = callPointsExpanded;
	}
	
	public List<Message> getMessagesForDiagram(String diagramName) throws IllegalArgumentException
	{
		if (diagramName == null)
		{
			throw new IllegalArgumentException("diagramName cannot be null");
		}
		
		List<Message> toReturn = new ArrayList<Message>();
		
		for (Message message : this.internalGetMessages())
		{
			if (message.getDiagramName().equals(diagramName))
			{
				toReturn.add(message);
			}
		}
		
		Collections.sort(toReturn);
		
		return toReturn;
	}
	
	public Optional<Message> getNextMessage(Message message)
	{
		List<Message> messages = this.getMessagesForDiagram(message.getDiagramName());
		
		try
		{
			return Optional.of(messages.get(message.getSDPoint().getSequenceNumber()));
		}
		catch (IndexOutOfBoundsException e)
		{
			return Optional.empty();
		}
	}
	
	private final Map<Message, Message> callPoints;
	
	private Map<Message, Message> internalGetCallPoints()
	{
		return this.callPoints;
	}
	
	public Map<Message, Message> getCallPoints()
	{
		return Collections.unmodifiableMap(this.internalGetCallPoints());
	}
	
	private void buildCallPoints()
	{
		Map<String, Message> firstInstructions = new HashMap<String, Message>();
		
		for (Message message : this.getMessages())
		{
			if (message.getSDPoint().getSequenceNumber() == 1)
			{
				firstInstructions.put(message.getDiagramName(), message);
			}
		}
		
		for (Message message : this.getMessages())
		{
			if (message.getSDPoint().getSequenceNumber() != 1)
			{
				for (Entry<String, Message> entry : firstInstructions.entrySet())
				{
					if (message.getContent().contains(entry.getKey()))
					{
						callPoints.put(message, entry.getValue());
					}
				}
			}
		}
	}
	
	@Override
	public void expandWithCallPoints(List<Message> messages) throws IllegalArgumentException
	{
		if (messages == null)
		{
			throw new IllegalArgumentException("messages cannot be null");
		}
		
		if (this.getCallPoints().isEmpty())
		{
			this.buildCallPoints();
		}
		
		for (int i = 0; i < messages.size(); i++)
		{
			Message ele = messages.get(i);
			
			for (Message cpMessage : this.internalGetCallPoints().keySet())
			{
				if (ele.equals(cpMessage))
				{
					if (ele.getFragment().isPresent())
					{
						messages.add((i+1), new Message("", ele.getSDPoint().toString(), ele.getSDPoint().getSequenceNumber(), false, Optional.empty(), Optional.empty(),
								ele.getDiagramName(), true, ele.getFragment().get()));
					}
					else
					{
						messages.add((i+1), new Message("", ele.getSDPoint().toString(), ele.getSDPoint().getSequenceNumber(), false, Optional.empty(), Optional.empty(),
								ele.getDiagramName(), true));
					}
					i++;
				}
			}
		}
		
		this.setCallPointsExpanded(true);
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
	
	public void addAltCombinedFragment(AltCombinedFragment fragment)
	{
		this.internalGetAltCombinedFragments().add(fragment);
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
	
	public void addLoopCombinedFragment(LoopCombinedFragment fragment)
	{
		this.internalGetLoopCombinedFragments().add(fragment);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		this.internalGetIdsToNames().clear();
		this.internalGetTempVars().clear();
		this.internalGetIdsToMessages().clear();
		this.internalGetMessages().clear();
		this.internalGetAltCombinedFragments().clear();
		this.internalGetLoopCombinedFragments().clear();
	}
}
