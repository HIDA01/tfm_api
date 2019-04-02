package fm;

import java.util.Collection;

import api.fm.FMView;
import api.fm.listener.adapter.FMViewListener2FMViewFeatureModelListenersAdapter;
import api.fm.listener.individual.FMViewFeatureModelListener;

/**
 * Mere proxy element to communicate with the API. 
 * Method calls are forwarded to the respective instance of the API 
 * that instantiated the element, which are mirrored for convenient use.
 */
public class FeatureModel extends FMViewElement {
	private FMViewListener2FMViewFeatureModelListenersAdapter listenerAdapter;
	
	public FeatureModel(FMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
		listenerAdapter = new FMViewListener2FMViewFeatureModelListenersAdapter(this);
	}
	
	public Feature getRootFeature() {
		return getOwningView().getRootFeature();
	}
	
	public void setRootFeature(Feature feature) {
		getOwningView().setRootFeature(feature);
	}
	
	public Feature getFeature(String identifier) {
		return getOwningView().getFeature(identifier);
	}
	
	public Collection<Feature> getAllFeatures() {
		return getOwningView().getAllFeatures();
	}
	
	public Collection<Group> getAllGroup() {
		return getOwningView().getAllGroups();
	}
	
	public Feature createFeature(String identifier) {
		return getOwningView().createFeature(identifier);
	}
	
	
	public void addListener(FMViewFeatureModelListener listener) {
		listenerAdapter.addListener(listener);
	}
	
	public void removeListener(FMViewFeatureModelListener listener) {
		listenerAdapter.removeListener(listener);
	}
	
	@Override
	public String toString() {
		return "FeatureModel";
	}
}
