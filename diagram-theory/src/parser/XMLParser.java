package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import data.classdiagrams.AssociationEnd;
import data.classdiagrams.ComplexType;
import data.classdiagrams.DataUnit;
import data.classdiagrams.Generalization;
import data.classdiagrams.Multiplicity;
import data.classdiagrams.MultiplicityFactory;
import data.classdiagrams.Operation;
import data.classdiagrams.PrimitiveType;
import data.classdiagrams.Type;
import data.classdiagrams.TypeParameterType;
import data.classdiagrams.UserDefinedType;
import data.classdiagrams.Class;
import data.sequencediagrams.AltCombinedFragment;
import data.sequencediagrams.AltCombinedFragmentBuilder;
import data.sequencediagrams.DiagramInfoBuilder;
import data.sequencediagrams.LoopCombinedFragment;
import data.sequencediagrams.LoopCombinedFragmentBuilder;
import data.sequencediagrams.Message;
import data.sequencediagrams.MessageBuilder;
import data.sequencediagrams.TempVar;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import theory.DiagramStore;
import theory.DiagramStoreFactory;
import theory.Factors;
import theory.OutputConvenienceFunctions;
import theory.SeqDiagramStore;
import theory.SeqFactors;
import theory.TheoryGenerator;
import theory.theory.sequencediagrams.TempVarCausationBuilder;

public class XMLParser
{

	public static final String ISORDERED_DEFAULT = "false";
	public static final String ISUNIQUE_DEFAULT = "true";
	
	public static final double STRINGFACTOR_DEFAULT = 1;
	public static final double INTFACTOR_DEFAULT = 1;
	public static final double FLOATFACTOR_DEFAULT = 1;
	public static final int TIMESTEPS_DEFAULT = 21;
	
	public static final boolean SEQ = true;
	
	/**
	 * In an expression such as one=two+three, remove all operators to get the names of the temp vars
	 */
	public static final String TEMPVAR_SEPARATOR =  "\\+|\\-|\\&|\\||\\!|(\\()+|(\\))+|\\=\\<|\\>\\=|\\<|\\>|=|~";
	
	public static void main(String[] args) throws ParseException
	{
		Options options = new Options();
		options.addRequiredOption("numobjects", "Number of objects", true, "Number of objects in the generated structure");
		options.addOption("stringfactor", true, "Number of strings in generated structure will be ceil(stringfactor * numobjects); default is 1");
		options.addOption("intfactor", true, "Number of ints in generated structure will be ceil(intfactor * numobjects); default is 1");
		options.addOption("floatfactor", true, "Number of floats in generated structure will be ceil(floatfactor * numobjects; default is 1");
		options.addOption("timesteps", true, "Number of time steps that the generated theory will range over; default is 21");
		options.addOption("stacklevels", true, "Number of stack levels that will be available in the theory; default is 10");
		
		int numObjects = 0;
		double stringFactor = STRINGFACTOR_DEFAULT;
		double intFactor = INTFACTOR_DEFAULT;
		double floatFactor = FLOATFACTOR_DEFAULT;
		int timeSteps = TIMESTEPS_DEFAULT;
		
		try
		{
			CommandLine cmd = new DefaultParser().parse(options, args);
			
			numObjects = Integer.parseInt(cmd.getOptionValue("numobjects"));
			
			if (cmd.hasOption("stringfactor"))
			{
				stringFactor = Double.parseDouble(cmd.getOptionValue("stringfactor"));
			}
			if (cmd.hasOption("intfactor"))
			{
				intFactor = Double.parseDouble(cmd.getOptionValue("intfactor"));
			}
			if (cmd.hasOption("floatfactor"))
			{
				floatFactor = Double.parseDouble(cmd.getOptionValue("floatfactor"));
			}
			if (cmd.hasOption("timesteps"))
			{
				timeSteps = Integer.parseInt(cmd.getOptionValue("timesteps"));
			}
		}
		catch (ParseException e)
		{
			throw e;
		}
		
		//Factors factors = new Factors(numObjects, stringFactor, intFactor, floatFactor);
		SeqFactors seqFactors = new SeqFactors(numObjects, stringFactor, intFactor, floatFactor, timeSteps);
		
		SAXBuilder saxBuilder = new SAXBuilder();
		try
		{
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "XML Files", "xml");
		    chooser.setFileFilter(filter);
		    chooser.showOpenDialog(null);
			
			Document doc = saxBuilder.build(chooser.getSelectedFile());

			XMLParser parser = new XMLParser();
			parser.parseModel(doc.getRootElement().getChild("Models"), seqFactors);
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
	
	private void parseModel(Element models, Factors factors) throws IOException
	{
		SymbolStore store;
		if (SEQ)
		{
			store = new SeqSymbolStore();
		}
		else
		{
			store = new SymbolStore();
		}
		
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
		
		if (SEQ)
		{
			SeqSymbolStore seqStore = (SeqSymbolStore) store;
			
			for (Element element : models.getChildren())
			{
				if (element.getName().equals("Frame"))
				{
					String diagramName = element.getAttributeValue("Name");
					this.parseLifelines(element.getChild("ModelChildren").getChildren("InteractionLifeLine"), diagramName, seqStore);
				}
			}
			
			Element relationships = models.getChild("ModelRelationshipContainer").getChild("ModelChildren");
			
			for (Element element : relationships.getChildren())
			{
				if (element.getAttributeValue("Name").equals("Message"))
				{
					this.parseDiagramInfo(element.getChild("ModelChildren"), seqStore);
				}
			}
			
			for (Element element : models.getChildren())
			{
				if (element.getName().equals("Frame"))
				{
					this.parseCombinedFragments(element.getChild("ModelChildren").getChildren("CombinedFragment"), seqStore);
				}
				if (element.getName().equals("ModelRelationshipContainer"))
				{
					Element modelChildren = element.getChild("ModelChildren");
					
					for (Element modelChild : modelChildren.getChildren())
					{
						if (modelChild.getAttributeValue("Name").equals("Message"))
						{
							this.parseMessages(modelChild.getChild("ModelChildren").getChildren(), seqStore);
						}
					}
				}
			}
			
			SeqDiagramStore diagramStore = new DiagramStoreFactory().parserMakeSeqDiagramStore(seqStore);
			new TheoryGenerator().generateLTCTheory(diagramStore, "generatedltctheory.idp", (SeqFactors) factors);
		}
		else
		{
			DiagramStore diagramStore = new DiagramStoreFactory().makeDiagramStore(store);
			new TheoryGenerator().generateTheory(diagramStore, "generatedtheory.idp", factors);
		}
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
	
	private void parseLifelines(List<Element> lifelines, String diagramName, SeqSymbolStore store)
	{
		for (Element lifeline : lifelines)
		{
			String name = lifeline.getAttributeValue("Name");
			String lifeId = lifeline.getAttributeValue("Id");
			String classId = lifeline.getChild("BaseClassifier").getChild("Class").getAttributeValue("Idref");
			Type lifeType = new UserDefinedType(classId);
			TempVar tempVar = new TempVar(lifeType, name);
			store.addTempVar(name, tempVar);
			store.addName(lifeId, name);
			store.addTempVarToDiagram(name, diagramName);
		}
	}
	
	private void parseMessages(List<Element> messages, SeqSymbolStore store)
	{
		MessageBuilder pairedGetMessage = null;
		
		for (Element message : messages)
		{
			String id = message.getAttributeValue("Id");
			String content = OutputConvenienceFunctions.toIDPOperators(message.getAttributeValue("Name").replaceAll("\\s", ""));

			int sdPoint = Integer.parseInt(message.getAttributeValue("SequenceNumber"));
			Optional<String> fromId = Optional.empty();
			Optional<String> toId = Optional.empty();
			boolean isReturn = message.getChild("ActionType") != null && message.getChild("ActionType").getChild("ActionTypeReturn") != null;
			
			if (message.getChild("FromEnd").getChild("MessageEnd").getAttributeValue("EndModelElement") != null)
			{
				fromId = Optional.of(message.getChild("FromEnd").getChild("MessageEnd").getAttributeValue("EndModelElement"));
			}
			if (message.getChild("ToEnd").getChild("MessageEnd").getAttributeValue("EndModelElement") != null)
			{
				toId = Optional.of(message.getChild("ToEnd").getChild("MessageEnd").getAttributeValue("EndModelElement"));
			}
			
			if (content.contains("="))
			{
				String[] sides = content.split("=");
				
//				StringBuilder merge = new StringBuilder();
//				
//				for (int i = 1; i < sides.length; i++)
//				{
//					merge.append(sides[i]);
//				}
				
				String rhs = content.substring(content.indexOf("=") + 1, content.length());
				
				if (! store.hasTempVar(sides[0]))
				{
					String diagramNameAttempt = rhs.split("\\(")[0];
					
					if (store.getDiagrams().keySet().contains(diagramNameAttempt))
					{
						TempVar toAdd = new TempVar(store.getDiagrams().get(diagramNameAttempt)
								.getReturnValue().get().getType(), sides[0]);
						store.addTempVar(sides[0], toAdd);
						store.addName(sides[0], sides[0]);
					}
					else
					{
						String[] names = rhs.split(TEMPVAR_SEPARATOR);
						PrimitiveType type = null;
						
						if (rhs.contains("randomInt"))
						{
							type = PrimitiveType.INTEGER;
						}
						else if (rhs.contains("getNum"))
						{
							type = PrimitiveType.INTEGER;
						}
						else if (rhs.contains("flipBool"))
						{
							type = PrimitiveType.BOOLEAN;
						}
						else if (rhs.contains("=") || rhs.contains("=<") || rhs.contains(">=") || rhs.contains("<")
								|| rhs.contains(">") || rhs.contains("&") || rhs.contains("|"))
						{
							type = PrimitiveType.BOOLEAN;
						}
						else
						{
							for (String ele : names)
							{
								if (ele.equals(""))
								{
									continue;
								}
								
								if (ele.equals("F") || ele.equals("T"))
								{
									type = PrimitiveType.BOOLEAN;
									continue;
								}
								
								if (OutputConvenienceFunctions.representsInteger(ele))
								{
									type = PrimitiveType.INTEGER;
									continue;
								}
								
								if (OutputConvenienceFunctions.representsFloat(ele))
								{
									type = PrimitiveType.FLOAT;
									continue;
								}
								
								if (toId.isPresent() && rhs.startsWith("get"))
								{
									TempVar to = store.resolveTempVar(store.getNameOf(toId.get()));
									Set<DataUnit> attrs = store.getClassByName(to.getType()
											.getTypeName(store)).get().getAttributes();
									String partlySplit = rhs.split("By")[0];
									String findAttr = partlySplit.substring(3, partlySplit.length()).split("\\(")[0];
									
									boolean attrFound = false;
									for (DataUnit attr : attrs)
									{
										if (attr.getName().equalsIgnoreCase(findAttr))
										{
											type = (PrimitiveType) attr.getType();
											attrFound = true;
											break;
										}
									}
									
									if (attrFound)
									{
										continue;
									}
								}
								
								PrimitiveType eleType = (PrimitiveType) store.resolveTempVar(ele).getType();
								
								if (type == PrimitiveType.FLOAT || type == PrimitiveType.DOUBLE
										&& (eleType == PrimitiveType.BYTE || eleType == PrimitiveType.SHORT || eleType == PrimitiveType.INTEGER || eleType == PrimitiveType.LONG))
								{
									continue;
								}
								
								type = eleType;
							}
						}
						
						TempVar toAdd = new TempVar(type, sides[0]);
						store.addTempVar(sides[0], toAdd);
						store.addName(sides[0], sides[0]);
					}
				}
			}
			
			if (isReturn && ! content.contains("=") && ! store.hasTempVar(content))
			{
				String name = StringUtils.uncapitalize(pairedGetMessage.getContent().replaceAll("get", "").split("\\(")[0]);
				Class fromClass = store.getClassByName(store.resolveTempVar(store.getNameOf(fromId.get())).getType().getTypeName(store)).get();
				DataUnit attr = fromClass.getAttributeByName(name).get();
				
				TempVar toAdd = new TempVar(attr.getType(), content);
				store.addTempVar(content, toAdd);
				store.addName(content, content);
			}
			
			MessageBuilder builder = new MessageBuilder();
			store.addMsgBuilder(id, builder);
			store.addMessageBuilder(builder);
			builder.setContent(content).setId(id).setSdPoint(sdPoint)
				.setReturn(! toId.isPresent()).setFromId(fromId).setToId(toId);

			if (! isReturn && content.startsWith("get"))
			{
				pairedGetMessage = builder;
			}
		}
	}
	
	private void parseDiagramInfo(Element messageContainer, SeqSymbolStore store)
	{
		DiagramInfoBuilder builder = null;
		Operation operation = null;
		
		for (Element message : messageContainer.getChildren())
		{
			if (message.getAttributeValue("SequenceNumber").equals("1"))
			{
				if (builder == null)
				{
					builder = new DiagramInfoBuilder();
				}
				else if (! builder.fresh())
				{
					store.addDiagramInfo(builder.getName(), builder.build());
					builder.reset();
				}
				
				String content = message.getAttributeValue("Name");
				TempVar callObject = store.resolveTempVar(store.getNameOf(
						message.getChild("ToEnd").getChild("MessageEnd")
						.getAttributeValue("EndModelElement")));
				String diagramName = content.split("\\(")[0];
				builder.setName(diagramName).setCallObject(callObject);
				
				Set<Operation> ops = store.getClassByName(callObject.getType()
						.getTypeName(store)).get().getAllOperations().get();
				
				for (Operation ele : ops)
				{
					if (ele.getName().equals(diagramName))
					{
						operation = ele;
					}
				}
				
				Matcher m = TempVarCausationBuilder.GETTER_PARAMETER.matcher(content);
				m.find();
				String found = m.group(0);
				if (found.equals("()"))
				{
					continue;
				}
				builder.setParameters(Optional.of(new ArrayList<TempVar>()));
				String[] para = found.substring(1, found.length() - 1).split(TempVarCausationBuilder.ARGLIST_SEPARATOR);
				
				int paraCounter = 0;
				
				for (String ele : para)
				{
					if (store.hasTempVar(ele))
					{
						TempVar parameter = store.resolveTempVar(ele);
						builder.getParameters().get().add(parameter);
					}
					else
					{
						TempVar parameter = new TempVar(operation.getAllParameters().get().get(paraCounter).getType(), ele);
						store.addTempVar(ele, parameter);
						store.addName(ele, ele);
						store.addTempVarToDiagram(ele, diagramName);
						builder.getParameters().get().add(parameter);
					}
					paraCounter++;
				}
			}
			else if (message.getChild("ToEnd").getChild("MessageEnd").getAttributeValue("EndModelElement") == null)
			{
				String returnVar = message.getAttributeValue("Name");
				
				if (store.hasTempVar(returnVar))
				{
					TempVar toReturn = store.resolveTempVar(returnVar);
					builder.setReturnValue(Optional.of(toReturn));
				}
				else
				{
					TempVar toReturn = new TempVar(operation.getResultType().getType(), returnVar);
					store.addTempVar(returnVar, toReturn);
					store.addName(returnVar, returnVar);
					store.addTempVarToDiagram(returnVar, builder.getName());
					builder.setReturnValue(Optional.of(toReturn));
				}
				
				store.addDiagramInfo(builder.getName(), builder.build());
				builder.reset();
			}
		}
		
		if (! builder.fresh())
		{
			store.addDiagramInfo(builder.getName(), builder.build());
			builder.reset();
		}
	}
	
	private void parseCombinedFragments(List<Element> fragments, SeqSymbolStore store)
	{
		for (Element ele : fragments)
		{
			if (ele.getAttributeValue("OperatorKind").equals("alt"))
			{
				String id = ele.getAttributeValue("Id");
				AltCombinedFragmentBuilder frag = new AltCombinedFragmentBuilder();
				store.addTopAcf(id, frag);
				store.addAcf(id, frag);
				this.parseAlt(ele, store, id);
			}
			else if (ele.getAttributeValue("OperatorKind").equals("loop"))
			{
				String id = ele.getAttributeValue("Id");
				LoopCombinedFragmentBuilder frag = new LoopCombinedFragmentBuilder();
				store.addTopLcf(id, frag);
				store.addLcf(id, frag);
				this.parseLoop(ele, store, id);
			}
		}
	}
	
	private void parseAlt(Element element, SeqSymbolStore store, String topId)
	{
		String id = element.getAttributeValue("Id");
		List<Element> operands = element.getChild("ModelChildren").getChildren();
		
		String ifGuard = OutputConvenienceFunctions.toIDPOperators(operands.get(0).getChild("Guard").getChild("InteractionConstraint").getAttributeValue("Constraint"));
		String thenGuard = OutputConvenienceFunctions.toIDPOperators(operands.get(1).getChild("Guard").getChild("InteractionConstraint").getAttributeValue("Constraint"));
		
		List<Element> ifMessages = operands.get(0).getChild("Messages") == null ? 
				Collections.emptyList() : operands.get(0).getChild("Messages").getChildren();
		List<Element> thenMessages = operands.get(1).getChild("Messages") == null ?
				Collections.emptyList() : operands.get(1).getChild("Messages").getChildren();
		
		List<String> ifMsgIDs = new ArrayList<String>();
		List<String> thenMsgIDs = new ArrayList<String>();
		
		List<String> ifChildIDs = new ArrayList<String>();
		List<String> thenChildIDs = new ArrayList<String>();
		
		for (Element ifMessage : ifMessages)
		{
			ifMsgIDs.add(ifMessage.getAttributeValue("Idref"));
		}
		
		for (Element thenMessage : thenMessages)
		{
			thenMsgIDs.add(thenMessage.getAttributeValue("Idref"));
		}
		
		if (operands.get(0).getChild("ModelChildren") != null)
		{
			List<Element> ifChildren = operands.get(0).getChild("ModelChildren").getChildren();
			
			for (Element ifChild : ifChildren)
			{
				String childId = ifChild.getAttributeValue("Id");
				ifChildIDs.add(childId);
				if (ifChild.getAttributeValue("OperatorKind").equals("alt"))
				{
					this.parseAlt(ifChild, store, topId);
				}
				else if (ifChild.getAttributeValue("OperatorKind").equals("loop"))
				{
					this.parseLoop(ifChild, store, topId);
				}
			}
		}
		
		if (operands.get(1).getChild("ModelChildren") != null)
		{
			List<Element> thenChildren = operands.get(1).getChild("ModelChildren").getChildren();
			
			for (Element thenChild : thenChildren)
			{
				String childId = thenChild.getAttributeValue("Id");
				thenChildIDs.add(childId);
				if (thenChild.getAttributeValue("OperatorKind").equals("alt"))
				{
					this.parseAlt(thenChild, store, topId);
				}
				else if (thenChild.getAttributeValue("OperatorKind").equals("loop"))
				{
					this.parseLoop(thenChild, store, topId);
				}
			}
		}
		
		if (id.equals(topId))
		{
			AltCombinedFragmentBuilder builder = store.getAcf(id);
			builder.setIfGuard(ifGuard).setThenGuard(thenGuard).setIfMessageIDs(ifMsgIDs)
				.setThenMessageIDs(thenMsgIDs).setIfChildIDs(ifChildIDs).setThenChildIDs(thenChildIDs);
		}
		else
		{
			AltCombinedFragmentBuilder builder = new AltCombinedFragmentBuilder();
			builder.setIfGuard(ifGuard).setThenGuard(thenGuard).setIfMessageIDs(ifMsgIDs)
				.setThenMessageIDs(thenMsgIDs).setIfChildIDs(ifChildIDs).setThenChildIDs(thenChildIDs);
			
			store.addAcf(id, builder);
		}
	}
	
	private void parseLoop(Element element, SeqSymbolStore store, String topId)
	{
		String id = element.getAttributeValue("Id");
		Element operand = element.getChild("ModelChildren").getChild("InteractionOperand");
		
		String guard = OutputConvenienceFunctions.toIDPOperators(operand.getChild("Guard").getChild("InteractionConstraint").getAttributeValue("Constraint"));
		
		List<Element> messages = operand.getChild("Messages") == null ?
				Collections.emptyList() : operand.getChild("Messages").getChildren();
		
		List<String> msgIDs = new ArrayList<String>();
		List<String> childIDs = new ArrayList<String>();
		
		for (Element message : messages)
		{
			msgIDs.add(message.getAttributeValue("Idref"));
		}
		
		if (operand.getChild("ModelChildren") != null)
		{
			List<Element> children = operand.getChild("ModelChildren").getChildren();
			
			for (Element child : children)
			{
				String childId = child.getAttributeValue("Id");
				childIDs.add(childId);
				if (child.getAttributeValue("OperatorKind").equals("alt"))
				{
					this.parseAlt(child, store, topId);
				}
				else if (child.getAttributeValue("OperatorKind").equals("loop"))
				{
					this.parseLoop(child, store, topId);
				}
			}
		}
		
		if (id.equals(topId))
		{
			LoopCombinedFragmentBuilder builder = store.getLcf(id);
			builder.setGuard(guard).setMessageIDs(msgIDs).setChildIDs(childIDs);
		}
		else
		{
			LoopCombinedFragmentBuilder builder = new LoopCombinedFragmentBuilder();
			builder.setGuard(guard).setMessageIDs(msgIDs).setChildIDs(childIDs);
			
			store.addLcf(id, builder);
		}
	}
}
