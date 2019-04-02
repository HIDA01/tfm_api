package api.operation.serializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import api.operation.EvolutionOperation;
import api.operation.feature.FeatureAddOperation;
import api.operation.feature.FeatureChangeVariationTypeOperation;
import api.operation.feature.FeatureCreateDefaultChildGroupOperation;
import api.operation.feature.FeatureDetachOperation;
import api.operation.feature.FeatureEvolutionOperation;
import api.operation.feature.FeatureMoveOperation;
import api.operation.feature.FeatureRemoveOperation;
import api.operation.feature.FeatureRenameOperation;
import api.operation.fm.ChangeRootFeatureOperation;
import api.operation.fm.CreateFeatureOperation;
import api.operation.fm.CreateGroupOperation;
import api.operation.group.GroupAddOperation;
import api.operation.group.GroupChangeVariationTypeOperation;
import api.operation.group.GroupDetachOperation;
import api.operation.group.GroupEvolutionOperation;
import api.operation.group.GroupMoveOperation;
import api.operation.group.GroupRemoveOperation;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalFeatureModel;
import tfm.util.DateTemporalPoint;
import tfm.util.RevisionTemporalPoint;
import tfm.util.TemporalPoint;

public class EvolutionOperationXMLDeserializer extends EvolutionOperationDeSerializerBase implements EvolutionOperationDeserializer {

	protected FeatureModel featureModel;
	protected TemporalFeatureModel temporalFeatureModel;
	
	@Override
	public List<EvolutionOperation> deserialize(File file, TemporalFeatureModel temporalFeatureModel) {
		this.temporalFeatureModel = temporalFeatureModel;
		return doDeserialize(file);
	}
	
	@Override
	public List<EvolutionOperation> deserialize(File file, FeatureModel featureModel) {
		this.featureModel = featureModel;
		return doDeserialize(file);
	}
	
	private List<EvolutionOperation> doDeserialize(File file) {
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

	@Override
	public List<EvolutionOperation> deserialize(String serializedEvolutionOperations, FeatureModel featureModel) {
		this.featureModel = featureModel;		
		return doDeserialize(serializedEvolutionOperations);
	}

	@Override
	public List<EvolutionOperation> deserialize(String serializedEvolutionOperations,
			TemporalFeatureModel temporalFeatureModel) {
		this.temporalFeatureModel = temporalFeatureModel;
		return doDeserialize(serializedEvolutionOperations);
	}
	
	private List<EvolutionOperation> doDeserialize(String serializedEvolutionOperations) {
		InputStream inputStream = new ByteArrayInputStream(serializedEvolutionOperations.getBytes());
		Element rootElement = loadRootElementFromInputStream(inputStream);
		
		return doDeserialize(rootElement);
	}
	
	private Element loadRootElementFromInputStream(InputStream inputStream) {
		Document document = loadDocumentFromInputStream(inputStream);
		Node rootNode = document.getFirstChild();
		
		if (rootNode instanceof Element) {
			return (Element) rootNode;
		}
		
		throw new UnsupportedOperationException();
	}
	
	private Document loadDocumentFromInputStream(InputStream inputStream) {
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

	private List<EvolutionOperation> doDeserialize(Element rootElement) {
		if (elementNameSerializedEvolutionOperations.equals(rootElement.getNodeName())) {
			String rawVersionMajor = rootElement.getAttribute("versionMajor");
			int versionMajor = Integer.parseInt(rawVersionMajor);
			
			String rawVersionMinor = rootElement.getAttribute("versionMinor");
			int versionMinor = Integer.parseInt(rawVersionMinor);
			
			if (versionMajor != EvolutionOperationXMLDeserializer.versionMajor) {
				//TODO: Incompatible.
			}
			
			if (versionMinor != EvolutionOperationXMLDeserializer.versionMinor) {
				//TODO: Compatible but not an exact match
			}
			
			try {
				List<EvolutionOperation> evolutionOperations = doDeserializeEvolutionOperations(rootElement);
				
				return evolutionOperations;
			} catch (Exception e) {
				e.printStackTrace();
				//TODO: Error reporting!
			}
		}
		
		//TODO: Error handling
		return null;
	}

	private List<EvolutionOperation> doDeserializeEvolutionOperations(Element rootElement) {
		List<EvolutionOperation> evolutionOperations = new LinkedList<>();
		
		NodeList childNodes = rootElement.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			
			if (child == null) {
				continue;
			}
			
			if (child instanceof Element) {
				evolutionOperations.add(doDeserializeOperation((Element) child));				
			}
		}
		
		return evolutionOperations;
	}

	private EvolutionOperation doDeserializeOperation(Element element) {
		switch(element.getTagName()) {
		case elementNameChangeRootFeature:
			return doDeserializeChangeRootFeature(element);
		case elementNameCreateFeature:
			return doDeserializeCreateFeature(element);
		case elementNameCreateGroup:
			return doDeserializeCreateGroup(element);
		case elementNameFeatureAdd:
			return doDeserializeFeatureAdd(element);
		case elementNameFeatureChangeVariationType:
			return doDeserializeFeatureChangeVariationType(element);
		case elementNameFeatureCreateDefaultChildGroup:
			return doDeserializeFeatureCreateDefaultChildGroup(element);
		case elementNameFeatureDetach:
			return doDeserializeFeatureDetach(element);
		case elementNameFeatureMove:
			return doDeserializeFeatureMove(element);
		case elementNameFeatureRemove:
			return doDeserializeFeatureRemove(element);
		case elementNameFeatureRename:
			return doDeserializeFeatureRename(element);
		case elementNameGroupAdd:
			return doDeserializeGroupAdd(element);
		case elementNameGroupChangeVariationType:
			return doDeserializeGroupChangeVariationType(element);
		case elementNameGroupDetach:
			return doDeserializeGroupDetach(element);
		case elementNameGroupMove:
			return doDeserializeGroupMove(element);
		case elementNameGroupRemove:
			return doDeserializeGroupRemove(element);
		}
		
		return null;
	}

	private EvolutionOperation doDeserializeGroupRemove(Element element) {
		GroupRemoveOperation operation = new GroupRemoveOperation();
		doDeserializeStandardGroupData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			String oldParentFeatureName = element.getAttribute(attributeNameOldParentFeature);
			Feature oldParentFeature = resolveFeature(oldParentFeatureName, operation.getOperationDate());
			operation.setOldParentFeature(oldParentFeature);
		}
		
		return operation;
	}

	private GroupMoveOperation doDeserializeGroupMove(Element element) {
		GroupMoveOperation operation = new GroupMoveOperation();
		doDeserializeStandardGroupData(element, operation);
		
		NodeList detachNodeList = element.getElementsByTagName(elementNameGroupDetach);
		// TODO optimistic
		operation.setDetachOperation(doDeserializeGroupDetach((Element) detachNodeList.item(0)));
		
		NodeList addNodeList = element.getElementsByTagName(elementNameGroupAdd);
		// TODO optimistic
		operation.setAddOperation(doDeserializeGroupAdd((Element) addNodeList.item(0)));
		
		return operation;
	}

	private GroupDetachOperation doDeserializeGroupDetach(Element element) {
		GroupDetachOperation operation = new GroupDetachOperation();
		doDeserializeStandardGroupData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			String oldParentFeatureName = element.getAttribute(attributeNameOldParentFeature);
			Feature oldParentFeature = resolveFeature(oldParentFeatureName, operation.getOperationDate());
			operation.setOldParentFeature(oldParentFeature);
		}
		
		return operation;
	}

	private GroupChangeVariationTypeOperation doDeserializeGroupChangeVariationType(Element element) {
		GroupChangeVariationTypeOperation operation = new GroupChangeVariationTypeOperation();
		doDeserializeStandardGroupData(element, operation);

		String newVariationTypeString = element.getAttribute(attributeNameNewGroupVariationType);
		GroupVariationType newVariationType = deserializeGroupVariationType(newVariationTypeString);
		operation.setNewVariationType(newVariationType);
		
		if (operation.hasBeenExecuted()) {
			String oldVariationTypeString = element.getAttribute(attributeNameOldGroupVariationType);
			if (oldVariationTypeString != null && !oldVariationTypeString.equals("")) {
				GroupVariationType oldVariationType = deserializeGroupVariationType(oldVariationTypeString);
				operation.setOldVariationType(oldVariationType);
			}
		}
		
		return operation;
		
	}

	private GroupAddOperation doDeserializeGroupAdd(Element element) {
		GroupAddOperation operation = new GroupAddOperation();
		doDeserializeStandardGroupData(element, operation);
		
		String newParentFeatureName = element.getAttribute(attributeNameNewParentFeature);
		Feature newParentFeature = resolveFeature(newParentFeatureName, operation.getOperationDate());
		operation.setNewParentFeature(newParentFeature);
		
		return operation;
	}

	private FeatureRenameOperation doDeserializeFeatureRename(Element element) {
		FeatureRenameOperation operation = new FeatureRenameOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		String newName = element.getAttribute(attributeNameNewName);
		operation.setNewName(newName);
		
		String oldName = element.getAttribute(attributeNameOldName);
		if (oldName != null && !oldName.equals("")) {
			operation.setOldName(oldName);
		}
		
		return operation;
	}

	private FeatureRemoveOperation doDeserializeFeatureRemove(Element element) {
		FeatureRemoveOperation operation = new FeatureRemoveOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			String oldParentFeatureName = element.getAttribute(attributeNameOldParentFeature);
			Feature oldParentFeature = resolveFeature(oldParentFeatureName, operation.getOperationDate());
			operation.setOldParentFeature(oldParentFeature);
			
			String oldParentGroupIndexString = element.getAttribute(attributeNameOldParentGroupIndex);
			if (oldParentGroupIndexString != null && !oldParentGroupIndexString.equals("")) {
				int oldParentGroupIndex = Integer.parseInt(oldParentGroupIndexString);
				Group oldParentGroup = resolveGroup(oldParentFeature, oldParentGroupIndex);
				operation.setOldParentGroup(oldParentGroup);
			}
		}
		
		return operation;
	}

	private FeatureMoveOperation doDeserializeFeatureMove(Element element) {
		FeatureMoveOperation operation = new FeatureMoveOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		NodeList detachNodeList = element.getElementsByTagName(elementNameFeatureDetach);
		// TODO optimistic
		operation.setDetachOperation(doDeserializeFeatureDetach((Element) detachNodeList.item(0)));
		
		NodeList addNodeList = element.getElementsByTagName(elementNameFeatureAdd);
		// TODO optimistic
		operation.setAddOperation(doDeserializeFeatureAdd((Element) addNodeList.item(0)));
		
		return operation;
	}

	private FeatureDetachOperation doDeserializeFeatureDetach(Element element) {
		FeatureDetachOperation operation = new FeatureDetachOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			String oldParentFeatureName = element.getAttribute(attributeNameOldParentFeature);
			Feature oldParentFeature = resolveFeature(oldParentFeatureName, operation.getOperationDate());
			operation.setOldParentFeature(oldParentFeature);
			
			String oldParentGroupIndexString = element.getAttribute(attributeNameOldParentGroupIndex);
			if (oldParentGroupIndexString != null && !oldParentGroupIndexString.equals("")) {
				int oldParentGroupIndex = Integer.parseInt(oldParentGroupIndexString);
				
				Group oldParentGroup = resolveGroup(oldParentFeature, oldParentGroupIndex);
				operation.setOldParentGroup(oldParentGroup);
			}
		}
		
		return operation;
	}

	private FeatureCreateDefaultChildGroupOperation doDeserializeFeatureCreateDefaultChildGroup(Element element) {
		FeatureCreateDefaultChildGroupOperation operation = new FeatureCreateDefaultChildGroupOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			Group defaultGroup = operation.getFeature().getDefaultChildGroup();
			operation.setNewDefaultChildGroup(defaultGroup);
		}
		
		return operation;
	}

	private FeatureChangeVariationTypeOperation doDeserializeFeatureChangeVariationType(Element element) {
		FeatureChangeVariationTypeOperation operation = new FeatureChangeVariationTypeOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		String newVariationTypeString = element.getAttribute(attributeNameNewFeatureVariationType);
		FeatureVariationType newVariationType = deserializeFeatureVariationType(newVariationTypeString);
		operation.setNewVariationType(newVariationType);
		
		if (operation.hasBeenExecuted()) {
			String oldVariationTypeString = element.getAttribute(attributeNameOldFeatureVariationType);
			FeatureVariationType oldVariationType = deserializeFeatureVariationType(oldVariationTypeString);
			operation.setOldVariationType(oldVariationType);
		}
		
		return operation;
	}

	private FeatureAddOperation doDeserializeFeatureAdd(Element element) {
		FeatureAddOperation operation = new FeatureAddOperation();
		doDeserializeStandardFeatureData(element, operation);
		
		String newParentFeatureName = element.getAttribute(attributeNameNewParentFeature);
		if (newParentFeatureName != null && !newParentFeatureName.equals("")) {
			Feature newParentFeature = resolveFeature(newParentFeatureName, operation.getOperationDate());
			operation.setNewParentFeature(newParentFeature);
		}
		
		String newParentGroupParentFeatureName = element.getAttribute(attributeNameNewParentGroupParentFeature);
		if (newParentGroupParentFeatureName != null && !newParentGroupParentFeatureName.equals("")) {
			Feature newGroupParentFeature = resolveFeature(newParentGroupParentFeatureName, operation.getOperationDate());
			
			int groupIndex = Integer.parseInt(element.getAttribute(attributeNameNewParentGroupIndex));
			Group newParentGroup = resolveGroup(newGroupParentFeature, groupIndex);
			operation.setNewParentGroup(newParentGroup);
		}
		
		return operation;
	}

	private CreateGroupOperation doDeserializeCreateGroup(Element element) {
		CreateGroupOperation operation = new CreateGroupOperation();
		doDeserializeStandardData(element, operation);
		
		if (operation.hasBeenExecuted()) {
			String parentFeatureName = element.getAttribute(attributeNameGroupParentFeature);
			Feature parentFeature = resolveFeature(parentFeatureName, operation.getOperationDate());
			
			int groupIndex = Integer.parseInt(element.getAttribute(attributeNameGroupIndex));
			Group group = resolveGroup(parentFeature, groupIndex);
			operation.setGroup(group);
		}
		
		return operation;
	}

	private CreateFeatureOperation doDeserializeCreateFeature(Element element) {
		CreateFeatureOperation operation = new CreateFeatureOperation();
		doDeserializeStandardData(element, operation);
		
		String featureName = element.getAttribute(attributeNameFeature);
		operation.setFeatureName(featureName);
		
		if (operation.hasBeenExecuted()) {
			Feature feature = resolveFeature(featureName, operation.getOperationDate());
			operation.setFeature(feature);
		}
		
		return operation;
	}

	private ChangeRootFeatureOperation doDeserializeChangeRootFeature(Element element) {
		ChangeRootFeatureOperation operation = new ChangeRootFeatureOperation();
		doDeserializeStandardData(element, operation);
		
		String newRootFeatureName = element.getAttribute(attributeNameNewRootFeature);
		Feature newRootFeature = resolveFeature(newRootFeatureName, operation.getOperationDate());
		operation.setNewRootFeature(newRootFeature);
		if (operation.hasBeenExecuted()) {
			String oldRootFeatureName = element.getAttribute(attributeNameOldRootFeature);
			Feature oldRootFeature = resolveFeature(oldRootFeatureName, operation.getOperationDate());
			operation.setOldRootFeature(oldRootFeature);
		}
		
		return operation;
	}

	private void doDeserializeStandardGroupData(Element element, GroupEvolutionOperation operation) {
		doDeserializeStandardData(element, operation);
		
		String parentFeatureName = element.getAttribute(attributeNameGroupParentFeature);
		Feature parentFeature = resolveFeature(parentFeatureName, operation.getOperationDate());
		
		int groupIndex = Integer.parseInt(element.getAttribute(attributeNameGroupIndex));
		
		Group group = resolveGroup(parentFeature, groupIndex);
		operation.setGroup(group);
	}
	
	private void doDeserializeStandardFeatureData(Element element, FeatureEvolutionOperation operation) {
		doDeserializeStandardData(element, operation);
		
		String featureName = element.getAttribute(attributeNameFeature);
		Feature feature = resolveFeature(featureName, operation.getOperationDate());
		operation.setFeature(feature);
	}
	
	private void doDeserializeStandardData(Element element, EvolutionOperation evolutionOperation) {
		int commandStackIndex = Integer.parseInt(element.getAttribute(attributeNameCommandStackIndex));
		evolutionOperation.setCommandStackIndex(commandStackIndex);
		
		String temporalPointType = element.getAttribute(attributeNameOperationDateType);
		String temporalPointString = element.getAttribute(attributeNameOperationDate);
		TemporalPoint temporalPoint = null;
		switch(temporalPointType) {
		case attributeNameOperationDateTypeDate:
			temporalPoint = doDeserializeDateTemporalPoint(temporalPointString);
			break;
		case attributeNameOperationDateTypeRevision:
			temporalPoint = doDeserializeRevisionTemporalPoint(temporalPointString);
			break;
		case "null":
			temporalPoint = null;
			break;
		}
		evolutionOperation.setOperationDate(temporalPoint);
		
		boolean hasBeenExecuted = Boolean.parseBoolean(element.getAttribute(attributeNameHasBeenExecuted));
		evolutionOperation.setHasBeenExecuted(hasBeenExecuted);
		
		if (temporalFeatureModel != null) {
			evolutionOperation.setFeatureModel(temporalFeatureModel.getFeatureModelAt(temporalPoint));
		}
		else {
			evolutionOperation.setFeatureModel(featureModel);
		}
	}
	
	private Feature resolveFeature(String newRootFeatureName, TemporalPoint temporalPoint) {
		FeatureModel featureModel = this.featureModel;
		
		if (temporalFeatureModel != null) {
			featureModel = temporalFeatureModel.getFeatureModelAt(temporalPoint);
		}
		
		return featureModel.getFeature(newRootFeatureName);
	}
	
	private Group resolveGroup(Feature parentFeature, int groupIndex) {
		return parentFeature.getChildGroups().get(groupIndex);
	}
	
	private DateTemporalPoint doDeserializeDateTemporalPoint(String content) {
		DateTemporalPoint temporalPoint;
		if (content == "null") {
			temporalPoint = new DateTemporalPoint(null);
		}
		else {
			long time = Long.parseLong(content);
			temporalPoint = new DateTemporalPoint(new Date(time));
		}
		return temporalPoint;
	}
	
	private RevisionTemporalPoint doDeserializeRevisionTemporalPoint(String content) {
		RevisionTemporalPoint temporalPoint = new RevisionTemporalPoint(content);
		return temporalPoint;
	}

	private FeatureVariationType deserializeFeatureVariationType(String content) {
		switch(content) {
		case "MANDATORY":
			return FeatureVariationType.MANDATORY;
		case "OPTIONAL":
			return FeatureVariationType.OPTIONAL;
		case "NON_STANDARD":
			return FeatureVariationType.NON_STANDARD;
		}
		
		throw new UnsupportedOperationException();
	}

	private GroupVariationType deserializeGroupVariationType(String content) {
		switch(content) {
		case "AND":
			return GroupVariationType.AND;
		case "OR":
			return GroupVariationType.OR;
		case "XOR":
			return GroupVariationType.XOR;
		case "NON_STANDARD":
			return GroupVariationType.NON_STANDARD;
		}
		
		throw new UnsupportedOperationException();
	}
}
