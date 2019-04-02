package api.tfm.listener;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalFeatureModel;
import tfm.util.TemporalPoint;

public interface TFMViewListener {

	//Feature Model
	public void onRootFeatureChange(TemporalFeatureModel temporalFeatureModel, Feature oldRootFeature, Feature newRootFeature, TemporalPoint temporalPoint);
	public void onFeatureCreated(Feature feature, TemporalPoint temporalPoint);
	public void onGroupCreated(Group group, TemporalPoint temporalPoint);
	
	// Feature
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature, TemporalPoint temporalPoint);
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, TemporalPoint temporalPoint);

	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature, TemporalPoint temporalPoint);
	
	public void onFeatureNameChanged(Feature feature, String oldName, String newName, TemporalPoint temporalPoint);
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType, TemporalPoint temporalPoint);
	
	
	// Groups
	public void onGroupAdded(Group group, Feature newParentFeature, TemporalPoint temporalPoint);
	public void onGroupRemoved(Group group, Feature oldParentFeature, TemporalPoint temporalPoint);
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature, TemporalPoint temporalPoint);
	
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType, TemporalPoint temporalPoint);
	
	
	// Emergent
	
	/**
	 * This is an emergent notification that is fired when a property of a feature model is changed, e.g., the feature model's set of features, its structure or individual elements' properties.  
	 * 
	 * @param feature The changed feature.
	 */
	public void onFeatureModelChanged(TemporalFeatureModel temporalFeatureModel, TemporalPoint temporalPoint);
	
	/**
	 * Fired when the set of feature/group entities changes, i.e., NOT when just the properties of a feature/group are changed.
	 * 
	 * @param featureModel
	 */
	public void onFeatureSetChanged(TemporalFeatureModel featureModel, TemporalPoint temporalPoint);
	
	
	/**
	 * This is an emergent notification that is fired when a property of a feature is changed, e.g., the feature's name, variation type, parent group/feature or child groups/features. 
	 * 
	 * @param feature The changed feature.
	 */
	public void onFeatureChanged(Feature feature, TemporalPoint temporalPoint);
	
	
	/**
	 * This is an emergent notification that is fired when a property of a group is changed, e.g., the group's variation type, parent feature or child features. 
	 * 
	 * @param feature The changed feature.
	 */
	public void onGroupChanged(Group group, TemporalPoint temporalPoint);
	
}
