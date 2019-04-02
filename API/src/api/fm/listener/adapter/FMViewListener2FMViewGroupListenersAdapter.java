package api.fm.listener.adapter;

import java.util.LinkedList;
import java.util.List;

import api.fm.FMView;
import api.fm.listener.FMViewAbstractListener;
import api.fm.listener.individual.FMViewGroupListener;
import fm.Feature;
import fm.Group;
import fm.GroupVariationType;

/**
 * Forwards all notifications of the API concerning the specified group.
 * 
 * @author Christoph Seidl
 */
public class FMViewListener2FMViewGroupListenersAdapter extends FMViewAbstractListener implements FMViewListenerAdapter, FMViewGroupListener {
	private Group group;
	private List<FMViewGroupListener> listeners;
	private boolean isActivated;
	
	public FMViewListener2FMViewGroupListenersAdapter(Group group) {
		this.group = group;
		listeners = new LinkedList<FMViewGroupListener>();
		isActivated = false;
//		activate(); // Removed for lazy listening.
	}
	
	public void activate() {
		FMView fmView = group.getOwningView();
		fmView.addListener(this);
	}
	
	public void deactivate() {
		FMView fmView = group.getOwningView();
		fmView.removeListener(this);
	}
	
	public void addListener(FMViewGroupListener listener) {
		if (!isActivated) {
			activate();
		}
		listeners.add(listener);
	}
	
	public void removeListener(FMViewGroupListener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			deactivate();
		}
	}

	@Override
	public void onGroupAdded(Group group, Feature oldParentFeature) {
		if (group.equals(this.group)) {
			listeners.forEach(listener -> listener.onGroupAdded(group, oldParentFeature));
		}
	}

	@Override
	public void onGroupRemoved(Group group, Feature oldParentFeature) {
		if (group.equals(this.group)) {
			listeners.forEach(listener -> listener.onGroupRemoved(group, oldParentFeature));
		}
	}

	@Override
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature) {
		if (group.equals(this.group)) {
			listeners.forEach(listener -> listener.onGroupMoved(group, oldParentFeature, newParentFeature));
		}
	}

	@Override
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType, GroupVariationType newVariationType) {
		if (group.equals(this.group)) {
			listeners.forEach(listener -> listener.onGroupVariationTypeChanged(group, oldVariationType, newVariationType));
		}
	}

	@Override
	public void onGroupChanged(Group group) {
		if (group.equals(this.group)) {
			listeners.forEach(listener -> listener.onGroupChanged(group));
		}
	}
}
