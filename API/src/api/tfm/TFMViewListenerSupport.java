package api.tfm;

import java.util.Collection;
import java.util.LinkedList;

import api.tfm.listener.TFMViewListener;
import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.util.TemporalPoint;

public abstract class TFMViewListenerSupport implements TFMView {
	private Collection<TFMViewListener> listeners;
	
	public TFMViewListenerSupport() {
		listeners = new LinkedList<TFMViewListener>();
	}
	
	
	// Adding / removing listeners

	@Override
	public void addListener(TFMViewListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(TFMViewListener listener) {
		listeners.remove(listener);
	}

	// Firing notifications
	
	protected void fireRootFeatureChanged(Feature oldRootFeature, Feature newRootFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onRootFeatureChange(getTemporalFeatureModel(), oldRootFeature, newRootFeature, temporalPoint));
		fireFeatureSetChanged(temporalPoint);
	}
	
	protected void fireFeatureCreated(Feature feature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureCreated(feature, temporalPoint));
		
		//NOTE: Do NOT fire feature set changed notification as the feature is not part of the feature model's structure yet.
		doFireFeatureChanged(feature, temporalPoint);
	}
	
	protected void fireGroupCreated(Group group, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupCreated(group, temporalPoint));
		
		//NOTE: Do NOT fire feature set changed notification as the group is not part of the feature model's structure yet.
		doFireGroupChanged(group, temporalPoint);
	}
	
	
	protected void fireFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureAdded(feature, newParentGroup, newParentFeature, temporalPoint));

		//Entails notification of parent group/feature!
		doFireFeatureChanged(feature, temporalPoint);
		
		fireFeatureSetChanged(temporalPoint);
	}

	protected void fireFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureRemoved(feature, oldParentGroup, oldParentFeature, temporalPoint));
		
		fireFeatureSetChanged(temporalPoint);
	}
	
	protected void fireFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature, temporalPoint));

		doFireGroupChanged(oldParentGroup, temporalPoint);
		//Entails notification of parent group/feature!
		doFireFeatureChanged(feature, temporalPoint);
		
		fireFeatureModelChanged(temporalPoint);
	}
	
	protected void fireFeatureNameChanged(Feature feature, String oldName, String newName, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureNameChanged(feature, oldName, newName, temporalPoint));
		fireFeatureChanged(feature, temporalPoint);
	}
	
	protected void fireFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureVariationTypeChanged(feature, oldVariationType, newVariationType, temporalPoint));
		fireFeatureChanged(feature, temporalPoint);
	}
	
	
	protected void fireGroupAdded(Group group, Feature newParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupAdded(group, newParentFeature, temporalPoint));
		
		fireFeatureSetChanged(temporalPoint);
	}

	protected void fireGroupRemoved(Group group, Feature oldParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupRemoved(group, oldParentFeature, temporalPoint));
		
		fireFeatureSetChanged(temporalPoint);
	}
	
	protected void fireGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupMoved(group, oldParentFeature, newParentFeature, temporalPoint));
		
		doFireFeatureChanged(oldParentFeature, temporalPoint);
		//Entails notification of parent feature!
		doFireGroupChanged(group, temporalPoint);
		
		fireFeatureModelChanged(temporalPoint);
	}
	
	protected void fireGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupVariationTypeChanged(group, oldVariationType, newVariationType, temporalPoint));
		fireGroupChanged(group, temporalPoint);
	}
	
	
	
	
	//Emergent
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param featureModel The changed feature model.
	 */
	protected void fireFeatureModelChanged(TemporalPoint temporalPoint) {
		doFireFeatureModelChanged(temporalPoint);
	}
	
	private void doFireFeatureModelChanged(TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureModelChanged(getTemporalFeatureModel(), temporalPoint));
	}
	
	
	protected void fireFeatureSetChanged(TemporalPoint temporalPoint) {
		doFireFeatureSetChanged(temporalPoint);
		fireFeatureModelChanged(temporalPoint);
	}
	
	private void doFireFeatureSetChanged(TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureSetChanged(getTemporalFeatureModel(), temporalPoint));
	}
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param feature The changed feature.
	 */
	//Recursive firing of notification _with_ notifying of feature model change.
	protected void fireFeatureChanged(Feature feature, TemporalPoint temporalPoint) {
		doFireFeatureChanged(feature, temporalPoint);
		
		doFireFeatureModelChanged(temporalPoint);
	}
	
	//Recursive firing of notification _without_ notifying of feature model change.
	private void doFireFeatureChanged(Feature feature, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onFeatureChanged(feature, temporalPoint));
		
		//Recursion
		Group parentGroup = feature.getParentGroup();
		
		if (parentGroup != null) {
			doFireGroupChanged(parentGroup, temporalPoint);
		} else {
			Feature parentFeature = feature.getParentFeature();
			
			if (parentFeature != null) {
				doFireFeatureChanged(parentFeature, temporalPoint);
			}
		}
	}
	
	
	/**
	 * This is an emergent notification that should only be fired in conjunction with another notification. 
	 * 
	 * @param group The changed group.
	 */
	//Recursive firing of notification _with_ notifying of feature model change.
	protected void fireGroupChanged(Group group, TemporalPoint temporalPoint) {
		doFireGroupChanged(group, temporalPoint);
		
		doFireFeatureModelChanged(temporalPoint);
	}
	
	//Recursive firing of notification _without_ notifying of feature model change.
	private void doFireGroupChanged(Group group, TemporalPoint temporalPoint) {
		listeners.forEach(listener -> listener.onGroupChanged(group, temporalPoint));
		
		//Recursion
		Feature parentFeature = group.getParentFeature();
		
		if (parentFeature != null) {
			doFireFeatureChanged(parentFeature, temporalPoint);
		}
	}
}
