package api.fm;

import java.util.Collection;
import java.util.List;

import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * Interface providing read support for feature models.
 * 
 * @author Christoph Seidl, Michael Nieke
 */
public interface FMReadView {
	//Feature Model
	public FeatureModel getFeatureModel();
	public Collection<Feature> getAllFeatures();
	public Collection<Group> getAllGroups();
	
	public Feature getFeature(String identifier);
	public Feature getRootFeature();

	
	//Feature
	public boolean isRootFeature(Feature feature);
	
	public Group getFeatureParentGroup(Feature feature);
	public Feature getFeatureParentFeature(Feature feature);
	
	//Default: Return first defined group.
	/**
	 * This method is part of the read-API. Therefore, the default group MUST NOT be created lazily (because there would be a write in the read-API).
	 * If no default group exists, this should return null.
	 * 
	 * @param feature
	 * @return
	 */
	public Group getFeatureDefaultChildGroup(Feature feature);
	//NOTE: Features and groups are ordered! (e.g., for graphical editors) 
	public List<Group> getFeatureChildGroups(Feature feature);
	
	public String getFeatureName(Feature feature);
	public FeatureVariationType getFeatureVariationType(Feature feature);
	
	
	
	//Group
	
	//NOTE: Features and groups are ordered! (e.g., for graphical editors)
	public List<Feature> getGroupChildFeatures(Group group);
	public Feature getGroupParentFeature(Group group);
	
	public GroupVariationType getGroupVariationType(Group group);
}
