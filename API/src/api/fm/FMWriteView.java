package api.fm;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * Interface providing write support for feature models.
 * 
 * @author Christoph Seidl, Michael Nieke
 */
public interface FMWriteView {
	//Feature Model
	
	/**
	 * After setting the root feature, the feature has to be mandatory!
	 * @param feature
	 */
	public void setRootFeature(Feature feature);
	
	/**
	 * Feature has to be created as OPTIONAL.
	 */
	public Feature createFeature(String identifier);
	
	/**
	 * Group has to be created as AND.
	 */
	public Group createGroup();
	
	
	//Feature
	
	public void addFeature(Feature feature, Group parentGroup);
	public void addFeature(Feature feature, Feature parentFeature);
	
	/**
	 * Removes the feature and, transitively, all its descendant features/groups.
	 * NOTE: MUST NOT remove potentially remaining (empty) parent group. 
	 */
	public void removeFeature(Feature feature);
	public void moveFeature(Feature feature, Group targetGroup);
	public void moveFeature(Feature feature, Feature targetFeature);
	
	//TODO: Document whether this only creates the group or also adds it (which would be against API convention)
	public Group createDefaultChildGroup(Feature feature);	
	
	public void setFeatureName(Feature feature, String name);
	public void setFeatureVariationType(Feature feature, FeatureVariationType variationType);

	
	//Group
	public void addGroup(Group group, Feature parentFeature);
	
	/**
	 * Removes the group and, transitively, all its descendant features/groups.  
	 */
	public void removeGroup(Group group);
	public void moveGroup(Group group, Feature targetFeature);
	//NOTE: Target group does not make sense for moving groups so there is no dedicated method (unlike with features).
	
	public void setGroupVariationType(Group group, GroupVariationType variationType);
}
