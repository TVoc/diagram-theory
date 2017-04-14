package parser;

import java.io.IOException;
import java.util.Optional;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import data.DataUnit;
import data.Multiplicity;
import data.MultiplicityFactory;
import data.Operation;
import data.PrimitiveType;
import data.Type;
import data.UserDefinedType;

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
		}
		
		System.out.println("done");
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
				DataUnit attribute = this.parseClassAttribute(classElement);
				store.getClass(classId).get().addAttribute(attribute);
			}
			else if (classElement.getName().equals("Operation"))
			{
				
			}
		}
	}

	private DataUnit parseClassAttribute(Element element)
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
	
	private Operation parseOperation(Element element)
	{
		
	}
}
