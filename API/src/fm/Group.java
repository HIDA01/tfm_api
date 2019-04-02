package fm;

import java.util.List;

import api.fm.FMView;
import api.fm.listener.adapter.FMViewListener2FMViewGroupListenersAdapter;
import api.fm.listener.individual.FMViewGroupListener;

/**
 * Mere proxy element to communicate with the API. 
 * Method calls are forwarded to the respective instance of the API 
 * that instantiated the element, which are mirrored for convenient use.
 */
public class Group extends FMViewElement {
	private FMViewListener2FMViewGroupListenersAdapter listenerAdapter;
	
	public Group(FMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
		
		listenerAdapter = new FMViewListener2FMViewGroupListenersAdapter(this);
	}
	
	
	public FeatureModel getFeatureModel() {
		return getOwningView().getFeatureModel();
	}
	
	
	public GroupVariationType getVariationType() {
		return getOwningView().getGroupVariationType(this);
	}
	
	public void setVariationType(GroupVariationType variationType) {
		getOwningView().setGroupVariationType(this, variationType);
	}
	
	
	public List<Feature> getChildFeatures() {
		return getOwningView().getGroupChildFeatures(this);
	}
	
	public Feature getParentFeature() {
		return getOwningView().getGroupParentFeature(this);
	}
	
	public void addFeature(Feature feature) {
		getOwningView().addFeature(feature, this);
	}
	
	//NOTE: Detach (unset the parent without deleting anything) seems to be superfluous as it is used only in move or remove operations which both are provided.
	
	public void remove() {
		getOwningView().removeGroup(this);
	}
	
	public void move(Feature targetFeature) {
		getOwningView().moveGroup(this, targetFeature);
	}
	
	
	public void addListener(FMViewGroupListener listener) {
		listenerAdapter.addListener(listener);
	}
	
	public void removeListener(FMViewGroupListener listener) {
		listenerAdapter.removeListener(listener);
	}
	
	@Override
	public String toString() {
		Feature parentFeature = getParentFeature();
		
		return "Group" + (parentFeature != null ? " below \"" + parentFeature.getName() + "\"" : "");
	}
}
