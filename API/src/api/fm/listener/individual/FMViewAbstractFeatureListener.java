package api.fm.listener.individual;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;

public abstract class FMViewAbstractFeatureListener implements FMViewFeatureListener {

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
	public void onFeatureChanged(Feature feature) {
	}
}
