package data.sequencediagrams;

import java.util.ArrayList;
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
import org.apache.commons.lang3.tuple.Pair;

import theory.SeqDiagramStore;

public class AltCombinedFragment extends CombinedFragment
{
	AltCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> ifChildren
			, Optional<List<CombinedFragment>> thenChildren, Optional<List<Message>> ifMessages, Optional<List<Message>> thenMessages
			, String ifGuard, String thenGuard, SDPoint sdIf, SDPoint sdThen, SDPoint sdExit) throws IllegalArgumentException
	{
		super(parent);

		if (ifGuard == null)
		{
			throw new IllegalArgumentException("ifGuard cannot be null");
		}
		if (thenGuard == null)
		{
			throw new IllegalArgumentException("thenGuard cannot be null");
		}
		if (sdIf.getSequenceNumber() < 1)
		{
			throw new IllegalArgumentException("sdIf cannot be smaller than 1");
		}
		if (sdThen.getSequenceNumber() <= sdIf.getSequenceNumber())
		{
			throw new IllegalArgumentException("sdThen cannot be less than or equal to sdIf");
		}
		if (sdExit.getSequenceNumber() <= sdThen.getSequenceNumber())
		{
			throw new IllegalArgumentException("sdExit cannot be less than or equal to sdThen");
		}
		if (ifChildren == null)
		{
			throw new IllegalArgumentException("ifChildren cannot be null");
		}
		if (thenChildren == null)
		{
			throw new IllegalArgumentException("thenChildren cannot be null");
		}
		if (ifMessages == null)
		{
			throw new IllegalArgumentException("ifMesages cannot be null");
		}
		if (thenMessages == null)
		{
			throw new IllegalArgumentException("thenMessages cannot be null");
		}

		this.ifGuard = ifGuard.replaceAll("\\s*", "");
		this.thenGuard = thenGuard.replaceAll("\\s*", "");
		
//		String diagramName;
//		
//		if (ifMessages.isPresent())
//		{
//			diagramName = ifMessages.get().get(0).getDiagramName();
//		}
//		else if (ifChildren.isPresent())
//		{
//			diagramName = ifChildren.get().get(0).getDiagramName();
//		}
//		else if (thenMessages.isPresent())
//		{
//			diagramName = thenMessages.get().get(0).getDiagramName();
//		}
//		else
//		{
//			diagramName = thenChildren.get().get(0).getDiagramName();
//		}
		
		this.sdIf = sdIf;
		this.sdThen = sdThen;
		this.sdExit = sdExit;
		
		this.ifChildren = ifChildren.isPresent() ? ifChildren.get() : Collections.emptyList();
		this.thenChildren = thenChildren.isPresent() ? thenChildren.get() : Collections.emptyList();
		this.ifMessages = ifMessages.isPresent() ? ifMessages.get() : Collections.emptyList();
		this.thenMessages = thenMessages.isPresent() ? thenMessages.get() : Collections.emptyList();
	}

	private final String ifGuard;

	private final String thenGuard;

	private final SDPoint sdIf;

	private final SDPoint sdThen;

	private final SDPoint sdExit;

	public String getIfGuard()
	{
		if ("".equals(this.ifGuard))
		{
			return "";
		}
		
		return "(" + this.ifGuard + ")";
	}

	public String getThenGuard()
	{
		if ("".equals(this.thenGuard))
		{
			return "";
		}
		
		return "(" + this.thenGuard + ")";
	}

	public SDPoint getSdIf()
	{
		return this.sdIf;
	}

	public SDPoint getSdThen()
	{
		return this.sdThen;
	}

	public SDPoint getSdExit()
	{
		return this.sdExit;
	}

	private List<CombinedFragment> ifChildren;

	private List<CombinedFragment> thenChildren;

	private List<CombinedFragment> internalGetIfChildren()
	{
		return this.ifChildren;
	}

	public List<CombinedFragment> getIfChildren()
	{
		return Collections.unmodifiableList(this.internalGetIfChildren());
	}
	
	public void setIfChildren(List<CombinedFragment> ifChildren) throws IllegalArgumentException
	{
		if (ifChildren == null)
		{
			throw new IllegalArgumentException("ifChildren cannot be null");
		}
		
		this.ifChildren = ifChildren;
	}

	public CombinedFragment getIfChild(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetIfChildren().get(index);
	}
	
	public List<CombinedFragment> getIfChildrenAfter(CombinedFragment frag) throws IllegalArgumentException
	{
		if (! this.internalGetIfChildren().contains(frag))
		{
			throw new IllegalArgumentException("did not have frag as child");
		}
		
		List<CombinedFragment> toReturn = new ArrayList<CombinedFragment>(this.internalGetIfChildren());
		
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

	private final List<CombinedFragment> internalGetThenChildren()
	{
		return this.thenChildren;
	}

	public CombinedFragment getThenChild(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetThenChildren().get(index);
	}
	
	public List<CombinedFragment> getThenChildrenAfter(CombinedFragment frag) throws IllegalArgumentException
	{
		if (! this.internalGetThenChildren().contains(frag))
		{
			throw new IllegalArgumentException("did not have frag as child");
		}
		
		List<CombinedFragment> toReturn = new ArrayList<CombinedFragment>(this.internalGetThenChildren());
		
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

	public List<CombinedFragment> getThenChildren()
	{
		return Collections.unmodifiableList(this.internalGetThenChildren());
	}
	
	public void setThenChildren(List<CombinedFragment> thenChildren) throws IllegalArgumentException
	{
		if (thenChildren == null)
		{
			throw new IllegalArgumentException("thenChildren cannot be null");
		}
		
		this.thenChildren = thenChildren;
	}
	
	@Override
	protected void fillTree(List<CombinedFragment> output)
	{
		output.add(this);
		
		for (CombinedFragment ele : this.internalGetIfChildren())
		{
			ele.fillTree(output);
		}
		for (CombinedFragment ele : this.internalGetThenChildren())
		{
			ele.fillTree(output);
		}
	}

	private final List<Message> ifMessages;

	private final List<Message> thenMessages;

	private List<Message> internalGetIfMessages()
	{
		return this.ifMessages;
	}

	public List<Message> getIfMessages()
	{
		return Collections.unmodifiableList(this.internalGetIfMessages());
	}

	public Message getIfMessage(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetIfMessages().get(index);
	}

	public boolean ifContains(Message message)
	{
		return this.internalGetIfMessages().contains(message);
	}

	private List<Message> internalGetThenMessages()
	{
		return this.thenMessages;
	}

	public List<Message> getThenMessages()
	{
		return Collections.unmodifiableList(this.internalGetThenMessages());
	}

	public Message getThenMessage(int index) throws IndexOutOfBoundsException
	{
		return this.internalGetThenMessages().get(index);
	}

	public boolean thenContains(Message message)
	{
		return this.internalGetThenMessages().contains(message);
	}
	
	@Override
	public List<Message> getMessages()
	{
		List<Message> toReturn = new ArrayList<Message>(this.internalGetIfMessages());
		toReturn.addAll(this.internalGetThenMessages());
		
		return toReturn;
	}
	
	@Override
	public String getDiagramName()
	{
		if (! this.internalGetIfMessages().isEmpty())
		{
			return this.internalGetIfMessages().get(0).getDiagramName();
		}
		else if (! this.internalGetIfChildren().isEmpty())
		{
			return this.internalGetIfChildren().get(0).getDiagramName();
		}
		else if (! this.internalGetThenMessages().isEmpty())
		{
			return this.internalGetThenMessages().get(0).getDiagramName();
		}
		return this.internalGetThenChildren().get(0).getDiagramName();
	}
	
	@Override
	public boolean containsMessage(Message message)
	{
		return this.ifContains(message) || this.thenContains(message);
	}
	
	@Override
	public boolean optional()
	{
		return false;
	}

	public List<Message> flattenMessages()
	{
		List<Message> toReturn = new ArrayList<Message>();
		
		toReturn.addAll(this.flattenIf());
		toReturn.addAll(this.flattenThen());

		Collections.sort(toReturn);

		return toReturn;
	}

	public List<Message> flattenIf()
	{
		List<Message> toReturn = new ArrayList<Message>(this.internalGetIfMessages());

		for (CombinedFragment ele : this.internalGetIfChildren())
		{
			toReturn.addAll(ele.flattenMessages());
		}

		Collections.sort(toReturn);

		return toReturn;
	}

	public List<Message> flattenThen()
	{
		List<Message> toReturn = new ArrayList<Message>(this.internalGetThenMessages());

		for (CombinedFragment ele : this.internalGetThenChildren())
		{
			toReturn.addAll(ele.flattenMessages());
		}

		Collections.sort(toReturn);

		return toReturn;
	}
	
	public List<MessageContainer> getIfAsContainers()
	{
		List<MessageContainer> toReturn = new ArrayList<MessageContainer>();
		
		toReturn.addAll(this.internalGetIfMessages());
		toReturn.addAll(this.internalGetIfChildren());
		
		MessageContainerSorter.sortMessageContainers(toReturn);
		
		return toReturn;
	}
	
	public List<MessageContainer> getThenAsContainers()
	{
		List<MessageContainer> toReturn = new ArrayList<MessageContainer>();
		
		toReturn.addAll(this.internalGetThenMessages());
		toReturn.addAll(this.internalGetThenChildren());
		
		MessageContainerSorter.sortMessageContainers(toReturn);
		
		return toReturn;
	}
	
	@Override
	protected boolean isAFinalMessage(Message message)
	{	
		List<MessageContainer> containers = this.getIfAsContainers();
		
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
		
		containers = this.getThenAsContainers();
		
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
	public TreeMap<Message, String> getEntryPoints(Optional<Set<CombinedFragment>> excluded)
	{
		return this.getEntryPoints(excluded, "");
	}
	
	@Override
	public TreeMap<Message, String> getEntryPoints(Optional<Set<CombinedFragment>> excluded, String intermediate)
	{
		TreeMap<Message, String> output = new TreeMap<Message, String>();

		this.getEntryPointsRec(output, intermediate, excluded);

		return output;
	}

	protected void getEntryPointsRec(TreeMap<Message, String> output, String intermediate, Optional<Set<CombinedFragment>> excluded)
	{
		List<Message> ifMsgs = this.flattenIf();

		if (this.internalGetIfMessages().isEmpty() || 
				(! this.internalGetIfChildren().isEmpty() && this.internalGetIfMessages().get(0).getSDPoint().getSequenceNumber() > ifMsgs.get(0).getSDPoint().getSequenceNumber()))
		{
			CombinedFragment first = this.internalGetIfChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				if (! excluded.isPresent() ||
						! excluded.get().contains(firstL))
				{
					firstL.getEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard(), excluded);
				}
				String intermediateP = (intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
				this.wrapLoops(output, seen, excluded, intermediateP + " & ~" + firstL.getGuard(), this.internalGetIfChildren(), true, true);
			}
			
			else
			{
				if (! excluded.isPresent() ||
						! excluded.get().contains(this.internalGetIfChildren().get(0)))
				{
					this.internalGetIfChildren().get(0).getEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard(), excluded);
				}
			}
			
			Iterator<CombinedFragment> it = this.internalGetIfChildren().iterator();
			it.next();
			
			while (it.hasNext())
			{
				CombinedFragment ele = it.next();
				
				if (! seen.contains(ele) ||
						! (excluded.isPresent() && excluded.get().contains(ele)))
				{
					ele.getEntryPointsRec(output, "", excluded);
				}
			}
		}
		else
		{
			output.put(this.getIfMessage(0), intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
			
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			this.wrapLoops(output, seen, excluded, "", this.internalGetIfChildren(), false, true);
			
			for (CombinedFragment ele : this.internalGetIfChildren())
			{
				if (! seen.contains(ele) &&
						! (excluded.isPresent() && excluded.get().contains(ele)))
				{
					ele.getEntryPointsRec(output, "", excluded);
				}
			}
		}

		List<Message> thenMsgs = this.flattenThen();

		if (this.internalGetThenMessages().isEmpty() || 
				(! this.internalGetThenChildren().isEmpty() && this.internalGetThenMessages().get(0).getSDPoint().getSequenceNumber() > thenMsgs.get(0).getSDPoint().getSequenceNumber()))
		{
			CombinedFragment first = this.internalGetThenChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				if (! excluded.isPresent() ||
						! excluded.get().contains(firstL))
				{
					firstL.getEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard(), excluded);
				}
				String intermediateP = (intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
				this.wrapLoops(output, seen, excluded, intermediateP + " & ~" + firstL.getGuard(), this.internalGetThenChildren(), true, true);
			}
			
			else
			{
				if (! excluded.isPresent() &&
						! excluded.get().contains(this.internalGetThenChildren().get(0)))
				{
					this.internalGetThenChildren().get(0).getEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard(), excluded);
				}
			}
			
			Iterator<CombinedFragment> it = this.internalGetThenChildren().iterator();
			it.next();
			
			while (it.hasNext())
			{
				CombinedFragment ele = it.next();
				
				if (! seen.contains(ele) &&
						! (excluded.isPresent() && excluded.get().contains(ele)))
				{
					ele.getEntryPointsRec(output, "", excluded);
				}
			}
		}
		else
		{
			output.put(this.getThenMessage(0), intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());

			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			this.wrapLoops(output, seen, excluded, "", this.internalGetThenChildren(), false, true);
			
			for (CombinedFragment ele : this.internalGetThenChildren())
			{
				if (! seen.contains(ele)
						&& ! (excluded.isPresent() && excluded.get().contains(ele)))
				{
					ele.getEntryPointsRec(output, "", excluded);
				}
			}
		}
	}
	
	@Override
	public TreeMap<Message, String> getFirstEntryPoints(Optional<Set<CombinedFragment>> excluded)
	{
		TreeMap<Message, String> output = new TreeMap<Message, String>();
		
		this.getFirstEntryPointsRec(output, "", excluded);
		
		return output;
	}
	
	@Override
	protected void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate, Optional<Set<CombinedFragment>> excluded)
	{
		List<Message> ifMsgs = this.flattenIf();

		if (this.internalGetIfMessages().isEmpty() || 
				(! this.internalGetIfChildren().isEmpty() && this.internalGetIfMessages().get(0).getSDPoint().getSequenceNumber() > ifMsgs.get(0).getSDPoint().getSequenceNumber()))
		{

			CombinedFragment first = this.internalGetIfChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				if (! excluded.isPresent() ||
					! excluded.get().contains(firstL))
				{
					firstL.getFirstEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard(), excluded);

				}
				String intermediateP = (intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
				this.wrapLoops(output, seen, excluded, intermediateP + " & ~" + firstL.getGuard(), this.internalGetIfChildren(), true, false);
			}
			
			else
			{
				if (! excluded.isPresent() ||
						! excluded.get().contains(this.internalGetIfChildren().get(0)))
				{
					this.internalGetIfChildren().get(0).getFirstEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard(), excluded);
				}
			}
			
		}
		else
		{
			output.put(this.getIfMessage(0), intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
		}

		List<Message> thenMsgs = this.flattenThen();

		if (this.internalGetThenMessages().isEmpty() || 
				(! this.internalGetThenChildren().isEmpty() && this.internalGetThenMessages().get(0).getSDPoint().getSequenceNumber() > thenMsgs.get(0).getSDPoint().getSequenceNumber()))
		{
			CombinedFragment first = this.internalGetThenChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				if (! excluded.isPresent() ||
						! excluded.get().contains(firstL))
				{
					firstL.getFirstEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard(), excluded);
				}
				String intermediateP = (intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
				this.wrapLoops(output, seen, excluded, intermediateP + " & ~" + firstL.getGuard(), this.internalGetThenChildren(), true, false);
			}
			
			else
			{
				if (! excluded.isPresent() ||
						! excluded.get().contains(this.internalGetThenChildren().get(0)))
				{
					this.internalGetThenChildren().get(0).getFirstEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard(), excluded);
				}
			}
		}
		else
		{
			output.put(this.getThenMessage(0), intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
		}
	}
	
	protected void exitForHandleChildren(SeqDiagramStore store, List<ExitForMessage> output)
	{
		List<MessageContainer> containers = this.getIfAsContainers();
		
		StringBuilder intermediate = new StringBuilder();
		
		for (int i = containers.size() - 1; i >= 0; i--)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				ExitForMessageBuilder exitFor = new ExitForMessageBuilder((Message) container);
				
				this.processExit(store, output, exitFor, intermediate.toString());
				output.add(exitFor.build());
				break;
			}
			
			if (! container.optional())
			{
				break;
			}
			
			if (container instanceof LoopCombinedFragment)
			{
				if (intermediate.length() == 0)
				{
					intermediate.append("~" + ((LoopCombinedFragment) container).getGuard());
				}
				else
				{
					intermediate.append(" & ~" + ((LoopCombinedFragment) container).getGuard());
				}
			}
		}
		
		containers = this.getThenAsContainers();
		
		intermediate = new StringBuilder();
		
		for (int i = containers.size() - 1; i >= 0; i--)
		{
			MessageContainer container = containers.get(i);
			
			if (container instanceof Message)
			{
				ExitForMessageBuilder exitFor = new ExitForMessageBuilder((Message) container);
				
				this.processExit(store, output, exitFor, intermediate.toString());
				output.add(exitFor.build());
				break;
			}
			
			if (! container.optional())
			{
				break;
			}
			
			if (container instanceof LoopCombinedFragment)
			{
				if (intermediate.length() == 0)
				{
					intermediate.append("~" + ((LoopCombinedFragment) container).getGuard());
				}
				else
				{
					intermediate.append(" & ~" + ((LoopCombinedFragment) container).getGuard());
				}
			}
		}
		
		for (CombinedFragment ele : this.internalGetIfChildren())
		{
			ele.calcExitForMessagesRec(store, output);
		}
		for (CombinedFragment ele : this.internalGetThenChildren())
		{
			ele.calcExitForMessagesRec(store, output);
		}
	}
	
	private void processExit(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate)
	{
		if (this.getParent().isPresent())
		{
			this.getParent().get().traverseUp(store, output, exit, intermediate, Optional.of(new HashSet<CombinedFragment>()));
		}
		else
		{
			this.exitToOutside(store, output, exit, Optional.of(intermediate));
		}
	}
	
	protected void traverseUp(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate, Optional<Set<CombinedFragment>> excluded)
	{
		List<Message> ifMsgs = this.flattenIf();
		
		if (ifMsgs.contains(exit.getMessage()))
		{
			Optional<Message> messageAfter = this.getMessageAfter(exit.getMessage(), ifMsgs, this.calculateSkips(exit.getMessage(), store));
			
			if (messageAfter.isPresent())
			{
				CombinedFragment frag = messageAfter.get().getFragment().get();
				
				while (! frag.equals(this) && ! frag.getParent().get().equals(this))
				{
					frag = frag.getParent().get();
				}
				
				if (frag.equals(this))
				{
					this.putExit(exit, messageAfter.get(), intermediate);
					return;
				}
				
				List<CombinedFragment> nextFragments = this.getIfChildrenAfter(frag);
				this.extractConsecutiveLoops(nextFragments);
				
				Map<Message, String> entryPoints = new HashMap<Message, String>();
				
				StringBuilder aggregate = new StringBuilder();
				
				for (CombinedFragment ele : nextFragments)
				{
					Map<Message, String> elePoints = ele.getEntryPoints(excluded);
					
					for (Entry<Message, String> entry : elePoints.entrySet())
					{
						String newVal = this.constructGuard(aggregate.toString(), entry.getValue(), intermediate);
						entry.setValue(newVal);
					}
					
					entryPoints.putAll(elePoints);
					
					if (ele instanceof OptionalCombinedFragment)
					{
						aggregate.append(aggregate.length() == 0 ? "~" + ((OptionalCombinedFragment) ele).getGuard()
								: " & ~" + ((OptionalCombinedFragment) ele).getGuard());
					}
				}

				this.putExits(exit, entryPoints);
				// TODO execution must actually continue if last member of nextFragments is a loop
				if (! nextFragments.get(nextFragments.size() - 1).optional())
				{
					return;
				}
				
				intermediate = this.constructGuard(intermediate, aggregate.toString());
				
				Optional<Message> afterLastLoop = this.getMessageAfter(nextFragments.get(nextFragments.size() - 1).getFinalMessage(), this.flattenIf(), 0);
				
				if (afterLastLoop.isPresent())
				{
					this.putExit(exit, afterLastLoop.get(), intermediate);
					return;
				}
			}
			
			if (this.getParent().isPresent())
			{
				if (excluded.isPresent())
				{
					excluded.get().add(this);
				}
				this.getParent().get().traverseUp(store, output, exit, intermediate, excluded);
			}
			else
			{
				this.exitToOutside(store, output, exit, intermediate.equals("") ? Optional.empty() : Optional.of(intermediate));
			}
		}
		else // thenMsgs.contains(exit.getMessage))
		{
			List<Message> thenMsgs = this.flattenThen();
			
			Optional<Message> messageAfter = this.getMessageAfter(exit.getMessage(), thenMsgs, this.calculateSkips(exit.getMessage(), store));
			
			if (messageAfter.isPresent())
			{
				CombinedFragment frag = messageAfter.get().getFragment().get();
				
				while (! frag.equals(this) && ! frag.getParent().get().equals(this))
				{
					frag = frag.getParent().get();
				}
				
				if (frag.equals(this))
				{
					this.putExit(exit, messageAfter.get(), intermediate);
					return;
				}
				
				List<CombinedFragment> nextFragments = this.getThenChildrenAfter(frag);
				this.extractConsecutiveLoops(nextFragments);
				
				Map<Message, String> entryPoints = new HashMap<Message, String>();
				
				StringBuilder aggregate = new StringBuilder();
				
				for (CombinedFragment ele : nextFragments)
				{
					Map<Message, String> elePoints = ele.getEntryPoints(excluded);
					
					for (Entry<Message, String> entry : elePoints.entrySet())
					{
						String newVal = this.constructGuard(aggregate.toString(), entry.getValue(), intermediate);
						entry.setValue(newVal);
					}
					
					entryPoints.putAll(elePoints);
					
					if (ele instanceof OptionalCombinedFragment)
					{
						aggregate.append(aggregate.length() == 0 ? "~" + ((OptionalCombinedFragment) ele).getGuard()
								: " & ~" + ((OptionalCombinedFragment) ele).getGuard());
					}
				}
				
				this.putExits(exit, entryPoints);
				
				if (! nextFragments.get(nextFragments.size() - 1).optional())
				{
					return;
				}
				
				intermediate = this.constructGuard(intermediate, aggregate.toString());
				
				Optional<Message> afterLastLoop = this.getMessageAfter(nextFragments.get(nextFragments.size() - 1).getFinalMessage(), this.flattenThen(), 0);
				
				if (afterLastLoop.isPresent())
				{
					this.putExit(exit, afterLastLoop.get(), intermediate);
					return;
				}
			}
			
			if (this.getParent().isPresent())
			{
				if (excluded.isPresent())
				{
					excluded.get().add(this);
				}
				this.getParent().get().traverseUp(store, output, exit, intermediate, excluded);
			}
			else
			{
				this.exitToOutside(store, output, exit, intermediate.equals("") ? Optional.empty() : Optional.of(intermediate));
			}
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ifChildren == null) ? 0 : ifChildren.hashCode());
		result = prime * result + ((ifGuard == null) ? 0 : ifGuard.hashCode());
		result = prime * result + ((ifMessages == null) ? 0 : ifMessages.hashCode());
		result = prime * result + ((sdExit == null) ? 0 : sdExit.hashCode());
		result = prime * result + ((sdIf == null) ? 0 : sdIf.hashCode());
		result = prime * result + ((sdThen == null) ? 0 : sdThen.hashCode());
		result = prime * result + ((thenChildren == null) ? 0 : thenChildren.hashCode());
		result = prime * result + ((thenGuard == null) ? 0 : thenGuard.hashCode());
		result = prime * result + ((thenMessages == null) ? 0 : thenMessages.hashCode());
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
		AltCombinedFragment other = (AltCombinedFragment) obj;
		if (ifChildren == null)
		{
			if (other.ifChildren != null)
				return false;
		}
		else if (!ifChildren.equals(other.ifChildren))
			return false;
		if (ifGuard == null)
		{
			if (other.ifGuard != null)
				return false;
		}
		else if (!ifGuard.equals(other.ifGuard))
			return false;
		if (ifMessages == null)
		{
			if (other.ifMessages != null)
				return false;
		}
		else if (!ifMessages.equals(other.ifMessages))
			return false;
		if (sdExit == null)
		{
			if (other.sdExit != null)
				return false;
		}
		else if (!sdExit.equals(other.sdExit))
			return false;
		if (sdIf == null)
		{
			if (other.sdIf != null)
				return false;
		}
		else if (!sdIf.equals(other.sdIf))
			return false;
		if (sdThen == null)
		{
			if (other.sdThen != null)
				return false;
		}
		else if (!sdThen.equals(other.sdThen))
			return false;
		if (thenChildren == null)
		{
			if (other.thenChildren != null)
				return false;
		}
		else if (!thenChildren.equals(other.thenChildren))
			return false;
		if (thenGuard == null)
		{
			if (other.thenGuard != null)
				return false;
		}
		else if (!thenGuard.equals(other.thenGuard))
			return false;
		if (thenMessages == null)
		{
			if (other.thenMessages != null)
				return false;
		}
		else if (!thenMessages.equals(other.thenMessages))
			return false;
		return true;
	}
	
	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
