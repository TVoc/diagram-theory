package data.sequencediagrams;

import java.util.List;
import java.util.Optional;

import theory.SeqDiagramStore;

public class CombinedFragmentFactory
{
	public static AltCombinedFragment createAltCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> ifChildren
			, Optional<List<CombinedFragment>> thenChildren, Optional<List<Message>> ifMessages, Optional<List<Message>> thenMessages
			, String ifGuard, String thenGuard, SDPoint sdIf, SDPoint sdThen, SDPoint sdExit, SeqDiagramStore store) throws IllegalArgumentException
	{
		if (ifMessages.isPresent())
		{
			store.expandWithCallPoints(ifMessages.get());
		}
		if (thenMessages.isPresent())
		{
			store.expandWithCallPoints(thenMessages.get());
		}
		return new AltCombinedFragment(parent, ifChildren, thenChildren, ifMessages, thenMessages, ifGuard, thenGuard, sdIf, sdThen, sdExit);
	}
	
	public static LoopCombinedFragment createLoopCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> children, Optional<List<Message>> messages
			, String guard, SDPoint sdStart, SDPoint sdEnd, SeqDiagramStore store) throws IllegalArgumentException
	{
		if (messages.isPresent())
		{
			store.expandWithCallPoints(messages.get());
		}
		return new LoopCombinedFragment(parent, children, messages, guard, sdStart, sdEnd);
	}
}