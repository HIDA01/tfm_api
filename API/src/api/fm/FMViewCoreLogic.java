package api.fm;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * Realizes the core logic of the FM API, i.e., methods' pre and post conditions, their general logic and
 * notification support. The core functionality is independent of how the actual feature model is being
 * realized (i.e., there may be a feature model of a specific notation backing the core logic but there
 * even does not have to be a specific model at all, e.g., when writing an adapter).  
 * 
 * @author Christoph Seidl
 */
public abstract class FMViewCoreLogic extends FMViewListenerSupport {
	// Read
	
	//NOTE: Methods in this section are mere delegations to worker methods. This structure is present to a) be consistent with the implementation of the write methods
	//(and, thus, be consistent for the implementation of concrete sub classes) as well as b) to have control over future changes, such as adding further listeners.
	//get..() -> doGet..()

	@Override
	public FeatureModel getFeatureModel() {
		return doGetFeatureModel();
	}
	
	protected abstract FeatureModel doGetFeatureModel();
	
	@Override
	public Collection<Feature> getAllFeatures() {
		return doGetAllFeatures();
	}
	
	protected abstract Collection<Feature> doGetAllFeatures();
	
	@Override
	public Collection<Group> getAllGroups() {
		return doGetAllGroups();
	}
	
	//TODO: Implement!!!!
	protected abstract Collection<Group> doGetAllGroups();
	
	
	@Override
	public Feature getFeature(String identifier) {
		if (identifier == null) {
			return null;
		}
		
		return doGetFeature(identifier);
	}
	
	protected abstract Feature doGetFeature(String identifier);
	
	
	@Override
	public Feature getRootFeature() {
		return doGetRootFeature();
	}
	
	protected abstract Feature doGetRootFeature();
	
	
	@Override
	public boolean isRootFeature(Feature feature) {
		if (feature == null) {
			return false;
		}
		
		Feature rootFeature = getRootFeature();
		return areIdenticalFeatures(rootFeature, feature);
	}
	
	protected abstract boolean areIdenticalFeatures(Feature feature1, Feature feature2);
	
	
	@Override
	public Group getFeatureParentGroup(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureParentGroup(feature);
	}
	
	protected abstract Group doGetFeatureParentGroup(Feature feature);
	
	
	@Override
	public Feature getFeatureParentFeature(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureParentFeature(feature);
	}
	
	protected abstract Feature doGetFeatureParentFeature(Feature feature);
	
	
	@Override
	public Group getFeatureDefaultChildGroup(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureDefaultChildGroup(feature);
	}
	
	protected abstract Group doGetFeatureDefaultChildGroup(Feature feature);

	
	@Override
	public List<Group> getFeatureChildGroups(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureChildGroups(feature);
	}
	
	protected abstract List<Group> doGetFeatureChildGroups(Feature feature);
	
	
	@Override
	public String getFeatureName(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureName(feature);
	}
	
	protected abstract String doGetFeatureName(Feature feature);
	
	
	@Override
	public FeatureVariationType getFeatureVariationType(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		return doGetFeatureVariationType(feature);
	}
	
	protected abstract FeatureVariationType doGetFeatureVariationType(Feature feature);
	
	
	
	@Override
	public List<Feature> getGroupChildFeatures(Group group) {
		if (group == null) {
			return null;
		}
		
		return doGetGroupChildFeatures(group);
	}
	
	protected abstract List<Feature> doGetGroupChildFeatures(Group group);
	
	
	@Override
	public Feature getGroupParentFeature(Group group) {
		if (group == null) {
			return null;
		}
		
		return doGetGroupParentFeature(group);
	}
	
	protected abstract Feature doGetGroupParentFeature(Group group);
	
	
	@Override
	public GroupVariationType getGroupVariationType(Group group) {
		if (group == null) {
			return null;
		}
		
		return doGetGroupVariationType(group);
	}
	
	protected abstract GroupVariationType doGetGroupVariationType(Group group);
	
	
	
	// Write
	
	// NOTE: Methods in this section are written according to the following style:
	// if (<precondition violated>) throwPreconditionViolatedException();
	// <collect old state>
	// <perform action via do...() method>
	// <collect new state>
	// if (<postcondition satisfied, i.e., new state different from old state>) fireNotification(element, oldState, newState);
	
	// TODO MN: I have the impression that returning null and throwPreconditionViolatedException are not used consistently
	
	@Override
	public void setRootFeature(Feature feature) {
		if (feature == null) {
			throwPreconditionViolatedException();
		}
		
		FeatureModel featureModel = getFeatureModel();
		Feature oldRootFeature = featureModel.getRootFeature();
		
		doSetRootFeature(feature);
		
		Feature newRootFeature = featureModel.getRootFeature();
		
		//If necessary, make new root feature mandatory.
		newRootFeature.setVariationType(FeatureVariationType.MANDATORY);
		
		if (newRootFeature != oldRootFeature) {
			fireRootFeatureChanged(featureModel, oldRootFeature, newRootFeature);
		}
	}
	
	protected abstract void doSetRootFeature(Feature feature);
	
	
	/**
	 * Creates a feature not yet integrated into the tree-hierarchy of the feature model. Be sure to add the feature at an appropriate location.
	 */
	//NOTE CS: While creating features without a context to a parent feature/group seems odd, e.g., GEF requires to possibility to do this.
	@Override
	public Feature createFeature(String identifier) {
		//TODO: Determine this
		boolean identifierIsValid = true;
		boolean identifierAlreadyExists = false;
		
		if (!identifierIsValid) {
			throwPreconditionViolatedException("The provided identifier \"" + identifier + "\" is not valid.");
		}
		
		if (identifierAlreadyExists) {
			throwPreconditionViolatedException("The provided identifier \"" + identifier + "\" already exists.");
		}
		
		Feature feature = doCreateFeature(identifier);
		
		if (feature != null) {
			fireFeatureCreated(feature);
		}
		
		return feature;
	}
	
	protected abstract Feature doCreateFeature(String identifier);
	
	
	@Override
	public Group createGroup() {
		Group group = doCreateGroup();
		
		if (group != null) {
			fireGroupCreated(group);
		}
		
		return group;
	}

	protected abstract Group doCreateGroup();
	
	
	
	@Override
	public void addFeature(Feature feature, Group parentGroup) {
		if (feature == null || parentGroup == null) {
			throwPreconditionViolatedException();
		}
		
		Group oldParentGroup = feature.getParentGroup();
		Feature oldParentFeature = feature.getParentFeature();
	
		//If old parent feature already set, this is not an add but a move!
		if (oldParentGroup != null || oldParentFeature != null) {
			throwPreconditionViolatedException("Feature to add may not already have a parent group/feature. Use move instead.");
		}
		
		doAddFeature(feature, parentGroup);
		
		Group newParentGroup = feature.getParentGroup();
		Feature newParentFeature = feature.getParentFeature();
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureAdded(feature, newParentGroup, newParentFeature);
		}
	}
	
	protected abstract void doAddFeature(Feature feature, Group parentGroup);
	
	@Override
	public void addFeature(Feature feature, Feature parentFeature) {
		if (feature == null || parentFeature == null) {
			throwPreconditionViolatedException();
		}
		
		Group oldParentGroup = feature.getParentGroup();
		Feature oldParentFeature = feature.getParentFeature();
	
		//If old parent feature already set, this is not an add but a move!
		if (oldParentGroup != null || oldParentFeature != null) {
			throwPreconditionViolatedException("Feature to add may not already have a parent group/feature. Use move instead.");
		}
		
		Group defaultGroup = parentFeature.getDefaultChildGroup();
		
		if (defaultGroup == null) {
			throwPreconditionViolatedException("Default group of parent feature does not exist. Use create default group.");
		}
		
		doAddFeature(feature, defaultGroup);
		
		Group newParentGroup = feature.getParentGroup();
		Feature newParentFeature = feature.getParentFeature();
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureAdded(feature, newParentGroup, newParentFeature);
		}
	}
	
	
	@Override
	public void removeFeature(Feature feature) {
		if (feature == null) {
			throwPreconditionViolatedException();
		}
		
		Group oldParentGroup = feature.getParentGroup();
		Feature oldParentFeature = feature.getParentFeature();
		
		
		//Recursion
		Collection<Group> childGroups = new LinkedList<>();
		childGroups.addAll(feature.getChildGroups());
		childGroups.forEach(childGroup -> removeGroup(childGroup));
		
		
		doRemoveFeature(feature);
		
//		Group newParentGroup = feature.getParentGroup();
//		Feature newParentFeature = feature.getParentFeature();
		
		Group newParentGroup = null;
		Feature newParentFeature = null;
		
		try {
			newParentGroup = feature.getParentGroup();
		} catch (Exception e) {
		}
		
		try {
			newParentFeature = feature.getParentFeature();
		} catch (Exception e) {
		}
		
		//New parents must no longer exist
		if (newParentGroup == null && newParentFeature == null) {
			fireFeatureRemoved(feature, oldParentGroup, oldParentFeature);
		}
	}
	
	protected abstract void doRemoveFeature(Feature feature);
	
	
	@Override
	public void moveFeature(Feature feature, Group targetGroup) {
		if (feature == null || targetGroup == null) {
			throwPreconditionViolatedException();
		}
		
		Group oldParentGroup = feature.getParentGroup();
		Feature oldParentFeature = feature.getParentFeature();
		
		doMoveFeature(feature, targetGroup);
		
		Group newParentGroup = feature.getParentGroup();
		Feature newParentFeature = feature.getParentFeature();
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature);
		}
	}
	
	@Override
	public void moveFeature(Feature feature, Feature targetFeature) {
		if (feature == null || targetFeature == null) {
			throwPreconditionViolatedException();
		}
		
		Group oldParentGroup = feature.getParentGroup();
		Feature oldParentFeature = feature.getParentFeature();
		
		Group targetGroup = targetFeature.getDefaultChildGroup();
		
		if (targetGroup == null) {
			//Default group may be non-existent.
			//TODO: create manually if this is the case? Might be tricky with undo.
//			targetGroup = createDefaultChildGroup(feature);
		}
		
		doMoveFeature(feature, targetGroup);
		
		Group newParentGroup = feature.getParentGroup();
		Feature newParentFeature = feature.getParentFeature();
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature);
		}
	}

	protected void doMoveFeature(Feature feature, Group targetGroup) {
		//Calling internal methods so that no separate notifications are fired.
		detachFeatureFromParent(feature);
		doAddFeature(feature, targetGroup);
	}
	
	//TODO: Should this detach from feature model if root feature?
	protected abstract void detachFeatureFromParent(Feature feature);
	
	
	@Override
	public Group createDefaultChildGroup(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		Group oldDefaultGroup = feature.getDefaultChildGroup();
		
		if (oldDefaultGroup != null) {
			throwPreconditionViolatedException("Default group already exists.");
		}
		
		Group defaultGroup = doCreateDefaultChildGroup(feature);
		
		//TODO: The default child group has to be added to the feature somewhere (choice has also impact on operation recorder!):
		//a) delegated to user doing it manually via feature.addGroup();
		//b) doing it automatically with the risk of having a non-atomic operation // MN: would be in favor. It seems that the recorder needs to differentiate between "normal" group creation and default group creation.
		//c) having different behavior in the API and the feature abstraction where the API merely creates the group and the abstract creates and automatically adds it. (use different names in this case)
		//Be sure to fire appropriate notifications. MN: adds needs to be fired as well?
		// TODO how to "replay" this using the recorder. It's currently not aware of default group creation.
		if (defaultGroup != null) {
			fireGroupCreated(defaultGroup);
		}
		
		return defaultGroup;
	}

	protected abstract Group doCreateDefaultChildGroup(Feature feature);
	
	
	@Override
	public void setFeatureName(Feature feature, String name) {
		if (feature == null || name == null) {
			throwPreconditionViolatedException();
		}
		
		String oldName = feature.getName();
		
		doSetFeatureName(feature, name);
		
		String newName = feature.getName();
		
		if (newName != null && !newName.equals(oldName)) {
			fireFeatureNameChanged(feature, oldName, newName);
		}
	}
	
	protected abstract void doSetFeatureName(Feature feature, String newName);
	
	
	@Override
	public void setFeatureVariationType(Feature feature, FeatureVariationType variationType) {
		//Cannot set non-standard variation type.
		if (feature == null || variationType == null) {
			throwPreconditionViolatedException();
		}
		
		if (variationType == FeatureVariationType.NON_STANDARD) {
			throwPreconditionViolatedException("Non-standard feature variation type cannot be set.");
		}
		
		FeatureVariationType oldVariationType = feature.getVariationType();
		
		doSetFeatureVariationType(feature, variationType);
		
		FeatureVariationType newVariationType = feature.getVariationType();
		
		if (newVariationType != oldVariationType) {
			fireFeatureVariationTypeChanged(feature, oldVariationType, newVariationType);
		}
	}
	
	protected abstract void doSetFeatureVariationType(Feature feature, FeatureVariationType featureVariationType);
	
	
	
	
	@Override
	public void addGroup(Group group, Feature parentFeature) {
		if (group == null || parentFeature == null) {
			throwPreconditionViolatedException();
		}
		
		Feature oldParentFeature = group.getParentFeature();
	
		//If old parent feature already set, this is not an add but a move!
		if (oldParentFeature != null) {
			throwPreconditionViolatedException("Group to add may not already have a parent feature. Use move instead.");
		}
		
		doAddGroup(group, parentFeature);
		
		Feature newParentFeature = group.getParentFeature();
		
		if (newParentFeature != oldParentFeature) {
			fireGroupAdded(group, oldParentFeature);
		}
	}
	
	protected abstract void doAddGroup(Group group, Feature parentFeature);
	

	@Override
	public void removeGroup(Group group) {
		if (group == null) {
			throwPreconditionViolatedException();
		}
		
		Feature oldParentFeature = group.getParentFeature();
		
		
		//Recursion
		Collection<Feature> childFeatures = new LinkedList<Feature>();
		childFeatures.addAll(group.getChildFeatures());
		childFeatures.forEach(childFeature -> removeFeature(childFeature));
		
		
		doRemoveGroup(group);
		
		Feature newParentFeature = group.getParentFeature();
		
		//New parent must no longer exist.
		if (newParentFeature == null) {
			fireGroupRemoved(group, oldParentFeature);
		}
	}
	
	protected abstract void doRemoveGroup(Group group);
	
	
	@Override
	public void moveGroup(Group group, Feature targetFeature) {
		if (group == null || targetFeature == null) {
			throwPreconditionViolatedException();
		}
		
		Feature oldParentFeature = group.getParentFeature();
		
		doMoveGroup(group, targetFeature);
		
		Feature newParentFeature = group.getParentFeature();
		
		if (newParentFeature != oldParentFeature) {
			fireGroupMoved(group, oldParentFeature, newParentFeature);
		}
	}

	protected void doMoveGroup(Group group, Feature targetFeature) {
		//Calling internal methods so that no separate notifications are fired.
		detachGroupFromParent(group);
		doAddGroup(group, targetFeature);
	}
	
	protected abstract void detachGroupFromParent(Group group);
	
	@Override
	public void setGroupVariationType(Group group, GroupVariationType variationType) {
		if (group == null || variationType == null) {
			throwPreconditionViolatedException();
		}
		
		if (variationType == GroupVariationType.NON_STANDARD) {
			throwPreconditionViolatedException("Non-standard group variation type cannot be set.");
		}
		
		GroupVariationType oldVariationType = group.getVariationType();
		
		doSetGroupVariationType(group, variationType);
		GroupVariationType newVariationType = group.getVariationType();
		
		if (newVariationType != oldVariationType) {
			fireGroupVariationTypeChanged(group, oldVariationType, newVariationType);
		}
	}
	
	protected abstract void doSetGroupVariationType(Group group, GroupVariationType variationType);
	
	
	
	// Auxiliary
	
	protected void throwPreconditionViolatedException() {
		throwPreconditionViolatedException("");
	}
	
	protected void throwPreconditionViolatedException(String message) {
		//TODO: Replace with something meaningful.
		throw new RuntimeException(message);
	}
	
	//Placed this high in the inheritance hierarchy for consistency's sake.
	protected List<Feature> createFeatureList() {
		return new LinkedList<Feature>();
	}
	
	//Placed this high in the inheritance hierarchy for consistency's sake.
	protected List<Group> createGroupList() {
		return new LinkedList<Group>();
	}
}
