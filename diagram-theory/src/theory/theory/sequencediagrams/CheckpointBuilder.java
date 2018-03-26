package theory.theory.sequencediagrams;

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
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.CombinedFragment;
import data.sequencediagrams.ExitForMessage;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.OptionalCombinedFragment;
import data.sequencediagrams.TempVar;
import parser.XMLParser;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import data.sequencediagrams.SDPoint;

// TODO update for more complex combined fragment handling
// TODO overhaul SDPoint progression based on Prev(Time) : Time?

public class CheckpointBuilder
{
	public CheckpointBuilder(int tabLevel, SeqDiagramStore store)
	{
		this.store = store;

		this.tabLevel = tabLevel;

		this.nonStandardPoints = new TreeSet<SDPoint>();
		this.returnPoints = new ArrayList<SDPoint>();
		this.checkpoints = new TreeMap<SDPoint, String>();

		this.nonStandardPoints.add(new SDPoint("finished", -1, false));
		this.entryPointsDetermined = new HashSet<CombinedFragment>();
		this.exitPointsDetermined = new HashSet<CombinedFragment>();
		this.canonicalEntryGuards = new TreeMap<Message, String>();
		this.entryGuards = new TreeMap<Message, List<Pair<SDPoint, String>>>();
	}

	private final SeqDiagramStore store;

	private SeqDiagramStore getStore()
	{
		return store;
	}

	private final int tabLevel;

	private int getTabLevel()
	{
		return this.tabLevel;
	}

	private final Set<SDPoint> nonStandardPoints;

	private final Set<SDPoint> getNonStandardPoints() 
	{
		return this.nonStandardPoints;
	}

	private final List<SDPoint> returnPoints;

	private final List<SDPoint> getReturnPoints()
	{
		return this.returnPoints;
	}

	private final Map<SDPoint, String> checkpoints;

	private final Map<SDPoint, String> getCheckpoints()
	{
		return this.checkpoints;
	}

	private final Set<CombinedFragment> entryPointsDetermined;

	private Set<CombinedFragment> getEntryPointsDetermined()
	{
		return this.entryPointsDetermined;
	}

	private final Set<CombinedFragment> exitPointsDetermined;

	private Set<CombinedFragment> getExitPointsDetermined()
	{
		return this.exitPointsDetermined;
	}

	private final Map<Message, String> canonicalEntryGuards;

	private Map<Message, String> getCanonicalEntryGuards()
	{
		return this.canonicalEntryGuards;
	}

	private final Map<Message, List<Pair<SDPoint, String>>> entryGuards;

	private Map<Message, List<Pair<SDPoint, String>>> getEntryGuards()
	{
		return entryGuards;
	}

	public CheckpointBuilder processCombinedFragment(CombinedFragment frag, SeqDiagramStore store)
	{
		if (frag instanceof OptionalCombinedFragment)
		{
			this.ensureOptionalFragmentSkip((OptionalCombinedFragment) frag, store);
		}
		
		Message prev = store.getRelativeMessage(frag.getMessage(0), -1);

		if (prev.getFragment().isPresent())
		{
			CombinedFragment top = prev.getFragment().get().getTopLevelFragment();

			if (! this.getEntryPointsDetermined().contains(top))
			{
				this.processCombinedFragment(top, store);
			}
			
			this.determineExits(frag, store);
			this.calculateLoopReentry(frag, store);
		}
		else
		{
			this.processEntryPoints(frag, store);

			this.determineExits(frag, store);
			this.calculateLoopReentry(frag, store);
		}
		
		return this;
	}

	private void processEntryPoints(CombinedFragment frag, SeqDiagramStore store) {
		if (! this.getEntryPointsDetermined().contains(frag))
		{
			Map<Message, String> entryPoints = frag.getEntryPoints();

			this.getCanonicalEntryGuards().putAll(entryPoints);

			for (Entry<Message, String> entryPoint : entryPoints.entrySet())
			{
				List<Message> trans = this.calculateEntryPointTransition(entryPoint.getKey(), store);
				
				for (Message ele : trans)
				{
					if (this.getEntryGuards().containsKey(entryPoint.getKey()))
					{
						this.getEntryGuards().get(entryPoint.getKey())
						.add(new ImmutablePair<SDPoint, String>(ele.getSDPoint(), entryPoint.getValue()));
					}
					else
					{
						this.getEntryGuards().put(entryPoint.getKey()
								, new ArrayList<Pair<SDPoint, String>>(Arrays.asList(new ImmutablePair<SDPoint
										, String>(ele.getSDPoint(), entryPoint.getValue()))));
					}
					
					this.getNonStandardPoints().add(ele.getSDPoint());
				}
			}
			
			this.getEntryPointsDetermined().add(frag);
		}
	}
	
	private List<Message> calculateEntryPointTransition(Message message, SeqDiagramStore store)
	{
		CombinedFragment frag = message.getFragment().get();
		Message prev = store.getRelativeMessage(frag.getMessage(0), -1);
		
		if (! prev.getFragment().isPresent())
		{
			return Collections.singletonList(prev);
		}
		
		List<Message> toReturn = new ArrayList<Message>();
		
		List<ExitForMessage> exitsForPrevFrag = prev.getFragment().get().calcExitForMessages(store);
		
		for (ExitForMessage ele : exitsForPrevFrag)
		{
			if (this.containsExitTo(ele, message))
			{
				toReturn.add(ele.getMessage());
			}
		}
		
		if (toReturn.isEmpty())
		{
			toReturn.add(prev);
		}
		
		return toReturn;
	}
	
	private boolean containsExitTo(ExitForMessage exits, Message to)
	{
		return exits.getExitTos().keySet().contains(to);
	}
	
	private void calculateLoopReentry(CombinedFragment frag, SeqDiagramStore store)
	{
		List<CombinedFragment> tree = frag.getTree();
		
		for (CombinedFragment node : tree)
		{
			for (Entry<Message, List<Pair<SDPoint, String>>> loopReentry : node.calculateLoopReentryGuards().entrySet())
			{
				if (this.getEntryGuards().containsKey(loopReentry.getKey()))
				{
					this.getEntryGuards().get(loopReentry.getKey()).addAll(loopReentry.getValue());
				}
				else
				{
					this.getEntryGuards().put(loopReentry.getKey(), loopReentry.getValue());
				}
			}
		}
	}

	private void determineExits(CombinedFragment frag, SeqDiagramStore store)
	{
		if (! this.getExitPointsDetermined().contains(frag))
		{
			List<ExitForMessage> exitPoints = frag.calcExitForMessages(store);

			for (ExitForMessage exitForMessage : exitPoints)
			{
				for (Entry<Message, String> exitPoint : exitForMessage.getExitTos().entrySet())
				{
					if (this.getEntryGuards().containsKey(exitPoint.getKey()))
					{
						this.getEntryGuards().get(exitPoint.getKey())
						.add(new ImmutablePair<SDPoint, String>(exitForMessage.getMessage().getSDPoint(), exitPoint.getValue()));
					}
					else
					{
						this.getEntryGuards().put(exitPoint.getKey()
								, new ArrayList<Pair<SDPoint, String>>(Arrays.asList(new ImmutablePair<SDPoint, String>
								(exitForMessage.getMessage().getSDPoint(), exitPoint.getValue()))));
					}
				}
				
				this.getNonStandardPoints().add(exitForMessage.getMessage().getSDPoint());
			}

			this.getExitPointsDetermined().add(frag);
		}
	}

	@Deprecated
	public CheckpointBuilder handleAltCombinedFragment(AltCombinedFragment frag, SeqDiagramStore store)
	{
		StringBuilder ifGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder ifGuardBuilder = new StringBuilder(": ");
		StringBuilder thenGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder thenGuardBuilder = new StringBuilder(": ");

		String[] ifGuardParts = frag.getIfGuard().split(XMLParser.TEMPVAR_SEPARATOR);

		for (String part : ifGuardParts)
		{
			if (part.equals("") || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}

			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);

			ifGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			ifGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}

		String[] thenGuardParts = frag.getThenGuard().split(XMLParser.TEMPVAR_SEPARATOR);

		for (String part : thenGuardParts)
		{
			if (part.equals("") || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}

			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);

			thenGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			thenGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}

		this.getCheckpoints().put(frag.getSdIf(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdIf() + ") <- SDPointAt(t, " + (frag.getSdIf().offset(-1)) + ") & " + ifGuardQuantifiers.toString() + ifGuardBuilder.toString() + frag.getIfGuard() + ").");
		this.getCheckpoints().put(frag.getSdThen(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdThen() + ") <- SDPointAt(t, " + (frag.getSdIf().offset(-1)) + ") & " + thenGuardQuantifiers.toString() + thenGuardBuilder.toString() + frag.getThenGuard() + ").");
		this.getCheckpoints().put(frag.getSdExit(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdExit() + ") <- SDPointAt(t, " + (frag.getSdThen().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdExit().offset(-1)) + ").");

		this.getNonStandardPoints().add(frag.getSdIf().offset(-1));
		this.getNonStandardPoints().add(frag.getSdThen().offset(-1));

		return this;
	}

	@Deprecated
	public CheckpointBuilder handleLoopCombinedFragment(LoopCombinedFragment frag, SeqDiagramStore store)
	{
		StringBuilder loopGuardQuantifiers = new StringBuilder("( ? ");
		StringBuilder loopGuardBuilder = new StringBuilder(": ");

		String[] loopGuardParts = frag.getGuard().split(XMLParser.TEMPVAR_SEPARATOR);

		for (String part : loopGuardParts)
		{
			if (part.equals(""))
			{
				continue;
			}

			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);

			loopGuardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			loopGuardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(t, " + tempVar.getName() + ") & ");
		}

		this.getCheckpoints().put(frag.getSdStart(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdStart() + ") <- (SDPointAt(t, " + (frag.getSdStart().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdEnd().offset(-1)) + ")) & " + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ").");
		this.getCheckpoints().put(frag.getSdEnd(), "! t [Time] : C_SDPointAt(Next(t), " + frag.getSdEnd() + ") <- (SDPointAt(t, " + (frag.getSdStart().offset(-1)) + ") | SDPointAt(t, " + (frag.getSdEnd().offset(-1)) + ")) & ~(" + loopGuardQuantifiers.toString() + loopGuardBuilder.toString() + frag.getGuard() + ")).");

		this.getNonStandardPoints().add(frag.getSdStart().offset(-1));
		this.getNonStandardPoints().add(frag.getSdEnd().offset(-1));

		return this;
	}

	public CheckpointBuilder handleCallPoint(Message call, SeqDiagramStore store)
	{
		this.getNonStandardPoints().add(call.getSDPoint());
		
		Message callMsg = store.getCallMessage(call);

		if (! this.getCheckpoints().containsKey(callMsg.getSDPoint()))
		{
			this.getCheckpoints().put(callMsg.getSDPoint(), "! t [Time] : C_SDPointAt(Next(t), " + callMsg.getSDPoint() + ") <- SDPointAt(t, " + call.getSDPoint() + ")");
		}
		else
		{
			this.getCheckpoints().put(callMsg.getSDPoint(), this.getCheckpoints().get(callMsg.getSDPoint())
					+ " | SDPointAt(t, " + call.getSDPoint() + ")");
		}

		return this;
	}
	
	public CheckpointBuilder handleReturnMessage(Message returnMessage, SeqDiagramStore store)
	{
		this.getNonStandardPoints().add(returnMessage.getSDPoint());
		this.getReturnPoints().add(returnMessage.getSDPoint());
		
		return this;
	}
	
	private void ensureOptionalFragmentSkip(OptionalCombinedFragment frag, SeqDiagramStore store)
	{
		List<Message> diagramMessages = store.getMessagesForDiagram(frag.getDiagramName());
		
		Message beforeFrag = diagramMessages.get(diagramMessages.indexOf(frag.getMessage(0)) - 1);
		
		if (beforeFrag.getFragment().isPresent())
		{
			return;
		}
		
		StringBuilder intermediate = new StringBuilder("~" + frag.getGuard());
		
		boolean done = false;
		CombinedFragment iterFrag = frag;
		
		while (! done)
		{	
			Message fragFinal = iterFrag.getFinalMessage();
			
			Message afterFrag = diagramMessages.get(diagramMessages.indexOf(fragFinal) + 1);
			
			if (! afterFrag.getFragment().isPresent())
			{
				if (! this.getEntryGuards().containsKey(afterFrag))
				{
					this.getEntryGuards().put(afterFrag, new ArrayList<Pair<SDPoint, String>>(
							Arrays.asList(new ImmutablePair<SDPoint, String>(
									beforeFrag.getSDPoint(), intermediate.toString()))));
				}
				else
				{
					this.getEntryGuards().get(afterFrag).add(new ImmutablePair<SDPoint, String>(
							beforeFrag.getSDPoint(), intermediate.toString()));
				}
				
				this.getNonStandardPoints().add(beforeFrag.getSDPoint());
				
				return;
			}
			
			iterFrag = afterFrag.getFragment().get().getTopLevelFragment();
			
			Map<Message, String> entryPoints = iterFrag.getEntryPoints();
			
			for (Entry<Message, String> entry : entryPoints.entrySet())
			{
				if (! this.getEntryGuards().containsKey(entry.getKey()))
				{
					this.getEntryGuards().put(entry.getKey(), new ArrayList<Pair<SDPoint, String>>(
							Arrays.asList(new ImmutablePair<SDPoint, String>(
									beforeFrag.getSDPoint(), intermediate.toString()))));
				}
				else
				{
					this.getEntryGuards().get(entry.getKey()).add(
							new ImmutablePair<SDPoint, String>(beforeFrag.getSDPoint(), intermediate.toString()));
				}
			}
			
			if (iterFrag instanceof OptionalCombinedFragment)
			{
				intermediate.append("& ~" + ((OptionalCombinedFragment) iterFrag).getGuard());
			}
			else
			{
				done = true;
			}
		}
		
		this.getNonStandardPoints().add(beforeFrag.getSDPoint());
	}

	//	public CheckpointBuilder handleCallPoint(Message callFrom, Message callTo, SeqDiagramStore store)
	//	{
	//		this.getNonStandardPoints().add
	//	}

	public String build()
	{
		StringBuilder toReturn = new StringBuilder();

		StringBuilder general = new StringBuilder("! t [Time] s [SDPoint] : C_SDPointAt(Next(t), NextSD(s)) <- SDPointAt(t, s)");

		if (! this.getNonStandardPoints().isEmpty())
		{
			general.append("& ~(");

			for (Iterator<SDPoint> it = this.getNonStandardPoints().iterator(); it.hasNext(); )
			{
				SDPoint ele = it.next();
				
				if (it.hasNext())
				{
					general.append("(s = " + ele + ") | ");
				}
				else
				{
					general.append("(s = " + ele + ")).");
				}
			}
			
//			for (int i = 0; i < this.getNonStandardPoints().size(); i++)
//			{
//				if (i == this.getNonStandardPoints().size() - 1)
//				{
//					general.append("(s = " + this.getNonStandardPoints().get(i) + ")).");
//				}
//				else
//				{
//					general.append("(s = " + this.getNonStandardPoints().get(i) + ") | ");
//				}
//			}
		}
		else
		{
			general.append(".");
		}

		toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(general.toString(), this.getTabLevel()));
		
		toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));
		
		for (String ele : this.getCheckpoints().values())
		{
			toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(ele + ".", this.getTabLevel()));
		}
		
		for (Entry<Message, List<Pair<SDPoint, String>>> entryGuardList : this.getEntryGuards().entrySet())
		{
			MessageSDPointUnifier unifier = new MessageSDPointUnifier(entryGuardList);
			
			for (Entry<String, Set<SDPoint>> sdPointList : unifier.getSameGuards().entrySet())
			{
				StringBuilder sdPointCheck = new StringBuilder();
				
				if (sdPointList.getValue().size() == 1)
				{
					sdPointCheck.append("SDPointAt(t, " + sdPointList.getValue().stream().findFirst().get() + ")");
				}
				else
				{
					sdPointCheck.append("(");
					Iterator<SDPoint> sdPoints = sdPointList.getValue().iterator();
					
					while (sdPoints.hasNext())
					{
						SDPoint sdPoint = sdPoints.next();
						
						sdPointCheck.append("SDPointAt(t, " + sdPoint + ")");
						
						if (sdPoints.hasNext())
						{
							sdPointCheck.append(" | ");
						}
						else
						{
							sdPointCheck.append(")");
						}
					}
				}
				
				if ("".equals(sdPointList.getKey()))
				{
					toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(
							"! t [Time] : C_SDPointAt(Next(t), " + entryGuardList.getKey().getSDPoint()
							+ ") <- " + sdPointCheck.toString() + ".", this.getTabLevel()));
				}
				else
				{
					String idpGuard = this.guardToIDP(sdPointList.getKey());
					
					if (this.hasTempVars(sdPointList.getKey()))
					{
						toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(
								"! t [Time] st [StackLevel] : C_SDPointAt(Next(t), " + entryGuardList.getKey().getSDPoint()
								+ ") <- (CurrentStackLevel(t) = st) & " + sdPointCheck.toString() + " & " + idpGuard + ".", this.getTabLevel()));
					}
					else
					{
						toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(
								"! t [Time] : C_SDPointAt(Next(t), " + entryGuardList.getKey().getSDPoint()
								+ ") <- " + sdPointCheck.toString() + " & " + idpGuard + ".", this.getTabLevel()));
					}
				}
//				StringBuilder line = new StringBuilder("! t [Time] sd [SDPoint] C_SDPointAt(Next(t), sd")
			}
			
		}

		toReturn.append(OutputConvenienceFunctions.insertTabsBlankLine(this.getTabLevel()));

		if (! this.getReturnPoints().isEmpty())
		{
			StringBuilder returnCauses = new StringBuilder("! t [Time] s [SDPoint] : C_SDPointAt(Next(t), s) <- ReturnPoint(t, CurrentStackLevel(t), s) & (");

			Iterator<SDPoint> it = this.getReturnPoints().iterator();

			while (it.hasNext())
			{
				SDPoint returnPoint = it.next();

				returnCauses.append("SDPointAt(t, " + returnPoint + ")");

				if (it.hasNext())
				{
					returnCauses.append(" | ");
				}
				else
				{
					returnCauses.append(").");
				}
			}

			toReturn.append(OutputConvenienceFunctions.insertTabsNewLine(returnCauses.toString(), this.getTabLevel()));
		}

		return toReturn.toString();
	}
	
	private String guardToIDP(String guard)
	{
		if ("".equals(guard))
		{
			return "";
		}
		
		StringBuilder guardQuantifiers = new StringBuilder("( ? ");
		StringBuilder guardBuilder = new StringBuilder(": ");

		String[] guardParts = guard.split(XMLParser.TEMPVAR_SEPARATOR);
		
		for (String part : guardParts)
		{
			if (part.equals("") || part.equals("T") || part.equals("F") || OutputConvenienceFunctions.representsInteger(part)
					|| OutputConvenienceFunctions.representsFloat(part) || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}
			
			TempVar tempVar = store.resolveTempVar(part.replaceAll("\\s", ""));
			String idpType = OutputConvenienceFunctions.toIDPType(tempVar.getType(), store);

			guardQuantifiers.append(tempVar.getName() + " [" + idpType + "] ");
			guardBuilder.append(OutputConvenienceFunctions.singleTempVarPredicateName(tempVar) + "(Next(t), st, " + tempVar.getName() + ") & ");
		}
		
		return guardQuantifiers.toString() + guardBuilder.toString() + guard + ")";
	}
	
	private boolean hasTempVars(String guard)
	{
		if ("".equals(guard))
		{
			return false;
		}
		
		for (String part : guard.split(XMLParser.TEMPVAR_SEPARATOR))
		{
			if (part.equals("") || part.equals("T") || part.equals("F") || OutputConvenienceFunctions.representsInteger(part)
					|| OutputConvenienceFunctions.representsFloat(part) || ! store.hasTempVar(part.replaceAll("\\s", "")))
			{
				continue;
			}
			
			if (store.hasTempVar(part))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private class MessageSDPointUnifier
	{
		public MessageSDPointUnifier(Entry<Message, List<Pair<SDPoint, String>>> entryGuards)
		{
			this.to = entryGuards.getKey();
			this.sameGuards = new HashMap<String, Set<SDPoint>>();
			
			for (Pair<SDPoint, String> entryGuard : entryGuards.getValue())
			{
				if (this.sameGuards.containsKey(entryGuard.getRight()))
				{
					this.sameGuards.get(entryGuard.getRight()).add(entryGuard.getLeft());
				}
				else
				{
					this.sameGuards.put(entryGuard.getRight(), new HashSet<SDPoint>(Arrays.asList(entryGuard.getLeft())));
				}
			}
		}
		
		private final Message to;
		
		public Message getTo()
		{
			return this.to;
		}
		
		private final Map<String, Set<SDPoint>> sameGuards;
		
		public Map<String, Set<SDPoint>> getSameGuards()
		{
			return this.sameGuards;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((sameGuards == null) ? 0 : sameGuards.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
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
			MessageSDPointUnifier other = (MessageSDPointUnifier) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (sameGuards == null)
			{
				if (other.sameGuards != null)
					return false;
			}
			else if (!sameGuards.equals(other.sameGuards))
				return false;
			if (to == null)
			{
				if (other.to != null)
					return false;
			}
			else if (!to.equals(other.to))
				return false;
			return true;
		}

		private CheckpointBuilder getOuterType()
		{
			return CheckpointBuilder.this;
		}
	}
}
