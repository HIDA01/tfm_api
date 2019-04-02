package api.fm.listener;

import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

public abstract class FMViewAbstractListener implements FMViewListener {
	@Override
	public void onRootFeatureChanged(FeatureModel featureModel, Feature oldRootFeature, Feature newRootFeature) {
	}

	@Override
	public void onFeatureCreated(Feature feature) {
	}

	@Override
	public void onGroupCreated(Group group) {
	}

	@Override
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature) {
	}

	@Override
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature) {
	}

	@Override
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup, Feature newParentFeature) {
	}

	@Override
	public void onFeatureNameChanged(Feature feature, String oldName, String newName) {
	}

	@Override
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType) {
	}

	@Override
	public void onGroupAdded(Group group, Feature oldParentFeature) {
	}

	@Override
	public void onGroupRemoved(Group group, Feature oldParentFeature) {
	}

	@Override
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature) {
	}

	@Override
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType) {
	}

	@Override
	public void onFeatureModelChanged(FeatureModel featureModel) {
	}

	@Override
	public void onFeatureSetChanged(FeatureModel featureModel) {
	}

	@Override
	public void onFeatureChanged(Feature feature) {
	}

	@Override
	public void onGroupChanged(Group group) {
	}

}
