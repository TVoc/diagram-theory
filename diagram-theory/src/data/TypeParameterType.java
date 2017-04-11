package data;

/**
 * Expresses operation parameters and return types of the sort <T extends Type>
 * 
 * @author Thomas
 *
 */
public class TypeParameterType implements Type
{

	@Override
	public String getTypeName(TypeContext context) throws NoSuchTypeException
	{
		return context.resolveName(this);
	}

	@Override
	public Class getType(TypeContext context) throws NoSuchTypeException
	{
		return context.resolve(this);
	}

}
