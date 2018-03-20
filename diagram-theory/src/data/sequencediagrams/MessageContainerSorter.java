package data.sequencediagrams;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageContainerSorter {
	public static void sortMessageContainers(List<MessageContainer> messageContainers)
	{
		Collections.sort(messageContainers, new Comparator<MessageContainer>(){

			@Override
			public int compare(MessageContainer arg0, MessageContainer arg1) {
				// TODO Auto-generated method stub
				return arg0.compareMessageContainer(arg1);
			}
		});
	}
}
