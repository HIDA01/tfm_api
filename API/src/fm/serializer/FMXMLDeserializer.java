package fm.serializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import api.fm.FMView;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import fm.util.SerializerDeserializerBase;

public class FMXMLDeserializer extends SerializerDeserializerBase implements FMDeserializer {
	public FeatureModel deserialize(File file) {
		try {
			FileInputStream inputStream = new FileInputStream(file);
			Element rootElement = loadRootElementFromInputStream(inputStream);
			return doDeserialize(rootElement);
        } catch (FileNotFoundException e) {
        	//TODO: Error reporting!
            e.printStackTrace();
        }
		
		return null;
	}
	
	public FeatureModel deserialize(String serializedFeatureModel) {
		InputStream inputStream = new ByteArrayInputStream(serializedFeatureModel.getBytes());
		Element rootElement = loadRootElementFromInputStream(inputStream);
		return doDeserialize(rootElement);
	}
	
	protected Element loadRootElementFromInputStream(InputStream inputStream) {
		Document document = loadDocumentFromInputStream(inputStream);
		Node rootNode = document.getFirstChild();
		
		if (rootNode instanceof Element) {
			return (Element) rootNode;
		}
		
		throw new UnsupportedOperationException();
	}
	
	protected Document loadDocumentFromInputStream(InputStream inputStream) {
		try {
			DocumentBuilder documentBuilder = createDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			document.getDocumentElement().normalize();
			
			return document;
	    } catch (Exception e) {
	    	//TODO: Error reporting!
	        e.printStackTrace();
	    }
		
		return null;
	}
	
	protected FeatureModel doDeserialize(Element rootElement) {
		if (elementNameSerializedFeatureModel.equals(rootElement.getNodeName())) {
			String rawVersionMajor = rootElement.getAttribute("versionMajor");
			int versionMajor = Integer.parseInt(rawVersionMajor);
			
			String rawVersionMinor = rootElement.getAttribute("versionMinor");
			int versionMinor = Integer.parseInt(rawVersionMinor);
			
			if (versionMajor != FMXMLDeserializer.versionMajor) {
				//TODO: Incompatible.
			}
			
			if (versionMinor != FMXMLDeserializer.versionMinor) {
				//TODO: Compatible but not an exact match
			}
			
			Class<? extends FMView> apiClass = doDeserializeMetaData(rootElement);
			
			try {
				Constructor<? extends FMView> constructor = apiClass.getConstructor(String.class);
				FMView api = doDeserializeFeatureModel(constructor, rootElement);
				return api.getFeatureModel();
			} catch (Exception e) {
				e.printStackTrace();
				//TODO: Error reporting!
			}
		}
		
		//TODO: Error handling
		return null;
	}
	
	protected static Element getChildElementByTagName(Element parentElement, String nodeName) {
		List<Element> elements = getChildElementsByTagName(parentElement, nodeName);
		
		if (elements.isEmpty()) {
			return null;
		}
		
		return elements.get(0);
	}
	
	protected static List<Element> getChildElementsByTagName(Element parentElement, String nodeName) {
		List<Element> elements = new LinkedList<Element>();
		
		if (nodeName == null || nodeName.isEmpty()) {
			return elements;
		}
		
		NodeList nodeList = parentElement.getChildNodes();
		
		int n = nodeList.getLength();

		for (int i = 0; i < n; i++) {
			Node node = nodeList.item(i);
			
			if (node instanceof Element) {
				Element element = (Element) node;
				
				if (nodeName.equals(element.getNodeName())) {
					elements.add(element);
				}
			}
		}
		
		return elements;
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends FMView> doDeserializeMetaData(Element serializedFeatureModelElement) {
		Element metaDataElement = getChildElementByTagName(serializedFeatureModelElement, elementNameMetaData);
		
		if (metaDataElement == null) {
			//TODO: Error reporting!
		}

		String rawPreviousAPIInstanceClass = metaDataElement.getAttribute("previousAPIInstanceClass");
		
		try {
			Class<?> previousAPIInstanceClass = Class.forName(rawPreviousAPIInstanceClass);
			
			if (FMView.class.isAssignableFrom(previousAPIInstanceClass)) {
				return (Class<? extends FMView>) previousAPIInstanceClass;
			}
		} catch(Exception e) {
			e.printStackTrace();
			//TODO: Error reporting!
		}
		
		return null;
	}
	
	protected FMView doDeserializeFeatureModel(Constructor<? extends FMView> constructor, Element serializedFeatureModelElement) {
		Element featureModelElement = getChildElementByTagName(serializedFeatureModelElement, elementNameFeatureModel);
		
		try {
			Element rootFeatureElement = getChildElementByTagName(featureModelElement, elementNameFeature);
			
			String rootFeatureName = rootFeatureElement.getAttribute("name");
			String rawVariationType = rootFeatureElement.getAttribute("variationType");
			FeatureVariationType variationType = deserialzeFeatureVariationType(rawVariationType);
			
			FMView api = constructor.newInstance(rootFeatureName);
			FeatureModel featureModel = api.getFeatureModel();
			
			Feature rootFeature = featureModel.getRootFeature();
			rootFeature.setVariationType(variationType);
			
			doDeserializeFeatureChildGroups(api, rootFeature, rootFeatureElement);
			
			return api;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	protected void doDeserializeFeature(FMView api, Group parentGroup, Element featureElement) {
		String name = featureElement.getAttribute("name");
		String rawVariationType = featureElement.getAttribute("variationType");
		FeatureVariationType variationType = deserialzeFeatureVariationType(rawVariationType);
		
		Feature feature = api.createFeature(name);
		feature.setName(name);
		feature.setVariationType(variationType);
		parentGroup.addFeature(feature);
		
		doDeserializeFeatureChildGroups(api, feature, featureElement);
	}
	
	protected void doDeserializeFeatureChildGroups(FMView api, Feature parentFeature, Element parentFeatureElement) {
		//Child groups
		Element childGroupsElement = getChildElementByTagName(parentFeatureElement, elementNameChildGroups);
		
		if (childGroupsElement == null) {
			return;
		}
		
		List<Element> groupElements = getChildElementsByTagName(childGroupsElement, elementNameGroup);
		
		for (Element groupElement : groupElements) {
			doDeserializeGroup(api, parentFeature, groupElement);
		}
	}
	
	
	protected void doDeserializeGroup(FMView api, Feature parentFeature, Element groupElement) {
		String rawVariationType = groupElement.getAttribute("variationType");
		GroupVariationType variationType = deserialzeGroupVariationType(rawVariationType);
		
		Group group = api.createGroup();
		group.setVariationType(variationType);
		parentFeature.addGroup(group);
		
		doDeserializeGroupChildFeatures(api, group, groupElement);
	}
	
	protected void doDeserializeGroupChildFeatures(FMView api, Group parentGroup, Element parentGroupElement) {
		//Child features
		Element childFeaturesElement = getChildElementByTagName(parentGroupElement, elementNameChildFeatures);
		
		if (childFeaturesElement == null) {
			return;
		}
		
		List<Element> featureElements = getChildElementsByTagName(childFeaturesElement, elementNameFeature);
		
		for (Element featureElement : featureElements) {
			doDeserializeFeature(api, parentGroup, featureElement);
		}
	}
	
	
	protected FeatureVariationType deserialzeFeatureVariationType(String rawVariationType) {
		if (rawVariationType == null) {
			return null;
		}
		
		if (rawVariationType.equals("MANDATORY")) {
			return FeatureVariationType.MANDATORY;
		}
		
		if (rawVariationType.equals("OPTIONAL")) {
			return FeatureVariationType.OPTIONAL;
		}
		
		if (rawVariationType.equals("NON_STANDARD")) {
			return FeatureVariationType.NON_STANDARD;
		}
		
		throw new UnsupportedOperationException();
	}
	
	protected GroupVariationType deserialzeGroupVariationType(String rawVariationType) {
		if (rawVariationType == null) {
			return null;
		}
		
		if (rawVariationType.equals("AND")) {
			return GroupVariationType.AND;
		}
		
		if (rawVariationType.equals("OR")) {
			return GroupVariationType.OR;
		}
		
		if (rawVariationType.equals("XOR")) {
			return GroupVariationType.XOR;
		}
		
		if (rawVariationType.equals("NON_STANDARD")) {
			return GroupVariationType.NON_STANDARD;
		}
		
		throw new UnsupportedOperationException();
	}
}
