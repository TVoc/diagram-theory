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

import theory.SeqDiagramStore;

public class AltCombinedFragment extends CombinedFragment
{
	public AltCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> ifChildren
			, Optional<List<CombinedFragment>> thenChildren, Optional<List<Message>> ifMessages, Optional<List<Message>> thenMessages
			, String ifGuard, String thenGuard, int sdIf, int sdThen, int sdExit) throws IllegalArgumentException
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
		if (sdIf < 1)
		{
			throw new IllegalArgumentException("sdIf cannot be smaller than 1");
		}
		if (sdThen <= sdIf)
		{
			throw new IllegalArgumentException("sdThen cannot be less than or equal to sdIf");
		}
		if (sdExit <= sdThen)
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

		this.ifGuard = ifGuard;
		this.thenGuard = thenGuard;
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

	private final int sdIf;

	private final int sdThen;

	private final int sdExit;

	public String getIfGuard()
	{
		return this.ifGuard;
	}

	public String getThenGuard()
	{
		return this.thenGuard;
	}

	public int getSdIf()
	{
		return this.sdIf;
	}

	public int getSdThen()
	{
		return this.sdThen;
	}

	public int getSdExit()
	{
		return this.sdExit;
	}

	private final List<CombinedFragment> ifChildren;

	private final List<CombinedFragment> thenChildren;

	private List<CombinedFragment> internalGetIfChildren()
	{
		return this.ifChildren;
	}

	public List<CombinedFragment> getIfChildren()
	{
		return Collections.unmodifiableList(this.internalGetIfChildren());
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
	
	public boolean containsMessage(Message message)
	{
		return this.ifContains(message) || this.thenContains(message);
	}

	public List<Message> flattenMessages()
	{
		List<Message> toReturn = new ArrayList<Message>();

		toReturn.addAll(this.internalGetIfMessages());
		toReturn.addAll(this.internalGetThenMessages());
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

	@Override
	public TreeMap<Message, String> getEntryPoints()
	{
		TreeMap<Message, String> output = new TreeMap<Message, String>();

		this.getEntryPointsRec(output, "");

		return output;
	}

	protected void getEntryPointsRec(TreeMap<Message, String> output, String intermediate)
	{
		List<Message> ifMsgs = this.flattenIf();

		if (this.internalGetIfMessages().isEmpty() || 
				(! this.internalGetIfChildren().isEmpty() && this.internalGetIfMessages().get(0).getSdPoint() > ifMsgs.get(0).getSdPoint()))
		{
			CombinedFragment first = this.internalGetIfChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				firstL.getEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
				String intermediateP = (intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
				this.wrapLoops(output, seen, intermediateP + " & ~(" + firstL.getGuard() + ")", this.internalGetIfChildren(), true);
			}
			
			else
			{
				this.internalGetIfChildren().get(0).getEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
			}
			
			Iterator<CombinedFragment> it = this.internalGetIfChildren().iterator();
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
			output.put(this.getIfMessage(0), intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
			
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			this.wrapLoops(output, seen, "", this.internalGetIfChildren(), false);
			
			for (CombinedFragment ele : this.internalGetIfChildren())
			{
				if (! seen.contains(ele))
				{
					ele.getEntryPointsRec(output, "");
				}
			}
		}

		List<Message> thenMsgs = this.flattenThen();

		if (this.internalGetThenMessages().isEmpty() || 
				(! this.internalGetThenChildren().isEmpty() && this.internalGetThenMessages().get(0).getSdPoint() > thenMsgs.get(0).getSdPoint()))
		{
			CombinedFragment first = this.internalGetThenChildren().get(0);
			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			if (first instanceof LoopCombinedFragment)
			{
				LoopCombinedFragment firstL = (LoopCombinedFragment) first;
				firstL.getEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
				String intermediateP = (intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
				this.wrapLoops(output, seen, intermediateP + " & ~(" + firstL.getGuard() + ")", this.internalGetThenChildren(), true);
			}
			
			else
			{
				this.internalGetThenChildren().get(0).getEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
			}
			
			Iterator<CombinedFragment> it = this.internalGetThenChildren().iterator();
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
			output.put(this.getThenMessage(0), intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());

			Set<CombinedFragment> seen = new HashSet<CombinedFragment>();
			
			this.wrapLoops(output, seen, "", this.internalGetThenChildren(), false);
			
			for (CombinedFragment ele : this.internalGetThenChildren())
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
	
	@Override
	protected void getFirstEntryPointsRec(TreeMap<Message, String> output, String intermediate)
	{
		List<Message> ifMsgs = this.flattenIf();

		if (this.internalGetIfMessages().isEmpty() || 
				(! this.internalGetIfChildren().isEmpty() && this.internalGetIfMessages().get(0).getSdPoint() > ifMsgs.get(0).getSdPoint()))
		{

			this.internalGetIfChildren().get(0).getFirstEntryPointsRec(output, intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
		}
		else
		{
			output.put(this.getIfMessage(0), intermediate.equals("") ? this.getIfGuard() : intermediate + " & " + this.getIfGuard());
		}

		List<Message> thenMsgs = this.flattenThen();

		if (this.internalGetThenMessages().isEmpty() || 
				(! this.internalGetThenChildren().isEmpty() && this.internalGetThenMessages().get(0).getSdPoint() > thenMsgs.get(0).getSdPoint()))
		{
			this.internalGetThenChildren().get(0).getFirstEntryPointsRec(output, intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
		}
		else
		{
			output.put(this.getThenMessage(0), intermediate.equals("") ? this.getThenGuard() : intermediate + " & " + this.getThenGuard());
		}
	}
	
	protected void exitForHandleChildren(SeqDiagramStore store, List<ExitForMessage> output)
	{
		List<Message> ifMessages = this.flattenIf();
		
		if (this.internalGetIfMessages().get(this.internalGetIfMessages().size() - 1).getSdPoint() == ifMessages.get(ifMessages.size() - 1).getSdPoint())
		{
			ExitForMessageBuilder exitFor = new ExitForMessageBuilder(this.getIfMessage(this.internalGetIfMessages().size() - 1));
			
			this.processExit(store, output, exitFor);
		}
		
		List<Message> thenMessages = this.flattenThen();
		
		if (this.internalGetThenMessages().get(this.internalGetThenMessages().size() - 1).getSdPoint() == thenMessages.get(thenMessages.size() - 1).getSdPoint())
		{
			ExitForMessageBuilder exitFor = new ExitForMessageBuilder(this.getThenMessage(this.internalGetThenMessages().size() - 1));
			
			this.processExit(store, output, exitFor);
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
	
	private void processExit(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit)
	{
		if (this.getParent().isPresent())
		{
			this.getParent().get().traverseUp(store, output, exit, "");
		}
		else
		{
			this.exitToOutside(store, output, exit, Optional.empty());
		}
	}
	
	protected void traverseUp(SeqDiagramStore store, List<ExitForMessage> output, ExitForMessageBuilder exit, String intermediate)
	{
		List<Message> ifMsgs = this.flattenIf();
		
		if (ifMsgs.contains(exit.getMessage()))
		{
			Optional<Message> messageAfter = this.getMessageAfter(exit.getMessage(), ifMsgs);
			
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
				
				List<CombinedFragment> nextFragments = this.getIfChildrenAfter(frag);
				this.extractConsecutiveLoops(nextFragments);
				
				Map<Message, String> entryPoints = new HashMap<Message, String>();
				
				for (CombinedFragment ele : nextFragments)
				{
					entryPoints.putAll(ele.getEntryPoints());
				}
				
				for (Entry<Message, String> ele : entryPoints.entrySet())
				{
					if (ele.getValue().equals(""))
					{
						ele.setValue(intermediate);
					}
					else
					{
						ele.setValue(intermediate + " & " + ele.getValue());
					}
				}

				exit.putExits(entryPoints);
			}
			
			if (this.getParent().isPresent())
			{
				this.traverseUp(store, output, exit, intermediate);
			}
			else
			{
				this.exitToOutside(store, output, exit, intermediate.equals("") ? Optional.empty() : Optional.of(intermediate));
			}
		}
		else // thenMsgs.contains(exit.getMessage))
		{
			List<Message> thenMsgs = this.flattenThen();
			
			Optional<Message> messageAfter = this.getMessageAfter(exit.getMessage(), thenMsgs);
			
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
				
				List<CombinedFragment> nextFragments = this.getThenChildrenAfter(frag);
				this.extractConsecutiveLoops(nextFragments);
				
				Map<Message, String> entryPoints = new HashMap<Message, String>();
				
				for (CombinedFragment ele : nextFragments)
				{
					entryPoints.putAll(ele.getEntryPoints());
				}
				
				for (Entry<Message, String> ele : entryPoints.entrySet())
				{
					if (ele.getValue().equals(""))
					{
						ele.setValue(intermediate);
					}
					else
					{
						ele.setValue(intermediate + " & " + ele.getValue());
					}
				}
				
				exit.putExits(entryPoints);
			}
			
			if (this.getParent().isPresent())
			{
				this.traverseUp(store, output, exit, intermediate);
			}
			else
			{
				this.exitToOutside(store, output, exit, intermediate.equals("") ? Optional.empty() : Optional.of(intermediate));
			}
		}
	}
}
