package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import parser.SeqSymbolStore;

public class LoopCombinedFragmentBuilder {

	public LoopCombinedFragmentBuilder()
	{
		this.parent = Optional.empty();
		this.childIDs = new ArrayList<String>();
		this.messageIDs = new ArrayList<String>();
	}
	
	private Optional<CombinedFragment> parent;
	
	private List<String> childIDs;
	
	private List<String> messageIDs;
	
	private String guard;

	public Optional<CombinedFragment> getParent() {
		return parent;
	}

	public LoopCombinedFragmentBuilder setParent(Optional<CombinedFragment> parent) {
		this.parent = parent;
		return this;
	}

	public List<String> getChildIDs() {
		return childIDs;
	}

	public LoopCombinedFragmentBuilder setChildIDs(List<String> childIDs) {
		this.childIDs = childIDs;
		return this;
	}

	public List<String> getMessageIDs() {
		return messageIDs;
	}

	public LoopCombinedFragmentBuilder setMessageIDs(List<String> messageIDs) {
		this.messageIDs = messageIDs;
		return this;
	}

	public String getGuard() {
		return guard;
	}

	public LoopCombinedFragmentBuilder setGuard(String guard) {
		this.guard = guard;
		return this;
	}
	
	public LoopCombinedFragment build(SeqSymbolStore store)
	{
		List<CombinedFragment> children = new ArrayList<CombinedFragment>();
		List<Message> messages = new ArrayList<Message>();
		
		for (String childID : this.getChildIDs())
		{
			CombinedFragment frag = this.createFragment(store, childID);
			children.add(frag);
		}
		
		Collections.sort(children);
		
		for (String msgID : this.getMessageIDs())
		{
			messages.add(store.idToMessage(msgID));
		}
		
		Collections.sort(messages);
		
		LoopCombinedFragment toReturn = CombinedFragmentFactory
				.createLoopCombinedFragment(Optional.empty()
					, children.isEmpty() ? Optional.empty() : Optional.of(children)
					, Optional.of(messages), this.getGuard(), SDPoint.DUMMY_1, SDPoint.DUMMY_2, store);
		
		for (CombinedFragment child : children)
		{
			child.setParent(Optional.of(toReturn));
		}
		
		for (Message ifMsg : messages)
		{
			ifMsg.setFragment(Optional.of(toReturn));
		}
		
		return toReturn;
	}
	
	private CombinedFragment createFragment(SeqSymbolStore store, String id)
	{
		if (store.hasAcf(id))
		{
			return store.getAcf(id).build(store);
		}
		if (store.hasLcf(id))
		{
			return store.getLcf(id).build(store);
		}
		
		throw new IllegalArgumentException("store did not have combined fragment with id: " + id);
	}
}
