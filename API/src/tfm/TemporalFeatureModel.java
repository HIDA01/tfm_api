package tfm;

import java.util.Collection;
import java.util.List;

import api.tfm.TFMView;
import fm.Feature;
import fm.FeatureModel;
import fm.Group;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRangeElementTuple;

public class TemporalFeatureModel extends TFMViewElement {

	public TemporalFeatureModel(TFMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
	}
	
	public FeatureModel getFeatureModelAt(TemporalPoint temporalPoint) {
		return getOwningView().getFeatureModelAt(temporalPoint);
	}
	
	public Collection<Feature> getAllFeaturesAt(TemporalPoint temporalPoint) {
		return getOwningView().getAllFeaturesAt(temporalPoint);
	}
	
	public Collection<Group> getAllGroupsAt(TemporalPoint temporalPoint) {
		return getOwningView().getAllGroupsAt(temporalPoint);
	}
	
	public Feature getFeatureAt(String identifier, TemporalPoint temporalPoint) {
		return getOwningView().getFeatureAt(identifier, temporalPoint);
	}
	
	public Feature getRootFeatureAt(TemporalPoint temporalPoint) {
		return getOwningView().getRootFeatureAt(temporalPoint);
	}
	
	//Temporal Feature Model
	public Collection<TemporalFeature> getAllTemporalFeatures() {
		return getOwningView().getAllTemporalFeatures();
	}
	
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllRootFeatures() {
		return getOwningView().getAllRootFeatures();
	}
	
	public Collection<TemporalGroup> getAllTemporalGroups() {
		return getOwningView().getAllTemporalGroups();
	}
	
	public TemporalFeature getTemporalFeature(Feature feature) {
		return getOwningView().getTemporalFeature(feature);
	}
	
	public TemporalGroup getTemporalGroup(Group group) {
		return getOwningView().getTemporalGroup(group);
	}

	/**
	 * Temporal Points should be in ascending order, i.e., the greater the index, the more recent the temporal point
	 * @return
	 */
	public List<TemporalPoint> getAllTemporalPoints() {
		return getOwningView().getAllTemporalPoints();
	}
}
