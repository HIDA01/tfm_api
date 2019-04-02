package tfm;

import java.util.Collection;
import java.util.List;

import api.tfm.TFMView;
import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRangeElementTuple;

/**
 * Mere proxy element to communicate with the API. 
 * Method calls are forwarded to the respective instance of the API 
 * that instantiated the element, which are mirrored for convenient use.
 */
public class TemporalFeature extends TFMViewElement {

	
	public TemporalFeature(TFMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
	}
	
	// Read methods
	
	public TemporalFeatureModel getTemporalFeatureModel() {
		return getOwningView().getTemporalFeatureModel();
	}
	
	public boolean isRootAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().isRootFeatureAt(feature, temporalPoint);
	}
	
	public Group getParentGroupAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureParentGroupAt(feature, temporalPoint);
	}
	
	public Feature getParentFeatureAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureParentFeatureAt(feature, temporalPoint);
	}
	
	
	public Group getDefaultChildGroupAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureDefaultChildGroupAt(feature, temporalPoint);
	}
	
	//NOTE: Features and groups are ordered! (e.g., for graphical editors) 
	public List<Group> getChildGroupsAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureChildGroupsAt(feature, temporalPoint);
	}
	
	public String getNameAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureNameAt(feature, temporalPoint);
	}
	
	public FeatureVariationType getVariationTypeAt(TemporalPoint temporalPoint) {
		Feature feature = getFeatureAt(temporalPoint);
		return getOwningView().getFeatureVariationTypeAt(feature, temporalPoint);
	}
	
	// Temporal Read methods
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllParentGroups() {
		return getOwningView().getAllFeatureParentGroups(this);
	}
	
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllParentFeatures() {
		return getOwningView().getAllFeatureParentFeatures(this);
	}
	
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllDefaultChildGroups() {
		return getOwningView().getAllFeatureDefaultChildGroups(this);
	}
	
	//NOTE: Features and groups are ordered! (e.g., for graphical editors) 
	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	public List<TemporalRangeElementTuple<TemporalGroup>> getAllChildGroups() {
		return getOwningView().getAllFeatureChildGroups(this);
	}
	
	public Collection<TemporalRangeElementTuple<String>> getAllNames() {
		return getOwningView().getAllFeatureNames(this);
	}
	
	public Collection<TemporalFeatureVariationType> getAllVariationTypes() {
		return getOwningView().getAllFeatureVariationTypes(this);
	}
	
	// Helper Method
	
	public Feature getFeatureAt(TemporalPoint temporalPoint) {
		return getOwningView().getFeatureAt(this, temporalPoint);
	}
	
	// Write methods
	
	public void addAt(Feature parentFeature, TemporalPoint temporalPoint) {
		getOwningView().addFeatureAt(getFeatureAt(temporalPoint), parentFeature, temporalPoint);
	}
	
	public void addAt(Group parentGroup, TemporalPoint temporalPoint) {
		getOwningView().addFeatureAt(getFeatureAt(temporalPoint), parentGroup, temporalPoint);
	}
	
	/**
	 * Removes the feature and, transitively, all its descendant features/groups.
	 * NOTE: MUST NOT remove potentially remaining (empty) parent group. 
	 */
	public void removeAt(TemporalPoint temporalPoint) {
		getOwningView().removeFeatureAt(getFeatureAt(temporalPoint), temporalPoint);
	}
	
	public void moveAt(Group targetGroup, TemporalPoint temporalPoint) {
		getOwningView().moveFeatureAt(getFeatureAt(temporalPoint), targetGroup, temporalPoint);
	}
	
	public void moveAt(Feature targetFeature, TemporalPoint temporalPoint) {
		getOwningView().moveFeatureAt(getFeatureAt(temporalPoint), targetFeature, temporalPoint);
	}
	
	//TODO: Document whether this only creates the group or also adds it (which would be against API convention). Current DarwinSPL implementation: is added to parnet feature.
	public Group createDefaultChildGroupAt(TemporalPoint temporalPoint) {
		return getOwningView().createDefaultChildGroupAt(getFeatureAt(temporalPoint), temporalPoint);
	}
	
	public void setNameAt(String name, TemporalPoint temporalPoint) {
		getOwningView().setFeatureNameAt(getFeatureAt(temporalPoint), name, temporalPoint);
	}
	
	public void setVariationTypeAt(FeatureVariationType variationType, TemporalPoint temporalPoint) {
		getOwningView().setFeatureVariationTypeAt(getFeatureAt(temporalPoint), variationType, temporalPoint);
	}
}
