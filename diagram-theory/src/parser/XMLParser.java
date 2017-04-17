package parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import data.AssociationEnd;
import data.ComplexType;
import data.DataUnit;
import data.Generalization;
import data.Multiplicity;
import data.MultiplicityFactory;
import data.Operation;
import data.PrimitiveType;
import data.Type;
import data.TypeParameterType;
import data.UserDefinedType;
import theory.DiagramStore;
import theory.DiagramStoreFactory;

public class XMLParser
{

	public static final String ISORDERED_DEFAULT = "false";
	public static final String ISUNIQUE_DEFAULT = "true";
	
	public static void main(String[] args)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		try
		{
			Document doc = saxBuilder.build("C:\\Users\\Thomas\\Desktop\\Werk stuff\\Univ\\Thesis\\voorbeeld\\project.xml");
			XMLParser parser = new XMLParser();
			parser.parseModel(doc.getRootElement().getChild("Models"));
		}
		catch (JDOMException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void parseModel(Element models)
	{
		SymbolStore store = new SymbolStore();
		
		for (Element element : models.getChildren())
		{
			if (element.getName().equals("Class"))
			{
				this.parseClass(element, store);
			}
			if (element.getName().equals("ModelRelationshipContainer"))
			{
				for (Element child : element.getChild("ModelChildren").getChildren())
				{
					if (child.getAttributeValue("Name").equals("Association"))
					{
						for (Element assoc : element.getChild("ModelChildren").getChild("ModelRelationshipContainer").getChild("ModelChildren").getChildren())
						{
							this.parseAssociation(assoc, store);
						}
					}
					if (child.getAttributeValue("Name").equals("Generalization"))
					{
						for (Element gen : child.getChild("ModelChildren").getChildren())
						{
							this.parseGeneralization(gen, store);
						}
					}
				}
			}
		}
		
		DiagramStore diagramStore = new DiagramStoreFactory().makeDiagramStore(store);
		System.out.println(new ReflectionToStringBuilder(diagramStore, ToStringStyle.MULTI_LINE_STYLE).toString());
	}
	
	private void parseClass(Element element, SymbolStore store)
	{
		String classId = element.getAttributeValue("Id");
		String className = element.getAttributeValue("Name");
		
		store.addClass(classId, className);
		
		Element classElements = element.getChild("ModelChildren");
		
		if (classElements == null)
		{
			return;
		}
		
		for (Element classElement : classElements.getChildren())
		{
			if (classElement.getName().equals("Attribute"))
			{
				DataUnit attribute = this.parseDataUnit(classElement);
				store.getClass(classId).get().addAttribute(attribute);
			}
			else if (classElement.getName().equals("Operation"))
			{
				Operation operation = this.parseOperation(classElement);
				store.getClass(classId).get().addOperation(operation);
			}
		}
	}

	private DataUnit parseDataUnit(Element element)
	{
		String name = element.getAttributeValue("Name");
		String multiplicitySpec = element.getAttributeValue("Multiplicity");
		String isOrdered = ISORDERED_DEFAULT;
		String isUnique = ISUNIQUE_DEFAULT;

		Type type = null;
		
		for (Element child : element.getChildren())
		{
			if (child.getName().equals("MultiplicityDetail"))
			{
				isOrdered = child.getChild("Multiplicity").getAttributeValue("Ordered");
				isUnique = child.getChild("Multiplicity").getAttributeValue("Unique");
			}
			else if (child.getName().equals("Type"))
			{
				for (Element typeEle : child.getChildren())
				{
					if (typeEle.getName().equals("DataType"))
					{
						type = PrimitiveType.getType(typeEle.getAttributeValue("Name"));
					}
					else if (typeEle.getName().equals("Class"))
					{
						type = new UserDefinedType(typeEle.getAttributeValue("Idref"));
					}
				}
			}
		}
		
		Multiplicity multiplicity = MultiplicityFactory.parseMultiplicity(multiplicitySpec, isOrdered, isUnique);
		
		return new DataUnit(name, type, Optional.of(multiplicity));
	}
	
	private DataUnit parseParameter(Element element)
	{
		String name = element.getAttributeValue("Name");
		String multiplicitySpec = element.getAttributeValue("Multiplicity");
		String isOrdered = ISORDERED_DEFAULT;
		String isUnique = ISUNIQUE_DEFAULT;

		Type type = null;
		
		for (Element child : element.getChildren())
		{
			if (child.getName().equals("MultiplicityDetail"))
			{
				isOrdered = child.getChild("Multiplicity").getAttributeValue("Ordered");
				isUnique = child.getChild("Multiplicity").getAttributeValue("Unique");
			}
			else if (child.getName().equals("Type"))
			{
				for (Element typeEle : child.getChildren())
				{
					if (typeEle.getName().equals("DataType"))
					{
						type = PrimitiveType.getType(typeEle.getAttributeValue("Name"));
					}
					else if (typeEle.getName().equals("Class"))
					{
						type = new UserDefinedType(typeEle.getAttributeValue("Idref"));
					}
				}
			}
			else if (child.getName().equals("TemplateTypeBindInfo"))
			{
				ElementFilter filter = new ElementFilter("BindedType");
				
				for (Element descendant : child.getDescendants(filter))
				{
					type = new TypeParameterType(descendant.getChild("Class").getAttributeValue("Idref"));
				}
			}
		}
		
		Multiplicity multiplicity = MultiplicityFactory.parseMultiplicity(multiplicitySpec, isOrdered, isUnique);
		
		return new DataUnit(name, type, Optional.of(multiplicity));
	}
	
	private Operation parseOperation(Element element)
	{
		String name = element.getAttributeValue("Name");
		
		DataUnit returnType = null;
		List<DataUnit> parameters = new LinkedList<DataUnit>();
		
		for (Element child : element.getChildren())
		{
			if (child.getName().equals("ReturnType"))
			{
				returnType = determineReturnType(child, Optional.empty());
			}
			if (child.getName().equals("TemplateTypeBindInfo"))
			{				
				ElementFilter filter = new ElementFilter("BindedType");
				
				for (Element descendant : child.getDescendants(filter))
				{
					returnType = determineReturnType(descendant, Optional.of(child.getChild("TemplateTypeBindInfo")));
				}
				
			}
			if (child.getName().equals("ModelChildren"))
			{
				for (Element modelChild : child.getChildren())
				{
					if (modelChild.getName().equals("Parameter"))
					{
						parameters.add(this.parseParameter(modelChild));
					}
				}
			}
		}
		
		Optional<List<DataUnit>> parameterList = parameters.isEmpty() ? Optional.empty() : Optional.of(parameters);
		return new Operation(name, returnType, parameterList);
	}

	private DataUnit determineReturnType(Element child, Optional<Element> complexReturnElement)
	{
		if (child.getChild("DataType") != null)
		{
			if (complexReturnElement.isPresent())
			{
				Multiplicity multiplicity = Multiplicity.descriptionToMultiplicity(complexReturnElement.get().getAttributeValue("BindedType"));
				ComplexType theReturn = new ComplexType(new UserDefinedType(child.getChild("DataType").getAttributeValue("Idref")), multiplicity);
				return new DataUnit("return", theReturn, Optional.of(multiplicity));
			}
			else
			{
				PrimitiveType theReturn = PrimitiveType.getType(child.getChild("DataType").getAttributeValue("Name"));
				return new DataUnit("return", theReturn, Optional.empty());
			}
			
		}
		else if (child.getChild("Class") != null)
		{
			if (complexReturnElement.isPresent())
			{
				Multiplicity multiplicity = Multiplicity.descriptionToMultiplicity(complexReturnElement.get().getAttributeValue("BindedType"));
				ComplexType theReturn;
				if (child.getChild("Class") != null)
				{
					theReturn = new ComplexType(new UserDefinedType(child.getChild("Class").getAttributeValue("Idref")), multiplicity);
				}
				else
				{
					theReturn = new ComplexType(new UserDefinedType(child.getChild("DataType").getAttributeValue("Idref")), multiplicity);
				}
				return new DataUnit("return", theReturn, Optional.of(multiplicity));
			}
			UserDefinedType theReturn = new UserDefinedType(child.getChild("Class").getAttributeValue("Idref"));
			return new DataUnit("return", theReturn, Optional.empty());
		}
		else
		{
			throw new IllegalArgumentException("specified return type was not either DataType or Class");
		}
	}
	
	private void parseAssociation(Element ele, SymbolStore store)
	{
		if (ele.getChild("FromEnd").getChild("AssociationEnd").getChild("Type").getChild("NARY") != null)
		{
			this.parseNaryAssociation(ele.getChild("FromEnd").getChild("AssociationEnd").getChild("Type").getChild("NARY")
					, ele.getChild("ToEnd").getChild("AssociationEnd").getChild("Type").getChild("Class")
					, ele.getChild("ToEnd").getChild("AssociationEnd").getAttributeValue("Multiplicity")
					, ele.getChild("ToEnd").getChild("AssociationEnd").getAttributeValue("Name")
					, store);
		}
		else if (ele.getChild("ToEnd").getChild("AssociationEnd").getChild("Type").getChild("NARY") != null)
		{
			this.parseNaryAssociation(ele.getChild("ToEnd").getChild("AssociationEnd").getChild("Type").getChild("NARY")
					, ele.getChild("FromEnd").getChild("AssociationEnd").getChild("Type").getChild("Class")
					, ele.getChild("FromEnd").getChild("AssociationEnd").getAttributeValue("Multiplicity")
					, ele.getChild("FromEnd").getChild("AssociationEnd").getAttributeValue("Name")
					, store);
		}
		else
		{
			this.parseBinaryAssociation(ele, store);
		}
	}
	
	private void parseBinaryAssociation(Element ele, SymbolStore store)
	{
		String fromMultiplicityDesc = ele.getChild("FromEnd").getChild("AssociationEnd").getAttributeValue("Multiplicity");
		String fromRoleName = ele.getChild("FromEnd").getChild("AssociationEnd").getAttributeValue("Name");
		Optional<String> fromRoleNameOpt = Optional.ofNullable(fromRoleName);
		UserDefinedType fromType = new UserDefinedType(ele.getChild("FromEnd").getChild("AssociationEnd").getChild("Type").getChild("Class").getAttributeValue("Idref"));
		
		String toMultiplicityDesc = ele.getChild("ToEnd").getChild("AssociationEnd").getAttributeValue("Multiplicity");
		String toRoleName = ele.getChild("ToEnd").getChild("AssociationEnd").getAttributeValue("Name");
		Optional<String> toRoleNameOpt = Optional.ofNullable(toRoleName);
		UserDefinedType toType = new UserDefinedType(ele.getChild("ToEnd").getChild("AssociationEnd").getChild("Type").getChild("Class").getAttributeValue("Idref"));
		
		String assId = ele.getAttributeValue("Id");
		
		store.addAssociation(assId);
		store.getAssociation(assId).get().addAssociationEnd(new AssociationEnd(fromType, fromRoleNameOpt, Optional.of(MultiplicityFactory.parseMultiplicity(fromMultiplicityDesc, ISORDERED_DEFAULT, ISUNIQUE_DEFAULT))));
		store.getAssociation(assId).get().addAssociationEnd(new AssociationEnd(toType, toRoleNameOpt, Optional.of(MultiplicityFactory.parseMultiplicity(toMultiplicityDesc, ISORDERED_DEFAULT, ISUNIQUE_DEFAULT))));
	}
	
	private void parseNaryAssociation(Element nAryEnd, Element classEnd, String multiplicityDesc, String roleName, SymbolStore store)
	{
		if (! store.hasAssociation(nAryEnd.getAttributeValue("Idref")))
		{
			store.addAssociation(nAryEnd.getAttributeValue("Idref"));
		}
		
		Optional<String> roleNameOpt = Optional.ofNullable(roleName);
		AssociationEnd assEnd = new AssociationEnd(new UserDefinedType(classEnd.getAttributeValue("Idref"))
				, roleNameOpt
				, Optional.of(MultiplicityFactory.parseMultiplicity(multiplicityDesc, ISORDERED_DEFAULT, ISUNIQUE_DEFAULT)));
		store.getAssociation(nAryEnd.getAttributeValue("Idref")).get().addAssociationEnd(assEnd);
	}
	
	private void parseGeneralization(Element ele, SymbolStore store)
	{
		UserDefinedType superType = new UserDefinedType(ele.getAttributeValue("From"));
		UserDefinedType subType = new UserDefinedType(ele.getAttributeValue("To"));
		
		store.addGeneralization(new Generalization(superType, subType));
	}
}
