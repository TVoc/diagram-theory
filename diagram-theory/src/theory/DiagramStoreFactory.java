package theory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.Association;
import data.AssociationBuilder;
import data.Generalization;
import data.UserDefinedClassBuilder;
import data.Class;
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
}