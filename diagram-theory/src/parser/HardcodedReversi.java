package parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import data.classdiagrams.AssociationEnd;
import data.classdiagrams.DataUnit;
import data.classdiagrams.Multiplicity;
import data.classdiagrams.MultiplicityFactory;
import data.classdiagrams.Operation;
import data.classdiagrams.PrimitiveType;
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

public class HardcodedReversi {
	
	public static void main(String[] args)
	{
		SeqSymbolStore store = new SeqSymbolStore();
		
		SDPoint dummy = new SDPoint("none", 1, false);
		SDPoint dummy1 = new SDPoint("none", 2, false);
		SDPoint dummy2 = new SDPoint("none", 3, false);
		
		store.addClass("Board", "Board");
		store.addClass("Position", "Position");
		
		// CLASSES
		
		// BOARD
		
		// ATTRIBUTES
		
		DataUnit gameOver = new DataUnit("gameOver", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit blackWinner = new DataUnit("blackWinner", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xCoMax = new DataUnit("xCoMax", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yCoMax = new DataUnit("yCoMax", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit tie = new DataUnit("tie", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		
		// OPERATIONS
		
		DataUnit posCNP = new DataUnit("posCN", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xOffSetCNP = new DataUnit("xOffSetCN", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yOffSetCNP = new DataUnit("yOffSetCN", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit CNresultP = new DataUnit("return", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		Operation calcNeighbor = new Operation("calcNeighbor", CNresultP, Optional.of(Arrays.asList(posCNP, xOffSetCNP, yOffSetCNP)));
		
		DataUnit posTestP = new DataUnit("posTest", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xTestP = new DataUnit("xTest", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yTestP = new DataUnit("yTest", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit NEresultP = new DataUnit("return", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation neighborExists = new Operation("neighborExists", NEresultP, Optional.of(Arrays.asList(posTestP, xTestP, yTestP)));
		
		DataUnit blackCPP = new DataUnit("blackCP", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit CPresultP = new DataUnit("return", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation canPlay = new Operation("canPlay", CPresultP, Optional.of(Arrays.asList(blackCPP)));
		
		DataUnit blackRPPP = new DataUnit("blackRPP", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit RPPresultP = new DataUnit("return", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		Operation randomPossiblePos = new Operation("randomPossiblePos", RPPresultP, Optional.of(Arrays.asList(blackRPPP)));
		
		DataUnit posIPP = new DataUnit("posIP", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit blackIPP = new DataUnit("blackIP", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit IPresultP = new DataUnit("return", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation isPlayable = new Operation("isPlayable", IPresultP, Optional.of(Arrays.asList(posIPP, blackIPP)));
		
		DataUnit posNTP = new DataUnit("posNT", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit blackNTP = new DataUnit("blackNT", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xOffNTP = new DataUnit("xOffNT", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yOffNTP = new DataUnit("yOffNT", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit NTresultP = new DataUnit("return", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation calcNumTrapped = new Operation("calcNumTrapped", NTresultP, Optional.of(Arrays.asList(posNTP, blackNTP, xOffNTP, yOffNTP)));
		
		DataUnit beginBTFP = new DataUnit("beginBTF", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit endBTFP = new DataUnit("endBTF", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xOffSetBTFP = new DataUnit("xOffSetBTF", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yOffSetBTFP = new DataUnit("yOffSetBTF", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit BTFreturnP = new DataUnit("return", PrimitiveType.VOID, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation backtrackFlip = new Operation("backtrackFlip", BTFreturnP, Optional.of(Arrays.asList(beginBTFP, endBTFP, xOffSetBTFP, yOffSetBTFP)));
		
		DataUnit beginFIDP = new DataUnit("beginFID", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit endFIDP = new DataUnit("endFID", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit xOffSetFIDP = new DataUnit("xOffSetFID", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yOffSetFIDP = new DataUnit("yOffSetFID", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit FIDreturnP = new DataUnit("return", PrimitiveType.VOID, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation flipTilesInDir = new Operation("flipTilesInDir", FIDreturnP, Optional.of(Arrays.asList(beginFIDP, endFIDP, xOffSetFIDP, yOffSetFIDP)));
		
		DataUnit posFTP = new DataUnit("posFT", new UserDefinedType("Position"), Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit FTreturnP = new DataUnit("return", PrimitiveType.VOID, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation flipTiles = new Operation("flipTiles", FTreturnP, Optional.of(Arrays.asList(posFTP)));
		
		DataUnit playReturnP = new DataUnit("return", PrimitiveType.VOID, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation play = new Operation("play", playReturnP, Optional.empty());
		
		DataUnit blackCPPP = new DataUnit("blackC", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit CresultP = new DataUnit("return", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		Operation count = new Operation("calcNumTrapped", CresultP, Optional.of(Arrays.asList(blackCPPP)));
		
		store.getClass("Board").get().addAttribute(gameOver).addAttribute(blackWinner)
			.addAttribute(xCoMax).addAttribute(yCoMax).addAttribute(tie)
			.addOperation(calcNeighbor).addOperation(neighborExists).addOperation(canPlay)
			.addOperation(randomPossiblePos).addOperation(isPlayable).addOperation(calcNumTrapped)
			.addOperation(backtrackFlip).addOperation(flipTilesInDir).addOperation(flipTiles)
			.addOperation(play).addOperation(count);
		
		// POSITION
		
		// ATTRIBUTES
		
		DataUnit xCo = new DataUnit("xCo", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit yCo = new DataUnit("yCo", PrimitiveType.INTEGER, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit occupied = new DataUnit("occupied", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		DataUnit black = new DataUnit("black", PrimitiveType.BOOLEAN, Optional.of(Multiplicity.EXACTLY_ONE));
		
		store.getClass("Position").get().addAttribute(xCo).addAttribute(yCo).addAttribute(occupied).addAttribute(black);
		
		store.addAssociation("BoardPosition");
		store.getAssociation("BoardPosition").get().addAssociationEnd(new AssociationEnd(new UserDefinedType("Board"), Optional.empty(), 
				Optional.of(Multiplicity.EXACTLY_ONE)));
		store.getAssociation("BoardPosition").get().addAssociationEnd(new AssociationEnd(new UserDefinedType("Position"), Optional.empty(), 
				Optional.of(MultiplicityFactory.parseMultiplicity("1..*", "true", "false"))));
		
		// DIAGRAMS -- TEMPVARS AND MESSAGES
		
		// CALCNEIGHBOR
		
		// TEMPVARS
		
		TempVar posCN = new TempVar(new UserDefinedType("Position"), "posCN");
		TempVar xOffCN = new TempVar(new UserDefinedType("int"), "xOffCN");
		TempVar yOffCN = new TempVar(new UserDefinedType("int"), "yOffCN");
		TempVar xCoCN = new TempVar(new UserDefinedType("int"), "xCoCN");
		TempVar yCoCN = new TempVar(new UserDefinedType("int"), "yCoCN");
		TempVar boardCN = new TempVar(new UserDefinedType("Board"), "boardCN");
		TempVar neighborCN = new TempVar(new UserDefinedType("Position"), "neighborCN");
		
		// MESSAGES
		
		int counter = 1;
		
		Message calcNeighbor_1 = new Message("calcNeighbor(posCN, xOffCN, yOffCN)"
				, "calcNeighbor_1", counter++, false, Optional.empty(), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_2 = new Message("getXCo()"
				, "calcNeighbor_2", counter++, false, Optional.of("boardCN"), Optional.of("posCN"), "calcNeighbor", false);
		Message calcNeighbor_3 = new Message("xCoCN"
				, "calcNeighbor_3", counter++, false, Optional.of("posCN"), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_4 = new Message("getYCo()"
				, "calcNeighbor_4", counter++, false, Optional.of("boardCN"), Optional.of("posCN"), "calcNeighbor", false);
		Message calcNeighbor_5 = new Message("yCoCN"
				, "calcNeighbor_5", counter++, false, Optional.of("posCN"), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_6 = new Message("xCoCN = xCoCN + xOffCN"
				, "calcNeighbor_6", counter++, false, Optional.of("boardCN"), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_7 = new Message("yCoCN = yCoCN + yOffCN"
				, "calcNeighbor_7", counter++, false, Optional.of("boardCN"), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_8 = new Message("neighborCN = getPositionByXCoandYCo(xCoCN, yCoCN)"
				, "calcNeighbor_8", counter++, false, Optional.of("boardCN"), Optional.of("boardCN"), "calcNeighbor", false);
		Message calcNeighbor_9 = new Message("neighborCN"
				, "calcNeighbor_9", counter++, true, Optional.of("boardCN"), Optional.empty(), "calcNeighbor", false);
		
		store.addTempVar("posCN", posCN);
		store.addTempVar("xOffCN", xOffCN);
		store.addTempVar("yOffCN", yOffCN);
		store.addTempVar("xCoCN", xCoCN);
		store.addTempVar("yCoCN", yCoCN);
		store.addTempVar("neighborCN", neighborCN);
		store.addTempVar("boardCN", boardCN);
		
		store.addMessages(Arrays.asList(calcNeighbor_1, calcNeighbor_2, calcNeighbor_3, calcNeighbor_4
				, calcNeighbor_5, calcNeighbor_6, calcNeighbor_7, calcNeighbor_8, calcNeighbor_9));
		store.addIdToMessage("calcNeighbor_1", calcNeighbor_1);
		store.addIdToMessage("calcNeighbor_2", calcNeighbor_2);
		store.addIdToMessage("calcNeighbor_3", calcNeighbor_3);
		store.addIdToMessage("calcNeighbor_4", calcNeighbor_4);
		store.addIdToMessage("calcNeighbor_5", calcNeighbor_5);
		store.addIdToMessage("calcNeighbor_6", calcNeighbor_6);
		store.addIdToMessage("calcNeighbor_7", calcNeighbor_7);
		store.addIdToMessage("calcNeighbor_8", calcNeighbor_8);
		store.addIdToMessage("calcNeighbor_9", calcNeighbor_9);
		
		// NEIGHBOREXISTS
		
		counter = 1;
		
		// TEMPVARS
		
		TempVar givenNE = new TempVar(new UserDefinedType("Position"), "givenNE");
		TempVar xCoTestNE = new TempVar(new UserDefinedType("int"), "xCoTestNE");
		TempVar yCoTestNE = new TempVar(new UserDefinedType("int"), "yCoTestNE");
		TempVar boardNE = new TempVar(new UserDefinedType("Board"), "boardNE");
		TempVar xCoNE = new TempVar(new UserDefinedType("int"), "xCoNE");
		TempVar yCoNE = new TempVar(new UserDefinedType("int"), "yCoNE");
		TempVar xCoMaxNE = new TempVar(new UserDefinedType("int"), "xCoMaxNE");
		TempVar yCoMaxNE = new TempVar(new UserDefinedType("int"), "yCoMaxNE");
		TempVar existsNE = new TempVar(new UserDefinedType("boolean"), "xCoTestNE");
		
		// MESSAGES
		
		Message neighborExists_1 = new Message("neighborExists(givenNE, xCoTestNE, yCoTestNE)"
				, "neighborExists_1", counter++, false, Optional.empty(), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_2 = new Message("getXCo()"
				, "neighborExists_2", counter++, false, Optional.of("boardNE"), Optional.of("givenNE"), "neighborExists", false);
		Message neighborExists_3 = new Message("xCoNE"
				, "neighborExists_3", counter++, false, Optional.of("givenNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_4 = new Message("getYCo()"
				, "neighborExists_4", counter++, false, Optional.of("boardNE"), Optional.of("givenNE"), "neighborExists", false);
		Message neighborExists_5 = new Message("yCoNE"
				, "neighborExists_5", counter++, false, Optional.of("givenNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_6 = new Message("xCoMaxNE = getXCoMax()"
				, "neighborExists_6", counter++, false, Optional.of("boardNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_7 = new Message("yCoMaxNE = getYCoMax()"
				, "neighborExists_7", counter++, false, Optional.of("boardNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_8 = new Message("existsNE = T"
				, "neighborExists_8", counter++, false, Optional.of("boardNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_9 = new Message("existsNE = F"
				, "neighborExists_9", counter++, false, Optional.of("boardNE"), Optional.of("boardNE"), "neighborExists", false);
		Message neighborExists_10 = new Message("existsNE"
				, "neighborExists_10", counter++, true, Optional.of("boardNE"), Optional.empty(), "neighborExists", false);
		
		store.addTempVar("givenNE", givenNE);
		store.addTempVar("xCoTestNE", xCoTestNE);
		store.addTempVar("yCoTestNE", yCoTestNE);
		store.addTempVar("boardNE", boardNE);
		store.addTempVar("xCoNE", xCoNE);
		store.addTempVar("yCoNE", yCoNE);
		store.addTempVar("xCoMaxNE", xCoMaxNE);
		store.addTempVar("yCoMaxNE", yCoMaxNE);
		store.addTempVar("existsNE", existsNE);
		
		store.addMessages(Arrays.asList(neighborExists_1, neighborExists_2, neighborExists_3
				, neighborExists_4, neighborExists_5, neighborExists_6, neighborExists_7, neighborExists_8, neighborExists_9, neighborExists_10));
		store.addIdToMessage("neighborExists_1", neighborExists_1);
		store.addIdToMessage("neighborExists_2", neighborExists_2);
		store.addIdToMessage("neighborExists_3", neighborExists_3);
		store.addIdToMessage("neighborExists_4", neighborExists_4);
		store.addIdToMessage("neighborExists_5", neighborExists_5);
		store.addIdToMessage("neighborExists_6", neighborExists_6);
		store.addIdToMessage("neighborExists_7", neighborExists_7);
		store.addIdToMessage("neighborExists_8", neighborExists_8);
		store.addIdToMessage("neighborExists_9", neighborExists_9);
		store.addIdToMessage("neighborExists_10", neighborExists_10);
		
		// CANPLAY
		
		counter = 1;
		
		// TEMP VARS
		
		TempVar boardCP = new TempVar(new UserDefinedType("Board"), "boardCP");
		TempVar posCP = new TempVar(new UserDefinedType("Position"), "posCP");
		TempVar blackCP = new TempVar(new UserDefinedType("boolean"), "blackCP");
		TempVar xMaxCP = new TempVar(new UserDefinedType("int"), "xMaxCP");
		TempVar yMaxCP = new TempVar(new UserDefinedType("int"), "yMaxCP");
		TempVar canPlayCP = new TempVar(new UserDefinedType("boolean"), "canPlayCP");
		TempVar xCoCP = new TempVar(new UserDefinedType("int"), "xCoCP");
		TempVar yCoCP = new TempVar(new UserDefinedType("int"), "yCoCP");
		
		// MESSAGES
		
		Message canPlay_1 = new Message("canPlay(blackCP)"
				, "canPlay_1", counter++, false, Optional.empty(), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_2 = new Message("xMaxCP = getXCoMax()"
				, "canPlay_2", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_3 = new Message("yMaxCP = getYCoMax()"
				, "canPlay_3", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_4 = new Message("canPlayCP = F"
				, "canPlay_4", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_5 = new Message("xCoCP = 1"
				, "canPlay_5", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_6 = new Message("yCoCP = 1"
				, "canPlay_6", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_7 = new Message("posCP = getPositionByXCoandYCo(xCoCP, yCoCP)"
				, "canPlay_7", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_8 = new Message("canPlayCP = isPlayable(posCP, blackCP)"
				, "canPlay_8", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_9 = new Message("yCoCP = yCoCP + 1"
				, "canPlay_9", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_10 = new Message("xCoCP = xCoCP + 1"
				, "canPlay_10", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_11 = new Message("yCoCP = 1"
				, "canPlay_11", counter++, false, Optional.of("boardCP"), Optional.of("boardCP"), "canPlay", false);
		Message canPlay_12 = new Message("canPlayCP"
				, "canPlay_12", counter++, true, Optional.of("boardCP"), Optional.empty(), "canPlay", false);
		
		store.addTempVar("boardCP", boardCP);
		store.addTempVar("posCP", posCP);
		store.addTempVar("blackCP", blackCP);
		store.addTempVar("xMaxCP", xMaxCP);
		store.addTempVar("yMaxCP", yMaxCP);
		store.addTempVar("canPlayCP", canPlayCP);
		store.addTempVar("xCoCP", xCoCP);
		store.addTempVar("yCoCP", yCoCP);
		
		store.addMessages(Arrays.asList(canPlay_1, canPlay_2, canPlay_3, canPlay_4, canPlay_5, canPlay_6
				, canPlay_7, canPlay_8, canPlay_9, canPlay_10, canPlay_11, canPlay_12));
		store.addIdToMessage("canPlay_1", canPlay_1);
		store.addIdToMessage("canPlay_2", canPlay_2);
		store.addIdToMessage("canPlay_3", canPlay_3);
		store.addIdToMessage("canPlay_4", canPlay_4);
		store.addIdToMessage("canPlay_5", canPlay_5);
		store.addIdToMessage("canPlay_6", canPlay_6);
		store.addIdToMessage("canPlay_7", canPlay_7);
		store.addIdToMessage("canPlay_8", canPlay_8);
		store.addIdToMessage("canPlay_9", canPlay_9);
		store.addIdToMessage("canPlay_10", canPlay_10);
		store.addIdToMessage("canPlay_11", canPlay_11);
		store.addIdToMessage("canPlay_11", canPlay_12);
		
		// RANDOMPOSSIBLEPOS
		
		counter = 1;
		
		// TEMP VARS
		
		TempVar blackRPP = new TempVar(new UserDefinedType("boolean"), "blackRPP");
		TempVar boardRPP = new TempVar(new UserDefinedType("Board"), "boardRPP");
		TempVar foundRPP = new TempVar(new UserDefinedType("boolean"), "foundRPP");
		TempVar xCoMaxRPP = new TempVar(new UserDefinedType("int"), "xCoMaxRPP");
		TempVar yCoMaxRPP = new TempVar(new UserDefinedType("int"), "yCoMaxRPP");
		TempVar ranXRPP = new TempVar(new UserDefinedType("int"), "ranXRPP");
		TempVar ranYRPP = new TempVar(new UserDefinedType("int"), "ranYRPP");
		TempVar randomPosRPP = new TempVar(new UserDefinedType("Position"), "randomPosRPP");
		TempVar occupiedRPP = new TempVar(new UserDefinedType("boolean"), "occupiedRPP");
		TempVar isPlayableRPP = new TempVar(new UserDefinedType("boolean"), "isPlayableRPP");
		
		// MESSAGES

		Message randomPossiblePos_1 = new Message("randomPossiblePos(blackRPP)"
				, "randomPossiblePos_1", counter++, false, Optional.empty(), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_2 = new Message("foundRPP = F"
				, "randomPossiblePos_2", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_3 = new Message("xCoMaxRPP = getXCoMax()"
				, "randomPossiblePos_3", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_4 = new Message("yCoMaxRPP = getYCoMax()"
				, "randomPossiblePos_4", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_5 = new Message("ranXRPP = randomInt(1, xCoMaxRPP)"
				, "randomPossiblePos_5", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_6 = new Message("ranYRPP = randomInt(1, yCoMaxRPP)"
				, "randomPossiblePos_6", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_7 = new Message("randomPosRPP = getPositionByXCoandYCo(ranXRPP, ranYRPP)"
				, "randomPossiblePos_7", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_8 = new Message("occupiedRPP = getOccupied()"
				, "randomPossiblePos_8", counter++, false, Optional.of("boardRPP"), Optional.of("randomPosRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_9 = new Message("noop"
				, "randomPossiblePos_9", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_10 = new Message("isPlayableRPP = isPlayable(randomPosRPP, blackRPP)"
				, "randomPossiblePos_10", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_11 = new Message("foundRPP = T"
				, "randomPossiblePos_11", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_12 = new Message("noop"
				, "randomPossiblePos_12", counter++, false, Optional.of("boardRPP"), Optional.of("boardRPP"), "randomPossiblePos", false);
		Message randomPossiblePos_13 = new Message("randomPosRPP"
				, "randomPossiblePos_13", counter++, true, Optional.of("boardRPP"), Optional.empty(), "randomPossiblePos", false);

		store.addTempVar("blackRPP", blackRPP);
		store.addTempVar("boardRPP", boardRPP);
		store.addTempVar("foundRPP", foundRPP);
		store.addTempVar("xCoMaxRPP", xCoMaxRPP);
		store.addTempVar("yCoMaxRPP", yCoMaxRPP);
		store.addTempVar("ranXRPP", ranXRPP);
		store.addTempVar("ranYRPP", ranYRPP);
		store.addTempVar("randomPosRPP", randomPosRPP);
		store.addTempVar("occupiedRPP", occupiedRPP);
		store.addTempVar("isPlayableRPP", isPlayableRPP);
		
		store.addMessages(Arrays.asList(randomPossiblePos_1, randomPossiblePos_2, randomPossiblePos_3
				, randomPossiblePos_4, randomPossiblePos_5, randomPossiblePos_6, randomPossiblePos_7
				, randomPossiblePos_8, randomPossiblePos_9, randomPossiblePos_10, randomPossiblePos_11, randomPossiblePos_12, randomPossiblePos_13));
		
		// ISPLAYABLE
		
		counter = 1;
		
		// TEMPVARS

		TempVar givenIP = new TempVar(new UserDefinedType("Position"), "givenIP");
		TempVar blackIP = new TempVar(new UserDefinedType("boolean"), "blackIP");
		TempVar boardIP = new TempVar(new UserDefinedType("Board"), "boardIP");
		TempVar xOffSetIP = new TempVar(new UserDefinedType("int"), "xOffSetIP");
		TempVar yOffSetIP = new TempVar(new UserDefinedType("int"), "yOffSetIP");
		TempVar isPlayableIP = new TempVar(new UserDefinedType("boolean"), "isPlayableIP");
		TempVar numTrappedIP = new TempVar(new UserDefinedType("int"), "numTrappedIP");
		
		// MESSAGES

		Message isPlayable_1 = new Message("isPlayable(givenIP, blackIP)"
				, "isPlayable_1", counter++, false, Optional.empty(), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_2 = new Message("xOffSetIP = 1"
				, "isPlayable_2", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_3 = new Message("yOffSetIP = 0"
				, "isPlayable_3", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_4 = new Message("isPlayableIP = F"
				, "isPlayable_4", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_5 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_5", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_6 = new Message("isPlayableIP = T"
				, "isPlayable_6", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_7 = new Message("noop"
				, "isPlayable_7", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_8 = new Message("xOffSetIP = 1"
				, "isPlayable_8", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_9 = new Message("yOffSetIP = -1"
				, "isPlayable_9", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_10 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_10", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_11 = new Message("isPlayableIP = T"
				, "isPlayable_11", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_12 = new Message("noop"
				, "isPlayable_12", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_13 = new Message("noop"
				, "isPlayable_13", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_14 = new Message("xOffSetIP = 0"
				, "isPlayable_14", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_15 = new Message("yOffSetIP = -1"
				, "isPlayable_15", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_16 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_16", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_17 = new Message("isPlayableIP = T"
				, "isPlayable_17", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_18 = new Message("noop"
				, "isPlayable_18", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_19 = new Message("noop"
				, "isPlayable_19", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_20 = new Message("xOffSetIP = -1 "
				, "isPlayable_20", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_21 = new Message("yOffSetIP = -1"
				, "isPlayable_21", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_22 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_22", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_23 = new Message("isPlayableIP = T"
				, "isPlayable_23", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_24 = new Message("noop"
				, "isPlayable_24", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_25 = new Message("noop"
				, "isPlayable_25", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_26 = new Message("xOffSetIP = -1"
				, "isPlayable_26", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_27 = new Message("yOffSetIP = 0"
				, "isPlayable_27", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_28 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_28", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_29 = new Message("isPlayableIP = T"
				, "isPlayable_29", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_30 = new Message("noop"
				, "isPlayable_30", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_31 = new Message("noop"
				, "isPlayable_31", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_32 = new Message("xOffSetIP = -1"
				, "isPlayable_32", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_33 = new Message("yOffSetIP = 1"
				, "isPlayable_33", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_34 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_34", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_35 = new Message("isPlayableIP = T"
				, "isPlayable_35", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_36 = new Message("noop"
				, "isPlayable_36", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_37 = new Message("noop"
				, "isPlayable_37", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_38 = new Message("xOffSetIP = 0"
				, "isPlayable_38", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_39 = new Message("yOffSetIP = 1"
				, "isPlayable_39", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_40 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_40", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_41 = new Message("isPlayableIP = T"
				, "isPlayable_41", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_42 = new Message("noop"
				, "isPlayable_42", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_43 = new Message("noop"
				, "isPlayable_43", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_44 = new Message("xOffSetIP = 1"
				, "isPlayable_44", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_45 = new Message("yOffSetIP = 1"
				, "isPlayable_45", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_46 = new Message("numTrappedIP = calcNumTrapped(givenIP, blackIP, xOffSetIP, yOffSetIP)"
				, "isPlayable_46", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_47 = new Message("isPlayableIP = T"
				, "isPlayable_47", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_48 = new Message("noop"
				, "isPlayable_48", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_49 = new Message("noop"
				, "isPlayable_49", counter++, false, Optional.of("boardIP"), Optional.of("boardIP"), "isPlayable", false);
		Message isPlayable_50 = new Message("isPlayableIP"
				, "isPlayable_50", counter++, true, Optional.of("boardIP"), Optional.empty(), "isPlayable", false);

		store.addTempVar("givenIP", givenIP);
		store.addTempVar("blackIP", blackIP);
		store.addTempVar("boardIP", boardIP);
		store.addTempVar("xOffSetIP", xOffSetIP);
		store.addTempVar("yOffSetIP", yOffSetIP);
		store.addTempVar("isPlayableIP", isPlayableIP);
		store.addTempVar("numTrappedIP", numTrappedIP);
		
		store.addMessages(Arrays.asList(
				isPlayable_1, isPlayable_2, isPlayable_3, isPlayable_4, isPlayable_5, isPlayable_6,
				isPlayable_7, isPlayable_8, isPlayable_9, isPlayable_10, isPlayable_11, isPlayable_12, 
				isPlayable_13, isPlayable_14, isPlayable_15, isPlayable_16, isPlayable_17, isPlayable_18, 
				isPlayable_19, isPlayable_20, isPlayable_21, isPlayable_22, isPlayable_23, isPlayable_24, 
				isPlayable_25, isPlayable_26, isPlayable_27, isPlayable_28, isPlayable_29, isPlayable_30, 
				isPlayable_31, isPlayable_32, isPlayable_33, isPlayable_34, isPlayable_35, isPlayable_36, 
				isPlayable_37, isPlayable_38, isPlayable_39, isPlayable_40, isPlayable_41, isPlayable_42, 
				isPlayable_43, isPlayable_44, isPlayable_43, isPlayable_44, isPlayable_45, isPlayable_46, 
				isPlayable_47, isPlayable_48, isPlayable_49, isPlayable_50));
		store.addIdToMessage("isPlayable_1", isPlayable_1);
		store.addIdToMessage("isPlayable_2", isPlayable_2);
		store.addIdToMessage("isPlayable_3", isPlayable_3);
		store.addIdToMessage("isPlayable_4", isPlayable_4);
		store.addIdToMessage("isPlayable_5", isPlayable_5);
		store.addIdToMessage("isPlayable_6", isPlayable_6);
		store.addIdToMessage("isPlayable_7", isPlayable_7);
		store.addIdToMessage("isPlayable_8", isPlayable_8);
		store.addIdToMessage("isPlayable_9", isPlayable_9);
		store.addIdToMessage("isPlayable_10", isPlayable_10);
		store.addIdToMessage("isPlayable_11", isPlayable_11);
		store.addIdToMessage("isPlayable_12", isPlayable_12);
		store.addIdToMessage("isPlayable_13", isPlayable_13);
		store.addIdToMessage("isPlayable_14", isPlayable_14);
		store.addIdToMessage("isPlayable_15", isPlayable_15);
		store.addIdToMessage("isPlayable_16", isPlayable_16);
		store.addIdToMessage("isPlayable_17", isPlayable_17);
		store.addIdToMessage("isPlayable_18", isPlayable_18);
		store.addIdToMessage("isPlayable_19", isPlayable_19);
		store.addIdToMessage("isPlayable_20", isPlayable_20);
		store.addIdToMessage("isPlayable_21", isPlayable_21);
		store.addIdToMessage("isPlayable_22", isPlayable_22);
		store.addIdToMessage("isPlayable_23", isPlayable_23);
		store.addIdToMessage("isPlayable_24", isPlayable_24);
		store.addIdToMessage("isPlayable_25", isPlayable_25);
		store.addIdToMessage("isPlayable_26", isPlayable_26);
		store.addIdToMessage("isPlayable_27", isPlayable_27);
		store.addIdToMessage("isPlayable_28", isPlayable_28);
		store.addIdToMessage("isPlayable_29", isPlayable_29);
		store.addIdToMessage("isPlayable_30", isPlayable_30);
		store.addIdToMessage("isPlayable_31", isPlayable_31);
		store.addIdToMessage("isPlayable_32", isPlayable_32);
		store.addIdToMessage("isPlayable_33", isPlayable_33);
		store.addIdToMessage("isPlayable_34", isPlayable_34);
		store.addIdToMessage("isPlayable_35", isPlayable_35);
		store.addIdToMessage("isPlayable_36", isPlayable_36);
		store.addIdToMessage("isPlayable_37", isPlayable_37);
		store.addIdToMessage("isPlayable_38", isPlayable_38);
		store.addIdToMessage("isPlayable_39", isPlayable_39);
		store.addIdToMessage("isPlayable_40", isPlayable_40);
		store.addIdToMessage("isPlayable_41", isPlayable_41);
		store.addIdToMessage("isPlayable_42", isPlayable_42);
		store.addIdToMessage("isPlayable_43", isPlayable_43);
		store.addIdToMessage("isPlayable_44", isPlayable_44);
		store.addIdToMessage("isPlayable_45", isPlayable_45);
		store.addIdToMessage("isPlayable_46", isPlayable_46);
		store.addIdToMessage("isPlayable_47", isPlayable_47);
		store.addIdToMessage("isPlayable_48", isPlayable_48);
		store.addIdToMessage("isPlayable_49", isPlayable_49);
		store.addIdToMessage("isPlayable_50", isPlayable_50);
		
		// CALCNUMTRAPPED
		
		counter = 1;
		
		// TEMP VARS

		TempVar posNT = new TempVar(new UserDefinedType("Position"), "posNT");
		TempVar blackNT = new TempVar(new UserDefinedType("boolean"), "blackNT");
		TempVar xOffNT = new TempVar(new UserDefinedType("int"), "xOffNT");
		TempVar yOffNT = new TempVar(new UserDefinedType("int"), "yOffNT");
		TempVar boardNT = new TempVar(new UserDefinedType("Board"), "boardNT");
		TempVar posStepNT = new TempVar(new UserDefinedType("Position"), "posStepNT");
		TempVar numTrappedNT = new TempVar(new UserDefinedType("int"), "numTrappedNT");
		TempVar toCheckNT = new TempVar(new UserDefinedType("boolean"), "toCheckNT");
		TempVar neighExistsNT = new TempVar(new UserDefinedType("boolean"), "neighExistsNT");
		TempVar loopNT = new TempVar(new UserDefinedType("boolean"), "loopNT");
		TempVar occupiedNT = new TempVar(new UserDefinedType("boolean"), "occupiedNT");
		
		// MESSAGES

		Message calcNumTrapped_1 = new Message("calcNumTrapped(posNT, blackNT, xOffNT, yOffNT)"
				, "calcNumTrapped_1", counter++, false, Optional.empty(), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_2 = new Message("numTrappedNT = 0"
				, "calcNumTrapped_2", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_3 = new Message("toCheckNT = not(blackNT)"
				, "calcNumTrapped_3", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_4 = new Message("neighExistsNT = neighborExists(posNT, xOffNT, yOffNT)"
				, "calcNumTrapped_4", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_5 = new Message("loopNT = T"
				, "calcNumTrapped_5", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_6 = new Message("posStepNT = calcNeighbor(posNT, xOffNT, yOffNT)"
				, "calcNumTrapped_6", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_7 = new Message("occupiedNT = getOccupied()"
				, "calcNumTrapped_7", counter++, false, Optional.of("boardNT"), Optional.of("posStepNT"), "calcNumTrapped", false);
		Message calcNumTrapped_8 = new Message("blackNT = getBlack()"
				, "calcNumTrapped_8", counter++, false, Optional.of("boardNT"), Optional.of("posStepNT"), "calcNumTrapped", false);
		Message calcNumTrapped_9 = new Message("numTrappedNT = numTrappedNT + 1"
				, "calcNumTrapped_9", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_10 = new Message("neighExistsNT = neighborExists(posStepNT, xOffNT, yOffNT)"
				, "calcNumTrapped_10", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_11 = new Message("posStepNT = calcNeighbor(posStepNT, xOffNT, yOffNT)"
				, "calcNumTrapped_11", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_12 = new Message("loopNT = F"
				, "calcNumTrapped_12", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_13 = new Message("numTrappedNT = 0"
				, "calcNumTrapped_13", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_14 = new Message("noop"
				, "calcNumTrapped_14", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_15 = new Message("loopNT = F"
				, "calcNumTrapped_15", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_16 = new Message("noop"
				, "calcNumTrapped_16", counter++, false, Optional.of("boardNT"), Optional.of("boardNT"), "calcNumTrapped", false);
		Message calcNumTrapped_17 = new Message("numTrappedNT"
				, "calcNumTrapped_17", counter++, true, Optional.of("boardNT"), Optional.empty(), "calcNumTrapped", false);
		
		store.addTempVar("posNT", posNT);
		store.addTempVar("blackNT", blackNT);
		store.addTempVar("xOffNT", xOffNT);
		store.addTempVar("yOffNT", yOffNT);
		store.addTempVar("boardNT", boardNT);
		store.addTempVar("posStepNT", posStepNT);
		store.addTempVar("numTrappedNT", numTrappedNT);
		store.addTempVar("toCheckNT", toCheckNT);
		store.addTempVar("neighExistsNT", neighExistsNT);
		store.addTempVar("loopNT", loopNT);
		store.addTempVar("occupiedNT", occupiedNT);
		
		store.addMessages(Arrays.asList(calcNumTrapped_1, calcNumTrapped_2, calcNumTrapped_3
				, calcNumTrapped_4, calcNumTrapped_5, calcNumTrapped_6, calcNumTrapped_7
				, calcNumTrapped_8, calcNumTrapped_9, calcNumTrapped_10, calcNumTrapped_11
				, calcNumTrapped_12, calcNumTrapped_13, calcNumTrapped_14, calcNumTrapped_15
				, calcNumTrapped_16, calcNumTrapped_17));
		store.addIdToMessage("calcNumTrapped_1", calcNumTrapped_1);
		store.addIdToMessage("calcNumTrapped_2", calcNumTrapped_2);
		store.addIdToMessage("calcNumTrapped_3", calcNumTrapped_3);
		store.addIdToMessage("calcNumTrapped_4", calcNumTrapped_4);
		store.addIdToMessage("calcNumTrapped_5", calcNumTrapped_5);
		store.addIdToMessage("calcNumTrapped_6", calcNumTrapped_6);
		store.addIdToMessage("calcNumTrapped_7", calcNumTrapped_7);
		store.addIdToMessage("calcNumTrapped_8", calcNumTrapped_8);
		store.addIdToMessage("calcNumTrapped_9", calcNumTrapped_9);
		store.addIdToMessage("calcNumTrapped_10", calcNumTrapped_10);
		store.addIdToMessage("calcNumTrapped_11", calcNumTrapped_11);
		store.addIdToMessage("calcNumTrapped_12", calcNumTrapped_12);
		store.addIdToMessage("calcNumTrapped_13", calcNumTrapped_13);
		store.addIdToMessage("calcNumTrapped_14", calcNumTrapped_14);
		store.addIdToMessage("calcNumTrapped_15", calcNumTrapped_15);
		store.addIdToMessage("calcNumTrapped_16", calcNumTrapped_16);
		store.addIdToMessage("calcNumTrapped_17", calcNumTrapped_17);
		
		// BACKTRACKFLIP
		
		counter = 1;
				
		// TEMPVARS

		TempVar beginBTF = new TempVar(new UserDefinedType("Position"), "beginBTF");
		TempVar endBTF = new TempVar(new UserDefinedType("Position"), "endBTF");
		TempVar xOffSetBTF = new TempVar(new UserDefinedType("int"), "xOffSetBTF");
		TempVar yOffSetBTF = new TempVar(new UserDefinedType("int"), "yOffSetBTF");
		TempVar boardBTF = new TempVar(new UserDefinedType("Board"), "boardBTF");
		TempVar toFlipBTF = new TempVar(new UserDefinedType("Position"), "toFlipBTF");
		TempVar stopBTF = new TempVar(new UserDefinedType("boolean"), "stopBTF");
		TempVar blackBTF = new TempVar(new UserDefinedType("boolean"), "blackBTF");
		
		// MESSAGES

		Message backtrackFlip_1 = new Message("backtrackFlip(beginBTF, endBTF, xOffSetBTF, yOffSetBTF)"
				, "backtrackFlip_1", counter++, false, Optional.empty(), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_2 = new Message("toFlipBTF = calcNeighbor(beginBTF, xOffSetBTF, yOffSetBTF)"
				, "backtrackFlip_2", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_3 = new Message("stopBTF = (toFlipBTF = endBTF)"
				, "backtrackFlip_3", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_4 = new Message("getBlack()"
				, "backtrackFlip_4", counter++, false, Optional.of("boardBTF"), Optional.of("toFlipBTF"), "backtrackFlip", false);
		Message backtrackFlip_5 = new Message("blackBTF"
				, "backtrackFlip_5", counter++, false, Optional.of("toFlipBTF"), Optional.of("boardTF"), "backtrackFlip", false);
		Message backtrackFlip_6 = new Message("blackBTF = not(blackBTF)"
				, "backtrackFlip_6", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_7 = new Message("setBlack(blackBTF)"
				, "backtrackFlip_7", counter++, false, Optional.of("boardBTF"), Optional.of("toFlipBTF"), "backtrackFlip", false);
		Message backtrackFlip_8 = new Message("toFlipBTF = calcNeighbor(toFlipBTF, xOffSetBTF, yOffSetBTF)"
				, "backtrackFlip_8", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_9 = new Message("stopBTF = T"
				, "backtrackFlip_9", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);
		Message backtrackFlip_10 = new Message("noop"
				, "backtrackFlip_10", counter++, false, Optional.of("boardBTF"), Optional.of("boardBTF"), "backtrackFlip", false);

		store.addTempVar("beginBTF", beginBTF);
		store.addTempVar("endBTF", endBTF);
		store.addTempVar("xOffSetBTF", xOffSetBTF);
		store.addTempVar("yOffSetBTF", yOffSetBTF);
		store.addTempVar("boardBTF", boardBTF);
		store.addTempVar("toFlipBTF", toFlipBTF);
		store.addTempVar("stopBTF", stopBTF);
		store.addTempVar("blackBTF", blackBTF);
		
		store.addMessages(Arrays.asList(backtrackFlip_1, backtrackFlip_2, backtrackFlip_3
				, backtrackFlip_4, backtrackFlip_5, backtrackFlip_6, backtrackFlip_7
				, backtrackFlip_8, backtrackFlip_9, backtrackFlip_10));
		store.addIdToMessage("backtrackFlip_1", backtrackFlip_1);
		store.addIdToMessage("backtrackFlip_2", backtrackFlip_2);
		store.addIdToMessage("backtrackFlip_3", backtrackFlip_3);
		store.addIdToMessage("backtrackFlip_4", backtrackFlip_4);
		store.addIdToMessage("backtrackFlip_5", backtrackFlip_5);
		store.addIdToMessage("backtrackFlip_6", backtrackFlip_6);
		store.addIdToMessage("backtrackFlip_7", backtrackFlip_7);
		store.addIdToMessage("backtrackFlip_8", backtrackFlip_8);
		store.addIdToMessage("backtrackFlip_9", backtrackFlip_9);
		store.addIdToMessage("backtrackFlip_10", backtrackFlip_10);
		
		// FLIPTILESINDIR
		
		counter = 1;
		
		// TEMP VARS

		TempVar beginFID = new TempVar(new UserDefinedType("Position"), "beginFID");
		TempVar playerFID = new TempVar(new UserDefinedType("boolean"), "playerFID");
		TempVar xOffSetFID = new TempVar(new UserDefinedType("int"), "xOffSetFID");
		TempVar yOffSetFID = new TempVar(new UserDefinedType("int"), "yOffSetFID");
		TempVar boardFID = new TempVar(new UserDefinedType("Board"), "boardFID");
		TempVar neighborFID = new TempVar(new UserDefinedType("Position"), "neighborFID");
		TempVar neighborExistsFID = new TempVar(new UserDefinedType("boolean"), "neighborExistsFID");
		TempVar blackFID = new TempVar(new UserDefinedType("boolean"), "blackFID");
		TempVar occupiedFID = new TempVar(new UserDefinedType("boolean"), "occupiedFID");
		TempVar stopFID = new TempVar(new UserDefinedType("boolean"), "stopFID");

		Message flipTilesInDir_1 = new Message("flipTilesInDir(beginFID, playerFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_1", counter++, false, Optional.empty(), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_2 = new Message("neighborExistsFID = neighborExists(beginFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_2", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_3 = new Message("neighborFID = calcNeighbor(beginFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_3", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_4 = new Message("getBlack()"
				, "flipTilesInDir_4", counter++, false, Optional.of("boardFID"), Optional.of("neighborFID"), "flipTilesInDir", false);
		Message flipTilesInDir_5 = new Message("blackFID"
				, "flipTilesInDir_5", counter++, false, Optional.of("neighborFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_6 = new Message("getOccupied()"
				, "flipTilesInDir_6", counter++, false, Optional.of("boardFID"), Optional.of("neighborFID"), "flipTilesInDir", false);
		Message flipTilesInDir_7 = new Message("occupiedFID"
				, "flipTilesInDir_7", counter++, false, Optional.of("neighborFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_8 = new Message("stopFID = F"
				, "flipTilesInDir_8", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_9 = new Message("neighborExistsFID = neighborExists(neighborFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_9", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_10 = new Message("neighborFID = calcNeighbor(neighborFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_10", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_11 = new Message("getBlack()"
				, "flipTilesInDir_11", counter++, false, Optional.of("boardFID"), Optional.of("neighborFID"), "flipTilesInDir", false);
		Message flipTilesInDir_12 = new Message("blackFID"
				, "flipTilesInDir_12", counter++, false, Optional.of("neighborFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_13 = new Message("getOccupied()"
				, "flipTilesInDir_13", counter++, false, Optional.of("boardFID"), Optional.of("neighborFID"), "flipTilesInDir", false);
		Message flipTilesInDir_14 = new Message("occupiedFID"
				, "flipTilesInDir_14", counter++, false, Optional.of("neighborFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_15 = new Message("xOffSetFID = -xOffSetFID"
				, "flipTilesInDir_15", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_16 = new Message("yOffSetFID = -yOffSetFID"
				, "flipTilesInDir_16", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_17 = new Message("backtrackFlip(neighborFID, beginFID, xOffSetFID, yOffSetFID)"
				, "flipTilesInDir_17", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_18 = new Message("stopFID = T"
				, "flipTilesInDir_18", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_19 = new Message("stopFID = T"
				, "flipTilesInDir_19", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_20 = new Message("noop"
				, "flipTilesInDir_20", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_21 = new Message("stopFID = T"
				, "flipTilesInDir_21", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_22 = new Message("noop"
				, "flipTilesInDir_22", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);
		Message flipTilesInDir_23 = new Message("noop"
				, "flipTilesInDir_23", counter++, false, Optional.of("boardFID"), Optional.of("boardFID"), "flipTilesInDir", false);

		store.addTempVar("beginFID", beginFID);
		store.addTempVar("playerFID", playerFID);
		store.addTempVar("xOffSetFID", xOffSetFID);
		store.addTempVar("yOffSetFID", yOffSetFID);
		store.addTempVar("boardFID", boardFID);
		store.addTempVar("neighborFID", neighborFID);
		store.addTempVar("neighborExistsFID", neighborExistsFID);
		store.addTempVar("blackFID", blackFID);
		store.addTempVar("occupiedFID", occupiedFID);
		store.addTempVar("stopFID", stopFID);
		
		store.addMessages(Arrays.asList(flipTilesInDir_1, flipTilesInDir_2, flipTilesInDir_3
				, flipTilesInDir_4, flipTilesInDir_5, flipTilesInDir_6, flipTilesInDir_7 
				, flipTilesInDir_8, flipTilesInDir_9, flipTilesInDir_10, flipTilesInDir_11
				, flipTilesInDir_12, flipTilesInDir_13, flipTilesInDir_14, flipTilesInDir_15
				, flipTilesInDir_16, flipTilesInDir_17, flipTilesInDir_18, flipTilesInDir_19
				, flipTilesInDir_20, flipTilesInDir_21, flipTilesInDir_22, flipTilesInDir_23));
		store.addIdToMessage("flipTilesInDir_1", flipTilesInDir_1);
		store.addIdToMessage("flipTilesInDir_2", flipTilesInDir_2);
		store.addIdToMessage("flipTilesInDir_3", flipTilesInDir_3);
		store.addIdToMessage("flipTilesInDir_4", flipTilesInDir_4);
		store.addIdToMessage("flipTilesInDir_5", flipTilesInDir_5);
		store.addIdToMessage("flipTilesInDir_6", flipTilesInDir_6);
		store.addIdToMessage("flipTilesInDir_7", flipTilesInDir_7);
		store.addIdToMessage("flipTilesInDir_8", flipTilesInDir_8);
		store.addIdToMessage("flipTilesInDir_9", flipTilesInDir_9);
		store.addIdToMessage("flipTilesInDir_10", flipTilesInDir_10);
		store.addIdToMessage("flipTilesInDir_11", flipTilesInDir_11);
		store.addIdToMessage("flipTilesInDir_12", flipTilesInDir_12);
		store.addIdToMessage("flipTilesInDir_13", flipTilesInDir_13);
		store.addIdToMessage("flipTilesInDir_14", flipTilesInDir_14);
		store.addIdToMessage("flipTilesInDir_15", flipTilesInDir_15);
		store.addIdToMessage("flipTilesInDir_16", flipTilesInDir_16);
		store.addIdToMessage("flipTilesInDir_17", flipTilesInDir_17);
		store.addIdToMessage("flipTilesInDir_18", flipTilesInDir_18);
		store.addIdToMessage("flipTilesInDir_19", flipTilesInDir_19);
		store.addIdToMessage("flipTilesInDir_20", flipTilesInDir_20);
		store.addIdToMessage("flipTilesInDir_21", flipTilesInDir_21);
		store.addIdToMessage("flipTilesInDir_22", flipTilesInDir_22);
		store.addIdToMessage("flipTilesInDir_23", flipTilesInDir_23);
		
		// FLIPTILES
		
		counter = 1;
		
		// TEMP VARS

		TempVar posFT = new TempVar(new UserDefinedType("Position"), "posFT");
		TempVar boardFT = new TempVar(new UserDefinedType("Board"), "boardFT");
		TempVar blackFT = new TempVar(new UserDefinedType("boolean"), "blackFT");
		TempVar xOffSetFT = new TempVar(new UserDefinedType("int"), "xOffSetFT");
		TempVar yOffSetFT = new TempVar(new UserDefinedType("int"), "yOffSetFT");
		
		Message flipTiles_1 = new Message("flipTiles(posFT)"
				, "flipTiles_1", counter++, false, Optional.empty(), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_2 = new Message("getBlack()"
				, "flipTiles_2", counter++, false, Optional.of("boardFT"), Optional.of("posFT"), "flipTiles", false);		
		Message flipTiles_3 = new Message("blackFT"
				, "flipTiles_3", counter++, false, Optional.of("posFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_4 = new Message("xOffSetFT = 1"
				, "flipTiles_4", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_5 = new Message("yOffSetFT = 0"
				, "flipTiles_5", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_6 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_6", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_7 = new Message("xOffSetFT = 1"
				, "flipTiles_7", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_8 = new Message("yOffSetFT= -1"
				, "flipTiles_8", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_9 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_9", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_10 = new Message("xOffSetFT = 0"
				, "flipTiles_10", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_11 = new Message("yOffSetFT = -1"
				, "flipTiles_11", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_12 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_12", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_13 = new Message("xOffSetFT = -1"
				, "flipTiles_13", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_14 = new Message("yOffSetFT = -1"
				, "flipTiles_14", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_15 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_15", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_16 = new Message("xOffSetFT = -1"
				, "flipTiles_16", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_17 = new Message("yOffSetFT = 0"
				, "flipTiles_17", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_18 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_18", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_19 = new Message("xOffSetFT = -1"
				, "flipTiles_19", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_20 = new Message("yOffSetFT = 1"
				, "flipTiles_20", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_21 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_21", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_22 = new Message("xOffSetFT = 0"
				, "flipTiles_22", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_23 = new Message("yOffSetFT = 1"
				, "flipTiles_23", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_24 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_24", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_25 = new Message("xOffSetFT = 1"
				, "flipTiles_25", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_26 = new Message("yOffSetFT = 1"
				, "flipTiles_26", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);		
		Message flipTiles_27 = new Message("flipTilesInDir(posFT, blackFT, xOffSetFT, yOffSetFT)"
				, "flipTiles_27", counter++, false, Optional.of("boardFT"), Optional.of("boardFT"), "flipTiles", false);

		store.addTempVar("posFT", posFT);
		store.addTempVar("boardFT", boardFT);
		store.addTempVar("blackFT", blackFT);
		store.addTempVar("xOffSetFT", xOffSetFT);
		store.addTempVar("yOffSetFT", yOffSetFT);
		
		store.addMessages(Arrays.asList(flipTiles_1, flipTiles_2, flipTiles_3, flipTiles_4
				, flipTiles_5, flipTiles_6, flipTiles_7, flipTiles_8, flipTiles_9
				, flipTiles_10, flipTiles_11, flipTiles_12, flipTiles_13, flipTiles_14
				, flipTiles_15, flipTiles_16, flipTiles_17, flipTiles_18, flipTiles_19
				, flipTiles_20, flipTiles_21, flipTiles_22, flipTiles_23, flipTiles_24
				, flipTiles_25, flipTiles_26, flipTiles_27));
		store.addIdToMessage("flipTiles_1", flipTiles_1);
		store.addIdToMessage("flipTiles_2", flipTiles_2);
		store.addIdToMessage("flipTiles_3", flipTiles_3);
		store.addIdToMessage("flipTiles_4", flipTiles_4);
		store.addIdToMessage("flipTiles_5", flipTiles_5);
		store.addIdToMessage("flipTiles_6", flipTiles_6);
		store.addIdToMessage("flipTiles_7", flipTiles_7);
		store.addIdToMessage("flipTiles_8", flipTiles_8);
		store.addIdToMessage("flipTiles_9", flipTiles_9);
		store.addIdToMessage("flipTiles_10", flipTiles_10);
		store.addIdToMessage("flipTiles_11", flipTiles_11);
		store.addIdToMessage("flipTiles_12", flipTiles_12);
		store.addIdToMessage("flipTiles_13", flipTiles_13);
		store.addIdToMessage("flipTiles_14", flipTiles_14);
		store.addIdToMessage("flipTiles_15", flipTiles_15);
		store.addIdToMessage("flipTiles_16", flipTiles_16);
		store.addIdToMessage("flipTiles_17", flipTiles_17);
		store.addIdToMessage("flipTiles_18", flipTiles_18);
		store.addIdToMessage("flipTiles_19", flipTiles_19);
		store.addIdToMessage("flipTiles_20", flipTiles_20);
		store.addIdToMessage("flipTiles_21", flipTiles_21);
		store.addIdToMessage("flipTiles_22", flipTiles_22);
		store.addIdToMessage("flipTiles_23", flipTiles_23);
		store.addIdToMessage("flipTiles_24", flipTiles_24);
		store.addIdToMessage("flipTiles_25", flipTiles_25);
		store.addIdToMessage("flipTiles_26", flipTiles_26);
		store.addIdToMessage("flipTiles_27", flipTiles_27);
		
		// PLAY
		
		counter = 1;
		
		// TEMP VARS

		TempVar boardP = new TempVar(new UserDefinedType("Board"), "boardP");
		TempVar playPosP = new TempVar(new UserDefinedType("Position"), "playPosP");
		TempVar playerP = new TempVar(new UserDefinedType("boolean"), "playerP");
		TempVar skipCountP = new TempVar(new UserDefinedType("int"), "skipCountP");
		TempVar canPlayP = new TempVar(new UserDefinedType("boolean"), "canPlayP");
		TempVar countBlackP = new TempVar(new UserDefinedType("int"), "countBlackP");
		TempVar countWhiteP = new TempVar(new UserDefinedType("int"), "countWhiteP");
		TempVar tieP = new TempVar(new UserDefinedType("boolean"), "tieP");
		TempVar blackWinsP = new TempVar(new UserDefinedType("boolean"), "blackWinsP");
		

		Message play_1 = new Message("play()"
				, "play_1", counter++, false, Optional.empty(), Optional.of("boardP"), "play", false);	
		Message play_2 = new Message("playerP = T"
				, "play_2", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_3 = new Message("skipCountP = 0"
				, "play_3", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_4 = new Message("canPlayP = canPlay(T)"
				, "play_4", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_5 = new Message("skipCountP = 0"
				, "play_5", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_6 = new Message("playPosP = randomPossiblePos(T)"
				, "play_6", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_7 = new Message("setOccupied(T)"
				, "play_7", counter++, false, Optional.of("boardP"), Optional.of("playPosP"), "play", false);	
		Message play_8 = new Message("setBlack(T)"
				, "play_8", counter++, false, Optional.of("boardP"), Optional.of("playPosP"), "play", false);	
		Message play_9 = new Message("flipTiles(playPosP)"
				, "play_9", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_10 = new Message("skipCountP = skipCountP + 1"
				, "play_10", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_11 = new Message("canPlayP = canPlay(F)"
				, "play_11", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_12 = new Message("skipCountP = 0"
				, "play_12", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_13 = new Message("playPosP = randomPossiblePos(F)"
				, "play_13", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_14 = new Message("setOccupied(T)"
				, "play_14", counter++, false, Optional.of("boardP"), Optional.of("playPosP"), "play", false);	
		Message play_15 = new Message("setBlack(F)"
				, "play_15", counter++, false, Optional.of("boardP"), Optional.of("playPosP"), "play", false);	
		Message play_16 = new Message("flipTiles(playPosP)"
				, "play_16", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_17 = new Message("skipCountP = skipCountP + 1"
				, "play_17", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_18 = new Message("noop"
				, "play_18", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_19 = new Message("countBlackP = count(T)"
				, "play_19", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_20 = new Message("countWhiteP = count(F)"
				, "play_20", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_21 = new Message("tieP = (countBlackP = countWhiteP)"
				, "play_21", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_22 = new Message("blackWinsP = (countBlackP > countWhiteP)"
				, "play_22", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_23 = new Message("setTie(tieP)"
				, "play_23", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_24 = new Message("setBlackWinner(blackWinsP)"
				, "play_24", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	
		Message play_25 = new Message("setGameOver(T)"
				, "play_25", counter++, false, Optional.of("boardP"), Optional.of("boardP"), "play", false);	

		store.addTempVar("boardP", boardP);
		store.addTempVar("playPosP", playPosP);
		store.addTempVar("playerP", playerP);
		store.addTempVar("skipCountP", skipCountP);
		store.addTempVar("canPlayP", canPlayP);
		store.addTempVar("countBlackP", countBlackP);
		store.addTempVar("countWhiteP", countWhiteP);
		store.addTempVar("tieP", tieP);
		store.addTempVar("blackWinsP", blackWinsP);
		
		store.addMessages(Arrays.asList(play_1, play_2, play_3, play_4, play_5, play_6, play_7
				, play_8, play_9, play_10, play_11, play_12, play_13, play_14, play_15, play_16, play_17
				, play_18, play_19, play_20, play_21, play_22, play_23, play_24, play_25));
		store.addIdToMessage("play_1", play_1);
		store.addIdToMessage("play_2", play_2);
		store.addIdToMessage("play_3", play_3);
		store.addIdToMessage("play_4", play_4);
		store.addIdToMessage("play_5", play_5);
		store.addIdToMessage("play_6", play_6);
		store.addIdToMessage("play_7", play_7);
		store.addIdToMessage("play_8", play_8);
		store.addIdToMessage("play_9", play_9);
		store.addIdToMessage("play_10", play_10);
		store.addIdToMessage("play_11", play_11);
		store.addIdToMessage("play_12", play_12);
		store.addIdToMessage("play_13", play_13);
		store.addIdToMessage("play_14", play_14);
		store.addIdToMessage("play_15", play_15);
		store.addIdToMessage("play_16", play_16);
		store.addIdToMessage("play_17", play_17);
		store.addIdToMessage("play_18", play_18);
		store.addIdToMessage("play_19", play_19);
		store.addIdToMessage("play_20", play_20);
		store.addIdToMessage("play_21", play_21);
		store.addIdToMessage("play_22", play_22);
		store.addIdToMessage("play_23", play_23);
		store.addIdToMessage("play_24", play_24);
		store.addIdToMessage("play_25", play_25);
		
		// COUNT
		
		counter = 1;
		
		// TEMP VARS

		TempVar blackC = new TempVar(new UserDefinedType("boolean"), "blackC");
		TempVar boardC = new TempVar(new UserDefinedType("Board"), "boardC");
		TempVar countC = new TempVar(new UserDefinedType("int"), "countC");
		TempVar xCoMaxC = new TempVar(new UserDefinedType("int"), "xCoMaxC");
		TempVar yCoMaxC = new TempVar(new UserDefinedType("int"), "yCoMaxC");
		TempVar xCoC = new TempVar(new UserDefinedType("int"), "xCoC");
		TempVar yCoC = new TempVar(new UserDefinedType("int"), "yCoC");
		TempVar posC = new TempVar(new UserDefinedType("Position"), "posC");
		TempVar colorC = new TempVar(new UserDefinedType("boolean"), "colorC");
		TempVar occupiedC = new TempVar(new UserDefinedType("boolean"), "occupiedC");
		
		// MESSAGES
		
		Message count_1 = new Message("count(blackC)"
				, "count_1", counter++, false, Optional.empty(), Optional.of("boardC"), "count", false);			
		Message count_2 = new Message("countC = 0"
				, "count_2", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_3 = new Message("xCoMaxC = getXCoMax()"
				, "count_3", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_4 = new Message("yCoMaxC = getYCoMax()"
				, "count_4", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_5 = new Message("xCoC = 1"
				, "count_5", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_6 = new Message("yCoC = 1"
				, "count_6", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_7 = new Message("posC = getPositionByXCoandYCo(xCoC, yCoC)"
				, "count_7", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_8 = new Message("getBlack()"
				, "count_8", counter++, false, Optional.of("boardC"), Optional.of("posC"), "count", false);			
		Message count_9 = new Message("colorC"
				, "count_9", counter++, false, Optional.of("posC"), Optional.of("boardC"), "count", false);			
		Message count_10 = new Message("getOccupied()"
				, "count_10", counter++, false, Optional.of("boardC"), Optional.of("posC"), "count", false);			
		Message count_11 = new Message("occupiedC"
				, "count_11", counter++, false, Optional.of("posC"), Optional.of("boardC"), "count", false);			
		Message count_12 = new Message("countC = countC + 1"
				, "count_12", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_13 = new Message("noop"
				, "count_13", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_14 = new Message("yCoC = yCoC + 1"
				, "count_14", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_15 = new Message("xCoC = xCoC + 1"
				, "count_15", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_16 = new Message("yCoC = 1"
				, "count_16", counter++, false, Optional.of("boardC"), Optional.of("boardC"), "count", false);			
		Message count_17 = new Message("countC"
				, "count_17", counter++, true, Optional.of("boardC"), Optional.empty(), "count", false);	
		
		/*
		 * 		TempVar blackC = new TempVar(new UserDefinedType("boolean"), "blackC");
		TempVar boardC = new TempVar(new UserDefinedType("Board"), "boardC");
		TempVar countC = new TempVar(new UserDefinedType("int"), "countC");
		TempVar xCoMaxC = new TempVar(new UserDefinedType("int"), "xCoMaxC");
		TempVar yCoMaxC = new TempVar(new UserDefinedType("int"), "yCoMaxC");
		TempVar xCoC = new TempVar(new UserDefinedType("int"), "xCoC");
		TempVar yCoC = new TempVar(new UserDefinedType("int"), "yCoC");
		TempVar posC = new TempVar(new UserDefinedType("int"), "posC");
		TempVar colorC = new TempVar(new UserDefinedType("boolean"), "colorC");
		TempVar occupiedC = new TempVar(new UserDefinedType("boolean"), "occupiedC");
		 */

		store.addTempVar("blackC", blackC);
		store.addTempVar("boardC", boardC);
		store.addTempVar("countC", countC);
		store.addTempVar("xCoMaxC", xCoMaxC);
		store.addTempVar("yCoMaxC", yCoMaxC);
		store.addTempVar("xCoC", xCoC);
		store.addTempVar("yCoC", yCoC);
		store.addTempVar("posC", posC);
		store.addTempVar("colorC", colorC);
		store.addTempVar("occupiedC", occupiedC);
		
		store.addMessages(Arrays.asList(count_1, count_2, count_3, count_4, count_5, count_6
				, count_7, count_8, count_9, count_10, count_11, count_12, count_13, count_14
				, count_15, count_16, count_17));
		store.addIdToMessage("count_1", count_1);
		store.addIdToMessage("count_2", count_2);
		store.addIdToMessage("count_3", count_3);
		store.addIdToMessage("count_4", count_4);
		store.addIdToMessage("count_5", count_5);
		store.addIdToMessage("count_6", count_6);
		store.addIdToMessage("count_7", count_7);
		store.addIdToMessage("count_8", count_8);
		store.addIdToMessage("count_9", count_9);
		store.addIdToMessage("count_10", count_10);
		store.addIdToMessage("count_11", count_11);
		store.addIdToMessage("count_12", count_12);
		store.addIdToMessage("count_13", count_13);
		store.addIdToMessage("count_14", count_14);
		store.addIdToMessage("count_15", count_15);
		store.addIdToMessage("count_16", count_16);
		store.addIdToMessage("count_17", count_17);
		
		// COMBINED FRAGMENTS
		
		// NEIGHBOREXISTS
		
		AltCombinedFragment neAlt = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(neighborExists_8)), 
				Optional.of(Arrays.asList(neighborExists_9)), 
				"(1 =< xCoNE + xCoTestNE =< xCoMaxNE) & (1 =< yCoNE + yCoTestNE =< yCoMaxNE)", 
				"~((1 =< xCoNE + xCoTestNE =< xCoMaxNE) & (1 =< yCoNE + yCoTestNE =< yCoMaxNE))", dummy, dummy1, dummy2, store);
		neighborExists_8.setFragment(Optional.of(neAlt));
		neighborExists_9.setFragment(Optional.of(neAlt));
		
		// CANPLAY
		
		LoopCombinedFragment cpOuter = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty()
				, Optional.empty(), 
				Optional.of(Arrays.asList(canPlay_10, canPlay_11)), "xCoCP =< xMaxCP & (canPlayCP = F)", dummy, dummy1, store);
		canPlay_10.setFragment(Optional.of(cpOuter));
		canPlay_11.setFragment(Optional.of(cpOuter));
		LoopCombinedFragment cpInner = CombinedFragmentFactory.createLoopCombinedFragment(Optional.of(cpOuter)
				, Optional.empty(), Optional.of(Arrays.asList(canPlay_7, canPlay_8, canPlay_9))
				, "yCoCP =< yMaxCP & (canPlay = F)", dummy, dummy1, store);
		canPlay_7.setFragment(Optional.of(cpInner));
		canPlay_8.setFragment(Optional.of(cpInner));
		canPlay_9.setFragment(Optional.of(cpInner));
		cpOuter.setChildren(Arrays.asList(cpInner));
		
		// RANDOMPOSSIBLEPOS
		
		LoopCombinedFragment rppLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty()
				, Optional.empty()
				, Optional.of(Arrays.asList(randomPossiblePos_5, randomPossiblePos_6, randomPossiblePos_7, randomPossiblePos_8))
				, "foundRPP = F", dummy, dummy1, store);
		randomPossiblePos_5.setFragment(Optional.of(rppLoop));
		randomPossiblePos_6.setFragment(Optional.of(rppLoop));
		randomPossiblePos_7.setFragment(Optional.of(rppLoop));
		randomPossiblePos_8.setFragment(Optional.of(rppLoop));
		AltCombinedFragment rppOuter = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(rppLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(randomPossiblePos_9)), 
				Optional.of(Arrays.asList(randomPossiblePos_10)), 
				"occupiedRPP = T", 
				"occupiedRPP = F", dummy, dummy1, dummy2, store);
		randomPossiblePos_9.setFragment(Optional.of(rppOuter));
		randomPossiblePos_10.setFragment(Optional.of(rppOuter));
		rppLoop.setChildren(Arrays.asList(rppOuter));
		AltCombinedFragment rppInner = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(rppOuter)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(randomPossiblePos_11)), 
				Optional.of(Arrays.asList(randomPossiblePos_12)), 
				"isPlayableRPP = T", 
				"isPlayableRPP = F", dummy, dummy1, dummy2, store);
		randomPossiblePos_11.setFragment(Optional.of(rppInner));
		randomPossiblePos_12.setFragment(Optional.of(rppInner));
		rppOuter.setThenChildren(Arrays.asList(rppInner));
		
		// ISPLAYABLE
		
		AltCombinedFragment ipSimple = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_6)), 
				Optional.of(Arrays.asList(isPlayable_7)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_6.setFragment(Optional.of(ipSimple));
		isPlayable_7.setFragment(Optional.of(ipSimple));
		AltCombinedFragment ipOuter1 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_8, isPlayable_9, isPlayable_10)), 
				Optional.of(Arrays.asList(isPlayable_13)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_8.setFragment(Optional.of(ipOuter1));
		isPlayable_9.setFragment(Optional.of(ipOuter1));
		isPlayable_10.setFragment(Optional.of(ipOuter1));
		isPlayable_13.setFragment(Optional.of(ipOuter1));
		AltCombinedFragment ipInner1 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter1)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_11)), 
				Optional.of(Arrays.asList(isPlayable_12)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_11.setFragment(Optional.of(ipInner1));
		isPlayable_12.setFragment(Optional.of(ipInner1));
		ipOuter1.setIfChildren(Arrays.asList(ipInner1));
		AltCombinedFragment ipOuter2 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_14, isPlayable_15, isPlayable_16)), 
				Optional.of(Arrays.asList(isPlayable_19)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_14.setFragment(Optional.of(ipOuter2));
		isPlayable_15.setFragment(Optional.of(ipOuter2));
		isPlayable_16.setFragment(Optional.of(ipOuter2));
		isPlayable_19.setFragment(Optional.of(ipOuter2));
		AltCombinedFragment ipInner2 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter2)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_17)), 
				Optional.of(Arrays.asList(isPlayable_18)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_17.setFragment(Optional.of(ipInner2));
		isPlayable_18.setFragment(Optional.of(ipInner2));
		ipOuter2.setIfChildren(Arrays.asList(ipInner2));
		AltCombinedFragment ipOuter3 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_20, isPlayable_21, isPlayable_22)), 
				Optional.of(Arrays.asList(isPlayable_25)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_20.setFragment(Optional.of(ipOuter3));
		isPlayable_21.setFragment(Optional.of(ipOuter3));
		isPlayable_22.setFragment(Optional.of(ipOuter3));
		isPlayable_25.setFragment(Optional.of(ipOuter3));
		AltCombinedFragment ipInner3 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter3)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_23)), 
				Optional.of(Arrays.asList(isPlayable_24)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_23.setFragment(Optional.of(ipInner3));
		isPlayable_24.setFragment(Optional.of(ipInner3));
		ipOuter3.setIfChildren(Arrays.asList(ipInner3));
		AltCombinedFragment ipOuter4 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_26, isPlayable_27, isPlayable_28)), 
				Optional.of(Arrays.asList(isPlayable_31)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_26.setFragment(Optional.of(ipOuter4));
		isPlayable_27.setFragment(Optional.of(ipOuter4));
		isPlayable_28.setFragment(Optional.of(ipOuter4));
		isPlayable_31.setFragment(Optional.of(ipOuter4));
		AltCombinedFragment ipInner4 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter4)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_29)), 
				Optional.of(Arrays.asList(isPlayable_30)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_29.setFragment(Optional.of(ipInner4));
		isPlayable_30.setFragment(Optional.of(ipInner4));
		ipOuter4.setIfChildren(Arrays.asList(ipInner4));
		AltCombinedFragment ipOuter5 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_32, isPlayable_33, isPlayable_34)), 
				Optional.of(Arrays.asList(isPlayable_37)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_32.setFragment(Optional.of(ipOuter5));
		isPlayable_33.setFragment(Optional.of(ipOuter5));
		isPlayable_34.setFragment(Optional.of(ipOuter5));
		isPlayable_37.setFragment(Optional.of(ipOuter5));
		AltCombinedFragment ipInner5 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter5)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_35)), 
				Optional.of(Arrays.asList(isPlayable_36)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_35.setFragment(Optional.of(ipInner5));
		isPlayable_36.setFragment(Optional.of(ipInner5));
		ipOuter5.setIfChildren(Arrays.asList(ipInner5));
		AltCombinedFragment ipOuter6 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_38, isPlayable_39, isPlayable_40)), 
				Optional.of(Arrays.asList(isPlayable_43)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_38.setFragment(Optional.of(ipOuter6));
		isPlayable_39.setFragment(Optional.of(ipOuter6));
		isPlayable_40.setFragment(Optional.of(ipOuter6));
		isPlayable_43.setFragment(Optional.of(ipOuter6));
		AltCombinedFragment ipInner6 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter6)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_41)), 
				Optional.of(Arrays.asList(isPlayable_42)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_41.setFragment(Optional.of(ipInner6));
		isPlayable_42.setFragment(Optional.of(ipInner6));
		ipOuter6.setIfChildren(Arrays.asList(ipInner6));
		AltCombinedFragment ipOuter7 = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_44, isPlayable_45, isPlayable_46)), 
				Optional.of(Arrays.asList(isPlayable_49)), 
				"isPlayableIP = F", 
				"isPlayableIP = T", dummy, dummy1, dummy2, store);
		isPlayable_44.setFragment(Optional.of(ipOuter7));
		isPlayable_45.setFragment(Optional.of(ipOuter7));
		isPlayable_46.setFragment(Optional.of(ipOuter7));
		isPlayable_49.setFragment(Optional.of(ipOuter7));
		AltCombinedFragment ipInner7 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ipOuter7)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(isPlayable_47)), 
				Optional.of(Arrays.asList(isPlayable_48)), 
				"numTrappedIP > 0", 
				"numTrappedIP = 0", dummy, dummy1, dummy2, store);
		isPlayable_47.setFragment(Optional.of(ipInner7));
		isPlayable_48.setFragment(Optional.of(ipInner7));
		ipOuter7.setIfChildren(Arrays.asList(ipInner7));
		
		// CALCNUMTRAPPED
		
		AltCombinedFragment ntAltTop = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(calcNumTrapped_5, calcNumTrapped_6)), 
				Optional.of(Arrays.asList(calcNumTrapped_16)), 
				"neighExistsNT = T", 
				"neighExistsNT = F", dummy, dummy1, dummy2, store);
		calcNumTrapped_5.setFragment(Optional.of(ntAltTop));
		calcNumTrapped_6.setFragment(Optional.of(ntAltTop));
		calcNumTrapped_16.setFragment(Optional.of(ntAltTop));
		LoopCombinedFragment ntLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.of(ntAltTop)
				, Optional.empty(), 
				Optional.of(Arrays.asList(calcNumTrapped_7, calcNumTrapped_8)), "loopNT = T", dummy, dummy1, store);
		calcNumTrapped_7.setFragment(Optional.of(ntLoop));
		calcNumTrapped_8.setFragment(Optional.of(ntLoop));
		ntAltTop.setIfChildren(Arrays.asList(ntLoop));
		AltCombinedFragment ntAltInLoop = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ntLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(calcNumTrapped_9, calcNumTrapped_10)), 
				Optional.of(Arrays.asList(calcNumTrapped_15)), 
				"(occupiedNT = T) & (blackNT = toCheckNT)", 
				"~((occupiedNT = T) & (blackNT = toCheckNT))", dummy, dummy1, dummy2, store);
		calcNumTrapped_9.setFragment(Optional.of(ntAltInLoop));
		calcNumTrapped_10.setFragment(Optional.of(ntAltInLoop));
		calcNumTrapped_15.setFragment(Optional.of(ntAltInLoop));
		ntLoop.setChildren(Arrays.asList(ntAltInLoop));
		AltCombinedFragment ntAltInner1 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ntAltInLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(calcNumTrapped_11)), 
				Optional.of(Arrays.asList(calcNumTrapped_12)), 
				"neighExistsNT = T", 
				"neighExistsNT = F", dummy, dummy1, dummy2, store);
		calcNumTrapped_11.setFragment(Optional.of(ntAltInner1));
		calcNumTrapped_12.setFragment(Optional.of(ntAltInner1));
		AltCombinedFragment ntAltInner2 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(ntAltInLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(calcNumTrapped_13)), 
				Optional.of(Arrays.asList(calcNumTrapped_14)), 
				"occupiedNT = F", 
				"~(blackNT = toCheckNT)", dummy, dummy1, dummy2, store);
		calcNumTrapped_13.setFragment(Optional.of(ntAltInner2));
		calcNumTrapped_14.setFragment(Optional.of(ntAltInner2));
		ntAltInLoop.setIfChildren(Arrays.asList(ntAltInner1));
		ntAltInLoop.setThenChildren(Arrays.asList(ntAltInner2));
		
		// BACKTRACKFLIP
		
		LoopCombinedFragment btfLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty()
				, Optional.empty(), 
				Optional.of(Arrays.asList(backtrackFlip_4, backtrackFlip_5, backtrackFlip_6, backtrackFlip_7, backtrackFlip_8))
				, "stopBTF = F", dummy, dummy1, store);
		backtrackFlip_4.setFragment(Optional.of(btfLoop));
		backtrackFlip_5.setFragment(Optional.of(btfLoop));
		backtrackFlip_6.setFragment(Optional.of(btfLoop));
		backtrackFlip_7.setFragment(Optional.of(btfLoop));
		backtrackFlip_8.setFragment(Optional.of(btfLoop));
		AltCombinedFragment btfInner = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(btfLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(backtrackFlip_9)), 
				Optional.of(Arrays.asList(backtrackFlip_10)), 
				"toFlipBTF = endBTF", 
				"~(toFlipBTF = endBTF)", dummy, dummy1, dummy2, store);
		backtrackFlip_9.setFragment(Optional.of(btfInner));
		backtrackFlip_10.setFragment(Optional.of(btfInner));
		btfLoop.setChildren(Arrays.asList(btfInner));
		
		// FLIPTILESINDIR
		
		AltCombinedFragment fidAltTop = CombinedFragmentFactory.createAltCombinedFragment(Optional.empty()
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_3, flipTilesInDir_4, flipTilesInDir_5, flipTilesInDir_6, flipTilesInDir_7)), 
				Optional.of(Arrays.asList(flipTilesInDir_23)), 
				"neighborExistsFID = T", 
				"neighborExistsFID = F", dummy, dummy1, dummy2, store);
		flipTilesInDir_3.setFragment(Optional.of(fidAltTop));
		flipTilesInDir_4.setFragment(Optional.of(fidAltTop));
		flipTilesInDir_5.setFragment(Optional.of(fidAltTop));
		flipTilesInDir_6.setFragment(Optional.of(fidAltTop));
		flipTilesInDir_7.setFragment(Optional.of(fidAltTop));
		flipTilesInDir_23.setFragment(Optional.of(fidAltTop));
		AltCombinedFragment fidAltInner = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(fidAltTop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_8)), 
				Optional.of(Arrays.asList(flipTilesInDir_22)), 
				"(occupiedFID = T) & ~(playerFID = blackFID)", 
				"~(occupiedFID = T & ~(playerFID = blackFID))", dummy, dummy1, dummy2, store);
		flipTilesInDir_8.setFragment(Optional.of(fidAltInner));
		flipTilesInDir_22.setFragment(Optional.of(fidAltInner));
		fidAltTop.setIfChildren(Arrays.asList(fidAltInner));
		LoopCombinedFragment fidLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.of(fidAltInner)
				, Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_9)), "stopFID = F", dummy, dummy1, store);
		flipTilesInDir_9.setFragment(Optional.of(fidLoop));
		fidAltInner.setIfChildren(Arrays.asList(fidLoop));
		AltCombinedFragment fidAltLoop1 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(fidLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_10, flipTilesInDir_11, flipTilesInDir_12, flipTilesInDir_13, flipTilesInDir_14)), 
				Optional.of(Arrays.asList(flipTilesInDir_21)), 
				"neighborExistsFID = T", 
				"neighborExistsFID = F", dummy, dummy1, dummy2, store);
		flipTilesInDir_10.setFragment(Optional.of(fidAltLoop1));
		flipTilesInDir_11.setFragment(Optional.of(fidAltLoop1));
		flipTilesInDir_12.setFragment(Optional.of(fidAltLoop1));
		flipTilesInDir_13.setFragment(Optional.of(fidAltLoop1));
		flipTilesInDir_14.setFragment(Optional.of(fidAltLoop1));
		flipTilesInDir_21.setFragment(Optional.of(fidAltLoop1));
		fidLoop.setChildren(Arrays.asList(fidAltLoop1));
		AltCombinedFragment fidAltLoop2 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(fidAltLoop1)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_15, flipTilesInDir_16, flipTilesInDir_17, flipTilesInDir_18)), 
				Optional.empty(), 
				"(occupiedFID = T) & (playerFID = blackFID)", 
				"~((occupiedFID = T) & (playerFID = blackFID))", dummy, dummy1, dummy2, store);
		flipTilesInDir_15.setFragment(Optional.of(fidAltLoop2));
		flipTilesInDir_16.setFragment(Optional.of(fidAltLoop2));
		flipTilesInDir_17.setFragment(Optional.of(fidAltLoop2));
		flipTilesInDir_18.setFragment(Optional.of(fidAltLoop2));
		fidAltLoop1.setIfChildren(Arrays.asList(fidAltLoop2));
		AltCombinedFragment fidAltLoop3 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(fidAltLoop2)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(flipTilesInDir_19)), 
				Optional.of(Arrays.asList(flipTilesInDir_20)), 
				"occupiedFID = F", 
				"~(playerFID = blackFID)", dummy, dummy1, dummy2, store);
		flipTilesInDir_19.setFragment(Optional.of(fidAltLoop3));
		flipTilesInDir_20.setFragment(Optional.of(fidAltLoop3));
		fidAltLoop2.setThenChildren(Arrays.asList(fidAltLoop3));
		
		// PLAY
		
		LoopCombinedFragment playLoop = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty()
				, Optional.empty(), 
				Optional.of(Arrays.asList(play_4)), "skipCountP < 2", dummy, dummy1, store);
		play_4.setFragment(Optional.of(playLoop));
		AltCombinedFragment playAltChild1 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(playLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(play_5, play_6, play_7, play_8, play_9)), 
				Optional.of(Arrays.asList(play_10)), 
				"canPlayP = T", 
				"canPlayP = F", dummy, dummy1, dummy2, store);
		play_5.setFragment(Optional.of(playAltChild1));
		play_6.setFragment(Optional.of(playAltChild1));
		play_7.setFragment(Optional.of(playAltChild1));
		play_8.setFragment(Optional.of(playAltChild1));
		play_9.setFragment(Optional.of(playAltChild1));
		play_10.setFragment(Optional.of(playAltChild1));
		AltCombinedFragment playAltChild2 = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(playLoop)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(play_11)), 
				Optional.of(Arrays.asList(play_18)), 
				"skipCountP < 2", 
				"skipCountP >= 2", dummy, dummy1, dummy2, store);
		play_11.setFragment(Optional.of(playAltChild2));
		play_18.setFragment(Optional.of(playAltChild2));
		playLoop.setChildren(Arrays.asList(playAltChild1, playAltChild2));
		AltCombinedFragment playAltInner = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(playAltChild2)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(play_12, play_13, play_14, play_15, play_16)), 
				Optional.of(Arrays.asList(play_17)), 
				"canPlayP = T", 
				"canPlayP = F", dummy, dummy1, dummy2, store);
		play_12.setFragment(Optional.of(playAltInner));
		play_13.setFragment(Optional.of(playAltInner));
		play_14.setFragment(Optional.of(playAltInner));
		play_15.setFragment(Optional.of(playAltInner));
		play_16.setFragment(Optional.of(playAltInner));
		play_17.setFragment(Optional.of(playAltInner));
		playAltChild2.setIfChildren(Arrays.asList(playAltInner));
		
		// COUNT
		
		LoopCombinedFragment countOuter = CombinedFragmentFactory.createLoopCombinedFragment(Optional.empty()
				, Optional.empty(), 
				Optional.of(Arrays.asList(count_15, count_16)), "xCoC =< xCoMaxC", dummy, dummy1, store);
		count_15.setFragment(Optional.of(countOuter));
		count_16.setFragment(Optional.of(countOuter));
		LoopCombinedFragment countInner = CombinedFragmentFactory.createLoopCombinedFragment(Optional.of(countOuter)
				, Optional.empty(), 
				Optional.of(Arrays.asList(count_7, count_8, count_9, count_10, count_11, count_14)), "yCoC =< yCoMaxC", dummy, dummy1, store);
		count_7.setFragment(Optional.of(countInner));
		count_8.setFragment(Optional.of(countInner));
		count_9.setFragment(Optional.of(countInner));
		count_10.setFragment(Optional.of(countInner));
		count_11.setFragment(Optional.of(countInner));
		count_14.setFragment(Optional.of(countInner));
		countOuter.setChildren(Arrays.asList(countInner));
		AltCombinedFragment countAlt = CombinedFragmentFactory.createAltCombinedFragment(Optional.of(countInner)
				, Optional.empty(), Optional.empty(), 
				Optional.of(Arrays.asList(count_12)), 
				Optional.of(Arrays.asList(count_13)), 
				"(occupiedC = T) & (colorC = blackC)", 
				"~((occupiedC = T) & colorC = blackC))", dummy, dummy1, dummy2, store);
		count_12.setFragment(Optional.of(countAlt));
		count_13.setFragment(Optional.of(countAlt));
		countInner.setChildren(Arrays.asList(countAlt));
		
		store.addAltCombinedFragment(neAlt);
		store.addAltCombinedFragment(rppOuter);
		store.addAltCombinedFragment(rppInner);
		store.addAltCombinedFragment(ipSimple);
		store.addAltCombinedFragment(ipOuter1);
		store.addAltCombinedFragment(ipInner1);
		store.addAltCombinedFragment(ipOuter2);
		store.addAltCombinedFragment(ipInner2);
		store.addAltCombinedFragment(ipOuter3);
		store.addAltCombinedFragment(ipInner3);
		store.addAltCombinedFragment(ipOuter4);
		store.addAltCombinedFragment(ipInner4);
		store.addAltCombinedFragment(ipOuter5);
		store.addAltCombinedFragment(ipInner5);
		store.addAltCombinedFragment(ipOuter6);
		store.addAltCombinedFragment(ipInner6);
		store.addAltCombinedFragment(ipOuter7);
		store.addAltCombinedFragment(ipInner7);
		store.addAltCombinedFragment(ntAltTop);
		store.addAltCombinedFragment(ntAltInLoop);
		store.addAltCombinedFragment(ntAltInner1);
		store.addAltCombinedFragment(ntAltInner2);
		store.addAltCombinedFragment(btfInner);
		store.addAltCombinedFragment(fidAltTop);
		store.addAltCombinedFragment(fidAltInner);
		store.addAltCombinedFragment(fidAltLoop1);
		store.addAltCombinedFragment(fidAltLoop2);
		store.addAltCombinedFragment(fidAltLoop3);
		store.addAltCombinedFragment(playAltChild1);
		store.addAltCombinedFragment(playAltChild2);
		store.addAltCombinedFragment(playAltInner);
		store.addAltCombinedFragment(countAlt);

		store.addLoopCombinedFragment(cpOuter);
		store.addLoopCombinedFragment(rppLoop);
		store.addLoopCombinedFragment(ntLoop);
		store.addLoopCombinedFragment(btfLoop);
		store.addLoopCombinedFragment(fidLoop);
		store.addLoopCombinedFragment(playLoop);
		store.addLoopCombinedFragment(countOuter);
		store.addLoopCombinedFragment(countInner);

		store.addDiagramInfo("calcNeighbor", new DiagramInfo("calcNeighbor", boardCN, Optional.of(Arrays.asList(posCN, xOffCN, yOffCN)), Optional.of(neighborCN)));
		store.addDiagramInfo("neighborExists", new DiagramInfo("neighborExists", boardNE, Optional.of(Arrays.asList(givenNE, xCoTestNE, yCoTestNE)), Optional.of(existsNE)));
		store.addDiagramInfo("canPlay", new DiagramInfo("canPlay", boardCP, Optional.of(Arrays.asList(blackCP)), Optional.of(canPlayCP)));
		store.addDiagramInfo("randomPossiblePos", new DiagramInfo("randomPossiblePos", boardRPP, Optional.of(Arrays.asList(blackRPP)), Optional.of(randomPosRPP)));
		store.addDiagramInfo("isPlayable", new DiagramInfo("isPlayable", boardIP, Optional.of(Arrays.asList(givenIP, blackIP)), Optional.of(isPlayableIP)));
		store.addDiagramInfo("calcNumTrapped", new DiagramInfo("calcNumTrapped", boardNT, Optional.of(Arrays.asList(posNT, blackNT, xOffNT, yOffNT)), Optional.of(numTrappedNT)));
		store.addDiagramInfo("backtrackFlip", new DiagramInfo("backtrackFlip", boardBTF, Optional.of(Arrays.asList(beginBTF, endBTF, xOffSetBTF, yOffSetBTF)), Optional.empty()));
		store.addDiagramInfo("flipTilesInDir", new DiagramInfo("flipTilesInDir", boardFID, Optional.of(Arrays.asList(beginFID, playerFID, xOffSetFID, yOffSetFID)), Optional.empty()));
		store.addDiagramInfo("flipTiles", new DiagramInfo("flipTiles", boardFT, Optional.of(Arrays.asList(posFT)), Optional.empty()));
		store.addDiagramInfo("play", new DiagramInfo("play", boardP, Optional.empty(), Optional.of(neighborCN)));
		store.addDiagramInfo("count", new DiagramInfo("count", boardC, Optional.of(Arrays.asList(blackC)), Optional.of(countC)));
		
		SeqDiagramStore builtStore = new DiagramStoreFactory().makeSeqDiagramStore(store);
		
		SeqFactors fac = new SeqFactors(3, 1, 3, 3, 20);
		
		try
		{
			new TheoryGenerator().generateLTCTheory(builtStore, 
					"hardcodedreversi.idp", fac);
		}
		catch (IllegalArgumentException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
