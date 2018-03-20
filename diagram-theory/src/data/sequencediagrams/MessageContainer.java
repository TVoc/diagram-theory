package data.sequencediagrams;

public interface MessageContainer {
	public int compareMessageContainer(MessageContainer other);
	
	public boolean containsMessage(Message message);
	
	public boolean optional();
}
