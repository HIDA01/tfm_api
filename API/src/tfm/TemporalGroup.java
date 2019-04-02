package tfm;

import java.util.Collection;
import java.util.List;

import api.tfm.TFMView;
import fm.Feature;
import fm.Group;
import fm.GroupVariationType;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRangeElementTuple;

public class TemporalGroup extends TFMViewElement {

	public TemporalGroup(TFMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
	}
	
	public TemporalFeatureModel getTemporalFeatureModel() {
		return getOwningView().getTemporalFeatureModel();
	}

	// Read Methods
	public List<Feature> getChildFeaturesAt(TemporalPoint temporalPoint) {
		return getOwningView().getGroupChildFeaturesAt(getGroupAt(temporalPoint), temporalPoint);
	}
	
	public Feature getParentFeatureAt(TemporalPoint temporalPoint) {
		return getOwningView().getGroupParentFeatureAt(getGroupAt(temporalPoint), temporalPoint);
	}
	
	public GroupVariationType getVariationTypeAt(TemporalPoint temporalPoint) {
		return getOwningView().getGroupVariationTypeAt(getGroupAt(temporalPoint), temporalPoint);
	}
	
	// Temporal Read Methods
	public List<TemporalRangeElementTuple<TemporalFeature>> getAllChildFeatures() {
		return getOwningView().getAllGroupChildFeatures(this);
	}
	
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllParentFeatures() {
		return getOwningView().getAllGroupParentFeatures(this);
	}
	
	public Collection<TemporalGroupVariationType> getAllVariationTypes() {
		return getOwningView().getAllGroupVariationTypes(this);
	}
	
	
	// Write Methods
	public void addAt(Feature parentFeature, TemporalPoint temporalPoint) {
		getOwningView().addGroupAt(getGroupAt(temporalPoint), parentFeature, temporalPoint);
	}

	/**
	 * Removes the group and, transitively, all its descendant features/groups.  
	 */
	public void removeAt(TemporalPoint temporalPoint) {
		getOwningView().removeGroupAt(getGroupAt(temporalPoint), temporalPoint);
	}
	
	public void moveAt(Feature targetFeature, TemporalPoint temporalPoint) {
		getOwningView().moveGroupAt(getGroupAt(temporalPoint), targetFeature, temporalPoint);
	}
	//NOTE: Target group does not make sense for moving groups so there is no dedicated method (unlike with features).
	
	public void setVariationTypeAt(GroupVariationType variationType, TemporalPoint temporalPoint) {
		getOwningView().setGroupVariationTypeAt(getGroupAt(temporalPoint), variationType, temporalPoint);
	}
	
	// Helper Method
	public Group getGroupAt(TemporalPoint temporalPoint) {
		return getOwningView().getGroupAt(this, temporalPoint);
	}
}
