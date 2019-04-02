package api.fm.listener.individual;

import fm.Feature;
import fm.Group;
import fm.GroupVariationType;

public interface FMViewGroupListener {
	// Groups
	public void onGroupAdded(Group group, Feature newParentFeature);
	public void onGroupRemoved(Group group, Feature oldParentFeature);
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature);
	
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType);
	
	
	/**
	 * This is an emergent notification that is fired when a property of a group is changed, e.g., the group's variation type, parent feature or child features. 
	 * 
	 * @param feature The changed feature.
	 */
	public void onGroupChanged(Group group);
}
