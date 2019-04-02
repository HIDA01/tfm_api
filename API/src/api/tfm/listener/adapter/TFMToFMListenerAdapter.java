package api.tfm.listener.adapter;

import api.base.TFMToFMAdapter;
import api.fm.listener.FMViewListener;
import api.tfm.listener.TFMViewListener;
import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalFeatureModel;
import tfm.util.TemporalPoint;

/**
 * 
 * @author Michael Nieke I'm not sure whether it makes sense to fire all events
 *         that affect the feature model at the date of the adapter (i.e., also
 *         events that happened before that date) or only events that happened
 *         exactly at that date.
 *
 */
public class TFMToFMListenerAdapter implements TFMViewListener {

	private FMViewListener listener;
	private TFMToFMAdapter tfmToFmAdapter;

	public TFMToFMListenerAdapter(FMViewListener listener, TFMToFMAdapter tfmToFmAdapter) {
		this.listener = listener;
		this.tfmToFmAdapter = tfmToFmAdapter;
	}

	private boolean isRelevant(TemporalPoint temporalPoint) {
		// do not fire events that are irrelevant for the listener -> if the operation
		// has
		// been executed after the reference temporal point.
		if (temporalPoint != null && temporalPoint.after(tfmToFmAdapter.getReferenceTemporalPoint())) {
			return false;
		}

		return true;
	}

	@Override
	public void onRootFeatureChange(TemporalFeatureModel temporalFeatureModel, Feature oldRootFeature,
			Feature newRootFeature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onRootFeatureChanged(
					temporalFeatureModel.getFeatureModelAt(tfmToFmAdapter.getReferenceTemporalPoint()), oldRootFeature,
					newRootFeature);
		}
	}

	@Override
	public void onFeatureCreated(Feature feature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureCreated(feature);
		}
	}

	@Override
	public void onGroupCreated(Group group, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupCreated(group);
		}
	}

	@Override
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature,
			TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureAdded(feature, newParentGroup, newParentFeature);
		}
	}

	@Override
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature,
			TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureRemoved(feature, oldParentGroup, oldParentFeature);
		}
	}

	@Override
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup,
			Feature newParentFeature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureMoved(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature);
		}
	}

	@Override
	public void onFeatureNameChanged(Feature feature, String oldName, String newName, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureNameChanged(feature, oldName, newName);
		}
	}

	@Override
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType,
			FeatureVariationType newVariationType, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureVariationTypeChanged(feature, oldVariationType, newVariationType);
		}
	}

	@Override
	public void onGroupAdded(Group group, Feature newParentFeature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupAdded(group, newParentFeature);
		}
	}

	@Override
	public void onGroupRemoved(Group group, Feature oldParentFeature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupRemoved(group, oldParentFeature);
		}
	}

	@Override
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature,
			TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupMoved(group, oldParentFeature, newParentFeature);
		}
	}

	@Override
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType,
			GroupVariationType newVariationType, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupVariationTypeChanged(group, oldVariationType, newVariationType);
		}
	}

	@Override
	public void onFeatureModelChanged(TemporalFeatureModel temporalFeatureModel, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureModelChanged(temporalFeatureModel.getFeatureModelAt(tfmToFmAdapter.getReferenceTemporalPoint()));
		}
	}

	@Override
	public void onFeatureSetChanged(TemporalFeatureModel featureModel, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureSetChanged(featureModel.getFeatureModelAt(tfmToFmAdapter.getReferenceTemporalPoint()));
		}
	}

	@Override
	public void onFeatureChanged(Feature feature, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onFeatureChanged(feature);
		}
	}

	@Override
	public void onGroupChanged(Group group, TemporalPoint temporalPoint) {
		if (isRelevant(temporalPoint)) {
			listener.onGroupChanged(group);
		}
	}

}
