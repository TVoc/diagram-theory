package parser;

import java.util.Collections;
import java.util.Map;

import data.Class;
import data.ComplexType;
import data.NoSuchTypeException;
import data.PrimitiveType;
import data.Type;
import data.TypeContext;
import data.TypeParameterType;
import data.UserDefinedClass;
import data.UserDefinedClassBuilder;
import data.UserDefinedType;

/**
 * This class stores the Class, Association and Generalization objects derived from an XML file generated based on a UML Class Diagram.
 * 
 * @author Thomas
 *
 */
//TODO separate into 'temporary' and 'definite' store
public class SymbolStore implements TypeContext {
	
	private final Map<String,UserDefinedClassBuilder> classesInProgress;
	
	private Map<String,UserDefinedClassBuilder> internalGetClassesInProgress()
	{
		return this.classesInProgress;
	}
	
	/**
	 * 
	 * @return	An unmodifiable view of the classes in progress
	 */
	public Map<String,UserDefinedClassBuilder> getClassesInProgress()
	{
		return Collections.unmodifiableMap(this.internalGetClassesInProgress());
	}
	
	private final Map<String,UserDefinedClass> classes;
	
	private Map<String,UserDefinedClass> internalGetClasses()
	{
		return this.classes;
	}
	
	/**
	 * 
	 * @return	An unmodifiable view of the classes
	 */
	public Map<String,UserDefinedClass> getClasses()
	{
		return Collections.unmodifiableMap(this.internalGetClasses());
	}
	
	private final Map
	
	public boolean hasType(Type type)
	{
		return checkIsPrimitiveType(type)
				|| this.internalGetClassesInProgress().containsKey(type.getID())
				|| this.internalGetClasses().containsKey(type);
	}
	
	private boolean checkIsPrimitiveType(Type type)
	{
		return type instanceof PrimitiveType;
	}

	@Override
	public String resolveName(ComplexType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resolveName(PrimitiveType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resolveName(UserDefinedType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resolveName(TypeParameterType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class resolve(ComplexType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class resolve(UserDefinedType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class resolve(TypeParameterType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class resolve(PrimitiveType type) throws NoSuchTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canResolve(Type type)
	{
		return this.checkIsPrimitiveType(type)
				|| this.internalGetClasses().containsKey(type.getID());
	}
	
	
}
