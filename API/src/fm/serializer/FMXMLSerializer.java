package fm.serializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import api.fm.FMView;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import fm.util.SerializerDeserializerBase;

//TODO: Error reporting!
public class FMXMLSerializer extends SerializerDeserializerBase implements FMSerializer {
	public void serialize(FeatureModel featureModel, File file) {
		Document document = doSerialize(featureModel);

		try {
			FileOutputStream outputStream = new FileOutputStream(file);
        	saveToOutputStreamFromDocument(document, outputStream);
        } catch (FileNotFoundException e) {
        	//TODO: Error reporting!
            e.printStackTrace();
        }
	}
	
	public String serialize(FeatureModel featureModel) {
		Document document = doSerialize(featureModel);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	saveToOutputStreamFromDocument(document, outputStream);
    	
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
	}
	
	protected Document createDocument() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        return documentBuilder.newDocument();
	}
	
	protected void saveToOutputStreamFromDocument(Document document, OutputStream outputStream) {
        try {
            Transformer transformer = createTransformer();
            Source source = new DOMSource(document);
            Result result = new StreamResult(outputStream);
            transformer.transform(source, result);
        } catch (TransformerException e) {
        	//TODO: Error reporting!
        	e.printStackTrace();
        }
	}
	
	protected Document doSerialize(FeatureModel featureModel) {
        Document document = createDocument();
        
        Element rootElement = document.createElement(elementNameSerializedFeatureModel);
        document.appendChild(rootElement);
        rootElement.setAttribute("versionMajor", Integer.toString(versionMajor));
        rootElement.setAttribute("versionMinor", Integer.toString(versionMinor));
        
        doSerializeMetaData(featureModel, document, rootElement);
        doSerializeFeatureModel(featureModel, document, rootElement);
        
        return document;
	}
	
	protected void doSerializeMetaData(FeatureModel featureModel, Document document, Element parentElement) {
		Element metaDataElement = document.createElement(elementNameMetaData);
		parentElement.appendChild(metaDataElement);
		
		FMView previousAPIInstance = featureModel.getOwningView();
		metaDataElement.setAttribute("previousAPIInstanceClass", previousAPIInstance.getClass().getCanonicalName());
	}
	
	protected void doSerializeFeatureModel(FeatureModel featureModel, Document document, Element parentElement) {
		Element featureModelElement = document.createElement(elementNameFeatureModel);
		parentElement.appendChild(featureModelElement);
		
		Feature rootFeature = featureModel.getRootFeature();
		doSerializeFeature(rootFeature, document, featureModelElement);
	}
	
	protected void doSerializeFeature(Feature feature, Document document, Element parentElement) {
		Element featureElement = document.createElement(elementNameFeature);
		parentElement.appendChild(featureElement);
		featureElement.setAttribute("name", feature.getName());
		featureElement.setAttribute("variationType", serialzeFeatureVariationType(feature.getVariationType()));
		
		List<Group> childGroups = feature.getChildGroups();
		
		if (!childGroups.isEmpty()) {
			Element childGroupsElement = document.createElement(elementNameChildGroups);
			featureElement.appendChild(childGroupsElement);
			
			for (Group childGroup : childGroups) {
				doSerialzeGroup(childGroup, document, childGroupsElement);
			}
		}
	}
	
	protected void doSerialzeGroup(Group group, Document document, Element parentElement) {
		Element groupElement = document.createElement(elementNameGroup);
		parentElement.appendChild(groupElement);
		groupElement.setAttribute("variationType", serialzeGroupVariationType(group.getVariationType()));
		
		List<Feature> childFeatures = group.getChildFeatures();
		
		if (!childFeatures.isEmpty()) {
			Element childFeaturesElement = document.createElement(elementNameChildFeatures);
			groupElement.appendChild(childFeaturesElement);
			
			for (Feature childFeature : childFeatures) {
				doSerializeFeature(childFeature, document, childFeaturesElement);
			}
		}
	}
	
	protected String serialzeFeatureVariationType(FeatureVariationType variationType) {
		switch (variationType) {
			case MANDATORY:
				return "MANDATORY";
			case OPTIONAL:
				return "OPTIONAL";
			case NON_STANDARD:
				return "NON_STANDARD";
		}
		
		throw new UnsupportedOperationException();
	}

	protected String serialzeGroupVariationType(GroupVariationType variationType) {
		switch (variationType) {
			case AND:
				return "AND";
			case OR:
				return "OR";
			case XOR:
				return "XOR";
			case NON_STANDARD:
				return "NON_STANDARD";
		}
		
		throw new UnsupportedOperationException();
	}
}
