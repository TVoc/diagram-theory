package theory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.classdiagrams.Association;
import data.classdiagrams.AssociationBuilder;
import data.classdiagrams.Class;
import data.classdiagrams.Generalization;
import data.classdiagrams.UserDefinedClassBuilder;
import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.Message;
import data.sequencediagrams.TempVar;

import java.util.Set;

import parser.SeqSymbolStore;
import parser.SymbolStore;

public class DiagramStoreFactory
{
	/**
	 * 
	 * @param store
	 * @return	A new DiagramStore that contains the finalized versions of the given store's classes, associations and generalizations
	 * @throws IllegalArgumentException
	 * 		store == null
	 */
	public DiagramStore makeDiagramStore(SymbolStore store) throws IllegalArgumentException
	{
		if (store == null)
		{
			throw new IllegalArgumentException("store cannot be null");
		}
		
		Map<String,Class> classes = new HashMap<String,Class>();
		Set<Association> associations = new HashSet<Association>();
		Set<Generalization> generalizations = new HashSet<Generalization>();
		
		for (Entry<String,UserDefinedClassBuilder> clazz : store.getClassesInProgress().entrySet())
		{
			classes.put(clazz.getKey(), clazz.getValue().build());
		}
		
		for (AssociationBuilder association : store.getAssociationsInProgress().values())
		{
			associations.add(association.build());
		}
		
		generalizations.addAll(store.getGeneralizations());
		
		store.reset();
		
		return new DiagramStore(classes, associations, generalizations);
	}
	
	public SeqDiagramStore makeSeqDiagramStore(SeqSymbolStore store) throws IllegalArgumentException
	{
		if (store == null)
		{
			throw new IllegalArgumentException("store cannot be null");
		}
		
		Map<String,Class> classes = new HashMap<String,Class>();
		Set<Association> associations = new HashSet<Association>();
		Set<Generalization> generalizations = new HashSet<Generalization>();
		Map<String,TempVar> tempVars = new HashMap<String,TempVar>();
		List<Message> messages = new ArrayList<Message>();
		List<AltCombinedFragment> alts = new ArrayList<AltCombinedFragment>();
		List<LoopCombinedFragment> loops = new ArrayList<LoopCombinedFragment>();
		
		for (Entry<String,UserDefinedClassBuilder> clazz : store.getClassesInProgress().entrySet())
		{
			classes.put(clazz.getKey(), clazz.getValue().build());
		}
		
		for (AssociationBuilder association : store.getAssociationsInProgress().values())
		{
			associations.add(association.build());
		}
		
		generalizations.addAll(store.getGeneralizations());
		tempVars.putAll(store.getTempVars());
		messages.addAll(store.getMessages());
		alts.addAll(store.getAltCombinedFragments());
		loops.addAll(store.getLoopCombinedFragments());
		
		store.reset();
		
		return new SeqDiagramStore(classes, associations, generalizations, tempVars, messages, alts, loops);
	}
}
