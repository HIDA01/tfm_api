package api.operation.serializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import fm.FeatureVariationType;
import fm.GroupVariationType;
import tfm.util.DateTemporalPoint;
import tfm.util.RevisionTemporalPoint;
import tfm.util.TemporalPoint;

public class EvolutionOperationXMLSerializer extends EvolutionOperationDeSerializerBase implements EvolutionOperationSerializer {

	@Override
	public void serialize(Collection<EvolutionOperation> evolutionOperations, File file) {
		Document document = doSerialize(evolutionOperations);

		try {
			FileOutputStream outputStream = new FileOutputStream(file);
        	saveToOutputStreamFromDocument(document, outputStream);
        } catch (FileNotFoundException e) {
        	//TODO: Error reporting!
            e.printStackTrace();
        }
	}

	@Override
	public String serialize(Collection<EvolutionOperation> evolutionOperations) {
		Document document = doSerialize(evolutionOperations);

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
	
	protected Document doSerialize(Collection<EvolutionOperation> evolutionOperations) {
        Document document = createDocument();
        
        Element rootElement = document.createElement(elementNameSerializedEvolutionOperations);
        document.appendChild(rootElement);
        rootElement.setAttribute("versionMajor", Integer.toString(versionMajor));
        rootElement.setAttribute("versionMinor", Integer.toString(versionMinor));
        
        evolutionOperations.forEach(evolutionOperation -> rootElement.appendChild(doSerializeEvolutionOperation(evolutionOperation, document)));
        
        return document;
	}

	private Element doSerializeEvolutionOperation(EvolutionOperation evolutionOperation, Document document) {
		
		if (evolutionOperation instanceof ChangeRootFeatureOperation) {
			return doSerialize((ChangeRootFeatureOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof CreateFeatureOperation) {
			return doSerialize((CreateFeatureOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof CreateGroupOperation) {
			return doSerialize((CreateGroupOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureAddOperation) {
			return doSerialize((FeatureAddOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureChangeVariationTypeOperation) {
			return doSerialize((FeatureChangeVariationTypeOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureCreateDefaultChildGroupOperation) {
			return doSerialize((FeatureCreateDefaultChildGroupOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureDetachOperation) {
			return doSerialize((FeatureDetachOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureMoveOperation) {
			return doSerialize((FeatureMoveOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureRemoveOperation) {
			return doSerialize((FeatureRemoveOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof FeatureRenameOperation) {
			return doSerialize((FeatureRenameOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof GroupAddOperation) {
			return doSerialize((GroupAddOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof GroupChangeVariationTypeOperation) {
			return doSerialize((GroupChangeVariationTypeOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof GroupDetachOperation) {
			return doSerialize((GroupDetachOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof GroupMoveOperation) {
			return doSerialize((GroupMoveOperation) evolutionOperation, document);
		} else if (evolutionOperation instanceof GroupRemoveOperation) {
			return doSerialize((GroupRemoveOperation) evolutionOperation, document);
		}
		
		return null;
	}
	
	private Element doSerialize(GroupRemoveOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameGroupRemove);
		
		setStandardGroupOperationAttributes(evolutionOperation, evolutionOperationElement);

		if (evolutionOperation.getOldParentFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, evolutionOperation.getOldParentFeature().getName());
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(GroupMoveOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameGroupMove);
		
		setStandardGroupOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.appendChild(doSerialize(evolutionOperation.getDetachOperation(), document));
		evolutionOperationElement.appendChild(doSerialize(evolutionOperation.getAddOperation(), document));
		
		return evolutionOperationElement;
	}

	private Element doSerialize(GroupDetachOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameGroupDetach);
		
		setStandardGroupOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		if (evolutionOperation.getOldParentFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, evolutionOperation.getOldParentFeature().getName());
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(GroupChangeVariationTypeOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameGroupChangeVariationType);
		
		setStandardGroupOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.setAttribute(attributeNameNewGroupVariationType, serializeGroupVariationType(evolutionOperation.getNewVariationType()));
		if (evolutionOperation.getOldVariationType() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldGroupVariationType, serializeGroupVariationType(evolutionOperation.getOldVariationType()));			
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(GroupAddOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameGroupAdd);
		
		setStandardGroupOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.setAttribute(attributeNameNewParentFeature, evolutionOperation.getNewParentFeature().getName());
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureRenameOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureRename);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.setAttribute(attributeNameNewName, evolutionOperation.getNewName());
		
		if (evolutionOperation.getOldName() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldName, evolutionOperation.getOldName());
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureRemoveOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureRemove);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		if (evolutionOperation.getOldParentGroup() != null) {
			Feature parentFeature = evolutionOperation.getOldParentGroup().getParentFeature();
			
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, parentFeature.getName());
			
			int index = parentFeature.getChildGroups().indexOf(evolutionOperation.getOldParentGroup());
			evolutionOperationElement.setAttribute(attributeNameOldParentGroupIndex, Integer.toString(index));
		}
		else if (evolutionOperation.getOldParentFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, evolutionOperation.getOldParentFeature().getName());
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureMoveOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureMove);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.appendChild(doSerialize(evolutionOperation.getDetachOperation(), document));
		evolutionOperationElement.appendChild(doSerialize(evolutionOperation.getAddOperation(), document));
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureDetachOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureDetach);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		if (evolutionOperation.getOldParentGroup() != null) {
			Feature parentFeature = evolutionOperation.getOldParentGroup().getParentFeature();
			
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, parentFeature.getName());
			
			int index = parentFeature.getChildGroups().indexOf(evolutionOperation.getOldParentGroup());
			evolutionOperationElement.setAttribute(attributeNameOldParentGroupIndex, Integer.toString(index));
		}
		else if (evolutionOperation.getOldParentFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldParentFeature, evolutionOperation.getOldParentFeature().getName());
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureCreateDefaultChildGroupOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureCreateDefaultChildGroup);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureChangeVariationTypeOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureChangeVariationType);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		evolutionOperationElement.setAttribute(attributeNameNewFeatureVariationType, serializeFeatureVariationType(evolutionOperation.getNewVariationType()));
		if (evolutionOperation.getOldVariationType() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldFeatureVariationType, serializeFeatureVariationType(evolutionOperation.getOldVariationType()));			
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(FeatureAddOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameFeatureAdd);
		
		setStandardFeatureOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		if (evolutionOperation.getNewParentFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameNewParentFeature, evolutionOperation.getNewParentFeature().getName());
		}
		
		if (evolutionOperation.getNewParentGroup() != null) {
			Feature parentFeature = evolutionOperation.getNewParentGroup().getParentFeature();
			
			evolutionOperationElement.setAttribute(attributeNameNewParentGroupParentFeature, parentFeature.getName());
			
			int index = parentFeature.getChildGroups().indexOf(evolutionOperation.getNewParentGroup());
			evolutionOperationElement.setAttribute(attributeNameNewParentGroupIndex, Integer.toString(index));
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(CreateGroupOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameCreateGroup);
		
		setStandardOperationAttributes(evolutionOperation, evolutionOperationElement);
		
		// TODO currently only supported if group has been added afterwards. Otherwise, we are not able to identify groups.
		if (evolutionOperation.getGroup() != null) {
			Feature parentFeature = evolutionOperation.getGroup().getParentFeature();
			
			evolutionOperationElement.setAttribute(attributeNameGroupParentFeature, parentFeature.getName());
			
			int index = parentFeature.getChildGroups().indexOf(evolutionOperation.getGroup());
			evolutionOperationElement.setAttribute(attributeNameGroupIndex, Integer.toString(index));
		}
		
		return evolutionOperationElement;
	}

	private Element doSerialize(CreateFeatureOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameCreateFeature);
		
		setStandardOperationAttributes(evolutionOperation, evolutionOperationElement);
		evolutionOperationElement.setAttribute(attributeNameFeature, evolutionOperation.getFeatureName());
		
		return evolutionOperationElement;
	}

	private Element doSerialize(ChangeRootFeatureOperation evolutionOperation, Document document) {
		Element evolutionOperationElement = document.createElement(elementNameChangeRootFeature);
		
		setStandardOperationAttributes(evolutionOperation, evolutionOperationElement);
		evolutionOperationElement.setAttribute(attributeNameNewRootFeature, evolutionOperation.getNewRootFeature().getName());
		if (evolutionOperation.getOldRootFeature() != null) {
			evolutionOperationElement.setAttribute(attributeNameOldRootFeature, evolutionOperation.getOldRootFeature().getName());
		}
		
		return evolutionOperationElement;
	}
	
	private void setStandardOperationAttributes(EvolutionOperation evolutionOperation, Element element) {
		element.setAttribute(attributeNameCommandStackIndex, Integer.toString(evolutionOperation.getCommandStackIndex()));
		TemporalPoint operationTimePoint = evolutionOperation.getOperationDate();

		if (operationTimePoint == null) {
			element.setAttribute(attributeNameOperationDate, "null");
			element.setAttribute(attributeNameOperationDateType, "null");
		}
		else {
			element.setAttribute(attributeNameOperationDate, operationTimePoint.toString());
			if (operationTimePoint instanceof DateTemporalPoint) {
				element.setAttribute(attributeNameOperationDateType, attributeNameOperationDateTypeDate);
			}
			else if (operationTimePoint instanceof RevisionTemporalPoint) {
				element.setAttribute(attributeNameOperationDateType, attributeNameOperationDateTypeRevision);
			}
		}
		
		element.setAttribute(attributeNameHasBeenExecuted, Boolean.toString(evolutionOperation.hasBeenExecuted()));
	}
	
	private void setStandardFeatureOperationAttributes(FeatureEvolutionOperation evolutionOperation, Element element) {
		setStandardOperationAttributes(evolutionOperation, element);
		element.setAttribute(attributeNameFeature, evolutionOperation.getFeature().getName());
	}
	
	private void setStandardGroupOperationAttributes(GroupEvolutionOperation evolutionOperation, Element element) {
		setStandardOperationAttributes(evolutionOperation, element);
		
		Feature parentFeature = evolutionOperation.getGroup().getParentFeature();
		
		element.setAttribute(attributeNameGroupParentFeature, parentFeature.getName());
		
		int index = parentFeature.getChildGroups().indexOf(evolutionOperation.getGroup());
		element.setAttribute(attributeNameGroupIndex, Integer.toString(index));
	}
	

	protected String serializeFeatureVariationType(FeatureVariationType variationType) {
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

	protected String serializeGroupVariationType(GroupVariationType variationType) {
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
	
	protected String doSerializeTemporalPoint(TemporalPoint temporalPoint) {
		if (temporalPoint instanceof DateTemporalPoint) {
			DateTemporalPoint dateTemporalPoint = (DateTemporalPoint) temporalPoint;
			Date date = dateTemporalPoint.getDate();
			if (date == null) {
				return "null";
			}
			else {
				return Long.toString(date.getTime());
			}
		}
		else if(temporalPoint instanceof RevisionTemporalPoint) {
			return ((RevisionTemporalPoint) temporalPoint).getRevision();
		}
		
		throw new UnsupportedOperationException("");
	}
}
