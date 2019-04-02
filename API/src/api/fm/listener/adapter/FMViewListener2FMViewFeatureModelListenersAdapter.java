package api.fm.listener.adapter;

import java.util.LinkedList;
import java.util.List;

import api.fm.FMView;
import api.fm.listener.FMViewAbstractListener;
import api.fm.listener.individual.FMViewFeatureModelListener;
import fm.Feature;
import fm.FeatureModel;
import fm.Group;

/**
 * Forwards all notifications of the API concerning the specified feature model.
 * 
 * @author Christoph Seidl
 */
public class FMViewListener2FMViewFeatureModelListenersAdapter extends FMViewAbstractListener implements FMViewListenerAdapter, FMViewFeatureModelListener {
	private FeatureModel featureModel;
	private List<FMViewFeatureModelListener> listeners;
	private boolean isActivated;
	
	public FMViewListener2FMViewFeatureModelListenersAdapter(FeatureModel featureModel) {
		this.featureModel = featureModel;
		listeners = new LinkedList<FMViewFeatureModelListener>();
		isActivated = false;
//		activate(); // Removed for lazy listening.
	}
	
	public void activate() {
		FMView fmView = featureModel.getOwningView();
		fmView.addListener(this);
		isActivated = true;
	}
	
	public void deactivate() {
		FMView fmView = featureModel.getOwningView();
		fmView.removeListener(this);
		isActivated = false;
	}
	
	public void addListener(FMViewFeatureModelListener listener) {
		if (!isActivated) {
			activate();
		}
		listeners.add(listener);
	}
	
	public void removeListener(FMViewFeatureModelListener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			deactivate();
		}
	}

	@Override
	public void onRootFeatureChanged(FeatureModel featureModel, Feature oldRootFeature, Feature newRootFeature) {
		if (featureModel.equals(this.featureModel)) {
			listeners.forEach(listener -> listener.onRootFeatureChanged(featureModel, oldRootFeature, newRootFeature));
		}
	}

	@Override
	public void onFeatureCreated(Feature feature) {
		if (featureModel.equals(this.featureModel)) {
			listeners.forEach(listener -> listener.onFeatureCreated(feature));
		}
	}

	@Override
	public void onGroupCreated(Group group) {
		if (featureModel.equals(this.featureModel)) {
			listeners.forEach(listener -> listener.onGroupCreated(group));
		}
	}

	@Override
	public void onFeatureModelChanged(FeatureModel featureModel) {
		if (featureModel.equals(this.featureModel)) {
			listeners.forEach(listener -> listener.onFeatureModelChanged(featureModel));
		}
	}

	@Override
	public void onFeatureSetChanged(FeatureModel featureModel) {
		if (featureModel.equals(this.featureModel)) {
			listeners.forEach(listener -> listener.onFeatureSetChanged(featureModel));
		}
	}
}
