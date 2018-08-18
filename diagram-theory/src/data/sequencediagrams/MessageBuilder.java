package data.sequencediagrams;

import java.util.Optional;

import parser.SeqSymbolStore;
import theory.SeqDiagramStore;

public class MessageBuilder {

	private String content;
	
	private String id;
	
	private int sdPoint;
	
	private Optional<String> fromId = Optional.empty();
	
	private Optional<String> toId = Optional.empty();
	
	private boolean isReturn;
	
	private String diagramName;
	
	private Optional<CombinedFragment> fragment = Optional.empty();

	public String getContent() {
		return content;
	}

	public MessageBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	public String getId() {
		return id;
	}

	public MessageBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public int getSdPoint() {
		return sdPoint;
	}

	public MessageBuilder setSdPoint(int sdPoint) {
		this.sdPoint = sdPoint;
		return this;
	}

	public Optional<String> getFromId() {
		return fromId;
	}

	public MessageBuilder setFromId(Optional<String> fromId) {
		this.fromId = fromId;
		return this;
	}

	public Optional<String> getToId() {
		return toId;
	}

	public MessageBuilder setToId(Optional<String> toId) {
		this.toId = toId;
		return this;
	}

	public boolean isReturn() {
		return isReturn;
	}

	public MessageBuilder setReturn(boolean isReturn) {
		this.isReturn = isReturn;
		return this;
	}

	public String getDiagramName() {
		return diagramName;
	}

	public MessageBuilder setDiagramName(String diagramName) {
		this.diagramName = diagramName;
		return this;
	}

	public Optional<CombinedFragment> getFragment() {
		return fragment;
	}

	public MessageBuilder setFragment(Optional<CombinedFragment> fragment) {
		this.fragment = fragment;
		return this;
	}
	
	public Message build(SeqSymbolStore store) throws IllegalStateException
	{
		if (! (this.getFromId().isPresent() | this.getToId().isPresent()))
		{
			throw new IllegalStateException("message has neither a sender nor receiver");
		}
		
		Optional<String> fromName = Optional.empty();
		Optional<String> toName = Optional.empty();
		
		if (this.getFromId().isPresent())
		{
			String varName = store.getNameOf(this.getFromId().get());
			fromName = Optional.of(varName);
			this.setDiagramName(store.getSourceDiagram(varName));
		}
		
		if (this.getToId().isPresent())
		{
			String varName = store.getNameOf(this.getToId().get());
			toName = Optional.of(varName);
			this.setDiagramName(store.getSourceDiagram(varName));
		}
		
		if (this.getFragment().isPresent())
		{
			return new Message(this.getContent(), this.getId(), this.getSdPoint(), this.isReturn(), fromName, toName, this.getDiagramName(), false, this.getFragment().get());
		}
		
		return new Message(this.getContent(), this.getId(), this.getSdPoint(), this.isReturn(), fromName, toName, this.getDiagramName(), false);
	}
}
