package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
		List<Message> ifInternal = alt.getIfMessages();
		List<Message> ifPart = alt.flattenIf();
		List<Message> thenInternal = alt.getThenMessages();
		List<Message> thenPart = alt.flattenThen();
		Message finalIf = ifPart.get(ifPart.size() - 1);
		Message finalThen = thenPart.get(thenPart.size() - 1);

		if ((! ifInternal.isEmpty()) && finalIf.equals(ifInternal.get(ifInternal.size() - 1)))
		{
			this.loopReentryCrawlUp(output, finalIf, alt, "");
		}
		if ((! thenInternal.isEmpty()) && finalThen.equals(thenInternal.get(thenInternal.size() - 1)))
		{
			this.loopReentryCrawlUp(output, finalThen, alt, "");
		}
	}

	private void loopReentryHandleLoop(Map<Message, List<Pair<SDPoint, String>>> output, LoopCombinedFragment loop)
	{
		List<Message> internal = loop.getMessages();
		List<Message> flattened = loop.flattenMessages();
		Message finalFlat = flattened.get(flattened.size() - 1);

		if ((! internal.isEmpty()) && finalFlat.equals(internal.get(internal.size() - 1)))
		{
			this.loopReentryCrawlUp(output, finalFlat, loop, "");
		}
	}

	private void loopReentryCrawlUp(Map<Message, List<Pair<SDPoint, String>>> output, Message message, CombinedFragment frag
			, String intermediate)
	{
		CombinedFragment prev;

		do
		{
			prev = frag;

			LoopCombinedFragment loop;

			if (frag instanceof LoopCombinedFragment)
			{
				loop = (LoopCombinedFragment) frag;

				if (loop.isAFinalMessage(message))
				{
					Map<Message, String> entries = loop.getFirstEntryPoints();

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
			}

			if (frag.getParent().isPresent())
			{
				if (frag instanceof LoopCombinedFragment)
				{
					intermediate = intermediate.equals("") ? "~(" + ((LoopCombinedFragment) frag).getGuard() + ")"
							: intermediate + " & ~(" + ((LoopCombinedFragment) frag).getGuard() + ")";
				}

				frag = frag.getParent().get();
			}
		}
		while (prev.getParent().isPresent());
	}

	protected abstract void fillTree(List<CombinedFragment> output);

	public abstract TreeMap<Message, String> getEntryPoints();

	public abstract List<Message> getMessages();

	public Message getMessage(int index) throws IndexOutOfBoundsException
	{
		return this.flattenMessages().get(0);
	}

	public abstract String getDiagramName();

	public abstract boolean containsMessage(Message message);

	public Message getFinalMessage()
	{
		return this.flattenMessages().get(this.flattenMessages().size() - 1);
	}

	public abstract List<Message> flattenMessages();

	protected abstract boolean isAFinalMessage(Message message);

	protected abstract void getEntryPointsRec(TreeMap<Message, String> output, String intermediate);

	protected void wrapLoops(TreeMap<Message, String> output, Set<CombinedFragment> seen, String intermediate, List<CombinedFragment> children
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

			if (ele instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment eleL = (LoopCombinedFragment) ele;
				if (all)
				{
					eleL.getEntryPointsRec(output, intermediate);
				}
				else
				{
					eleL.getFirstEntryPointsRec(output, intermediate);
				}
				intermediate = intermediate + " & ~(" + eleL.getGuard() + ")";
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
				break;
			}

			upTo++;
		}

		fragments.subList(upTo, fragments.size()).clear();
	}

	public abstract TreeMap<Message, String> getFirstEntryPoints();

	protected abstract void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate);

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
		List<Message> diagramMessages = store.getMessagesForDiagram(finalMsg.getDiagramName());

		if (finalMsg.getSDPoint().getSequenceNumber() < diagramMessages.size())
		{
			List<LoopCombinedFragment> loops = this.gatherNextLoops(diagramMessages);
			Map<Message, String> entryPoints = new TreeMap<Message, String>();

			if (! loops.isEmpty())
			{
				for (LoopCombinedFragment ele : loops)
				{
					Map<Message, String> elePoints = ele.getEntryPoints();

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

					exit.putExits(elePoints);
				}

				return;
			}

			Message next = diagramMessages.get(finalMsg.getSDPoint().getSequenceNumber());

			if (next.getFragment().isPresent())
			{
				CombinedFragment frag = next.getFragment().get();

				while (frag.getParent().isPresent())
				{
					frag = frag.getParent().get();
				}

				entryPoints.putAll(next.getFragment().get().getEntryPoints());

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

	protected List<LoopCombinedFragment> gatherNextLoops(List<Message> diagramMessages)
	{
		List<LoopCombinedFragment> toReturn = new ArrayList<LoopCombinedFragment>();

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
					toReturn.add((LoopCombinedFragment) frag);				
					
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
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
