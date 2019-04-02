package api.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.fm.FMView;
import api.fm.listener.FMViewListener;
import api.tfm.TFMView;
import api.tfm.listener.adapter.TFMToFMListenerAdapter;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.util.TemporalPoint;

public class TFMToFMAdapter implements FMView {
	// TODO add FMListener support?
	
	private TFMView temporalFeatureModelView;
	private TemporalPoint referenceTemporalPoint;
	
	private Map<FMViewListener, TFMToFMListenerAdapter> listenerAdapterMap;
	
	public TFMToFMAdapter(TFMView temporalFeatureModelView, TemporalPoint referenceTemporalPoint) {
		this.temporalFeatureModelView = temporalFeatureModelView;
		this.referenceTemporalPoint = referenceTemporalPoint;
		initialize();
	}
	
	private void initialize() {
		this.listenerAdapterMap = new HashMap<>();
	}

	public TemporalPoint getReferenceTemporalPoint() {
		return referenceTemporalPoint;
	}

	public void setReferenceTemporalPoint(TemporalPoint referenceTemporalPoint) {
		this.referenceTemporalPoint = referenceTemporalPoint;
	}

	@Override
	public Feature getRootFeature() {
		return temporalFeatureModelView.getRootFeatureAt(referenceTemporalPoint);
	}

	@Override
	public void setRootFeature(Feature feature) {
		temporalFeatureModelView.setRootFeatureAt(feature, referenceTemporalPoint);
	}

	@Override
	public boolean isRootFeature(Feature feature) {
		return temporalFeatureModelView.isRootFeatureAt(feature, referenceTemporalPoint);
	}
	
	@Override
	public Feature getFeature(String identifier) {
		return temporalFeatureModelView.getFeatureAt(identifier, referenceTemporalPoint);
	}

	@Override
	public Collection<Feature> getAllFeatures() {
		return temporalFeatureModelView.getAllFeaturesAt(referenceTemporalPoint);
	}

	@Override
	public Group getFeatureDefaultChildGroup(Feature feature) {
		return temporalFeatureModelView.getFeatureDefaultChildGroupAt(feature, referenceTemporalPoint);
	}

	@Override
	public List<Group> getFeatureChildGroups(Feature feature) {
		return temporalFeatureModelView.getFeatureChildGroupsAt(feature, referenceTemporalPoint);
	}

	@Override
	public Group getFeatureParentGroup(Feature feature) {
		return temporalFeatureModelView.getFeatureParentGroupAt(feature, referenceTemporalPoint);
	}
	
	@Override
	public Feature getFeatureParentFeature(Feature feature) {
		return temporalFeatureModelView.getFeatureParentFeatureAt(feature, referenceTemporalPoint);
	}
	
	@Override
	public List<Feature> getGroupChildFeatures(Group group) {
		return temporalFeatureModelView.getGroupChildFeaturesAt(group, referenceTemporalPoint);
	}

	@Override
	public Feature getGroupParentFeature(Group group) {
		return temporalFeatureModelView.getGroupParentFeatureAt(group, referenceTemporalPoint);
	}
	
	@Override
	public Feature createFeature(String identifier) {
		return temporalFeatureModelView.createFeatureAt(identifier, referenceTemporalPoint);
	}

	@Override
	public void addFeature(Feature feature, Feature parentFeature) {
		temporalFeatureModelView.addFeatureAt(feature, parentFeature, referenceTemporalPoint);
	}

	@Override
	public void addFeature(Feature feature, Group parentGroup) {
		temporalFeatureModelView.addFeatureAt(feature, parentGroup, referenceTemporalPoint);
	}

	@Override
	public void removeFeature(Feature feature) {
		temporalFeatureModelView.removeFeatureAt(feature, referenceTemporalPoint);
	}

	@Override
	public void moveFeature(Feature feature, Group targetGroup) {
		temporalFeatureModelView.moveFeatureAt(feature, targetGroup, referenceTemporalPoint);
	}

	@Override
	public void moveFeature(Feature feature, Feature targetFeature) {
		temporalFeatureModelView.moveFeatureAt(feature, targetFeature, referenceTemporalPoint);
	}
	
	@Override
	public String getFeatureName(Feature feature) {
		return temporalFeatureModelView.getFeatureNameAt(feature, referenceTemporalPoint);
	}

	@Override
	public void setFeatureName(Feature feature, String name) {
		temporalFeatureModelView.setFeatureNameAt(feature, name, referenceTemporalPoint);
	}

	@Override
	public FeatureVariationType getFeatureVariationType(Feature feature) {
		return temporalFeatureModelView.getFeatureVariationTypeAt(feature, referenceTemporalPoint);
	}

	@Override
	public void setFeatureVariationType(Feature feature, FeatureVariationType variationType) {
		temporalFeatureModelView.setFeatureVariationTypeAt(feature, variationType, referenceTemporalPoint);
	}

	@Override
	public GroupVariationType getGroupVariationType(Group group) {
		return temporalFeatureModelView.getGroupVariationTypeAt(group, referenceTemporalPoint);
	}

	@Override
	public void setGroupVariationType(Group group, GroupVariationType variationType) {
		temporalFeatureModelView.setGroupVariationTypeAt(group, variationType, referenceTemporalPoint);
	}

	@Override
	public FeatureModel getFeatureModel() {
		return temporalFeatureModelView.getFeatureModelAt(referenceTemporalPoint);
	}

	
	@Override
	public Group createGroup() {
		return temporalFeatureModelView.createGroupAt(referenceTemporalPoint);
	}
	
	@Override
	public void addGroup(Group group, Feature parentFeature) {
		temporalFeatureModelView.addGroupAt(group, parentFeature, referenceTemporalPoint);
	}
	
	@Override
	public Group createDefaultChildGroup(Feature feature) {
		return temporalFeatureModelView.createDefaultChildGroupAt(feature, referenceTemporalPoint);
	}
	
	@Override
	public void moveGroup(Group group, Feature targetFeature) {
		temporalFeatureModelView.moveGroupAt(group, targetFeature, referenceTemporalPoint);
	}
	
	@Override
	public void removeGroup(Group group) {
		temporalFeatureModelView.removeGroupAt(group, referenceTemporalPoint);
	}
	
	
	@Override
	public void addListener(FMViewListener listener) {
		TFMToFMListenerAdapter listenerAdapter = new TFMToFMListenerAdapter(listener, this);
		listenerAdapterMap.put(listener, listenerAdapter);
		temporalFeatureModelView.addListener(listenerAdapter);
	}

	@Override
	public void removeListener(FMViewListener listener) {
		TFMToFMListenerAdapter listenerAdapter = listenerAdapterMap.get(listener);
		if (listenerAdapter == null) {
			return;
		}
		
		temporalFeatureModelView.removeListener(listenerAdapter);
		listenerAdapterMap.remove(listener);
	}

	@Override
	public Collection<Group> getAllGroups() {
		return temporalFeatureModelView.getAllGroupsAt(referenceTemporalPoint);
	}
}
