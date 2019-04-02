package fm.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

public abstract class SerializerDeserializerBase {
	protected static final int versionMajor = 1;
	protected static final int versionMinor = 1;
	
	protected static final String elementNameSerializedFeatureModel = "SerializedFeatureModel";
	protected static final String elementNameMetaData = "MetaData";
	protected static final String elementNameFeatureModel = "FeatureModel";
	protected static final String elementNameFeature = "Feature";
	protected static final String elementNameChildGroups = "ChildGroups";
	protected static final String elementNameGroup = "Group";
	protected static final String elementNameChildFeatures = "ChildFeatures";
	
	protected String getVersionString() {
		return versionMajor + "." + versionMinor;
	}
	
	protected Transformer createTransformer() {
        try {
        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//          transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "FeatureModel.dtd");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            return transformer; 
        } catch (TransformerException e) {
        	//TODO: Error reporting!
        	e.printStackTrace();
        	return null;
        }
	}
	
	protected DocumentBuilder createDocumentBuilder() {
	    try {
	    	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        return documentBuilderFactory.newDocumentBuilder();
	    } catch (ParserConfigurationException e) {
	    	//TODO: Error reporting!
	    	e.printStackTrace();
	        return null;
	    }
	}
}
