package fm;

import java.util.List;

import api.fm.FMView;
import api.fm.listener.adapter.FMViewListener2FMViewFeatureListenersAdapter;
import api.fm.listener.individual.FMViewFeatureListener;

/**
 * Mere proxy element to communicate with the API. 
 * Method calls are forwarded to the respective instance of the API 
 * that instantiated the element, which are mirrored for convenient use.
 */
public class Feature extends FMViewElement {
	private FMViewListener2FMViewFeatureListenersAdapter listenerAdapter;
	
	public Feature(FMView owningView, Object notationSpecificElement) {
		super(owningView, notationSpecificElement);
		listenerAdapter = new FMViewListener2FMViewFeatureListenersAdapter(this);
	}
	
	public FeatureModel getFeatureModel() {
		return getOwningView().getFeatureModel();
	}
	
	
	public String getName() {
		return getOwningView().getFeatureName(this);
	}
	
	public void setName(String name) {
		getOwningView().setFeatureName(this, name);
	}
	
	public FeatureVariationType getVariationType() {
		return getOwningView().getFeatureVariationType(this);
	}
	
	public void setVariationType(FeatureVariationType variationType) {
		getOwningView().setFeatureVariationType(this, variationType);
	}
	
	
	public boolean isRootFeature() {
		return getOwningView().isRootFeature(this);
	}
	
	public Group getDefaultChildGroup() {
		return getOwningView().getFeatureDefaultChildGroup(this);
	}
	
	public Group createDefaultChildGroup() {
		return getOwningView().createDefaultChildGroup(this);
	}
	
	public List<Group> getChildGroups() {
		return getOwningView().getFeatureChildGroups(this);
	}

	//NOTE: Do NOT provide getChildFeatures() as this would produce inconsistent results when more than one group exists. Use getDefaultGroup().getChildFeatures() instead.
	
	public Group getParentGroup() {
		return getOwningView().getFeatureParentGroup(this);
	}

	public Feature getParentFeature() {
		return getOwningView().getFeatureParentFeature(this);
	}
	
	
	//NOTE: Detach (unset the parent without deleting anything) seems to be superfluous as it is used only in move or remove operations which both are provided.
	
	public void remove() {
		getOwningView().removeFeature(this);
	}
	
	public void move(Group targetGroup) {
		getOwningView().moveFeature(this, targetGroup);
	}
	
	public void move(Feature targetFeature) {
		getOwningView().moveFeature(this, targetFeature);
	}
	
	/**
	 * Adds the parameter feature as child of this feature (i.e.,
	 * within this feature's default group).
	 * 
	 * @param feature
	 */
	public void addFeature(Feature feature) {
		getOwningView().addFeature(feature, this);
	}
	
	//NOTE: Keep this for the moment in case we have more convenience methods.
//	public void addFeature(Feature feature) {
//		Group defaultGroup = getDefaultGroup();
//		
//		if (defaultGroup == null) {
//			defaultGroup = createDefaultGroup();
//		}
//		
//		getOwningView().addFeature(feature, this);
//	}
	
	/**
	 * Adds the parameter group as child of this feature (i.e.,
	 * within this feature's default group).
	 * 
	 * @param group
	 */
	public void addGroup(Group group) {
		getOwningView().addGroup(group, this);
	}
	
	
	public void addListener(FMViewFeatureListener listener) {
		listenerAdapter.addListener(listener);
	}
	
	public void removeListener(FMViewFeatureListener listener) {
		listenerAdapter.removeListener(listener);
	}
	
	@Override
	public String toString() {
		return "Feature \"" + getName() + "\"";
	}
}
