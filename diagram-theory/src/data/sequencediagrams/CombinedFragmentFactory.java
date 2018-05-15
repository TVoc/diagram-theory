package data.sequencediagrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import theory.CallPointExpander;

public class CombinedFragmentFactory
{
	public static AltCombinedFragment createAltCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> ifChildren
			, Optional<List<CombinedFragment>> thenChildren, Optional<List<Message>> ifMessages, Optional<List<Message>> thenMessages
			, String ifGuard, String thenGuard, SDPoint sdIf, SDPoint sdThen, SDPoint sdExit, CallPointExpander expander) throws IllegalArgumentException
	{	
		if (ifMessages.isPresent())
		{
			ifMessages = Optional.of(new ArrayList<Message>(ifMessages.get()));
			expander.expandWithCallPoints(ifMessages.get());
		}
		if (thenMessages.isPresent())
		{
			thenMessages = Optional.of(new ArrayList<Message>(thenMessages.get()));
			expander.expandWithCallPoints(thenMessages.get());
		}
		return new AltCombinedFragment(parent, ifChildren, thenChildren, ifMessages, thenMessages, ifGuard, thenGuard, sdIf, sdThen, sdExit);
	}
	
	public static LoopCombinedFragment createLoopCombinedFragment(Optional<CombinedFragment> parent, Optional<List<CombinedFragment>> children, Optional<List<Message>> messages
			, String guard, SDPoint sdStart, SDPoint sdEnd, CallPointExpander expander) throws IllegalArgumentException
	{
		if (messages.isPresent())
		{
			messages = Optional.of(new ArrayList<Message>(messages.get()));
			expander.expandWithCallPoints(messages.get());
		}
		return new LoopCombinedFragment(parent, children, messages, guard, sdStart, sdEnd);
	}
}
