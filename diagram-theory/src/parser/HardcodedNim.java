package parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import data.classdiagrams.AssociationEnd;
import data.classdiagrams.DataUnit;
import data.classdiagrams.Multiplicity;
import data.classdiagrams.MultiplicityFactory;
import data.classdiagrams.Operation;
import data.classdiagrams.PrimitiveType;
import data.classdiagrams.UserDefinedClass;
import data.classdiagrams.UserDefinedClassBuilder;
import data.classdiagrams.UserDefinedType;
import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.CombinedFragmentFactory;
import data.sequencediagrams.DiagramInfo;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.SDPoint;
import data.sequencediagrams.TempVar;
import theory.DiagramStoreFactory;
import theory.Factors;
import theory.SeqDiagramStore;
import theory.SeqFactors;
import theory.TheoryGenerator;

public class HardcodedNim
{

	public static void main(String[] args)
	{
		SeqSymbolStore store = new SeqSymbolStore();
		
		SDPoint dummy = new SDPoint("none", 1, false);
		SDPoint dummy1 = new SDPoint("none", 2, false);
		SDPoint dummy2 = new SDPoint("none", 3, false);
		
		// CLASSES
		
		// GAME
		
		// ATTRIBUTES
		
		DataUnit p1Win = new DataUnit("p1Win", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit gameFinished = new DataUnit("gameFinished", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		
		// OPERATIONS
		
		DataUnit allHeapsEmptyReturn = new DataUnit("return", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation allHeapsEmpty = new Operation("allHeapsEmpty", allHeapsEmptyReturn, Optional.empty());
		
		DataUnit playReturn = new DataUnit("return", PrimitiveType.VOID, Optional.empty());
		DataUnit pP1Begin = new DataUnit("pP1Begin", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation play = new Operation("play", playReturn, Optional.of(Arrays.asList(pP1Begin)));
		
		DataUnit takeTurnReturn = new DataUnit("return", PrimitiveType.VOID, Optional.empty());
		Operation takeTurn = new Operation("takeTurn", takeTurnReturn, Optional.empty());
		
		store.addClass("Game", "Game");
		store.getClass("Game").get().addAttribute(p1Win).addAttribute(gameFinished)
		.addOperation(allHeapsEmpty).addOperation(play).addOperation(takeTurn);
		
		
		// HEAP
		
		// ATTRIBUTES
		
		DataUnit amountObjects = new DataUnit("amountObjects", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		
		// OPERATIONS
		
		DataUnit isEmptyReturn = new DataUnit("return", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation isEmpty = new Operation("isEmpty", isEmptyReturn, Optional.empty());
		
		DataUnit takeReturn = new DataUnit("return", PrimitiveType.VOID, Optional.empty());
		DataUnit takeTNum = new DataUnit("tNum", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation take = new Operation("take", takeReturn, Optional.of(Arrays.asList(takeTNum)));
		
		store.addClass("Heap", "Heap");
		store.getClass("Heap").get().addAttribute(amountObjects)
		.addOperation(isEmpty).addOperation(take);

		store.addAssociation("GameHeap");
		store.getAssociation("GameHeap").get().addAssociationEnd(new AssociationEnd(new UserDefinedType("Game"), Optional.empty(), 
				Optional.of(Multiplicity.EXACTLY_ONE)));
		store.getAssociation("GameHeap").get().addAssociationEnd(new AssociationEnd(new UserDefinedType("Heap"), Optional.empty(), 
				Optional.of(MultiplicityFactory.parseMultiplicity("1..*", "true", "false"))));
		
		// DIAGRAMS—TEMPVARS AND MESSAGES
		
		// ALLHEAPSEMPTY
		
		// TEMPVARS
		
		TempVar aheGame = new TempVar(new UserDefinedType("Game"), "aheGame");
		TempVar aheHeap = new TempVar(new UserDefinedType("Heap"), "aheHeap");
		TempVar aheNumHeaps = new TempVar(new UserDefinedType("int"), "aheNumHeaps");
		TempVar aheCounter = new TempVar(new UserDefinedType("int"), "aheCounter");
		TempVar aheToReturn = new TempVar(new UserDefinedType("boolean"), "aheToReturn");
		TempVar aheHeapEmpty = new TempVar(new UserDefinedType("boolean"), "aheHeapEmpty");
		
		// MESSAGES
		
		Message allHeapsEmpty_1 = new Message("allHeapsEmpty()", "allHeapsEmpty_1", 1, false, Optional.empty(),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_2 = new Message("aheNumHeaps = getNumHeaps()", "allHeapsEmpty_2", 2, false,
				Optional.of("aheGame"), Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_3 = new Message("aheCounter = 1", "allHeapsEmpty_3", 3, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_4 = new Message("aheToReturn = T", "allHeapsEmpty_4", 4, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_5 = new Message("aheHeap = getHeap(aheCounter)", "allHeapsEmpty_5", 5, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_6 = new Message("aheHeapEmpty = isEmpty()", "allHeapsEmpty_6", 6, false, Optional.of("aheGame"),
				Optional.of("aheHeap"), "allHeapsEmpty", false);
		Message allHeapsEmpty_7 = new Message("noop", "allHeapsEmpty_7", 7, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_8 = new Message("aheToReturn = F", "allHeapsEmpty_8", 8, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_9 = new Message("aheCounter = aheCounter + 1", "allHeapsEmpty_9", 9, false, Optional.of("aheGame"),
				Optional.of("aheGame"), "allHeapsEmpty", false);
		Message allHeapsEmpty_10 = new Message("aheToReturn", "allHeapsEmpty_10", 10, true, Optional.of("aheGame"),
				Optional.empty(), "allHeapsEmpty", false);
		
		store.addTempVar("aheGame", aheGame);
		store.addTempVar("aheHeap", aheHeap);
		store.addTempVar("aheNumHeaps", aheNumHeaps);
		store.addTempVar("aheCounter", aheCounter);
		store.addTempVar("aheToReturn", aheToReturn);
		store.addTempVar("aheHeapEmpty", aheHeapEmpty);
		
		store.addMessages(Arrays.asList(allHeapsEmpty_1, allHeapsEmpty_2, allHeapsEmpty_3, allHeapsEmpty_4
				, allHeapsEmpty_5, allHeapsEmpty_6, allHeapsEmpty_7, allHeapsEmpty_8, allHeapsEmpty_9, allHeapsEmpty_10));
		store.addIdToMessage("allHeapsEmpty_1", allHeapsEmpty_1);
		store.addIdToMessage("allHeapsEmpty_2", allHeapsEmpty_2);
		store.addIdToMessage("allHeapsEmpty_3", allHeapsEmpty_3);
		store.addIdToMessage("allHeapsEmpty_4", allHeapsEmpty_4);
		store.addIdToMessage("allHeapsEmpty_5", allHeapsEmpty_5);
		store.addIdToMessage("allHeapsEmpty_6", allHeapsEmpty_6);
		store.addIdToMessage("allHeapsEmpty_7", allHeapsEmpty_7);
		store.addIdToMessage("allHeapsEmpty_8", allHeapsEmpty_8);
		store.addIdToMessage("allHeapsEmpty_9", allHeapsEmpty_9);
		store.addIdToMessage("allHeapsEmpty_10", allHeapsEmpty_10);
		
		// TAKE
		
		// TEMPVARS
		
		TempVar tHeap = new TempVar(new UserDefinedType("Heap"), "tHeap");
		TempVar tNum = new TempVar(new UserDefinedType("int"), "tNum");
		TempVar tCurr = new TempVar(new UserDefinedType("int"), "tCurr");
		TempVar tNew = new TempVar(new UserDefinedType("int"), "tNew");
		
		// MESSQGES
		
		int seq = 1;
		
		Message take_1 = new Message("take(tNum)", "take_1", seq++, false, Optional.empty(), Optional.of("tHeap"), 
				"take", false);
		Message take_2 = new Message("tCurr = getAmountObjects()", "take_2", seq++, false, Optional.of("tHeap"), Optional.of("tHeap"), 
				"take", false);
		Message take_3 = new Message("tNew = tCurr - tNum", "take_3", seq++, false, Optional.of("tHeap"), Optional.of("tHeap"), 
				"take", false);
		Message take_4 = new Message("tNew = 0", "take_4", seq++, false, Optional.of("tHeap"), Optional.of("tHeap"), 
				"take", false);
		Message take_5 = new Message("noop", "take_5", seq++, false, Optional.of("tHeap"), Optional.of("tHeap"), 
				"take", false);
		Message take_6 = new Message("setAmountObjects(tNew)", "take_6", seq++, false, Optional.of("tHeap"), Optional.of("tHeap"), 
				"take", false);
		
		store.addTempVar("tHeap", tHeap);
		store.addTempVar("tNum", tNum);
		store.addTempVar("tCurr", tCurr);
		store.addTempVar("tNew", tNew);
		
		store.addMessages(Arrays.asList(take_1, take_2, take_3, take_4, take_5, take_6));
		store.addIdToMessage("take_1", take_1);
		store.addIdToMessage("take_2", take_2);
		store.addIdToMessage("take_3", take_3);
		store.addIdToMessage("take_4", take_4);
		store.addIdToMessage("take_5", take_5);
		store.addIdToMessage("take_6", take_6);
		
		// ISEMPTY
		
		// TEMPVARS

		TempVar ieHeap = new TempVar(new UserDefinedType("Heap"), "ieHeap");
		TempVar ieNumObj = new TempVar(new UserDefinedType("int"), "ieNumObj");
		TempVar ieToReturn = new TempVar(new UserDefinedType("boolean"), "ieToReturn");
		
		// MESSAGES
		
		seq = 1;
		
		Message isEmpty_1 = new Message("isEmpty()", "isEmpty_1", seq++, false, Optional.empty(), Optional.of("ieHeap"), 
				"isEmpty", false);
		Message isEmpty_2 = new Message("ieToReturn = F", "isEmpty_2", seq++, false, Optional.of("ieHeap"), Optional.of("ieHeap"), 
				"isEmpty", false);
		Message isEmpty_3 = new Message("ieNumObj = getAmountObjects()", "isEmpty_3", seq++, false, Optional.of("ieHeap"), Optional.of("ieHeap"), 
				"isEmpty", false);
		Message isEmpty_4 = new Message("ieToReturn = T", "isEmpty_4", seq++, false, Optional.of("ieHeap"), Optional.of("ieHeap"), 
				"isEmpty", false);
		Message isEmpty_5 = new Message("noop", "isEmpty_5", seq++, false, Optional.of("ieHeap"), Optional.of("ieHeap"), 
				"isEmpty", false);
		Message isEmpty_6 = new Message("ieToReturn", "isEmpty_6", seq++, true, Optional.of("ieHeap"), Optional.empty(), 
				"isEmpty", false);
		
		store.addTempVar("ieHeap", ieHeap);
		store.addTempVar("ieNumObj", ieNumObj);
		store.addTempVar("ieToReturn", ieToReturn);
		
		store.addMessages(Arrays.asList(isEmpty_1, isEmpty_2, isEmpty_3, isEmpty_4, isEmpty_5, isEmpty_6));
		store.addIdToMessage("isEmpty_1", isEmpty_1);
		store.addIdToMessage("isEmpty_2", isEmpty_2);
		store.addIdToMessage("isEmpty_3", isEmpty_3);
		store.addIdToMessage("isEmpty_4", isEmpty_4);
		store.addIdToMessage("isEmpty_5", isEmpty_5);
		store.addIdToMessage("isEmpty_6", isEmpty_6);
		
		// TAKETURN
		
		// TEMP VARS

		TempVar ttGame = new TempVar(new UserDefinedType("Game"), "ttGame");
		TempVar ttHeap = new TempVar(new UserDefinedType("Heap"), "ttHeap");
		TempVar ttNumHeaps = new TempVar(new UserDefinedType("int"), "ttNumHeaps");
		TempVar ttWhichHeap = new TempVar(new UserDefinedType("int"), "ttWhichHeap");
		TempVar ttHeapEmpty = new TempVar(new UserDefinedType("boolean"), "ttHeapEmpty");
		TempVar ttBreak = new TempVar(new UserDefinedType("boolean"), "ttBreak");
		TempVar ttNumObj = new TempVar(new UserDefinedType("int"), "ttNumObj");
		TempVar ttTakeNum = new TempVar(new UserDefinedType("int"), "ttTakeNum");
		
		seq = 1;
		
		Message takeTurn_1 = new Message("takeTurn()", "takeTurn_1", seq++, false, Optional.empty(), Optional.of("ttGame")
				, "takeTurn", false);
		Message takeTurn_2 = new Message("ttNumHeaps = getNumHeaps()", "takeTurn_2", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_3 = new Message("ttWhichHeap = randomInt(1, ttNumHeaps)", "takeTurn_3", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_4 = new Message("ttBreak = F", "takeTurn_4", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_5 = new Message("ttHeap = getHeap(ttWhichHeap)", "takeTurn_5", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_6 = new Message("ttHeapEmpty = isEmpty()", "takeTurn_6", seq++, false, Optional.of("ttGame")
				, Optional.of("ttHeap"), "takeTurn", false);
		Message takeTurn_7 = new Message("ttBreak = T", "takeTurn_7", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_8 = new Message("ttWhichHeap = randomInt(1, ttNumHeaps)", "takeTurn_8", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_9 = new Message("ttHeap = getHeap(ttWhichHeap)", "takeTurn_9", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_10 = new Message("ttNumObj = getAmountObjects()", "takeTurn_10", seq++, false, Optional.of("ttGame")
				, Optional.of("ttHeap"), "takeTurn", false);
		Message takeTurn_11 = new Message("ttTakeNum = randomInt(1, ttNumObj)", "takeTurn_10", seq++, false, Optional.of("ttGame")
				, Optional.of("ttGame"), "takeTurn", false);
		Message takeTurn_12 = new Message("take(ttTakeNum)", "takeTurn_10", seq++, false, Optional.of("ttGame")
				, Optional.of("ttHeap"), "takeTurn", false);
		
		store.addTempVar("ttGame", ttGame);
		store.addTempVar("ttNumHeaps", ttNumHeaps);
		store.addTempVar("ttHeap", ttHeap);
		store.addTempVar("ttWhichHeap", ttWhichHeap);
		store.addTempVar("ttHeapEmpty", ttHeapEmpty);
		store.addTempVar("ttBreak", ttBreak);
		store.addTempVar("ttGame", ttGame);
		store.addTempVar("ttNumObj", ttNumObj);
		store.addTempVar("ttTakeNum", ttTakeNum);
		
		store.addMessages(Arrays.asList(takeTurn_1, takeTurn_2, takeTurn_3, takeTurn_4, takeTurn_5, takeTurn_6, takeTurn_7
				, takeTurn_8, takeTurn_9, takeTurn_10, takeTurn_11, takeTurn_12));
		store.addIdToMessage("takeTurn_1", takeTurn_1);
		store.addIdToMessage("takeTurn_2", takeTurn_2);
		store.addIdToMessage("takeTurn_3", takeTurn_3);
		store.addIdToMessage("takeTurn_4", takeTurn_4);
		store.addIdToMessage("takeTurn_5", takeTurn_5);
		store.addIdToMessage("takeTurn_6", takeTurn_6);
		store.addIdToMessage("takeTurn_7", takeTurn_7);
		store.addIdToMessage("takeTurn_8", takeTurn_8);
		store.addIdToMessage("takeTurn_9", takeTurn_9);
		store.addIdToMessage("takeTurn_10", takeTurn_10);
		store.addIdToMessage("takeTurn_11", takeTurn_11);
		store.addIdToMessage("takeTurn_12", takeTurn_12);
		
		// PLAY
		
		// TEMPVARS
		
		TempVar pGame = new TempVar(new UserDefinedType("Game"), "pGame");
		TempVar pP1Turn = new TempVar(new UserDefinedType("boolean"), "pP1Turn");
		TempVar pFinished = new TempVar(new UserDefinedType("boolean"), "pFinished");
		TempVar pAllEmpty = new TempVar(new UserDefinedType("boolean"), "pAllEmpty");
		
		seq = 1;
		
		Message play_1 = new Message("play(pP1Turn)", "play_1", seq++, false, Optional.empty(), Optional.of("pGame")
				, "play", false);
		Message play_2 = new Message("pFinished = F", "play_2", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_3 = new Message("pAllEmpty = allHeapsEmpty()", "play_3", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_4 = new Message("pFinished = T", "play_4", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_5 = new Message("setGameFinished(T)", "play_5", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_6 = new Message("setP1Win(pP1Turn)", "play_6", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_7 = new Message("takeTurn()", "play_7", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);
		Message play_8 = new Message("pP1Turn = flipBool(pP1Turn)", "play_8", seq++, false, Optional.of("pGame"), Optional.of("pGame")
				, "play", false);

		store.addTempVar("pGame", pGame);
		store.addTempVar("pP1Turn", pP1Turn);
		store.addTempVar("pFinished", pFinished);
		store.addTempVar("pAllEmpty", pAllEmpty);
		
		store.addMessages(Arrays.asList(play_1, play_2, play_3, play_4, play_5, play_6, play_7, play_8));
		store.addIdToMessage("play_1", play_1);
		store.addIdToMessage("play_2", play_2);
		store.addIdToMessage("play_3", play_3);
		store.addIdToMessage("play_4", play_4);
		store.addIdToMessage("play_5", play_5);
		store.addIdToMessage("play_6", play_6);
		store.addIdToMessage("play_7", play_7);
		store.addIdToMessage("play_8", play_8);
		
		// COMBINED FRAGMENTS
		
		// ALLHEAPSEMPTY
		
		LoopCombinedFragment emptyLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty(),
				Optional.empty(),
				Optional.of(new ArrayList<Message>(Arrays.asList(allHeapsEmpty_5, allHeapsEmpty_6, allHeapsEmpty_9))),
				"(aheCounter =< aheNumHeaps) & aheToReturn = T", dummy, dummy1, store);
		allHeapsEmpty_5.setFragment(Optional.of(emptyLoop));
		allHeapsEmpty_6.setFragment(Optional.of(emptyLoop));
		allHeapsEmpty_9.setFragment(Optional.of(emptyLoop));
		AltCombinedFragment emptyLoopCheck = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(emptyLoop),
				Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(allHeapsEmpty_7))),
				Optional.of(new ArrayList<Message>(Collections.singletonList(allHeapsEmpty_8))),
				"aheHeapEmpty = T", "aheHeapEmpty = F", dummy, dummy1, dummy2, store);
		allHeapsEmpty_7.setFragment(Optional.of(emptyLoopCheck));
		allHeapsEmpty_8.setFragment(Optional.of(emptyLoopCheck));
		emptyLoop.setChildren(Collections.singletonList(emptyLoopCheck));
		
		// TAKE
		
		AltCombinedFragment takeCheck = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty(), Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(take_4))),
				Optional.of(new ArrayList<Message>(Collections.singletonList(take_5))),
				"tNew < 0", "tNew >= 0", dummy, dummy1, dummy2, store);
		take_4.setFragment(Optional.of(takeCheck));
		take_5.setFragment(Optional.of(takeCheck));
		
		// ISEMPTY
		
		AltCombinedFragment isEmptyCheck = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty(), Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(isEmpty_4))),
				Optional.of(new ArrayList<Message>(Collections.singletonList(isEmpty_5))),
				"ieNumObj = 0", "ieNumObj > 0", dummy, dummy1, dummy2, store);
		isEmpty_4.setFragment(Optional.of(isEmptyCheck));
		isEmpty_5.setFragment(Optional.of(isEmptyCheck));
		
		// TAKETURN
		
		LoopCombinedFragment takeTurnLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(takeTurn_6))),
				"ttBreak = F", dummy, dummy1, store);
		takeTurn_6.setFragment(Optional.of(takeTurnLoop));
		AltCombinedFragment takeTurnCheck = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(takeTurnLoop), Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(takeTurn_7))),
				Optional.of(new ArrayList<Message>(Arrays.asList(takeTurn_8, takeTurn_9))),
				"ttHeapEmpty = F", "ttHeapEmpty = T", dummy, dummy1, dummy2, store);
		takeTurn_7.setFragment(Optional.of(takeTurnCheck));
		takeTurn_8.setFragment(Optional.of(takeTurnCheck));
		takeTurn_9.setFragment(Optional.of(takeTurnCheck));
		takeTurnLoop.setChildren(Collections.singletonList(takeTurnCheck));
		
		// PLAY
		
		LoopCombinedFragment playLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Collections.singletonList(play_3))),
				"pFinished = F", dummy, dummy1, store);
		play_3.setFragment(Optional.of(playLoop));
		AltCombinedFragment playCheck = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(playLoop), Optional.empty(), Optional.empty(),
				Optional.of(new ArrayList<Message>(Arrays.asList(play_4, play_5, play_6))),
				Optional.of(new ArrayList<Message>(Arrays.asList(play_7, play_8))),
				"pAllEmpty = T", "pAllEmpty = F", dummy, dummy1, dummy2, store);
		play_4.setFragment(Optional.of(playCheck));
		play_5.setFragment(Optional.of(playCheck));
		play_6.setFragment(Optional.of(playCheck));
		play_7.setFragment(Optional.of(playCheck));
		play_8.setFragment(Optional.of(playCheck));
		playLoop.setChildren(Collections.singletonList(playCheck));
		
		store.addLoopCombinedFragment(emptyLoop);
		store.addLoopCombinedFragment(takeTurnLoop);
		store.addLoopCombinedFragment(playLoop);
		store.addAltCombinedFragment(emptyLoopCheck);
		store.addAltCombinedFragment(takeCheck);
		store.addAltCombinedFragment(isEmptyCheck);
		store.addAltCombinedFragment(takeTurnCheck);
		store.addAltCombinedFragment(playCheck);
		
		store.addDiagramInfo("allHeapsEmpty", new DiagramInfo("allHeapsEmpty", aheGame, Optional.empty(), Optional.of(aheToReturn)));
		store.addDiagramInfo("take", new DiagramInfo("take", tHeap, Optional.of(Collections.singletonList(tNum)), Optional.empty()));
		store.addDiagramInfo("isEmpty", new DiagramInfo("isEmpty", ieHeap, Optional.empty(), Optional.of(ieToReturn)));
		store.addDiagramInfo("takeTurn", new DiagramInfo("takeTurn", ttGame, Optional.empty(), Optional.empty()));
		store.addDiagramInfo("play", new DiagramInfo("play", pGame, Optional.of(Collections.singletonList(pP1Turn)), Optional.empty()));
		
		SeqDiagramStore builtStore = new DiagramStoreFactory().makeSeqDiagramStore(store);
		
		SeqFactors fac = new SeqFactors(3, 1, 3, 3, 20);
		
//		try
//		{
//			FileWriter write = new FileWriter("storeoutput.txt");
//			write.append(builtStore.toString());
//			write.close();
//		}
//		catch (IOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		try
		{
			new TheoryGenerator().generateLTCTheory(builtStore, 
					"hardcodednim.idp", fac);
		}
		catch (IllegalArgumentException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
