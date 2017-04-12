package parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import data.AssociationBuilder;
import data.Class;
import data.ComplexType;
import data.Generalization;
import data.NoSuchTypeException;
import data.PrimitiveClass;
import data.PrimitiveType;
import data.Type;
import data.TypeContext;
import data.TypeParameterType;
import data.UserDefinedClassBuilder;
import data.UserDefinedType;

/**
 * This class stores the Class, Association and Generalization objects derived
 * from an XML file generated based on a UML Class Diagram.
 * 
 * @author Thomas
 *
 */
//TODO access methods for specific classes and associations
public class SymbolStore implements TypeContext
{
	public SymbolStore()
	{
		this.classesInProgress = new HashMap<String, UserDefinedClassBuilder>();
		this.associationsInProgress = new HashMap<String, AssociationBuilder>();
		this.generalizations = new HashSet<Generalization>();
	}

	private final Map<String, UserDefinedClassBuilder> classesInProgress;

	private Map<String, UserDefinedClassBuilder> internalGetClassesInProgress()
	{
		return this.classesInProgress;
	}

	/**
	 * 
	 * @return An unmodifiable view of the classes in progress
	 */
	public Map<String, UserDefinedClassBuilder> getClassesInProgress()
	{
		return Collections.unmodifiableMap(this.internalGetClassesInProgress());
	}
	
	/**
	 * Create a new Class object with the given id and name and add it to the classes in progress
	 * 
	 * @param id
	 * @param name
	 * @throws IllegalArgumentException
	 * 		id == null || id.equals("") || name == null || name.equals("")
	 */
	public void addClass(String id, String name) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (id.equals(""))
		{
			throw new IllegalArgumentException("id cannot be empty");
		}
		if (name == null)
		{
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.equals(""))
		{
			throw new IllegalArgumentException("name cannot be empty");
		}
		
		this.internalGetClassesInProgress().put(id, new UserDefinedClassBuilder(name));
	}
	
	public Optional<UserDefinedClassBuilder> getClass(String id)
	{
		return Optional.ofNullable(this.internalGetClassesInProgress().get(id));
	}

	private final Map<String, AssociationBuilder> associationsInProgress;

	private Map<String, AssociationBuilder> internalGetAssociationsInProgress()
	{
		return this.associationsInProgress;
	}

	/**
	 * 
	 * @return An unmodifiable view of the associations in progress
	 */
	public Map<String, AssociationBuilder> getAssociationsInProgress()
	{
		return Collections.unmodifiableMap(this.internalGetAssociationsInProgress());
	}
	
	/**
	 * Creates a new AssociationBuilder object with the given id
	 * 
	 * @param id
	 * @throws IllegalArgumentException
	 * 		id == null || id.equals("")
	 */
	public void addAssociation(String id) throws IllegalArgumentException
	{
		if (id == null)
		{
			throw new IllegalArgumentException("id cannot be null");
		}
		if (id.equals(""))
		{
			throw new IllegalArgumentException("id cannot be empty");
		}
		
		this.internalGetAssociationsInProgress().put(id, new AssociationBuilder());
	}
	
	public Optional<AssociationBuilder> getAssociation(String id)
	{
		return Optional.ofNullable(this.internalGetAssociationsInProgress().get(id));
	}

	private final Set<Generalization> generalizations;

	private Set<Generalization> internalGetGeneralizations()
	{
		return this.generalizations;
	}

	/**
	 * 
	 * @return An unmodifiable view of the generalizations
	 */
	public Set<Generalization> getGeneralizations()
	{
		return Collections.unmodifiableSet(this.internalGetGeneralizations());
	}
	
	/**
	 * 
	 * @param generalization
	 * @throws IllegalArgumentException
	 * 		generalization == null
	 */
	public void addGeneralization(Generalization generalization) throws IllegalArgumentException
	{
		if (generalization == null)
		{
			throw new IllegalArgumentException("generalization cannot be null");
		}
		
		this.internalGetGeneralizations().add(generalization);
	}

	private boolean checkIsPrimitiveType(Type type)
	{
		return type instanceof PrimitiveType;
	}

	private String getTypeName(Type type) throws NoSuchTypeException
	{
		if (! this.internalGetClassesInProgress().containsKey(type.getID()))
		{
			throw new NoSuchTypeException("did not have type with id: " + type.getID());
		}
		
		return this.internalGetClassesInProgress().get(type.getID()).getName();
	}
	
	private UserDefinedClassBuilder getType(Type type) throws NoSuchTypeException
	{
		if (! this.internalGetClassesInProgress().containsKey(type.getID()))
		{
			throw new NoSuchTypeException("did not have type with id: " + type.getID());
		}
		
		return this.internalGetClassesInProgress().get(type.getID());
	}

	@Override
	public String resolveName(ComplexType type) throws NoSuchTypeException
	{
		String typeName = this.getTypeName(type);
		if (type.getMultiplicity().getLowerBound() == 0 && type.getMultiplicity().getUpperBound() == 1)
		{
			return "Optional<" + typeName + ">";
		}
		else if (type.getMultiplicity().getLowerBound() == 1 && type.getMultiplicity().getUpperBound() == 1)
		{
			return typeName;
		}
		else // upperBound is always at least as large as lowerBound, so
				// distinguish between collection types now
		{
			return type.getMultiplicity().getCollectionType() + "<" + typeName + ">";
		}
	}

	@Override
	public String resolveName(PrimitiveType type) throws NoSuchTypeException
	{
		return type.getName();
	}

	@Override
	public String resolveName(UserDefinedType type) throws NoSuchTypeException
	{
		return this.getTypeName(type);
	}

	@Override
	public String resolveName(TypeParameterType type) throws NoSuchTypeException
	{
		return this.getTypeName(type);
	}

	@Override
	public Class resolve(ComplexType type) throws NoSuchTypeException
	{
		return this.getType(type);
	}

	@Override
	public Class resolve(UserDefinedType type) throws NoSuchTypeException
	{
		return this.getType(type);
	}

	@Override
	public Class resolve(TypeParameterType type) throws NoSuchTypeException
	{
		return this.getType(type);
	}

	@Override
	public Class resolve(PrimitiveType type) throws NoSuchTypeException
	{
		switch(type)
		{
			case BOOLEAN :
				return PrimitiveClass.BOOLEAN;
			case BYTE :
				return PrimitiveClass.BYTE;
			case CHAR : 
				return PrimitiveClass.CHAR;
			case DOUBLE :
				return PrimitiveClass.DOUBLE;
			case FLOAT :
				return PrimitiveClass.FLOAT;
			case INTEGER :
				return PrimitiveClass.INTEGER;
			case LONG:
				return PrimitiveClass.LONG;
			case SHORT:
				return PrimitiveClass.SHORT;
			case STRING :
				return PrimitiveClass.STRING;
			case VOID :
				return PrimitiveClass.VOID;
		}
		throw new IllegalStateException("could not find primitive type"); // should never reach here
	}

	@Override
	public boolean canResolve(Type type)
	{
		return this.checkIsPrimitiveType(type) || this.internalGetClassesInProgress().containsKey(type.getID());
	}

}
