package theory;

import java.util.List;

import data.sequencediagrams.Message;

public interface CallPointExpander
{
	public void expandWithCallPoints(List<Message> messages) throws IllegalStateException;
}
