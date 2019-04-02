package api.fm.listener.individual;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;

public interface FMViewFeatureListener {
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature);
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature);
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature);
	
	public void onFeatureNameChanged(Feature feature, String oldName, String newName);
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType);
	

	/**
	 * This is an emergent notification that is fired when a property of a feature is changed, e.g., the feature's name, variation type, parent group/feature or child groups/features. 
	 * 
	 * @param feature The changed feature.
	 */
	public void onFeatureChanged(Feature feature);
}
