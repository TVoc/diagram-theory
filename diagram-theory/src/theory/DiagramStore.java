package theory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import data.Association;
import data.Class;
import data.ComplexType;
import data.Generalization;
import data.NoSuchTypeException;
import data.PrimitiveType;
import data.Type;
import data.TypeContext;
import data.TypeParameterType;
import data.UserDefinedClassBuilder;
import data.UserDefinedType;

/**
 * Contains all classes, associations and generalizations read from a UML diagram
 * 
 * @author Thomas
 *
 */
public class DiagramStore extends TypeContext
{
	
	/**
	 * 
	 * @param classes
	 * @param associations
	 * @param generalizations
	 * @throws IllegalArgumentException
	 * 		classes == null || associations == null || generalizations == null
	 */
	DiagramStore(Map<String,Class> classes, Set<Association> associations, Set<Generalization> generalizations)
		throws IllegalArgumentException
	{
		if (classes == null)
		{
			throw new IllegalArgumentException("classes cannot be null");
		}
		if (associations == null)
		{
			throw new IllegalArgumentException("associations cannot be null");
		}
		if (generalizations == null)
		{
			throw new IllegalArgumentException("generalizations cannot be null");
		}
		
		this.classes = classes;
		this.associations = associations;
		this.generalizations = generalizations;
	}

	private final Map<String,Class> classes;
	
	private Map<String,Class> internalGetClasses()
	{
		return this.classes;
	}
	
	/**
	 * 
	 * @return
	 * 		An unmodifiable view of the classes
	 */
	public Map<String,Class> getClasses()
	{
		return Collections.unmodifiableMap(this.internalGetClasses());
	}
	
	private final Set<Association> associations;
	
	private Set<Association> internalGetAssociations()
	{
		return this.associations;
	}
	
	/**
	 * 
	 * @return	An unmodifiable view of the associations
	 */
	public Set<Association> getAssociations()
	{
		return Collections.unmodifiableSet(this.internalGetAssociations());
	}
	
	private final Set<Generalization> generalizations;
	
	private Set<Generalization> internalGetGeneralizations()
	{
		return this.generalizations;
	}
	
	/***
	 * 
	 * @return	An unmodifiable view of the generalizations
	 */
	public Set<Generalization> getGeneralizations()
	{
		return Collections.unmodifiableSet(this.internalGetGeneralizations());
	}
	
	private String getTypeName(Type type) throws NoSuchTypeException
	{
		if (! this.internalGetClasses().containsKey(type.getID()))
		{
			throw new NoSuchTypeException("did not have type with id: " + type.getID());
		}
		
		return this.internalGetClasses().get(type.getID()).getName();
	}
	
	private Class getType(Type type) throws NoSuchTypeException
	{
		if (! this.internalGetClasses().containsKey(type.getID()))
		{
			throw new NoSuchTypeException("did not have type with id: " + type.getID());
		}
		
		return this.internalGetClasses().get(type.getID());
	}
	
	@Override
	public String resolveName(ComplexType type) throws NoSuchTypeException
	{
		String typeName = this.getTypeName(type);
		return this.determineComplexType(type, typeName);
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
		return "<? extends " + this.getTypeName(type) + ">";
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
	public boolean canResolve(Type type)
	{
		return this.checkIsPrimitiveType(type) || this.internalGetClasses().containsKey(type.getID());
	}

}
