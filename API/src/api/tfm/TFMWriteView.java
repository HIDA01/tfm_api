package api.tfm;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.util.TemporalPoint;

/**
 * Interface providing write support for temporal feature models.
 * 
 * @author Michael Nieke, Christoph Seidl
 */
public interface TFMWriteView {
	// Feature Model
	public void setRootFeatureAt(Feature feature, TemporalPoint temporalPoint);
	
	public Feature createFeatureAt(String identifier, TemporalPoint temporalPoint);
	// TODO does this already have a type? Current DarwinSPL implementation: no
	public Group createGroupAt(TemporalPoint temporalPoint);
	
	//Feature
	public void addFeatureAt(Feature feature, Feature parentFeature, TemporalPoint temporalPoint);
	public void addFeatureAt(Feature feature, Group parentGroup, TemporalPoint temporalPoint);
	
	/**
	 * Removes the feature and, transitively, all its descendant features/groups.
	 * NOTE: MUST NOT remove potentially remaining (empty) parent group. 
	 */
	public void removeFeatureAt(Feature feature, TemporalPoint temporalPoint);
	public void moveFeatureAt(Feature feature, Group targetGroup, TemporalPoint temporalPoint);
	public void moveFeatureAt(Feature feature, Feature targetFeature, TemporalPoint temporalPoint);
	
	//TODO: Document whether this only creates the group or also adds it (which would be against API convention). Current DarwinSPL implementation: is added to parnet feature.
	public Group createDefaultChildGroupAt(Feature feature, TemporalPoint temporalPoint);
	
	public void setFeatureNameAt(Feature feature, String name, TemporalPoint temporalPoint);
	public void setFeatureVariationTypeAt(Feature feature, FeatureVariationType variationType, TemporalPoint temporalPoint);
	
	// Group
	public void addGroupAt(Group group, Feature parentFeature, TemporalPoint temporalPoint);

	/**
	 * Removes the group and, transitively, all its descendant features/groups.  
	 */
	public void removeGroupAt(Group group, TemporalPoint temporalPoint);
	public void moveGroupAt(Group group, Feature targetFeature, TemporalPoint temporalPoint);
	//NOTE: Target group does not make sense for moving groups so there is no dedicated method (unlike with features).
	
	public void setGroupVariationTypeAt(Group group, GroupVariationType variationType, TemporalPoint temporalPoint);
}
