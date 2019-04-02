package api.fm.listener.individual;

import fm.Feature;
import fm.Group;
import fm.GroupVariationType;

public abstract class FMViewAbstractGroupListener implements FMViewGroupListener {

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
	public void onGroupChanged(Group group) {
	}
}
