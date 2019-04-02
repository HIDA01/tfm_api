package api.example;

import java.io.File;

import api.fm.FMView;
import api.instance.deltaecore.DeltaEcoreFMView;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import fm.serializer.FMDeserializer;
import fm.serializer.FMSerializer;
import fm.serializer.FMXMLDeserializer;
import fm.serializer.FMXMLSerializer;
import fm.util.FMPrinter;

//For visual representation of feature model, see file "resources/ExampleFeatureModel.pdf".
public class APIUsageExample {
	//NOTE: This is the only line that should vary between
	//different feature modeling notations.
	protected FMView createFMView() {
		//TODO: Plug in your instantiation of the FMView here!
		return new DeltaEcoreFMView("Root");
	}
	
	private FMView fmView;

	public APIUsageExample() {
		fmView = createFMView();
	}
	
	public FeatureModel assembleFeatureModel() {
		FeatureModel featureModel = fmView.getFeatureModel();
		
		Feature turtleBotFeature = featureModel.createFeature("TurtleBot");
		turtleBotFeature.setVariationType(FeatureVariationType.MANDATORY);
		turtleBotFeature.createDefaultChildGroup();
		featureModel.setRootFeature(turtleBotFeature);
		
		
		Feature engineFeature = featureModel.createFeature("Engine");
		engineFeature.setVariationType(FeatureVariationType.MANDATORY);
//		turtleBotFeature.addFeature(engineFeature);
		safeAddFeature(engineFeature, turtleBotFeature);
		
		
		Feature movementFeature = featureModel.createFeature("Movement");
		movementFeature.setVariationType(FeatureVariationType.MANDATORY);
//		turtleBotFeature.addFeature(movementFeature);
		safeAddFeature(movementFeature, turtleBotFeature);
		
		
		Feature keyboardFeature = featureModel.createFeature("Keyboard");
		keyboardFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		movementFeature.addFeature(keyboardFeature);
		safeAddFeature(keyboardFeature, movementFeature);
		
		Feature gamepadFeature = featureModel.createFeature("Gamepad");
		gamepadFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		movementFeature.addFeature(gamepadFeature);
		safeAddFeature(gamepadFeature, movementFeature);
		
		Feature autonomousFeature = featureModel.createFeature("Autonomous");
		autonomousFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		movementFeature.addFeature(autonomousFeature);
		safeAddFeature(autonomousFeature, movementFeature);
		
		Group movementGroup = movementFeature.getDefaultChildGroup();
		movementGroup.setVariationType(GroupVariationType.XOR);
		
		
		Feature webserviceFeature = featureModel.createFeature("Webservice");
		webserviceFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		turtleBotFeature.addFeature(webserviceFeature);
		safeAddFeature(webserviceFeature, turtleBotFeature);
		
		
		Feature detectionFeature = featureModel.createFeature("Detection");
		detectionFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		turtleBotFeature.addFeature(detectionFeature);
		safeAddFeature(detectionFeature, turtleBotFeature);
		

		Feature bumpFeature = featureModel.createFeature("Bump");
		bumpFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		detectionFeature.addFeature(bumpFeature);
		safeAddFeature(bumpFeature, detectionFeature);
		
		Feature infraredFeature = featureModel.createFeature("Infrared");
		infraredFeature.setVariationType(FeatureVariationType.OPTIONAL);
//		detectionFeature.addFeature(infraredFeature);
		safeAddFeature(infraredFeature, detectionFeature);
		
		Feature ultrasoundFeature = featureModel.createFeature("Ultrasound");
		ultrasoundFeature.setVariationType(FeatureVariationType.OPTIONAL);
		safeAddFeature(ultrasoundFeature, detectionFeature);
		
		Group detectionGroup = detectionFeature.getDefaultChildGroup();
		detectionGroup.setVariationType(GroupVariationType.OR);
		
		Group turtleBotGroup = turtleBotFeature.getDefaultChildGroup();
		turtleBotGroup.setVariationType(GroupVariationType.AND);
		
		return featureModel;
	}
	
	
	public void saveFeatureModelToFile(FeatureModel featureModel, File file) {
		FMSerializer serializer = new FMXMLSerializer();
		serializer.serialize(featureModel, file);
	}

	public String saveFeatureModelToString(FeatureModel featureModel) {
		FMSerializer serializer = new FMXMLSerializer();
		return serializer.serialize(featureModel);
	}
	
	public FeatureModel loadFeatureModelFromFile(File file) {
		FMDeserializer deserializer = new FMXMLDeserializer();
		return deserializer.deserialize(file);
	}
	
	public FeatureModel loadFeatureModelFromString(String serializedFeatureModel) {
		FMDeserializer deserializer = new FMXMLDeserializer();
		return deserializer.deserialize(serializedFeatureModel);
	}
	
	//TODO: This should probably be in the API
	protected void safeAddFeature(Feature feature, Feature parentFeature) {
		Group defaultChildGroup = safeGetDefaultFeatureDefaultChildGroup(parentFeature);
		defaultChildGroup.addFeature(feature);
	}
	
	//TODO: This should probably be in the API!
	protected Group safeGetDefaultFeatureDefaultChildGroup(Feature feature) {
		Group defaultChildGroup = feature.getDefaultChildGroup();
		
		if (defaultChildGroup == null) {
			defaultChildGroup = feature.createDefaultChildGroup();
			feature.addGroup(defaultChildGroup);
		}
		
		return defaultChildGroup;
	}
	
	public static final void main(String[] args) {
		File file = new File("FeatureModel.fmxml");
		
		APIUsageExample example = new APIUsageExample();
		
		FeatureModel featureModel = example.assembleFeatureModel();
		
		FMPrinter printer = new FMPrinter();
		String printedFeatureModel = printer.printFeatureModel(featureModel);
		System.out.println(printedFeatureModel);
		
		example.saveFeatureModelToFile(featureModel, file);
		
		String serializedFeatureModel = example.saveFeatureModelToString(featureModel);
		System.out.println(serializedFeatureModel);
		
		FeatureModel featureModel2 = example.loadFeatureModelFromFile(file);
		String printedFeatureModel2 = printer.printFeatureModel(featureModel2);
		System.out.println(printedFeatureModel2);
		
		FeatureModel featureModel3 = example.loadFeatureModelFromString(serializedFeatureModel);
		String printedFeatureModel3 = printer.printFeatureModel(featureModel3);
		System.out.println(printedFeatureModel3);
	}
}
