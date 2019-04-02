package api.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import api.fm.FMView;
import api.test.instance.FMViewFactory;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

public class FMViewCreationTest extends FMViewAbstractTest {
	public FMViewCreationTest(FMViewFactory viewFactory) {
		super(viewFactory);
	}
	
	@Test
	public void testCreateFeature() {
		FMView fmView = getFMView();

		Collection<Feature> allFeaturesBefore = fmView.getAllFeatures();
		Feature feature = fmView.createFeature("SomeFeatureName");
		Collection<Feature> allFeaturesAfter = fmView.getAllFeatures();
		
		assertFeatureIntegrity(feature, "SomeFeatureName", FeatureVariationType.OPTIONAL, null, null);
		// FIXME MN: why should those be the same? A new feature had been created.
		assertFeatureSet(allFeaturesBefore, allFeaturesAfter);
	}
	
	//TODO: Write test cases for individual methods.
	
//	@Test
//	public void testCreateGroup() {
//		FMView fmView = getFMView();
//		
////		Collection<Group> allGroupsBefore = fmView.getAllGroups();
//		Group group = fmView.createGroup();
//		
////		Collection<Group> allGroupsAfter = fmView.getAllGroups();
//		
//		assertEquals(GroupVariationType.AND, group.getVariationType());
//		assertEquals(null, group.getParentFeature());
////		assertEquals("Group set should remain unchanged", allGroupsBefore, allGroupsAfter);
//	}
	
	
	@Test
	public void testComplexFMCreation() {
		FMView fmView = getFMView();
		FeatureModel featureModel = fmView.getFeatureModel();
		
		Feature originalRootFeature = featureModel.getRootFeature();
		assertEquals("Root", originalRootFeature.getName());
		assertFeatureSet(featureModel, originalRootFeature);
		
		
		//////////////////////////// TurtleBot ////////////////////////////
		Feature turtleBotFeature = featureModel.createFeature("TurtleBot");
		assertFeatureIntegrity(turtleBotFeature, "TurtleBot", FeatureVariationType.OPTIONAL, null, null);

		turtleBotFeature.setVariationType(FeatureVariationType.MANDATORY);
		assertFeatureIntegrity(turtleBotFeature, "TurtleBot", FeatureVariationType.MANDATORY, null, null);
		
		
		//TODO: ?!?!?
		Group defaultGroup = turtleBotFeature.createDefaultChildGroup();
		turtleBotFeature.addGroup(defaultGroup);
		
		featureModel.setRootFeature(turtleBotFeature);
		
		assertFeatureIntegrity(turtleBotFeature, "TurtleBot", FeatureVariationType.MANDATORY, null, null);
		assertEquals(turtleBotFeature, featureModel.getRootFeature());
		assertFeatureSet(featureModel, turtleBotFeature);
	
	
		//////////////////////////// Engine ////////////////////////////
		Feature engineFeature = featureModel.createFeature("Engine");
		assertFeatureIntegrity(engineFeature, "Engine", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(engineFeature, turtleBotFeature);
		//TODO: Exists only here?
		Group turtleBotFeatureDefaultGroup = turtleBotFeature.getDefaultChildGroup();
		assertFeatureIntegrity(engineFeature, "Engine", FeatureVariationType.OPTIONAL, turtleBotFeatureDefaultGroup, turtleBotFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature);
		
		engineFeature.setVariationType(FeatureVariationType.MANDATORY);
		assertFeatureIntegrity(engineFeature, "Engine", FeatureVariationType.MANDATORY, turtleBotFeatureDefaultGroup, turtleBotFeature);
		
		
		
		//////////////////////////// Movement ////////////////////////////
		Feature movementFeature = featureModel.createFeature("Movement");
		assertFeatureIntegrity(movementFeature, "Movement", FeatureVariationType.OPTIONAL, null, null);
		
		movementFeature.setVariationType(FeatureVariationType.MANDATORY);
		assertFeatureIntegrity(movementFeature, "Movement", FeatureVariationType.MANDATORY, null, null);
		
		safeAddFeature(movementFeature, turtleBotFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature);
		
		assertFeatureIntegrity(movementFeature, "Movement", FeatureVariationType.MANDATORY, turtleBotFeatureDefaultGroup, turtleBotFeature);

		
		//////////////////////////// Keyboard ////////////////////////////
		Feature keyboardFeature = featureModel.createFeature("Keyboard");
		assertFeatureIntegrity(keyboardFeature, "Keyboard", FeatureVariationType.OPTIONAL, null, null);
		
		keyboardFeature.setVariationType(FeatureVariationType.OPTIONAL);
		assertFeatureIntegrity(keyboardFeature, "Keyboard", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(keyboardFeature, movementFeature);
		//TODO: This should be earlier!
		Group movementFeatureDefaultGroup = movementFeature.getDefaultChildGroup();
		movementFeatureDefaultGroup.setVariationType(GroupVariationType.XOR);
		assertGroupIntegrity(movementFeatureDefaultGroup, GroupVariationType.XOR, movementFeature);
		
		assertFeatureIntegrity(keyboardFeature, "Keyboard", FeatureVariationType.OPTIONAL, movementFeatureDefaultGroup, movementFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature);

		
		//////////////////////////// Gamepad ////////////////////////////
		Feature gamepadFeature = featureModel.createFeature("Gamepad");
		assertFeatureIntegrity(gamepadFeature, "Gamepad", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(gamepadFeature, movementFeature);
		assertFeatureIntegrity(gamepadFeature, "Gamepad", FeatureVariationType.OPTIONAL, movementFeatureDefaultGroup, movementFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature);		
		
		//  Should still hold.
		assertGroupIntegrity(movementFeatureDefaultGroup, GroupVariationType.XOR, movementFeature);
		
		
		//////////////////////////// Autonomous ////////////////////////////
		Feature autonomousFeature = featureModel.createFeature("Autonomous");
		assertFeatureIntegrity(autonomousFeature, "Autonomous", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(autonomousFeature, movementFeature);
		assertFeatureIntegrity(autonomousFeature, "Autonomous", FeatureVariationType.OPTIONAL, movementFeatureDefaultGroup, movementFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature);	
		
		//  Should still hold.
		assertGroupIntegrity(movementFeatureDefaultGroup, GroupVariationType.XOR, movementFeature);
		
		
		
		//////////////////////////// Webservice ////////////////////////////
		Feature webserviceFeature = featureModel.createFeature("Webservice");
		assertFeatureIntegrity(webserviceFeature, "Webservice", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(webserviceFeature, turtleBotFeature);
		assertFeatureIntegrity(webserviceFeature, "Webservice", FeatureVariationType.OPTIONAL, turtleBotFeatureDefaultGroup, turtleBotFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature, webserviceFeature);
		
		
		
		//////////////////////////// Detection ////////////////////////////
		Feature detectionFeature = featureModel.createFeature("Detection");
		assertFeatureIntegrity(detectionFeature, "Detection", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(detectionFeature, turtleBotFeature);
		
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature, webserviceFeature, detectionFeature);
		assertFeatureIntegrity(detectionFeature, "Detection", FeatureVariationType.OPTIONAL, turtleBotFeatureDefaultGroup, turtleBotFeature);
		
		detectionFeature.setVariationType(FeatureVariationType.OPTIONAL);
		assertFeatureIntegrity(detectionFeature, "Detection", FeatureVariationType.OPTIONAL, turtleBotFeatureDefaultGroup, turtleBotFeature);

		
		//////////////////////////// Bump ////////////////////////////
		Feature bumpFeature = featureModel.createFeature("Bump");
		assertFeatureIntegrity(bumpFeature, "Bump", FeatureVariationType.OPTIONAL, null, null);
		
		bumpFeature.setVariationType(FeatureVariationType.OPTIONAL);
		assertFeatureIntegrity(bumpFeature, "Bump", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(bumpFeature, detectionFeature);
		//TODO: This should be earlier!
		Group detectionFeatureDefaultGroup = detectionFeature.getDefaultChildGroup();
		assertGroupIntegrity(detectionFeatureDefaultGroup, GroupVariationType.AND, detectionFeature);
		
		assertFeatureIntegrity(bumpFeature, "Bump", FeatureVariationType.OPTIONAL, detectionFeatureDefaultGroup, detectionFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature, webserviceFeature, detectionFeature, bumpFeature);

		
		//////////////////////////// Infrared ////////////////////////////
		Feature infraredFeature = featureModel.createFeature("Infrared");
		assertFeatureIntegrity(infraredFeature, "Infrared", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(infraredFeature, detectionFeature);
		assertFeatureIntegrity(infraredFeature, "Infrared", FeatureVariationType.OPTIONAL, detectionFeatureDefaultGroup, detectionFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature, webserviceFeature, detectionFeature, bumpFeature, infraredFeature);		
		
		//  Should still hold.
		assertGroupIntegrity(detectionFeatureDefaultGroup, GroupVariationType.AND, detectionFeature);
		
		
		//////////////////////////// Ultrasound ////////////////////////////
		Feature ultrasoundFeature = featureModel.createFeature("Ultrasound");
		assertFeatureIntegrity(ultrasoundFeature, "Ultrasound", FeatureVariationType.OPTIONAL, null, null);
		
		safeAddFeature(ultrasoundFeature, detectionFeature);
		assertFeatureIntegrity(ultrasoundFeature, "Ultrasound", FeatureVariationType.OPTIONAL, detectionFeatureDefaultGroup, detectionFeature);
		assertFeatureSet(featureModel, turtleBotFeature, engineFeature, movementFeature, keyboardFeature, gamepadFeature, autonomousFeature, webserviceFeature, detectionFeature, bumpFeature, infraredFeature, ultrasoundFeature);	
		
		//  Should still hold.
		assertGroupIntegrity(detectionFeatureDefaultGroup, GroupVariationType.AND, detectionFeature);
		
		
		//  Should still hold.
		assertGroupIntegrity(turtleBotFeatureDefaultGroup, GroupVariationType.AND, turtleBotFeature);
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
}
