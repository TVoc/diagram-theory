package data;

/**
 * When given a type, a TypeContext attempts to resolve it to a type's true
 * name.
 * 
 * @author Thomas
 *
 */
public abstract class TypeContext
{

	/**
	 * 
	 * @param type
	 * @return The type's true name
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract String resolveName(ComplexType type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return The type's true name
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract String resolveName(PrimitiveType type) throws NoSuchTypeException;

	/**
	 * 
	 * @param type
	 * @return The type's true name
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract String resolveName(UserDefinedType type) throws NoSuchTypeException;

	/**
	 * 
	 * @param type
	 * @return The type's true name
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract String resolveName(TypeParameterType type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	The Class object corresponding to type
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract Class resolve(ComplexType type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	The Class object corresponding to type
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract Class resolve(UserDefinedType type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	The Class object corresponding to type
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
	public abstract Class resolve(TypeParameterType type) throws NoSuchTypeException;
	
	/**
	 * 
	 * @param type
	 * @return	The Class object corresponding to type
	 * @throws NoSuchTypeException
	 * 		This type context cannot resolve type
	 */
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
	
	/**
	 * Determine if this complex type is an Optional or a collection type.
	 * 
	 * @param type
	 * @param typeName
	 * @return	A name of type ComplexType<T>, where ComplexType is either Optional or a collection type
	 */
	protected String determineComplexType(ComplexType type, String typeName)
	{
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
	
	/**
	 * 
	 * @param type
	 * @return	This type context can resolve type
	 */
	public abstract boolean canResolve(Type type);
	
	protected boolean checkIsPrimitiveType(Type type)
	{
		return type instanceof PrimitiveType;
	}	

}
