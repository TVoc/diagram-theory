package theory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import data.classdiagrams.Association;
import data.classdiagrams.Class;
import data.classdiagrams.ComplexType;
import data.classdiagrams.Generalization;
import data.classdiagrams.NoSuchTypeException;
import data.classdiagrams.PrimitiveClass;
import data.classdiagrams.PrimitiveType;
import data.classdiagrams.Type;
import data.classdiagrams.TypeContext;
import data.classdiagrams.TypeParameterType;
import data.classdiagrams.UserDefinedClassBuilder;
import data.classdiagrams.UserDefinedType;

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
	DiagramStore(Map<String,Class> classes, Map<String,Class> classesByName, Set<Association> associations, Set<Generalization> generalizations)
		throws IllegalArgumentException
	{
		if (classes == null)
		{
			throw new IllegalArgumentException("classes cannot be null");
		}
		if (classesByName == null)
		{
			throw new IllegalArgumentException("classesByName cannot be null");
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
		this.classesByName = classesByName;
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
	
	private final Map<String,Class> classesByName;
	
	private Map<String,Class> internalGetClassesByName()
	{
		return this.classesByName;
	}
	
	public Map<String,Class> getClassesByName()
	{
		return Collections.unmodifiableMap(this.internalGetClassesByName());
	}
	
	public boolean hasClass(String name)
	{
		return this.internalGetClasses().containsKey(name);
	}
	
	public boolean hasClassName(String name)
	{
		return this.internalGetClassesByName().containsKey(name);
	}
	
	public Class getClassByName(String name) throws IllegalArgumentException
	{
		if (! this.hasClassName(name))
		{
			throw new IllegalArgumentException("did not have class by given name: " + name);
		}
		
		return this.internalGetClassesByName().get(name);
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
		if (PrimitiveType.isPrimitiveType(type.getID()))
		{
			return type.getID();
		}
		
		if (! this.internalGetClasses().containsKey(type.getID()))
		{
			throw new NoSuchTypeException("did not have type with id: " + type.getID());
		}
		
		return this.internalGetClasses().get(type.getID()).getName();
	}
	
	private Class getType(Type type) throws NoSuchTypeException
	{
		if (PrimitiveType.isPrimitiveType(type.getID()))
		{
			return PrimitiveClass.getType(type.getID());
		}
		
		
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
		return "Class<" + this.getTypeName(type) + ">";
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

	public String toString()
	{
		return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
