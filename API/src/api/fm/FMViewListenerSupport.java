package api.fm;

import java.util.Collection;
import java.util.LinkedList;

import api.fm.listener.FMViewListener;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * Basic listener support.
 * 
 * @author Christoph Seidl
 *
 */
public abstract class FMViewListenerSupport implements FMView {
	private Collection<FMViewListener> listeners;
	
	public FMViewListenerSupport() {
		listeners = new LinkedList<FMViewListener>();
	}
	
	
	// Adding / removing listeners

	@Override
	public void addListener(FMViewListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(FMViewListener listener) {
		listeners.remove(listener);
	}
	
	
	// Firing notifications
	
	protected void fireRootFeatureChanged(FeatureModel featureModel, Feature oldRootFeature, Feature newRootFeature) {
		listeners.forEach(listener -> listener.onRootFeatureChanged(featureModel, oldRootFeature, newRootFeature));
		fireFeatureSetChanged(featureModel);
	}
	
	protected void fireFeatureCreated(Feature feature) {
		listeners.forEach(listener -> listener.onFeatureCreated(feature));
		
		//NOTE: Do NOT fire feature set changed notification as the feature is not part of the feature model's structure yet.
		doFireFeatureChanged(feature);
	}
	
	protected void fireGroupCreated(Group group) {
		listeners.forEach(listener -> listener.onGroupCreated(group));
		
		//NOTE: Do NOT fire feature set changed notification as the group is not part of the feature model's structure yet.
		doFireGroupChanged(group);
	}
	
	
	protected void fireFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature) {
		listeners.forEach(listener -> listener.onFeatureAdded(feature, newParentGroup, newParentFeature));

		//Entails notification of parent group/feature!
		doFireFeatureChanged(feature);
		
		FeatureModel featureModel = feature.getFeatureModel();
		fireFeatureSetChanged(featureModel);
	}

	protected void fireFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature) {
		listeners.forEach(listener -> listener.onFeatureRemoved(feature, oldParentGroup, oldParentFeature));
		
		FeatureModel featureModel = feature.getFeatureModel();
		doFireFeatureSetChanged(featureModel);
		
		if (oldParentGroup != null) {
			doFireGroupChanged(oldParentGroup);
		} else if (oldParentFeature != null) {
			doFireFeatureChanged(oldParentFeature);
		}
		
		fireFeatureModelChanged(featureModel);
	}
	
	protected void fireFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature) {
		listeners.forEach(listener -> listener.onFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature));

		doFireGroupChanged(oldParentGroup);
		//Entails notification of parent group/feature!
		doFireFeatureChanged(feature);
		
		FeatureModel featureModel = feature.getFeatureModel();
		fireFeatureModelChanged(featureModel);
	}
	
	protected void fireFeatureNameChanged(Feature feature, String oldName, String newName) {
		listeners.forEach(listener -> listener.onFeatureNameChanged(feature, oldName, newName));
		fireFeatureChanged(feature);
	}
	
	protected void fireFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType) {
		listeners.forEach(listener -> listener.onFeatureVariationTypeChanged(feature, oldVariationType, newVariationType));
		fireFeatureChanged(feature);
	}
	
	
	protected void fireGroupAdded(Group group, Feature oldParentFeature) {
		listeners.forEach(listener -> listener.onGroupAdded(group, oldParentFeature));
		
		FeatureModel featureModel = group.getFeatureModel();
		fireFeatureSetChanged(featureModel);
	}

	protected void fireGroupRemoved(Group group, Feature oldParentFeature) {
		listeners.forEach(listener -> listener.onGroupRemoved(group, oldParentFeature));
		
		FeatureModel featureModel = group.getFeatureModel();
		fireFeatureSetChanged(featureModel);
	}
	
	protected void fireGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature) {
		listeners.forEach(listener -> listener.onGroupMoved(group, oldParentFeature, newParentFeature));
		
		doFireFeatureChanged(oldParentFeature);
		//Entails notification of parent feature!
		doFireGroupChanged(group);
		
		FeatureModel featureModel = group.getFeatureModel();
		fireFeatureModelChanged(featureModel);
	}
	
	protected void fireGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType) {
		listeners.forEach(listener -> listener.onGroupVariationTypeChanged(group, oldVariationType, newVariationType));
		fireGroupChanged(group);
	}
	
	
	
	
	//Emergent
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param featureModel The changed feature model.
	 */
	protected void fireFeatureModelChanged(FeatureModel featureModel) {
		doFireFeatureModelChanged(featureModel);
	}
	
	private void doFireFeatureModelChanged(FeatureModel featureModel) {
		listeners.forEach(listener -> listener.onFeatureModelChanged(featureModel));
	}
	
	
	protected void fireFeatureSetChanged(FeatureModel featureModel) {
		doFireFeatureSetChanged(featureModel);
		fireFeatureModelChanged(featureModel);
	}
	
	private void doFireFeatureSetChanged(FeatureModel featureModel) {
		listeners.forEach(listener -> listener.onFeatureSetChanged(featureModel));
	}
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param feature The changed feature.
	 */
	//Recursive firing of notification _with_ notifying of feature model change.
	protected void fireFeatureChanged(Feature feature) {
		doFireFeatureChanged(feature);
		
		FeatureModel featureModel = feature.getFeatureModel();
		doFireFeatureModelChanged(featureModel);
	}
	
	//Recursive firing of notification _without_ notifying of feature model change.
	private void doFireFeatureChanged(Feature feature) {
		listeners.forEach(listener -> listener.onFeatureChanged(feature));
		
		//Recursion
		Group parentGroup = feature.getParentGroup();
		
		if (parentGroup != null) {
			doFireGroupChanged(parentGroup);
		} else {
			Feature parentFeature = feature.getParentFeature();
			
			if (parentFeature != null) {
				doFireFeatureChanged(parentFeature);
			}
		}
	}
	
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param group The changed group.
	 */
	//Recursive firing of notification _with_ notifying of feature model change.
	protected void fireGroupChanged(Group group) {
		doFireGroupChanged(group);
		
		FeatureModel featureModel = group.getFeatureModel();
		doFireFeatureModelChanged(featureModel);
	}
	
	//Recursive firing of notification _without_ notifying of feature model change.
	private void doFireGroupChanged(Group group) {
		listeners.forEach(listener -> listener.onGroupChanged(group));
		
		//Recursion
		Feature parentFeature = group.getParentFeature();
		
		if (parentFeature != null) {
			doFireFeatureChanged(parentFeature);
		}
	}

}
