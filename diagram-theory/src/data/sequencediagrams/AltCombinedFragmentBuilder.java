package data.sequencediagrams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import parser.SeqSymbolStore;
import theory.CallPointExpander;

public class AltCombinedFragmentBuilder {
	
	public AltCombinedFragmentBuilder()
	{
		this.parent = Optional.empty();
		this.ifChildIDs = new ArrayList<String>();
		this.thenChildIDs = new ArrayList<String>();
		this.ifMessageIDs = new ArrayList<String>();
		this.thenMessageIDs = new ArrayList<String>();
	}
	
	private Optional<CombinedFragment> parent;
	
	private List<String> ifChildIDs;
	
	private List<String> thenChildIDs;
	
	private List<String> ifMessageIDs;
	
	private List<String> thenMessageIDs;
	
	private String ifGuard;
	
	private String thenGuard;

	public Optional<CombinedFragment> getParent() {
		return parent;
	}

	public AltCombinedFragmentBuilder setParent(Optional<CombinedFragment> parent) {
		this.parent = parent;
		return this;
	}

	public List<String> getIfChildIDs() {
		return ifChildIDs;
	}

	public AltCombinedFragmentBuilder setIfChildIDs(List<String> ifChildIDs) {
		this.ifChildIDs = ifChildIDs;
		return this;
	}

	public List<String> getThenChildIDs() {
		return thenChildIDs;
	}

	public AltCombinedFragmentBuilder setThenChildIDs(List<String> thenChildIDs) {
		this.thenChildIDs = thenChildIDs;
		return this;
	}

	public List<String> getIfMessageIDs() {
		return ifMessageIDs;
	}

	public AltCombinedFragmentBuilder setIfMessageIDs(List<String> ifMessageIDs) {
		this.ifMessageIDs = ifMessageIDs;
		return this;
	}

	public List<String> getThenMessageIDs() {
		return thenMessageIDs;
	}

	public AltCombinedFragmentBuilder setThenMessageIDs(List<String> thenMessageIDs) {
		this.thenMessageIDs = thenMessageIDs;
		return this;
	}

	public String getIfGuard() {
		return ifGuard;
	}

	public AltCombinedFragmentBuilder setIfGuard(String ifGuard) {
		this.ifGuard = ifGuard;
		return this;
	}

	public String getThenGuard() {
		return thenGuard;
	}

	public AltCombinedFragmentBuilder setThenGuard(String thenGuard) {
		this.thenGuard = thenGuard;
		return this;
	}
	
	public AltCombinedFragment build(SeqSymbolStore store)
	{
		List<CombinedFragment> ifChildren = new ArrayList<CombinedFragment>();
		List<CombinedFragment> thenChildren = new ArrayList<CombinedFragment>();
		List<Message> ifMessages = new ArrayList<Message>();
		List<Message> thenMessages = new ArrayList<Message>();
		
		for (String childID : this.getIfChildIDs())
		{
			CombinedFragment frag = this.createFragment(store, childID);
			ifChildren.add(frag);
		}
		
		Collections.sort(ifChildren);
		
		for (String childID : this.getThenChildIDs())
		{
			CombinedFragment frag = this.createFragment(store, childID);
			thenChildren.add(frag);
		}
		
		Collections.sort(thenChildren);
		
		for (String ifMsgID : this.getIfMessageIDs())
		{
			ifMessages.add(store.idToMessage(ifMsgID));
		}
		
		Collections.sort(ifMessages);
		
		for (String thenMsgID : this.getThenMessageIDs())
		{
			thenMessages.add(store.idToMessage(thenMsgID));
		}
		
		Collections.sort(thenMessages);
		
		AltCombinedFragment toReturn = CombinedFragmentFactory
				.createAltCombinedFragment(Optional.empty(), Optional.of(ifChildren)
					, Optional.of(thenChildren)
					, ifMessages.isEmpty() ? Optional.empty() : Optional.of(ifMessages)
					, thenMessages.isEmpty() ? Optional.empty() : Optional.of(thenMessages)
					, this.getIfGuard(), this.getThenGuard(), SDPoint.DUMMY_1, SDPoint.DUMMY_2, SDPoint.DUMMY_3, store);
		
		for (CombinedFragment child : ifChildren)
		{
			child.setParent(Optional.of(toReturn));
		}
		
		for (CombinedFragment child : thenChildren)
		{
			child.setParent(Optional.of(toReturn));
		}
		
		for (Message ifMsg : ifMessages)
		{
			ifMsg.setFragment(Optional.of(toReturn));
		}
		
		for (Message thenMsg : thenMessages)
		{
			thenMsg.setFragment(Optional.of(toReturn));
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
