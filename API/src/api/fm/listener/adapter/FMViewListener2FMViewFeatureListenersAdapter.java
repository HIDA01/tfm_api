package api.fm.listener.adapter;

import java.util.LinkedList;
import java.util.List;

import api.fm.FMView;
import api.fm.listener.FMViewAbstractListener;
import api.fm.listener.individual.FMViewFeatureListener;
import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;

/**
 * Forwards all notifications of the API concerning the specified feature.
 * 
 * @author Christoph Seidl
 */
public class FMViewListener2FMViewFeatureListenersAdapter extends FMViewAbstractListener implements FMViewListenerAdapter, FMViewFeatureListener {
	private Feature feature;
	private List<FMViewFeatureListener> listeners;
	private boolean isActivated;
	
	public FMViewListener2FMViewFeatureListenersAdapter(Feature feature) {
		this.feature = feature;
		listeners = new LinkedList<FMViewFeatureListener>();
		isActivated = false;
//		activate(); // Removed for lazy listening. 
	}
	
	public void activate() {
		FMView fmView = feature.getOwningView();
		fmView.addListener(this);
		isActivated = true;
	}
	
	public void deactivate() {
		FMView fmView = feature.getOwningView();
		fmView.removeListener(this);
		isActivated = false;
	}
	
	public void addListener(FMViewFeatureListener listener) {
		if (!isActivated) {
			activate();
		}
		listeners.add(listener);
	}
	
	public void removeListener(FMViewFeatureListener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			deactivate();
		}
	}
	
	@Override
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureAdded(feature, newParentGroup, newParentFeature));
		}
	}
	
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureRemoved(feature, oldParentGroup, oldParentFeature));
		}
	}
	
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature));
		}
	}
	
	public void onFeatureNameChanged(Feature feature, String oldName, String newName) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureNameChanged(feature, oldName, newName));
		}
	}
	
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureVariationTypeChanged(feature, oldVariationType, newVariationType));
		}
	}

	public void onFeatureChanged(Feature feature) {
		if (feature.equals(this.feature)) {
			listeners.forEach(listener -> listener.onFeatureChanged(feature));
		}
	}
}
