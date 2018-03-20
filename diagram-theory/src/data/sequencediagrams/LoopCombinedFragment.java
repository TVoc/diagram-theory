package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import theory.SeqDiagramStore;

public class LoopCombinedFragment extends CombinedFragment
{
	LoopCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> children, Optional<List<Message>> messages
			, String guard, SDPoint sdStart, SDPoint sdEnd) throws IllegalArgumentException
	{
		super(parent);

		if (guard == null)
		{
			throw new IllegalArgumentException("guard cannot be null");
		}
		if (sdStart.getSequenceNumber() < 1)
		{
			throw new IllegalArgumentException("sdStart cannot be less than 1");
		}
		if (sdEnd.getSequenceNumber() <= sdStart.getSequenceNumber())
		{
			throw new IllegalArgumentException("sdEnd cannot be less than or equal to sdStart");
		}

		this.guard = guard.replaceAll("\\s*", "");
		
		this.children = children.isPresent() ? children.get() : Collections.emptyList();
		this.messages = messages.isPresent() ? messages.get() : Collections.emptyList();
		
		String diagramName;
		
		if (! this.messages.isEmpty())
		{
			diagramName = this.messages.get(0).getDiagramName();
		}
		else
		{
			diagramName = this.children.get(0).getDiagramName();
		}
		
		this.sdStart = sdStart;
		this.sdEnd = sdEnd;
	}

	private final String guard;

	private final SDPoint sdStart;

	private final SDPoint sdEnd;

	public String getGuard()
	{
		return this.guard;
	}

	public SDPoint getSdStart()
	{
		return this.sdStart;
	}

	public SDPoint getSdEnd()
	{
		return this.sdEnd;
	}

	private List<CombinedFragment> children;

	private List<CombinedFragment> internalGetChildren()
	{
		return this.children;
	}

	public List<CombinedFragment> getChildren()
	{
		return this.internalGetChildren();
	}

	public CombinedFragment getChild(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetChildren().get(index);
	}
	
	public List<CombinedFragment> getChildrenAfter(CombinedFragment frag) throws IllegalArgumentException
	{
		if (! this.internalGetChildren().contains(frag))
		{
			throw new IllegalArgumentException("did not have frag as child");
		}
		
		List<CombinedFragment> toReturn = new ArrayList<CombinedFragment>(this.internalGetChildren());
		
		Iterator<CombinedFragment> it = toReturn.iterator();
		
		while (it.hasNext())
		{
			CombinedFragment ele = it.next();
			
			if (frag.equals(ele))
			{
				break;
			}
			
			it.remove();
		}
		
		return toReturn;
	}
	
	public void setChildren(List<CombinedFragment> children) throws IllegalArgumentException
	{
		this.children = children;
	}
	
	protected void fillTree(List<CombinedFragment> output)
	{
		output.add(this);
		
		for (CombinedFragment ele : this.internalGetChildren())
		{
			ele.fillTree(output);
		}
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

	@Override
	public boolean containsMessage(Message message)
	{
		return this.internalGetMessages().contains(message);
	}
	
	@Override
	public boolean optional()
	{
		return true;
	}
	
	public List<MessageContainer> getAsContainers()
	{
		List<MessageContainer> toReturn = new ArrayList<MessageContainer>();
		
		toReturn.addAll(this.internalGetMessages());
		toReturn.addAll(this.internalGetChildren());
		
		MessageContainerSorter.sortMessageContainers(toReturn);
		
		return toReturn;
	}

	public List<Message> flattenMessages()
	{
		List<Message> toReturn = new ArrayList<Message>(this.internalGetMessages());

		for (CombinedFragment ele : this.internalGetChildren())
		{
			toReturn.addAll(ele.flattenMessages());
		}

		Collections.sort(toReturn);

		return toReturn;
	}
	
	@Override
	protected boolean isAFinalMessage(Message message)
	{
		List<MessageContainer> containers = this.getAsContainers();
		
		if (containers.get(containers.size() - 1) instanceof Message
				&& containers.get(containers.size() - 1).equals(message))
		{
			return true;
		}
		
		for (int i = containers.size() - 1; i >= 0; i--)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				if (container.equals(message))
				{
					return true;
				}
				
				break;
			}
			
			if (((CombinedFragment) container).isAFinalMessage(message))
			{
				return true;
			}
			
			if (! container.optional())
			{
				break;
			}
		}
		
		return false;
	}
	
	@Override
	public String getDiagramName()
	{
		if (! this.internalGetMessages().isEmpty())
		{
			return this.internalGetMessages().get(0).getDiagramName();
		}
		
		return this.internalGetChildren().get(0).getDiagramName();
	}

	@Override
	public TreeMap<Message, String> getEntryPoints()
	{
		return this.getEntryPoints("");
	}
	
	@Override
	public TreeMap<Message, String> getEntryPoints(String intermediate)
	{
		TreeMap<Message, String> output = new TreeMap<Message, String>();

		this.getEntryPointsRec(output, intermediate);

		return output;
	}

	protected void getEntryPointsRec(TreeMap<Message, String> output, String intermediate)
	{
		List<Message> messages = this.flattenMessages();

		if (this.internalGetMessages().isEmpty() ||
				(! messages.isEmpty() && this.internalGetMessages().get(0).getSDPoint().getSequenceNumber() > messages.get(0).getSDPoint().getSequenceNumber()))
		{
			CombinedFragment first = this.internalGetChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				firstL.getEntryPointsRec(output, intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
				String intermediateP = (intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
				this.wrapLoops(output, seen, intermediateP + " & ~(" + firstL.getGuard() + ")", this.internalGetChildren(), true, true);
			}
			
			else
			{
				this.internalGetChildren().get(0).getEntryPointsRec(output, intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
			}
			
			Iterator<CombinedFragment> it = this.internalGetChildren().iterator();
			it.next();

			while (it.hasNext())
			{
				CombinedFragment ele = it.next();

				if (! seen.contains(ele))
				{
					ele.getEntryPointsRec(output, "");
				}
			}
		}
		else
		{
			output.put(this.internalGetMessages().get(0), intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());

			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			this.wrapLoops(output, seen, "", this.internalGetChildren(), false, true);
			
			for (CombinedFragment ele : this.internalGetChildren())
			{
				if (! seen.contains(ele))
				{
					ele.getEntryPointsRec(output, "");
				}
			}
		}
	}
	

	
	@Override
	public TreeMap<Message, String> getFirstEntryPoints()
	{
		TreeMap<Message, String> output = new TreeMap<Message, String>();
		
		this.getFirstEntryPointsRec(output, "");
		
		return output;
	}
	
	protected void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate)
	{
		List<Message> msgs = this.flattenMessages();

		if (this.internalGetMessages().isEmpty() || 
				(! this.internalGetChildren().isEmpty() && this.internalGetMessages().get(0).getSDPoint().getSequenceNumber() > msgs.get(0).getSDPoint().getSequenceNumber()))
		{
			CombinedFragment first = this.internalGetChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				firstL.getFirstEntryPointsRec(output, intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
				String intermediateP = (intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
				this.wrapLoops(output, seen, intermediateP + " & ~(" + firstL.getGuard() + ")", this.internalGetChildren(), true, true);
			}
			
			else
			{
				this.internalGetChildren().get(0).getFirstEntryPointsRec(output, intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());	
			}
		}
		else
		{
			output.put(this.getMessage(0), intermediate.equals("") ? this.getGuard() : intermediate + " & " + this.getGuard());
		}
	}

	protected void exitForHandleChildren(SeqDiagramStore store, List<ExitForMessage> output)
	{
		List<Message> messages = this.flattenMessages();

		if (this.internalGetMessages().get(this.internalGetMessages().size() - 1).getSDPoint().getSequenceNumber() == messages.get(messages.size() - 1).getSDPoint().getSequenceNumber())
		{
			ExitForMessageBuilder exitFor = new ExitForMessageBuilder(this.getMessage(this.internalGetMessages().size() - 1));

			this.processExit(store, output, exitFor);
			output.add(exitFor.build());
		}

		for (CombinedFragment ele : this.internalGetChildren())
		{
			ele.calcExitForMessagesRec(store, output);
		}
	}

	private void processExit(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit)
	{
		if (this.getParent().isPresent())
		{
			this.getParent().get().traverseUp(store, output, exit, "~(" + this.getGuard() + ")");
		}
		else
		{
			this.exitToOutside(store, output, exit, Optional.of("~(" + this.getGuard() + ")"));
		}
	}

	protected void traverseUp(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate)
	{
		List<Message> msgs = this.flattenMessages();

		Optional<Message> messageAfter = this.getMessageAfter(exit.getMessage(), msgs, this.calculateSkips(exit.getMessage(), store));

		if (messageAfter.isPresent())
		{
			CombinedFragment frag = messageAfter.get().getFragment().get();

			while (! frag.equals(this) && ! frag.getParent().get().equals(this))
			{
				frag = frag.getParent().get();
			}

			if (frag.equals(this))
			{
				exit.putExit(messageAfter.get(), intermediate);
				return;
			}

			List<CombinedFragment> nextFragments = this.getChildrenAfter(frag);
			this.extractConsecutiveLoops(nextFragments);
			
			Map<Message, String> entryPoints = new HashMap<Message, String>();
			
			for (CombinedFragment ele : nextFragments)
			{
				entryPoints.putAll(ele.getEntryPoints());
			}

			for (Entry<Message, String> ele : entryPoints.entrySet())
			{
				if (ele.getValue().equals("") || "".equals(intermediate))
				{
					ele.setValue(intermediate);
				}
				else
				{
					ele.setValue(intermediate + " & " + ele.getValue());
				}
			}

			exit.putExits(entryPoints);
			return;
		}

		if (this.getParent().isPresent())
		{
			this.traverseUp(store, output, exit, intermediate); // TODO vervang door loop guard + intermediate
		}
		else
		{
			this.exitToOutside(store, output, exit, intermediate.equals("") ? Optional.of("~(" + this.getGuard() + ")") : Optional.of(intermediate + " & ~(" + this.getGuard() + ")"));
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((guard == null) ? 0 : guard.hashCode());
		result = prime * result + ((messages == null) ? 0 : messages.hashCode());
		result = prime * result + ((sdEnd == null) ? 0 : sdEnd.hashCode());
		result = prime * result + ((sdStart == null) ? 0 : sdStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoopCombinedFragment other = (LoopCombinedFragment) obj;
		if (children == null)
		{
			if (other.children != null)
				return false;
		}
		else if (!children.equals(other.children))
			return false;
		if (guard == null)
		{
			if (other.guard != null)
				return false;
		}
		else if (!guard.equals(other.guard))
			return false;
		if (messages == null)
		{
			if (other.messages != null)
				return false;
		}
		else if (!messages.equals(other.messages))
			return false;
		if (sdEnd == null)
		{
			if (other.sdEnd != null)
				return false;
		}
		else if (!sdEnd.equals(other.sdEnd))
			return false;
		if (sdStart == null)
		{
			if (other.sdStart != null)
				return false;
		}
		else if (!sdStart.equals(other.sdStart))
			return false;
		return true;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
