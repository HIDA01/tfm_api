package api.tfm;

import java.util.Collection;
import java.util.LinkedList;
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

public abstract class TFMViewCoreLogic extends TFMViewListenerSupport {

	@Override
	public FeatureModel getFeatureModelAt(TemporalPoint temporalPoint) {
		return doGetFeatureModelAt(temporalPoint);
	}

	protected abstract FeatureModel doGetFeatureModelAt(TemporalPoint temporalPoint);

	@Override
	public Collection<Feature> getAllFeaturesAt(TemporalPoint temporalPoint) {
		return doGetAllFeaturesAt(temporalPoint);
	}

	protected abstract Collection<Feature> doGetAllFeaturesAt(TemporalPoint temporalPoint);

	@Override
	public Feature getFeatureAt(String identifier, TemporalPoint temporalPoint) {
		if (identifier == null) {
			return null;
		}

		return doGetFeatureAt(identifier, temporalPoint);
	}

	protected abstract Feature doGetFeatureAt(String identifier, TemporalPoint temporalPoint);

	@Override
	public Feature getRootFeatureAt(TemporalPoint temporalPoint) {
		return doGetRootFeatureAt(temporalPoint);
	}

	protected abstract Feature doGetRootFeatureAt(TemporalPoint temporalPoint);
	
	@Override
	public Collection<Group> getAllGroupsAt(TemporalPoint temporalPoint) {
		return doGetAllGroupsAt(temporalPoint);
	}
	
	protected abstract Collection<Group> doGetAllGroupsAt(TemporalPoint temporalPoint);

	@Override
	public TemporalFeatureModel getTemporalFeatureModel() {
		return doGetTemporalFeatureModel();
	}

	protected abstract TemporalFeatureModel doGetTemporalFeatureModel();

	@Override
	public Collection<TemporalFeature> getAllTemporalFeatures() {
		return doGetAllTemporalFeatures();
	}

	protected abstract Collection<TemporalFeature> doGetAllTemporalFeatures();

	@Override
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllRootFeatures() {
		return doGetAllRootFeatures();
	}

	protected abstract Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllRootFeatures();
	
	@Override
	public Collection<TemporalGroup> getAllTemporalGroups() {
		return doGetAllTemporalGroups();
	}
	
	protected abstract Collection<TemporalGroup> doGetAllTemporalGroups();

	@Override
	public List<TemporalPoint> getAllTemporalPoints() {
		return doGetAllTemporalPoints();
	}

	protected abstract List<TemporalPoint> doGetAllTemporalPoints();

	@Override
	public boolean isRootFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return false;
		}

		TemporalFeature temporalFeature = getTemporalFeature(feature);

		Feature rootFeature = getRootFeatureAt(temporalPoint);

		if (rootFeature == null) {
			return false;
		}

		TemporalFeature temporalRootFeature = getTemporalFeature(rootFeature);
		return areIdenticalFeatures(temporalFeature, temporalRootFeature);
	}

	protected abstract boolean areIdenticalFeatures(TemporalFeature feature1, TemporalFeature feature2);

	@Override
	public Group getFeatureParentGroupAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return null;
		}

		return doGetFeatureParentGroupAt(feature, temporalPoint);
	}

	protected abstract Group doGetFeatureParentGroupAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public Feature getFeatureParentFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return null;
		}

		return doGetFeatureParentFeatureAt(feature, temporalPoint);
	}

	protected abstract Feature doGetFeatureParentFeatureAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public Group getFeatureDefaultChildGroupAt(Feature parentFeature, TemporalPoint temporalPoint) {
		if (parentFeature == null) {
			return null;
		}

		return doGetFeatureDefaultChildGroupAt(parentFeature, temporalPoint);
	}

	protected abstract Group doGetFeatureDefaultChildGroupAt(Feature parentFeature, TemporalPoint temporalPoint);

	@Override
	public List<Group> getFeatureChildGroupsAt(Feature parentFeature, TemporalPoint temporalPoint) {
		if (parentFeature == null) {
			return null;
		}

		return doGetFeatureChildGroupsAt(parentFeature, temporalPoint);
	}

	protected abstract List<Group> doGetFeatureChildGroupsAt(Feature parentFeature, TemporalPoint temporalPoint);

	@Override
	public String getFeatureNameAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return null;
		}

		return doGetFeatureNameAt(feature, temporalPoint);
	}

	protected abstract String doGetFeatureNameAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public FeatureVariationType getFeatureVariationTypeAt(Feature feature, TemporalPoint temporalPoint) {		
		if (feature == null) {
			return null;
		}

		return doGetFeatureVariationTypeAt(feature, temporalPoint);
	}

	protected abstract FeatureVariationType doGetFeatureVariationTypeAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureParentGroups(
			TemporalFeature temporalFeature) {
		if (temporalFeature == null) {
			return null;
		}

		return doGetAllFeatureParentGroups(temporalFeature);
	}

	protected abstract Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureParentGroups(
			TemporalFeature temporalFeature);

	@Override
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllFeatureParentFeatures(
			TemporalFeature temporalFeature) {
		if (temporalFeature == null) {
			return null;
		}

		return doGetAllFeatureParentFeatures(temporalFeature);
	}

	protected abstract Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllFeatureParentFeatures(
			TemporalFeature temporalFeature);

	@Override
	public Collection<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureDefaultChildGroups(
			TemporalFeature parentTemporalFeature) {
		if (parentTemporalFeature == null) {
			return null;
		}

		return doGetAllFeatureDefaultChildGroups(parentTemporalFeature);
	}

	protected abstract Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureDefaultChildGroups(
			TemporalFeature parentTemporalFeature);

	@Override
	public List<TemporalRangeElementTuple<TemporalGroup>> getAllFeatureChildGroups(
			TemporalFeature parentFeature) {
		if (parentFeature == null) {
			return null;
		}

		return doGetAllFeatureChildGroups(parentFeature);
	}

	protected abstract List<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureChildGroups(
			TemporalFeature parentFeature);

	@Override
	public Collection<TemporalRangeElementTuple<String>> getAllFeatureNames(TemporalFeature temporalFeature) {
		if (temporalFeature == null) {
			return null;
		}

		return doGetAllFeatureNames(temporalFeature);
	}

	protected abstract Collection<TemporalRangeElementTuple<String>> doGetAllFeatureNames(TemporalFeature feature);

	@Override
	public Collection<TemporalFeatureVariationType> getAllFeatureVariationTypes(TemporalFeature temporalFeature) {
		if (temporalFeature == null) {
			return null;
		}

		return doGetAllFeatureVariationTypes(temporalFeature);
	}

	protected abstract Collection<TemporalFeatureVariationType> doGetAllFeatureVariationTypes(
			TemporalFeature temporalFeature);

	@Override
	public TemporalFeature getTemporalFeature(Feature feature) {
		if (feature == null) {
			return null;
		}

		return doGetTemporalFeature(feature);
	}

	protected abstract TemporalFeature doGetTemporalFeature(Feature feature);

	@Override
	public Feature getFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		if (temporalFeature == null) {
			return null;
		}

		return doGetFeatureAt(temporalFeature, temporalPoint);
	}

	protected abstract Feature doGetFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint);

	@Override
	public List<Feature> getGroupChildFeaturesAt(Group group, TemporalPoint temporalPoint) {
		if (group == null) {
			return null;
		}

		return doGetGroupChildFeaturesAt(group, temporalPoint);
	}

	protected abstract List<Feature> doGetGroupChildFeaturesAt(Group group, TemporalPoint temporalPoint);

	@Override
	public Feature getGroupParentFeatureAt(Group group, TemporalPoint temporalPoint) {
		if (group == null) {
			return null;
		}

		return doGetGroupParentFeatureAt(group, temporalPoint);
	}

	protected abstract Feature doGetGroupParentFeatureAt(Group group, TemporalPoint temporalPoint);

	@Override
	public GroupVariationType getGroupVariationTypeAt(Group group, TemporalPoint temporalPoint) {
		// TODO MN: what to do with mandatory features if group variation type is OR/XOR? Change their type to optional? This is currently done in the DarwinSPL instatiation. Could be done here?
		
		if (group == null) {
			return null;
		}

		return doGetGroupVariationTypeAt(group, temporalPoint);
	}

	protected abstract GroupVariationType doGetGroupVariationTypeAt(Group group, TemporalPoint temporalPoint);

	@Override
	public List<TemporalRangeElementTuple<TemporalFeature>> getAllGroupChildFeatures(
			TemporalGroup temporalGroup) {
		if (temporalGroup == null) {
			return null;
		}

		return doGetAllGroupChildFeatures(temporalGroup);
	}

	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	protected abstract List<TemporalRangeElementTuple<TemporalFeature>> doGetAllGroupChildFeatures(
			TemporalGroup temporalGroup);

	@Override
	public Collection<TemporalRangeElementTuple<TemporalFeature>> getAllGroupParentFeatures(
			TemporalGroup temporalGroup) {
		if (temporalGroup == null) {
			return null;
		}

		return doGetAllGroupParentFeatures(temporalGroup);
	}

	protected abstract Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllGroupParentFeatures(
			TemporalGroup temporalGroup);

	@Override
	public Collection<TemporalGroupVariationType> getAllGroupVariationTypes(TemporalGroup temporalGroup) {
		if (temporalGroup == null) {
			return null;
		}

		return doGetAllGroupVariationTypes(temporalGroup);
	}

	protected abstract Collection<TemporalGroupVariationType> doGetAllGroupVariationTypes(TemporalGroup temporalGroup);

	@Override
	public TemporalGroup getTemporalGroup(Group group) {
		if (group == null) {
			return null;
		}

		return doGetTemporalGroup(group);
	}

	protected abstract TemporalGroup doGetTemporalGroup(Group group);

	@Override
	public Group getGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		if (temporalGroup == null) {
			return null;
		}

		return doGetGroupAt(temporalGroup, temporalPoint);
	}

	protected abstract Group doGetGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint);

	
	// Write
	
	@Override
	public void setRootFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}

		Feature oldRootFeature = getRootFeatureAt(temporalPoint);
		
		doSetRootFeatureAt(feature, temporalPoint);
		
		Feature newRootFeature = getRootFeatureAt(temporalPoint);
		if (getFeatureVariationTypeAt(newRootFeature, temporalPoint).equals(FeatureVariationType.OPTIONAL)) {
			setFeatureVariationTypeAt(newRootFeature, FeatureVariationType.MANDATORY, temporalPoint);
		}
		
		if (newRootFeature != oldRootFeature) {
			fireRootFeatureChanged(oldRootFeature, newRootFeature, temporalPoint);
		}
	}

	protected abstract void doSetRootFeatureAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public Feature createFeatureAt(String identifier, TemporalPoint temporalPoint) {
		if (identifier == null) {
			return null; // TODO or should we use a default identifier?
		}
		
		//TODO: Determine this
		boolean identifierIsValid = true;
		boolean identifierAlreadyExists = false;
		
		if (!identifierIsValid) {
			throwPreconditionViolatedException("The provided identifier \"" + identifier + "\" is not valid.");
		}
		
		if (identifierAlreadyExists) {
			throwPreconditionViolatedException("The provided identifier \"" + identifier + "\" already exists.");
		}

		Feature feature = doCreateFeatureAt(identifier, temporalPoint);
		
		if (feature != null) {
			fireFeatureCreated(feature, temporalPoint);
		}
		
		return feature;
	}

	protected abstract Feature doCreateFeatureAt(String identifier, TemporalPoint temporalPoint);

	@Override
	public Group createGroupAt(TemporalPoint temporalPoint) {
		Group group = doCreateGroupAt(temporalPoint);
		
		if (group != null) {
			fireGroupCreated(group, temporalPoint);
		}
		
		return group;
	}

	protected abstract Group doCreateGroupAt(TemporalPoint temporalPoint);

	@Override
	public void addFeatureAt(Feature feature, Feature parentFeature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}
		if (parentFeature == null) {
			return;
		}
		
		Group oldParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature oldParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
	
		//If old parent feature already set, this is not an add but a move!
		if (oldParentGroup != null || oldParentFeature != null) {
			throwPreconditionViolatedException("Feature to add may not already have a parent group/feature. Use move instead.");
		}

		doAddFeatureAt(feature, parentFeature, temporalPoint);
		
		Group newParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature newParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureAdded(feature, newParentGroup, newParentFeature, temporalPoint);
		}
	}

	protected abstract void doAddFeatureAt(Feature feature, Feature parentFeature, TemporalPoint temporalPoint);

	@Override
	public void addFeatureAt(Feature feature, Group parentGroup, TemporalPoint temporalPoint) {
		// TODO MN: what to do when group type != AND but feature type is mandatory? See also DarwinSPL instantiation.
		
		if (feature == null) {
			return;
		}
		if (parentGroup == null) {
			return;
		}
		
		Group oldParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature oldParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);

		doAddFeatureAt(feature, parentGroup, temporalPoint);
		
		Group newParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature newParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureAdded(feature, newParentGroup, newParentFeature, temporalPoint);
		}
	}
	
	protected abstract void doAddFeatureAt(Feature feature, Group parentGroup, TemporalPoint temporalPoint);

	@Override
	public void removeFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}
		
		Group oldParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature oldParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
		
		Collection<Group> childGroups = getFeatureChildGroupsAt(feature, temporalPoint);
		childGroups.forEach(childGroup -> removeGroupAt(childGroup, temporalPoint));
		
		doRemoveFeatureAt(feature, temporalPoint);
		
		Group newParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature newParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
		
		//New parents must no longer exist
		if (newParentGroup == null && newParentFeature == null) {
			fireFeatureRemoved(feature, oldParentGroup, oldParentFeature, temporalPoint);
		}
	}
	
	protected abstract void doRemoveFeatureAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public void moveFeatureAt(Feature feature, Group targetGroup, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}
		if (targetGroup == null) {
			return;
		}
		
		doMoveFeatureAt(feature, targetGroup, temporalPoint);
	}
	

	@Override
	public void moveFeatureAt(Feature feature, Feature targetFeature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}
		if (targetFeature == null) {
			return;
		}
		
		Group targetGroup = getFeatureDefaultChildGroupAt(targetFeature, temporalPoint);
		
		if (targetGroup == null) {
			//Default group may be non-existent.
			//TODO: create manually if this is the case? Might be tricky with undo.
//			targetGroup = createDefaultChildGroup(feature);
		}
		
		doMoveFeatureAt(feature, targetGroup, temporalPoint);
	}
	
	protected void doMoveFeatureAt(Feature feature, Group targetGroup, TemporalPoint temporalPoint) {
		Group oldParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature oldParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);

		//Calling internal methods so that no separate notifications are fired.
		doDetachFeatureFromParentAt(feature, temporalPoint);
		doAddFeatureAt(feature, targetGroup, temporalPoint);
		
		Group newParentGroup = getFeatureParentGroupAt(feature, temporalPoint);
		Feature newParentFeature = getFeatureParentFeatureAt(feature, temporalPoint);
		
		if (newParentGroup != oldParentGroup || newParentFeature != oldParentFeature) {
			fireFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature, temporalPoint);
		}
		
	}
	
	/**
	 * This method means that the relation between the feature and its containing group should be removed (i.e., remove the feature from the feature model hierarchy).
	 * The hierarchical state of that feature is the same as if the feature would have been newly created.
	 * @param feature
	 * @param temporalPoint
	 */
	protected abstract void doDetachFeatureFromParentAt(Feature feature, TemporalPoint temporalPoint);
	
	@Override
	public Group createDefaultChildGroupAt(Feature feature, TemporalPoint temporalPoint) {
		if (feature == null) {
			return null;
		}
		
		Group oldDefaultGroup = getFeatureDefaultChildGroupAt(feature, temporalPoint);
		
		if (oldDefaultGroup != null) {
			throwPreconditionViolatedException("Default group already exists.");
		}
		
		Group defaultGroup = doCreateDefaultChildGroupAt(feature, temporalPoint);
		
		//TODO: The default child group has to be added to the feature somewhere:
		//a) delegated to user doing it manually via feature.addGroup();
		//b) doing it automatically with the risk of having a non-atomic operation
		//c) having different behavior in the API and the feature abstraction where the API merely creates the group and the abstract creates and automatically adds it. (use different names in this case)
		//Be sure to fire appropriate notifications.
		if (defaultGroup != null) {
			fireGroupCreated(defaultGroup, temporalPoint);
		}
		
		return defaultGroup;
	}
	
	protected abstract Group doCreateDefaultChildGroupAt(Feature feature, TemporalPoint temporalPoint);

	@Override
	public void setFeatureNameAt(Feature feature, String name, TemporalPoint temporalPoint) {
		if (feature == null) {
			return;
		}
		if (name == null) {
			return;
		}
		
		//TODO: Determine this
		boolean identifierIsValid = true;
		boolean identifierAlreadyExists = false;
		
		if (!identifierIsValid) {
			throwPreconditionViolatedException("The provided name \"" + name + "\" is not valid.");
		}
		
		if (identifierAlreadyExists) {
			throwPreconditionViolatedException("The provided name \"" + name + "\" already exists.");
		}
		
		String oldName = getFeatureNameAt(feature, temporalPoint);
		
		doSetFeatureNameAt(feature, name, temporalPoint);
		
		String newName = getFeatureNameAt(feature, temporalPoint);
		
		if (newName != null && !newName.equals(oldName)) {
			fireFeatureNameChanged(feature, oldName, newName, temporalPoint);
		}
	}

	protected abstract void doSetFeatureNameAt(Feature feature, String name, TemporalPoint temporalPoint);
	
	@Override
	public void setFeatureVariationTypeAt(Feature feature, FeatureVariationType variationType,
			TemporalPoint temporalPoint) {
		// TODO MN: what to do if group type is OR/XOR and feature type is changed to mandatory? Keep consistent with setGroupTypeAt
		
		if (feature == null) {
			return;
		}
		if (variationType == null) {
			return;
		}
		
		if (variationType == FeatureVariationType.NON_STANDARD) {
			throwPreconditionViolatedException("Non-standard feature variation type cannot be set.");
		}
		
		FeatureVariationType oldVariationType = getFeatureVariationTypeAt(feature, temporalPoint);
		
		doSetFeatureVariationTypeAt(feature, variationType, temporalPoint);
		
		FeatureVariationType newVariationType = getFeatureVariationTypeAt(feature, temporalPoint);
		
		if (newVariationType != oldVariationType) {
			fireFeatureVariationTypeChanged(feature, oldVariationType, newVariationType, temporalPoint);
		}
	}
	
	protected abstract void doSetFeatureVariationTypeAt(Feature feature, FeatureVariationType variationType,
			TemporalPoint temporalPoint);

	@Override
	public void addGroupAt(Group group, Feature parentFeature, TemporalPoint temporalPoint) {
		if (group == null) {
			return;
		}
		if (parentFeature == null) {
			return;
		}
		
		Feature oldParentFeature = getGroupParentFeatureAt(group, temporalPoint);
	
		//If old parent feature already set, this is not an add but a move!
		if (oldParentFeature != null) {
			throwPreconditionViolatedException("Group to add may not already have a parent feature. Use move instead.");
		}
		
		doAddGroupAt(group, parentFeature, temporalPoint);
		
		Feature newParentFeature = getGroupParentFeatureAt(group, temporalPoint);
		
		if (newParentFeature != oldParentFeature) {
			fireGroupAdded(group, newParentFeature, temporalPoint);
		}
	}
	
	protected abstract void doAddGroupAt(Group group, Feature parentFeature, TemporalPoint temporalPoint);

	@Override
	public void removeGroupAt(Group group, TemporalPoint temporalPoint) {
		if (group == null) {
			return;
		}
		
		Feature oldParentFeature = getGroupParentFeatureAt(group, temporalPoint);
		
		//Recursion
		Collection<Feature> childFeatures = getGroupChildFeaturesAt(group, temporalPoint);
		childFeatures.forEach(childFeature -> removeFeatureAt(childFeature, temporalPoint));
		
		doRemoveGroupAt(group, temporalPoint);
		
		Feature newParentFeature = getGroupParentFeatureAt(group, temporalPoint);
		
		//New parent must no longer exist.
		if (newParentFeature == null) {
			fireGroupRemoved(group, oldParentFeature, temporalPoint);
		}
	}
	
	protected abstract void doRemoveGroupAt(Group group, TemporalPoint temporalPoint);

	@Override
	public void moveGroupAt(Group group, Feature targetFeature, TemporalPoint temporalPoint) {
		if (group == null) {
			return;
		}
		if (targetFeature == null) {
			return;
		}

		Feature oldParentFeature = getGroupParentFeatureAt(group, temporalPoint);
		
		doMoveGroupAt(group, targetFeature, temporalPoint);
		
		Feature newParentFeature = getGroupParentFeatureAt(group, temporalPoint);
		
		if (newParentFeature != oldParentFeature) {
			fireGroupMoved(group, oldParentFeature, newParentFeature, temporalPoint);
		}
	}
	
	protected void doMoveGroupAt(Group group, Feature targetFeature, TemporalPoint temporalPoint) {
		//Calling internal methods so that no separate notifications are fired.
		doDetachGroupFromParentAt(group, temporalPoint);
		doAddGroupAt(group, targetFeature, temporalPoint);
	}

	/**
	 * This method means that the relation between parent feature and group should be removed (i.e., remove the group from the feature model hierarchy).
	 * The hierarchical state of that group is the same as if the group would have been newly created.
	 * @param group
	 * @param temporalPoint
	 */
	protected abstract void doDetachGroupFromParentAt(Group group, TemporalPoint temporalPoint);

	@Override
	public void setGroupVariationTypeAt(Group group, GroupVariationType variationType, TemporalPoint temporalPoint) {
		if (group == null) {
			return;
		}
		if (variationType == null) {
			return;
		}
		
		if (variationType == GroupVariationType.NON_STANDARD) {
			throwPreconditionViolatedException("Non-standard group variation type cannot be set.");
		}
		
		GroupVariationType oldVariationType = getGroupVariationTypeAt(group, temporalPoint);
		
		doSetGroupVariationTypeAt(group, variationType, temporalPoint);
		
		GroupVariationType newVariationType = getGroupVariationTypeAt(group, temporalPoint);
		
		if (newVariationType != oldVariationType) {
			fireGroupVariationTypeChanged(group, oldVariationType, newVariationType, temporalPoint);
		}
	}
	
	protected abstract void doSetGroupVariationTypeAt(Group group, GroupVariationType variationType, TemporalPoint temporalPoint);
	
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
