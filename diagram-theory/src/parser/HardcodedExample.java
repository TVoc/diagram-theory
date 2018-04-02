package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import data.classdiagrams.UserDefinedType;
import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.CombinedFragmentFactory;
import data.sequencediagrams.DiagramInfo;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.SDPoint;
import data.sequencediagrams.TempVar;
import theory.DiagramStoreFactory;
import theory.SeqDiagramStore;
import theory.SeqFactors;
import theory.TheoryGenerator;

public class HardcodedExample {

	public static void main(String[] args)
	{
		SeqSymbolStore store = new SeqSymbolStore();
		
		SDPoint dummy = new SDPoint("none", 1, false);
		SDPoint dummy1 = new SDPoint("none", 2, false);
		SDPoint dummy2 = new SDPoint("none", 3, false);
		
		// CLASSES
		
		store.addClass("A", "A");
		
		// DIAGRAMS—TEMPVARS AND MESSAGES
		
		// TEMPVARS
		
		TempVar a = new TempVar(new UserDefinedType("A"), "a");
		
		TempVar loop1 = new TempVar(new UserDefinedType("boolean"), "loop1");
		TempVar loop1alt1if = new TempVar(new UserDefinedType("boolean"), "loop1alt1if");
		TempVar loop1alt1then = new TempVar(new UserDefinedType("boolean"), "loop1alt1then");
		TempVar loop1loop1 = new TempVar(new UserDefinedType("boolean"), "loop1loop1");
		TempVar loop1loop1loop1 = new TempVar(new UserDefinedType("boolean"), "loop1loop1loop1");
		TempVar loop1loop1loop1alt1if = new TempVar(new UserDefinedType("boolean"), "loop1loop1loop1alt1if");
		TempVar loop1loop1loop1alt1then = new TempVar(new UserDefinedType("boolean"), "loop1loop1loop1alt1then");
		TempVar loop1loop1loop2 = new TempVar(new UserDefinedType("boolean"), "loop1loop1loop2");
		
		// MESSAGES
		
		int seqnum = 1;
		
		Message message1 = new Message("message1", "message1", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message2 = new Message("message2", "message2", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message3 = new Message("message3", "message3", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message4 = new Message("message4", "message4", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message5 = new Message("message5", "message5", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message6 = new Message("message6", "message6", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message7 = new Message("message7", "message7", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message8 = new Message("message8", "message8", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message9 = new Message("message9", "message9", seqnum++, false,
				Optional.of("a"), Optional.of("a"), "fragexample", false);		
		Message message10 = new Message("message10", "message10", seqnum++, true,
				Optional.of("a"), Optional.of("a"), "fragexample", false);

		store.addTempVar("a", a);
		store.addTempVar("loop1", loop1);
		store.addTempVar("loop1alt1if", loop1alt1if);
		store.addTempVar("loop1alt1then", loop1alt1then);
		store.addTempVar("loop1loop1", loop1loop1);
		store.addTempVar("loop1loop1loop1", loop1loop1loop1);
		store.addTempVar("loop1loop1loop1alt1if", loop1loop1loop1alt1if);
		store.addTempVar("loop1loop1loop1alt1then", loop1loop1loop1alt1then);
		store.addTempVar("loop1loop1loop2", loop1loop1loop2);
		
		Message dummyMessage = new Message("fragexample", "fragexample",
				1, false, Optional.empty(), Optional.empty(), "fragexample", false);
		Message dummyMessage1 = new Message("otherexample", "otherexample",
				2, true, Optional.empty(), Optional.empty(), "fragexample", false);
		
		store.addMessages(Arrays.asList(message1, message2, message3, message4, message5,
				message6, message7, message8, message9, message10, dummyMessage, dummyMessage1));
		store.addIdToMessage("message1", message1);
		store.addIdToMessage("message2", message2);
		store.addIdToMessage("message3", message3);
		store.addIdToMessage("message4", message4);
		store.addIdToMessage("message5", message5);
		store.addIdToMessage("message6", message6);
		store.addIdToMessage("message7", message7);
		store.addIdToMessage("message8", message8);
		store.addIdToMessage("message9", message9);
		store.addIdToMessage("message10", message10);
		store.addIdToMessage("dummyMessage", dummyMessage);
		store.addIdToMessage("dummyMessage1", dummyMessage1);
		
		// COMBINED FRAGMENTS
		
		LoopCombinedFragment loop1frag = CombinedFragmentFactory.createLoopCombinedFragment(
				Optional.empty(), Optional.empty(), 
				Optional.of(new ArrayList<Message>(Arrays.asList(message2, message9))),
				"loop1=T", dummy, dummy2, store);
		AltCombinedFragment loop1alt1 = CombinedFragmentFactory.createAltCombinedFragment(
				Optional.of(loop1frag), Optional.empty(), Optional.empty(), 
				Optional.of(new ArrayList<Message>(Arrays.asList(message3, message4))),
				Optional.of(new ArrayList<Message>(Arrays.asList(message5))), 
				"loop1alt1if=T", "loop1alt1then=T", dummy, dummy1, dummy2, store);
		LoopCombinedFragment loop1loop1frag = CombinedFragmentFactory.createLoopCombinedFragment(
				Optional.of(loop1frag), Optional.empty(), 
				Optional.empty(),
				"loop1loop1=T", dummy, dummy2, store);
		LoopCombinedFragment loop1loop1loop1frag = CombinedFragmentFactory.createLoopCombinedFragment(
				Optional.of(loop1loop1frag), Optional.empty(), 
				Optional.empty(),
				"loop1loop1loop1=T", dummy, dummy2, store);
		AltCombinedFragment loop1loop1loop1alt1 = CombinedFragmentFactory.createAltCombinedFragment(
				Optional.of(loop1loop1loop1frag), Optional.empty(), Optional.empty(), 
				Optional.of(new ArrayList<Message>(Arrays.asList(message6))),
				Optional.of(new ArrayList<Message>(Arrays.asList(message7))), 
				"loop1loop1loop1alt1if=T", "loop1loop1loop1alt1then=T", dummy, dummy1, dummy2, store);
		LoopCombinedFragment loop1loop1loop2frag = CombinedFragmentFactory.createLoopCombinedFragment(
				Optional.of(loop1loop1frag), Optional.empty(), 
				Optional.of(new ArrayList<Message>(Arrays.asList(message8))),
				"loop1loop1loop2=T", dummy, dummy2, store);
		loop1frag.setChildren(Arrays.asList(loop1alt1, loop1loop1frag));
		loop1loop1frag.setChildren(Arrays.asList(loop1loop1loop1frag, loop1loop1loop2frag));
		loop1loop1loop1frag.setChildren(Arrays.asList(loop1loop1loop1alt1));

		message2.setFragment(Optional.of(loop1frag));
		message3.setFragment(Optional.of(loop1alt1));
		message4.setFragment(Optional.of(loop1alt1));
		message5.setFragment(Optional.of(loop1alt1));
		message6.setFragment(Optional.of(loop1loop1loop1alt1));
		message7.setFragment(Optional.of(loop1loop1loop1alt1));
		message8.setFragment(Optional.of(loop1loop1loop2frag));
		message9.setFragment(Optional.of(loop1frag));
		
		store.addAltCombinedFragment(loop1alt1);
		store.addAltCombinedFragment(loop1loop1loop1alt1);
		store.addLoopCombinedFragment(loop1frag);
		store.addLoopCombinedFragment(loop1loop1frag);
		store.addLoopCombinedFragment(loop1loop1loop1frag);
		store.addLoopCombinedFragment(loop1loop1loop2frag);
		
		store.addDiagramInfo("fragexample", new DiagramInfo("fragexample", loop1, Optional.empty(), Optional.empty()));
		
		SeqDiagramStore builtStore = new DiagramStoreFactory().makeSeqDiagramStore(store);
		
		SeqFactors fac = new SeqFactors(3, 1, 3, 3, 20);
		
		try
		{
			new TheoryGenerator().generateLTCTheory(builtStore, 
					"hardcodedfragexample.idp", fac);
		}
		catch (IllegalArgumentException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
