package api.fm.listener.individual;

import fm.Feature;
import fm.FeatureModel;
import fm.Group;

public abstract class FMViewAbstractFeatureModelListener implements FMViewFeatureModelListener {

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
	public void onFeatureModelChanged(FeatureModel featureModel) {
	}

	@Override
	public void onFeatureSetChanged(FeatureModel featureModel) {
	}
}
