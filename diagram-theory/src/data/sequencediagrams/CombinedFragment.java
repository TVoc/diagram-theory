package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import theory.SeqDiagramStore;

public abstract class CombinedFragment implements MessageContainer
{
	public CombinedFragment(Optional<CombinedFragment> parent) throws IllegalArgumentException
	{
		if (parent == null)
		{
			throw new IllegalArgumentException("parent cannot be null");
		}

		this.parent = parent;
	}

	private Optional<CombinedFragment> parent;

	public Optional<CombinedFragment> getParent()
	{
		return this.parent;
	}
	
	public void setParent(Optional<CombinedFragment> parent) throws IllegalArgumentException
	{
		if (parent == null)
		{
			throw new IllegalArgumentException("parent cannot be null");
		}
		
		this.parent = parent;
	}

	public CombinedFragment getTopLevelFragment()
	{
		if (this.getParent().isPresent())
		{
			return this.getParent().get().getTopLevelFragment();
		}

		return this;
	}

	public List<CombinedFragment> getTree()
	{
		List<CombinedFragment> toReturn = new ArrayList<CombinedFragment>();
		this.fillTree(toReturn);
		return toReturn;
	}
	
	public boolean hasAsDescendent(CombinedFragment frag)
	{
		return this.getTree().contains(frag);
	}

	public Map<Message, List<Pair<SDPoint, String>>> calculateLoopReentryGuards()
	{
		Map<Message, List<Pair<SDPoint, String>>> output = new TreeMap<Message, List<Pair<SDPoint, String>>>();
		this.calculateLoopReentryGuardsRec(output);
		return output;
	}

	protected void calculateLoopReentryGuardsRec(Map<Message, List<Pair<SDPoint, String>>> output)
	{
		if (this instanceof AltCombinedFragment)
		{
			this.loopReentryHandleAlt(output, (AltCombinedFragment) this);
		}
		else
		{
			this.loopReentryHandleLoop(output, (LoopCombinedFragment) this);
		}
	}

	private void loopReentryHandleAlt(Map<Message, List<Pair<SDPoint, String>>> output, AltCombinedFragment alt)
	{
		List<MessageContainer> containers = alt.getIfAsContainers();
		
		for (int i = containers.size() - 1; i >= 0; i++)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				this.loopReentryCrawlUp(output, (Message) container, alt, "");
			}
			
			if (! container.optional())
			{
				break;
			}
		}
		
		containers = alt.getThenAsContainers();
		
		for (int i = containers.size() - 1; i >= 0; i++)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				this.loopReentryCrawlUp(output, (Message) container, alt, "");
			}
			
			if (! container.optional())
			{
				break;
			}
		}
	}

	private void loopReentryHandleLoop(Map<Message, List<Pair<SDPoint, String>>> output, LoopCombinedFragment loop)
	{
		List<MessageContainer> containers = loop.getAsContainers();
		
		for (int i = containers.size() - 1; i >= 0; i--)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				this.loopReentryCrawlUp(output, (Message) container, loop, "");
			}
			
			if (! container.optional())
			{
				break;
			}
		}
	}

	private void loopReentryCrawlUp(Map<Message, List<Pair<SDPoint, String>>> output, Message message, CombinedFragment frag
			, String intermediate)
	{
		CombinedFragment prev;
		Set<CombinedFragment> excluded = new HashSet<CombinedFragment>();

		do
		{
			prev = frag;

			LoopCombinedFragment loop;

			if (frag instanceof LoopCombinedFragment)
			{
				loop = (LoopCombinedFragment) frag;

				if (loop.isAFinalMessage(message))
				{
					Map<Message, String> entries = loop.getFirstEntryPoints(Optional.of(excluded));

					for (Entry<Message, String> entry : entries.entrySet())
					{
						if (output.containsKey(entry.getKey()))
						{
							output.get(entry.getKey()).add(new ImmutablePair<SDPoint, String>(message.getSDPoint(),
								intermediate.equals("") ? entry.getValue() : intermediate + " & " + entry.getValue()));
						}
						else
						{
							output.put(entry.getKey(), new ArrayList<Pair<SDPoint, String>>(
								Arrays.asList(new ImmutablePair<SDPoint, String>(message.getSDPoint(),
									intermediate.equals("") ? entry.getValue() : intermediate + " & " + entry.getValue()))));
						}
						
					}
				}
				
				excluded.add(frag);
			}

			if (frag.getParent().isPresent())
			{
				if (frag instanceof LoopCombinedFragment)
				{
					intermediate = intermediate.equals("") ? "~" + ((LoopCombinedFragment) frag).getGuard()
							: intermediate + " & ~" + ((LoopCombinedFragment) frag).getGuard();
				}
				
				frag = frag.getParent().get();
			}
		}
		while (prev.getParent().isPresent());
	}

	protected abstract void fillTree(List<CombinedFragment> output);

	public abstract TreeMap<Message, String> getEntryPoints(Optional<Set<CombinedFragment>> excluded); // TODO what if a loop is never entered?
	
	public abstract TreeMap<Message, String> getEntryPoints(Optional<Set<CombinedFragment>> excluded, String intermediate);

	public abstract List<Message> getMessages();

	public Message getMessage(int index) throws IndexOutOfBoundsException
	{
		return this.flattenMessages().get(index);
	}

	public abstract String getDiagramName();

	@Override
	public abstract boolean containsMessage(Message message);

	public Message getFinalMessage()
	{
		return this.flattenMessages().get(this.flattenMessages().size() - 1);
	}

	public abstract List<Message> flattenMessages();

	protected abstract boolean isAFinalMessage(Message message);

	protected abstract void getEntryPointsRec(TreeMap<Message, String> output, String intermediate, Optional<Set<CombinedFragment>> excluded);

	protected void wrapLoops(TreeMap<Message, String> output, Set<CombinedFragment> seen, Optional<Set<CombinedFragment>> excluded, String intermediate, List<CombinedFragment> children
			, boolean skipFirst, boolean all)
	{
		Iterator<CombinedFragment> it = children.iterator();

		if (skipFirst)
		{
			it.next();
		}

		while (it.hasNext())
		{
			CombinedFragment ele = it.next();

			if (excluded.isPresent() && excluded.get().contains(ele))
			{
				continue;
			}
			
			if (ele instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment eleL = (LoopCombinedFragment) ele;
				
				if (all)
				{
					eleL.getEntryPointsRec(output, intermediate, excluded);
				}
				else
				{
					eleL.getFirstEntryPointsRec(output, intermediate, excluded);
				}
				
				if (intermediate.isEmpty())
				{
					intermediate = "~" + eleL.getGuard();
				}
				else
				{
					intermediate = intermediate + " & ~" + eleL.getGuard();
				}
				
				seen.add(eleL);
			}
			else
			{
				if (! all)
				{
					break;
				}
				intermediate = "";
			}
		}
	}

	protected void extractConsecutiveLoops(List<CombinedFragment> fragments)
	{
		if (! (fragments.get(0) instanceof LoopCombinedFragment))
		{
			fragments.subList(1, fragments.size()).clear();
			return;
		}
		
		int upTo = 1;

		for (int i = 1; i < fragments.size(); i++)
		{
			CombinedFragment ele = fragments.get(i);

			if (! (ele instanceof LoopCombinedFragment))
			{
				upTo++;
				break;
			}

			upTo++;
		}

		fragments.subList(upTo, fragments.size()).clear();
	}

	public abstract TreeMap<Message, String> getFirstEntryPoints(Optional<Set<CombinedFragment>> excluded);

	protected abstract void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate, Optional<Set<CombinedFragment>> excluded);

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

	protected abstract void traverseUp(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate, Optional<Set<CombinedFragment>> excluded);
	
	public String constructGuard(String aggregate, String entryGuard, String intermediate)
	{
		String toReturn = null;
		
		if (! ("".equals(aggregate) || "".equals(entryGuard)))
		{
			toReturn = aggregate + " & " + entryGuard; 
		}
		else if ("".equals(aggregate) && "".equals(entryGuard))
		{
			toReturn = "";
		}
		else if ("".equals(aggregate))
		{
			toReturn = entryGuard;
		}
		else // "".equals(entryGuard)
		{
			toReturn = aggregate;
		}
		
		if (! ("".equals(toReturn) || "".equals(intermediate)))
		{
			return toReturn + " & " + intermediate;
		}
		else if ("".equals(toReturn) && "".equals(intermediate))
		{
			return "";
		}
		else if ("".equals(toReturn))
		{
			return intermediate;
		}
		else // "".equals(intermediate)
		{
			return toReturn;
		}
	}
	
	public String constructGuard(String left, String right)
	{
		if (! ("".equals(left) || "".equals(right)))
		{
			return left + " & " + right; 
		}
		else if ("".equals(left) && "".equals(right))
		{
			return "";
		}
		else if ("".equals(left))
		{
			return right;
		}
		else // "".equals(right)
		{
			return left;
		}
		
	}
	
	protected void putExits(ExitForMessageBuilder exit, Map<Message, String> entryPoints)
	{
		for (Entry<Message, String> entry : entryPoints.entrySet())
		{
			this.putExit(exit, entry.getKey(), entry.getValue());
		}
	}
	
	protected void putExit(ExitForMessageBuilder exit, Message entry, String guard)
	{	
		if (entry.getSDPoint().compareTo(exit.getMessage().getSDPoint()) <= 0)
		{
			return;
		}
		
		if (entry.getFragment().isPresent() &&
				(entry.getFragment().get().equals(exit.getMessage().getFragment().get())
						||
				exit.getMessage().getFragment().get().hasAsDescendent(entry.getFragment().get())))
		{
			return;
		}
		
		exit.putExit(entry, guard);
	}

	protected int calculateSkips(Message message, SeqDiagramStore store)
	{
		Optional<CombinedFragment> frag = message.getFragment();
		
		if (! frag.isPresent())
		{
			return 0;
		}
		
		if (frag.get() instanceof AltCombinedFragment)
		{
			AltCombinedFragment alt = AltCombinedFragment.class.cast(frag.get());
			List<Message> ifs = alt.flattenIf();
			if (message.equals(ifs.get(ifs.size() - 1)))
			{
				return alt.flattenThen().size();
			}
		}
		
		return 0;
	}
	
	protected void exitToOutside(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, Optional<String> exitGuard)
	{
		List<Message> messages = this.flattenMessages();
		Message finalMsg = messages.get(messages.size() - 1);
		List<Message> diagramMessages = store.getMessagesForDiagram(finalMsg.getDiagramName());

		if (finalMsg.getSDPoint().getSequenceNumber() < diagramMessages.size())
		{
			List<CombinedFragment> loops = this.gatherNextLoops(diagramMessages);
			Map<Message, String> entryPoints = new TreeMap<Message, String>();

			if (! loops.isEmpty())
			{
				StringBuilder intermediate = new StringBuilder();
				
				for (CombinedFragment ele : loops)
				{
					Map<Message, String> elePoints = ele.getEntryPoints(Optional.empty(), intermediate.toString());

					if (exitGuard.isPresent())
					{
						for (Entry<Message, String> entry : elePoints.entrySet())
						{
							if (entry.getValue().equals(""))
							{
								entry.setValue(exitGuard.get());
							}
							else
							{
								entry.setValue(exitGuard.get() + " & " + entry.getValue());
							}
						}
					}

					this.putExits(exit, elePoints);
					
					if (ele instanceof LoopCombinedFragment)
					{
						if (intermediate.toString().isEmpty())
						{
							intermediate.append(" & ~" + ((LoopCombinedFragment) ele).getGuard());
						}
						else
						{
							intermediate.append("~" + ((LoopCombinedFragment) ele).getGuard());
						}
					}

				}

				if (! (loops.get(loops.size() - 1) instanceof LoopCombinedFragment))
				{
					return;
				}
			}

			Message next = store.getNextMessage(finalMsg).get();

			if (next.getFragment().isPresent())
			{
				CombinedFragment frag = next.getFragment().get();

				while (frag.getParent().isPresent())
				{
					frag = frag.getParent().get();
				}

				entryPoints.putAll(next.getFragment().get().getEntryPoints(Optional.empty()));

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

				this.putExits(exit, entryPoints);

				return;
			}

			if (exitGuard.isPresent())
			{
				this.putExit(exit, next, exitGuard.get());
			}
			else
			{
				this.putExit(exit, next, "");
			}
		}
	}

	protected List<CombinedFragment> gatherNextLoops(List<Message> diagramMessages)
	{
		List<CombinedFragment> toReturn = new ArrayList<CombinedFragment>();

		Message message = this.getFinalMessage();

		if (message.getSDPoint().getSequenceNumber() >= diagramMessages.size())
		{
			return toReturn;
		}


		message = diagramMessages.get(diagramMessages.indexOf(message) + 1);
		boolean done = false;

		while (! done)
		{
			//			if (message.getSDPoint().getSequenceNumber() >= diagramMessages.size())
			//			{
			//				done = true;
			//			}
			//			else
			//			{
			if (message.getFragment().isPresent())
			{
				CombinedFragment frag = message.getFragment().get();

				while (frag.getParent().isPresent())
				{
					frag = frag.getParent().get();
				}

				if ((frag instanceof LoopCombinedFragment))
				{
					toReturn.add(frag);				
					
					Message fragFinal = frag.getFinalMessage();

					if (fragFinal.getSDPoint().getSequenceNumber() < diagramMessages.size())
					{
						message = diagramMessages.get(diagramMessages.indexOf(frag.getFinalMessage()) + 1);
					}
					else
					{
						done = true;
					}
				}
				else
				{
					toReturn.add(frag);
					done = true;
				}
			}
			else
			{
				done = true;
			}
			//			}
		}

		return toReturn;
	}

	protected Optional<Message> getMessageAfter(Message message, List<Message> messages, int skip)
	{
		Optional<Message> toReturn = Optional.empty();

		int skipCount = 0;
		
		for (Message ele : messages)
		{
			int compare = message.compareTo(ele);

			if (compare >= 0)
			{
				continue;
			}
			else if (skipCount < skip)
			{
				skipCount++;
				continue;
			}
			else // compare < 0
			{
				toReturn = Optional.of(ele);
				break;
			}
		}

		return toReturn;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
	
	public int compareMessageContainer(MessageContainer other)
	{
		if (this == other)
		{
			return 0;
		}
		
		if (other instanceof Message)
		{
			List<Message> messages = this.flattenMessages();
			
			if (messages.contains(other))
			{
				return 0;
			}
			if (((Message) other).getSDPoint().getSequenceNumber() < messages.get(0).getSDPoint().getSequenceNumber())
			{
				return 1;
			}
			else // ((Message) other).getSDPoint().getSequenceNumber() > messages.get(last).getSDPoint().getSequenceNumber()
			{
				return -1;
			}
		}
		
		List<Message> messages = this.flattenMessages();
		List<Message> otherMessages = ((CombinedFragment) other).flattenMessages();
		
		if (messages.containsAll(otherMessages) || otherMessages.containsAll(messages))
		{
			return 0;
		}
		if (messages.get(messages.size() - 1).getSDPoint().getSequenceNumber() < otherMessages.get(0).getSDPoint().getSequenceNumber())
		{
			return -1;
		}
		else // first message in messages comes after otherMessages
		{
			return 1;
		}
	}
}
