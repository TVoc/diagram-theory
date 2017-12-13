package theory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
		this.diagramNames = new HashSet<String>();
		this.callPoints = new HashMap<Message, Message>();
		
		Map<String, Message> firstInstructions = new HashMap<String, Message>();
		
		for (Message message : this.messages)
		{
			diagramNames.add(message.getDiagramName());
			if (((int) message.getSdPoint()) == 1)
			{
				firstInstructions.put(message.getDiagramName(), message);
			}
		}
		
		for (Message message : this.messages)
		{
			if (message.getSdPoint().getSequenceNumber() != 1)
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
		
		for (Message key : this.callPoints.keySet())
		{
			int i = this.messages.indexOf(key);
			this.messages.add(i + 1, new Message("", key.getSDPoint().toString(), key.getSdPoint() + 0.5, false, Optional.empty(), Optional.empty(),
					key.getDiagramName(), key.getFragment().get()));
		}
		
		this.diagramMessages = new HashMap<String, List<Message>>();
		
		for (Message message : this.messages)
		{
			if (! diagramMessages.containsKey(message.getDiagramName()))
			{
				diagramMessages.put(message.getDiagramName(), new ArrayList<Message>());
			}
			diagramMessages.get(message.getDiagramName()).add(message);
		}
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
	
	public int numMessages()
	{
		return this.internalGetMessages().size();
	}
	
	private final Map<String, List<Message>> diagramMessages;
	
	private Map<String, List<Message>> internalGetDiagramMessages()
	{
		return this.diagramMessages;
	}
	
	public Map<String, List<Message>> getDiagramMessages()
	{
		return Collections.unmodifiableMap(this.internalGetDiagramMessages());
	}
	
	public List<Message> getMessagesForDiagram(String diagramName) throws IllegalArgumentException
	{
		if (diagramName == null)
		{
			throw new IllegalArgumentException("diagramName cannot be null");
		}
		if (! this.internalGetDiagramMessages().containsKey(diagramName))
		{
			throw new IllegalArgumentException("diagram does not contain messages for diagram name: " + diagramName);
		}
		return this.internalGetDiagramMessages().get(diagramName);
//		if (diagramName == null)
//		{
//			throw new IllegalArgumentException("diagramName cannot be null");
//		}
//		
//		List<Message> toReturn = new ArrayList<Message>();
//		
//		for (Message message : this.internalGetMessages())
//		{
//			if (message.getDiagramName().equals(diagramName))
//			{
//				toReturn.add(message);
//			}
//		}
//		
//		Collections.sort(toReturn);
//		
//		return toReturn;
	}
	
	public Optional<Message> getNextMessage(Message message)
	{
		List<Message> messages = this.getMessagesForDiagram(message.getDiagramName());
		
		try
		{
			return Optional.of(messages.get((int) message.getSdPoint()));
		}
		catch (IndexOutOfBoundsException e)
		{
			return Optional.empty();
		}
	}
	
	public Set<String> getAllSdPoints()
	{
		Set<String> toReturn = new HashSet<String>();
		
		for (Message message : this.internalGetMessages())
		{
			toReturn.add(message.getFullSDPoint());
		}
		
		return toReturn;
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
	
	public void expandWithCallPoints(List<Message> messages)
	{
		for (int i = 0; i < messages.size(); i++)
		{
			Message ele = messages.get(i);
			
			for (Message cpMessage : this.internalGetCallPoints().keySet())
			{
				if (ele.equals(cpMessage))
				{
					messages.add((i+1), new Message("", ele.getSDPointAsCallPoint(), ele.getSdPoint() + 0.5, false, Optional.empty(), Optional.empty(),
					ele.getDiagramName()));
					i++;
				}
			}
		}
	}
	
	private final Set<String> diagramNames;
	
	private Set<String> internalGetDiagramNames()
	{
		return this.diagramNames;
	}
	
	public Set<String> getDiagramNames()
	{
		return Collections.unmodifiableSet(this.diagramNames);
	}
	
	public boolean matchesDiagram(String content)
	{
		for (String diagramName : this.internalGetDiagramNames())
		{
			if (content.contains(diagramName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean callsDiagram(Message message)
	{
		if (message.getSdPoint() == 1)
		{
			return false;
		}
		else
		{
			return this.matchesDiagram(message.getContent());
		}
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
