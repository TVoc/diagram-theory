package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import theory.SeqDiagramStore;

public abstract class CombinedFragment
{
	public CombinedFragment(Optional<CombinedFragment> parent) throws IllegalArgumentException
	{
		if (parent == null)
		{
			throw new IllegalArgumentException("parent cannot be null");
		}
		
		this.parent = parent;
	}
	
	private final Optional<CombinedFragment> parent;

	public Optional<CombinedFragment> getParent()
	{
		return this.parent;
	}
	
	public abstract TreeMap<Message, String> getEntryPoints();
	
	public abstract List<Message> getMessages();
	
	public abstract boolean containsMessage(Message message);
	
	public Message getFinalMessage()
	{
		return this.flattenMessages().get(this.flattenMessages().size() - 1);
	}
	
	public abstract List<Message> flattenMessages();
	
	protected abstract void getEntryPointsRec(TreeMap<Message, String> output, String intermediate);
	
	protected void wrapLoops(TreeMap<Message, String> output, Set<CombinedFragment> seen, String intermediate, List<CombinedFragment> children, boolean skipFirst)
	{
		Iterator<CombinedFragment> it = children.iterator();
		
		if (skipFirst)
		{
			it.next();
		}
		
		while (it.hasNext())
		{
			CombinedFragment ele = it.next();
			
			if (ele instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment eleL = (LoopCombinedFragment) ele;
				eleL.getEntryPointsRec(output, intermediate);
				intermediate = intermediate + " & ~(" + eleL.getGuard() + ")";
				seen.add(eleL);
			}
			else
			{
				intermediate = "";
			}
		}
	}
	
	protected void extractConsecutiveLoops(List<CombinedFragment> fragments)
	{
		int upTo = 1;
		
		for (int i = 1; i < fragments.size(); i++)
		{
			CombinedFragment ele = fragments.get(i);
			
			if (! (ele instanceof LoopCombinedFragment))
			{
				break;
			}
			
			upTo++;
		}
		
		fragments.subList(0, upTo).clear();
	}
	
	public abstract TreeMap<Message, String> getFirstEntryPoints();
	
	protected abstract void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate);

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CombinedFragment other = (CombinedFragment) obj;
		if (parent == null)
		{
			if (other.parent != null)
				return false;
		}
		else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	
	public List<ExitForMessage> calcExitForMessages(SeqDiagramStore store)
	{
		List<ExitForMessage> toReturn = new ArrayList<ExitForMessage>();
		
		this.calcExitForMessagesRec(store, toReturn);
		
		return toReturn;
	}
	
	public void calcExitForMessagesRec(SeqDiagramStore store, List<ExitForMessage> output)
	{
		this.exitForHandleChildren(store, output);
	}
	
	protected abstract void exitForHandleChildren(SeqDiagramStore store, List<ExitForMessage> output);
	
	protected abstract void traverseUp(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate);
	
	protected void exitToOutside(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, Optional<String> exitGuard)
	{
		List<Message> messages = this.flattenMessages();
		Message finalMsg = messages.get(messages.size() - 1);
		
		if (finalMsg.getSdPoint() < store.getMessages().size())
		{
			Message next = store.getMessage(finalMsg.getSdPoint());
			
			if (next.getFragment().isPresent())
			{
				CombinedFragment frag = next.getFragment().get();
				
				while (frag.getParent().isPresent())
				{
					frag = frag.getParent().get();
				}
				
				Map<Message, String> entryPoints = next.getFragment().get().getEntryPoints();
				
				if (exitGuard.isPresent())
				{
					for (Entry<Message, String> ele : entryPoints.entrySet())
					{
						if (ele.getValue().equals(""))
						{
							ele.setValue(exitGuard.get());
						}
						else
						{
							ele.setValue(exitGuard.get() + " & " + ele.getValue());
						}
					}
				}
				
				exit.putExits(entryPoints);
				
				return;
			}
			
			if (exitGuard.isPresent())
			{
				exit.putExit(next, exitGuard.get());
			}
			else
			{
				exit.putExit(next, "");
			}
		}
	}
	
	protected List<LoopCombinedFragment> gatherNextLoops(SeqDiagramStore store)
	{
		List<LoopCombinedFragment> toReturn = new ArrayList<LoopCombinedFragment>();
		
		Message message = this.getFinalMessage();
		
		boolean done = false;
		
		while (! done)
		{
			if (message.getSdPoint() >= store.numMessages())
			{
				done = true;
			}
			else
			{
				message = store.getMessage(message.getSdPoint());
				
				if (message.getFragment().isPresent())
				{
					CombinedFragment frag = message.getFragment().get();
					
					while (frag.getParent().isPresent())
					{
						frag = frag.getParent().get();
					}
					
					if ((frag instanceof LoopCombinedFragment))
					{
						toReturn.add((LoopCombinedFragment) frag);
					}
					else
					{
						done = true;
					}
				}
				else
				{
					done = true;
				}
			}
		}
		
		return toReturn;
	}
	
	protected Optional<Message> getMessageAfter(Message message, List<Message> messages)
	{
		Optional<Message> toReturn = Optional.empty();
		
		for (Message ele : messages)
		{
			int compare = message.compareTo(ele);
			
			if (compare >= 0)
			{
				continue;
			}
			else // compare < 0
			{
				toReturn = Optional.of(ele);
			}
		}
		
		return toReturn;
	}
}
