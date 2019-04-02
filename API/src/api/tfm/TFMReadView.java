package api.tfm;

import java.util.Collection;
import java.util.List;

import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalFeature;
import tfm.TemporalFeatureModel;
import tfm.TemporalFeatureVariationType;
import tfm.TemporalGroup;
import tfm.TemporalGroupVariationType;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRangeElementTuple;

/**
 * Interface providing read support for temporal feature models.
 * 
 * @author Christoph Seidl, Michael Nieke
 */
public interface TFMReadView {
	//Feature Model
	public FeatureModel getFeatureModelAt(TemporalPoint temporalPoint);
	public Collection<Feature> getAllFeaturesAt(TemporalPoint temporalPoint);
	public Collection<Group> getAllGroupsAt(TemporalPoint temporalPoint);
	public Feature getFeatureAt(String identifier, TemporalPoint temporalPoint);
	public Feature getRootFeatureAt(TemporalPoint temporalPoint);
	
	//Temporal Feature Model
	public TemporalFeatureModel getTemporalFeatureModel();
	public Collection<TemporalFeature> getAllTemporalFeatures();
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllRootFeatures();
	public Collection<TemporalGroup> getAllTemporalGroups();

	/**
	 * Temporal Points should be in ascending order, i.e., the greater the index, the more recent the temporal point
	 * @return
	 */
	public List<TemporalPoint> getAllTemporalPoints();
	
	//Feature
	public boolean isRootFeatureAt(Feature feature, TemporalPoint temporalPoint);
	
	public Group getFeatureParentGroupAt(Feature feature, TemporalPoint temporalPoint);
	public Feature getFeatureParentFeatureAt(Feature feature, TemporalPoint temporalPoint);
	
	//Default: Return first defined group.
	/**
	 * This method is part of the read-API. Therefore, the default group MUST NOT be created lazily (because there would be a write in the read-API).
	 * If no default group exists, this should return null.
	 * 
	 * @param feature
	 * @return
	 */
	public Group getFeatureDefaultChildGroupAt(Feature parentFeature, TemporalPoint temporalPoint);
	//NOTE: Features and groups are ordered! (e.g., for graphical editors) 
	public List<Group> getFeatureChildGroupsAt(Feature parentFeature, TemporalPoint temporalPoint);
	
	public String getFeatureNameAt(Feature feature, TemporalPoint temporalPoint);
	public FeatureVariationType getFeatureVariationTypeAt(Feature feature, TemporalPoint temporalPoint);
	
	//Temporal Feature
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureParentGroups(TemporalFeature temporalFeature);
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllFeatureParentFeatures(TemporalFeature temporalFeature);
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureDefaultChildGroups(TemporalFeature parentTemporalFeature);
	//NOTE: Features and groups are ordered! (e.g., for graphical editors) 
	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	public List<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureChildGroups(TemporalFeature temporalParentFeature);
	public Collection<TemporalRangeElementTuple<String>> getAllFeatureNames(TemporalFeature temporalFeature);
	public Collection<TemporalFeatureVariationType> getAllFeatureVariationTypes(TemporalFeature temporalFeature);
	public TemporalFeature getTemporalFeature(Feature feature);
	public Feature getFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint);
	
	//Group
	//NOTE: Features and groups are ordered! (e.g., for graphical editors)
	public List<Feature> getGroupChildFeaturesAt(Group group, TemporalPoint temporalPoint);
	public Feature getGroupParentFeatureAt(Group group, TemporalPoint temporalPoint);
	
	public GroupVariationType getGroupVariationTypeAt(Group group, TemporalPoint temporalPoint);

	//Temporal Group
	//NOTE: Features and groups are ordered! (e.g., for graphical editors)
	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	public List<TemporalRangeElementTuple<TemporalFeature>> getAllGroupChildFeatures(TemporalGroup temporalGroup);
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllGroupParentFeatures(TemporalGroup temporalGroup);
	public Collection<TemporalGroupVariationType> getAllGroupVariationTypes(TemporalGroup temporalGroup);
	public TemporalGroup getTemporalGroup(Group group);
	public Group getGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint);
//	public TemporalGroupVariationType getTemporalGroupVariationType(GroupVariationType groupVariationType); // TODO Not sure. What do we do if temporal group type has changed between two calls. Is it then still the same group type?
}
