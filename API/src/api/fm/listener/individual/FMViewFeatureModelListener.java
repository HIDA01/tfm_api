package api.fm.listener.individual;

import fm.Feature;
import fm.FeatureModel;
import fm.Group;

public interface FMViewFeatureModelListener {
	public void onRootFeatureChanged(FeatureModel featureModel, Feature oldRootFeature, Feature newRootFeature);
	
	public void onFeatureCreated(Feature feature);
	public void onGroupCreated(Group group);
	
	
	/**
	 * This is an emergent notification that is fired when a property of a feature model is changed, e.g., the feature model's set of features, its structure or individual elements' properties.  
	 * 
	 * @param feature The changed feature.
	 */
	public void onFeatureModelChanged(FeatureModel featureModel);
	
	/**
	 * Fired when the set of feature/group entities changes, i.e., NOT when just the properties of a feature/group are changed.
	 * 
	 * @param featureModel
	 */
	public void onFeatureSetChanged(FeatureModel featureModel);
}
