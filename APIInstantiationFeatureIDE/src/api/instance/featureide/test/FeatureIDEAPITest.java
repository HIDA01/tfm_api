package api.instance.featureide.test;

import api.fm.FMView;
import api.instance.featureide.FeatureIdeFMView;
import fm.Feature;
import fm.Group;

public class FeatureIDEAPITest {

	public void test() {
		FMView fmView = new FeatureIdeFMView("Root");
		
		Feature root = fmView.getRootFeature();
		
		Feature test = fmView.createFeature("Test");
		
		safeAddFeature(test, root);
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
