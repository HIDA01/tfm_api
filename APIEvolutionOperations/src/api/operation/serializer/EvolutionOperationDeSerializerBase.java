package api.operation.serializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

public abstract class EvolutionOperationDeSerializerBase {

	protected static final int versionMajor = 1;
	protected static final int versionMinor = 1;
	
	protected static final String elementNameSerializedEvolutionOperations = "SerializedEvolutionOperations";
	protected static final String elementNameMetaData = "MetaData";
	
	protected static final String attributeNameCommandStackIndex = "commandStackIndex";
	protected static final String attributeNameOperationDate = "operationDate";
	protected static final String attributeNameOperationDateType = "temporalPointType";
	protected static final String attributeNameOperationDateTypeDate = "dateTemporalPoint";
	protected static final String attributeNameOperationDateTypeRevision = "revisionTemporalPoint";
	protected static final String attributeNameHasBeenExecuted = "hasBeenExecuted";
	
	protected static final String attributeNameFeature = "feature";
	protected static final String attributeNameGroupParentFeature = "groupParentFeature";
	protected static final String attributeNameGroupIndex = "groupIndex";
	
	// individual Operations
	protected static final String elementNameChangeRootFeature = "ChangeRootFeature";
	protected static final String attributeNameNewRootFeature = "newRootFeature";
	protected static final String attributeNameOldRootFeature = "oldRootFeature";
	
	protected static final String elementNameCreateFeature = "CreateFeature";
	
	protected static final String elementNameCreateGroup = "CreateGroup";
	
	protected static final String elementNameFeatureAdd = "FeatureAdd";
	protected static final String attributeNameNewParentFeature = "newParentFeature";
	protected static final String attributeNameNewParentGroupParentFeature = "newParentGroupParentFeature";
	protected static final String attributeNameNewParentGroupIndex = "newParentGroupParentIndex";
	
	protected static final String elementNameFeatureChangeVariationType = "FeatureChangeVariationType";
	protected static final String attributeNameNewFeatureVariationType = "newFeatureVariationType";
	protected static final String attributeNameOldFeatureVariationType = "oldFeatureVariationType";
	
	protected static final String elementNameFeatureCreateDefaultChildGroup = "FeatureCreateDefaultChildGroup";
	
	protected static final String elementNameFeatureDetach = "FeatureDetach";
	protected static final String attributeNameOldParentFeature = "oldParentFeature";
	protected static final String attributeNameOldParentGroupIndex = "oldParentGroupIndex";
	
	protected static final String elementNameFeatureMove = "FeatureMove";
	
	protected static final String elementNameFeatureRemove = "FeatureRemove";
	
	protected static final String elementNameFeatureRename = "FeatureRename";
	protected static final String attributeNameOldName = "oldName";
	protected static final String attributeNameNewName = "newName";
	
	protected static final String elementNameGroupAdd = "GroupAdd";
	
	protected static final String elementNameGroupChangeVariationType = "GroupChangeVariationType";
	protected static final String attributeNameNewGroupVariationType = "newGroupVariationType";
	protected static final String attributeNameOldGroupVariationType = "oldGroupVariationType";
	
	protected static final String elementNameGroupDetach = "GroupDetach";
	
	protected static final String elementNameGroupMove = "GroupMove";
	
	protected static final String elementNameGroupRemove = "GroupRemove";
	
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
