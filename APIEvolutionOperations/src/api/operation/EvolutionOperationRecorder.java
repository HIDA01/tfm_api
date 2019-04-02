package api.operation;

import java.util.LinkedList;
import java.util.List;

import api.fm.listener.FMViewListener;
import api.operation.feature.FeatureAddOperation;
import api.operation.feature.FeatureChangeVariationTypeOperation;
import api.operation.feature.FeatureMoveOperation;
import api.operation.feature.FeatureRemoveOperation;
import api.operation.feature.FeatureRenameOperation;
import api.operation.fm.ChangeRootFeatureOperation;
import api.operation.fm.CreateFeatureOperation;
import api.operation.fm.CreateGroupOperation;
import api.operation.group.GroupAddOperation;
import api.operation.group.GroupChangeVariationTypeOperation;
import api.operation.group.GroupMoveOperation;
import api.operation.group.GroupRemoveOperation;
import api.tfm.listener.TFMViewListener;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalFeatureModel;
import tfm.util.TemporalPoint;

public class EvolutionOperationRecorder implements FMViewListener, TFMViewListener {

	// TODO feature/group detach is not used (as it's not available in the listener
	// interface). Only indirectly via move.

	protected List<EvolutionOperation> evolutionOperations;

	public EvolutionOperationRecorder() {
		this.evolutionOperations = new LinkedList<>();
	}

	protected void addEvolutionOperation(EvolutionOperation evolutionOperation) {
		evolutionOperation.setCommandStackIndex(evolutionOperations.size());
		evolutionOperation.setHasBeenExecuted(true);

		evolutionOperations.add(evolutionOperation);
	}
	
	protected void addEvolutionOperation(EvolutionOperation evolutionOperation, TemporalPoint operationDate) {
		evolutionOperation.setOperationDate(operationDate);
		addEvolutionOperation(evolutionOperation);
	}

	@Override
	public void onRootFeatureChanged(FeatureModel featureModel, Feature oldRootFeature, Feature newRootFeature) {
		addEvolutionOperation(createChangeRootFeatureOperation(featureModel, oldRootFeature, newRootFeature));
	}

	protected ChangeRootFeatureOperation createChangeRootFeatureOperation(FeatureModel featureModel,
			Feature oldRootFeature, Feature newRootFeature) {
		ChangeRootFeatureOperation changeRootFeatureOperation = new ChangeRootFeatureOperation();

		changeRootFeatureOperation.setFeatureModel(featureModel);
		changeRootFeatureOperation.setOldRootFeature(oldRootFeature);
		changeRootFeatureOperation.setNewRootFeature(newRootFeature);

		return changeRootFeatureOperation;
	}

	@Override
	public void onFeatureCreated(Feature feature) {
		addEvolutionOperation(createCreateFeatureOperation(feature));
	}

	protected CreateFeatureOperation createCreateFeatureOperation(Feature feature) {
		CreateFeatureOperation createFeatureOperation = new CreateFeatureOperation();

		createFeatureOperation.setFeatureModel(feature.getFeatureModel());
		createFeatureOperation.setFeature(feature);
		createFeatureOperation.setFeatureName(feature.getName());

		return createFeatureOperation;
	}

	@Override
	public void onGroupCreated(Group group) {
		addEvolutionOperation(createCreateGroupOperation(group));
	}

	protected CreateGroupOperation createCreateGroupOperation(Group group) {
		// TODO what's about default groups? Should we distinguish here? A default group
		// in one notation could mean an ALTERNATIVE and in another notation an AND
		// group -> result would then be different. For the time being, no
		// differentiation.
		CreateGroupOperation createGroupOperation = new CreateGroupOperation();

		createGroupOperation.setFeatureModel(group.getFeatureModel());
		createGroupOperation.setGroup(group);

		return createGroupOperation;
	}

	@Override
	public void onFeatureModelChanged(FeatureModel featureModel) {
		// Nothing to do here.
	}

	@Override
	public void onFeatureSetChanged(FeatureModel featureModel) {
		// Nothing to do here
	}

	@Override
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature) {
		addEvolutionOperation(createFeatureAddOperation(feature, newParentGroup, newParentFeature));
	}

	protected FeatureAddOperation createFeatureAddOperation(Feature feature, Group newParentGroup,
			Feature newParentFeature) {
		FeatureAddOperation featureAddOperation = new FeatureAddOperation();

		featureAddOperation.setFeatureModel(feature.getFeatureModel());
		featureAddOperation.setFeature(feature);
		featureAddOperation.setNewParentGroup(newParentGroup);
		featureAddOperation.setNewParentFeature(newParentFeature);

		return featureAddOperation;
	}

	@Override
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature) {
		addEvolutionOperation(createFeatureRemoveOperation(feature, oldParentGroup, oldParentFeature));
	}

	protected FeatureRemoveOperation createFeatureRemoveOperation(Feature feature, Group oldParentGroup,
			Feature oldParentFeature) {
		FeatureRemoveOperation featureRemoveOperation = new FeatureRemoveOperation();

		featureRemoveOperation.setFeatureModel(oldParentFeature.getFeatureModel());
		featureRemoveOperation.setFeature(feature);
		featureRemoveOperation.setOldParentGroup(oldParentGroup);
		featureRemoveOperation.setOldParentFeature(oldParentFeature);

		return featureRemoveOperation;
	}

	@Override
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup,
			Feature newParentFeature) {
		addEvolutionOperation(createFeatureMoveOperation(feature, oldParentGroup, oldParentFeature, newParentGroup,
				newParentFeature));
	}

	protected FeatureMoveOperation createFeatureMoveOperation(Feature feature, Group oldParentGroup,
			Feature oldParentFeature, Group newParentGroup, Feature newParentFeature) {
		FeatureMoveOperation featureMoveOperation = new FeatureMoveOperation();

		featureMoveOperation.setFeatureModel(feature.getFeatureModel());
		featureMoveOperation.setFeature(feature);
		featureMoveOperation.setOldParentGroup(oldParentGroup);
		featureMoveOperation.setOldParentFeature(oldParentFeature);
		featureMoveOperation.setNewParentGroup(newParentGroup);
		featureMoveOperation.setNewParentFeature(newParentFeature);

		return featureMoveOperation;
	}

	@Override
	public void onFeatureNameChanged(Feature feature, String oldName, String newName) {
		addEvolutionOperation(createFeatureRenameOperation(feature, oldName, newName));
	}

	protected FeatureRenameOperation createFeatureRenameOperation(Feature feature, String oldName, String newName) {
		FeatureRenameOperation featureRenameOperation = new FeatureRenameOperation();

		featureRenameOperation.setFeatureModel(feature.getFeatureModel());
		featureRenameOperation.setFeature(feature);
		featureRenameOperation.setOldName(oldName);
		featureRenameOperation.setNewName(newName);

		return featureRenameOperation;
	}

	@Override
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType,
			FeatureVariationType newVariationType) {
		addEvolutionOperation(createFeatureChangeVariationTypeOperation(feature, oldVariationType, newVariationType));
	}

	protected FeatureChangeVariationTypeOperation createFeatureChangeVariationTypeOperation(Feature feature,
			FeatureVariationType oldVariationType, FeatureVariationType newVariationType) {
		FeatureChangeVariationTypeOperation changeVariationTypeOperation = new FeatureChangeVariationTypeOperation();

		changeVariationTypeOperation.setFeatureModel(feature.getFeatureModel());
		changeVariationTypeOperation.setFeature(feature);
		changeVariationTypeOperation.setOldVariationType(oldVariationType);
		changeVariationTypeOperation.setNewVariationType(newVariationType);

		return changeVariationTypeOperation;
	}

	@Override
	public void onFeatureChanged(Feature feature) {
		// Nothing to do here.
	}

	@Override
	public void onGroupAdded(Group group, Feature newParentFeature) {
		addEvolutionOperation(createGroupAddOperation(group, newParentFeature));
	}

	protected GroupAddOperation createGroupAddOperation(Group group, Feature newParentFeature) {
		GroupAddOperation groupAddOperation = new GroupAddOperation();

		groupAddOperation.setFeatureModel(group.getFeatureModel());
		groupAddOperation.setGroup(group);
		groupAddOperation.setNewParentFeature(newParentFeature);

		return groupAddOperation;
	}

	@Override
	public void onGroupRemoved(Group group, Feature oldParentFeature) {
		addEvolutionOperation(createGroupRemoveOperation(group, oldParentFeature));
	}

	protected GroupRemoveOperation createGroupRemoveOperation(Group group, Feature oldParentFeature) {
		GroupRemoveOperation groupRemoveOperation = new GroupRemoveOperation();

		groupRemoveOperation.setFeatureModel(oldParentFeature.getFeatureModel());
		groupRemoveOperation.setGroup(group);
		groupRemoveOperation.setOldParentFeature(oldParentFeature);

		return groupRemoveOperation;
	}

	@Override
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature) {
		addEvolutionOperation(createGroupMoveOperation(group, oldParentFeature, newParentFeature));
	}

	protected GroupMoveOperation createGroupMoveOperation(Group group, Feature oldParentFeature,
			Feature newParentFeature) {
		GroupMoveOperation groupMoveOperation = new GroupMoveOperation();

		groupMoveOperation.setFeatureModel(group.getFeatureModel());
		groupMoveOperation.setOldParentFeature(oldParentFeature);
		groupMoveOperation.setNewParentFeature(newParentFeature);

		return groupMoveOperation;
	}

	@Override
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType,
			GroupVariationType newVariationType) {
		addEvolutionOperation(createGroupChangeVariationTypeOperation(group, oldVariationType, newVariationType));
	}

	protected GroupChangeVariationTypeOperation createGroupChangeVariationTypeOperation(Group group,
			GroupVariationType oldVariationType, GroupVariationType newVariationType) {
		GroupChangeVariationTypeOperation changeVariationTypeOperation = new GroupChangeVariationTypeOperation();

		changeVariationTypeOperation.setFeatureModel(group.getFeatureModel());
		changeVariationTypeOperation.setGroup(group);
		changeVariationTypeOperation.setOldVariationType(oldVariationType);
		changeVariationTypeOperation.setNewVariationType(newVariationType);

		return changeVariationTypeOperation;
	}

	@Override
	public void onGroupChanged(Group group) {
		// Nothing to do here.
	}

	@Override
	public void onRootFeatureChange(TemporalFeatureModel temporalFeatureModel, Feature oldRootFeature,
			Feature newRootFeature, TemporalPoint temporalPoint) {
		addEvolutionOperation(createChangeRootFeatureOperation(
				temporalFeatureModel.getFeatureModelAt(temporalPoint), oldRootFeature, newRootFeature), temporalPoint);
	}

	@Override
	public void onFeatureCreated(Feature feature, TemporalPoint temporalPoint) {
		addEvolutionOperation(createCreateFeatureOperation(feature), temporalPoint);
	}

	@Override
	public void onGroupCreated(Group group, TemporalPoint temporalPoint) {
		addEvolutionOperation(createCreateGroupOperation(group), temporalPoint);
	}

	@Override
	public void onFeatureAdded(Feature feature, Group newParentGroup, Feature newParentFeature,
			TemporalPoint temporalPoint) {
		addEvolutionOperation(createFeatureAddOperation(feature, newParentGroup, newParentFeature), temporalPoint);
	}

	@Override
	public void onFeatureRemoved(Feature feature, Group oldParentGroup, Feature oldParentFeature,
			TemporalPoint temporalPoint) {
		addEvolutionOperation(createFeatureRemoveOperation(feature, oldParentGroup, oldParentFeature), temporalPoint);
	}

	@Override
	public void onFeatureMoved(Feature feature, Group oldParentGroup, Feature oldParentFeature, Group newParentGroup,
			Feature newParentFeature, TemporalPoint temporalPoint) {
		addEvolutionOperation(createFeatureMoveOperation(feature, oldParentGroup, oldParentFeature, newParentGroup, newParentFeature), temporalPoint);
	}

	@Override
	public void onFeatureNameChanged(Feature feature, String oldName, String newName, TemporalPoint temporalPoint) {
		addEvolutionOperation(createFeatureRenameOperation(feature, oldName, newName), temporalPoint);
	}

	@Override
	public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType,
			FeatureVariationType newVariationType, TemporalPoint temporalPoint) {
		addEvolutionOperation(createFeatureChangeVariationTypeOperation(feature, oldVariationType, newVariationType), temporalPoint);
	}

	@Override
	public void onGroupAdded(Group group, Feature newParentFeature, TemporalPoint temporalPoint) {
		addEvolutionOperation(createGroupAddOperation(group, newParentFeature), temporalPoint);
	}

	@Override
	public void onGroupRemoved(Group group, Feature oldParentFeature, TemporalPoint temporalPoint) {
		addEvolutionOperation(createGroupRemoveOperation(group, oldParentFeature), temporalPoint);
	}

	@Override
	public void onGroupMoved(Group group, Feature oldParentFeature, Feature newParentFeature,
			TemporalPoint temporalPoint) {
		addEvolutionOperation(createGroupMoveOperation(group, oldParentFeature, newParentFeature), temporalPoint);
	}

	@Override
	public void onGroupVariationTypeChanged(Group group, GroupVariationType oldVariationType,
			GroupVariationType newVariationType, TemporalPoint temporalPoint) {
		addEvolutionOperation(createGroupChangeVariationTypeOperation(group, oldVariationType, newVariationType), temporalPoint);
	}

	@Override
	public void onFeatureModelChanged(TemporalFeatureModel temporalFeatureModel, TemporalPoint temporalPoint) {
		// Nothing to do here.
	}

	@Override
	public void onFeatureSetChanged(TemporalFeatureModel featureModel, TemporalPoint temporalPoint) {
		// Nothing to do here.
	}

	@Override
	public void onFeatureChanged(Feature feature, TemporalPoint temporalPoint) {
		// Nothing to do here.
	}

	@Override
	public void onGroupChanged(Group group, TemporalPoint temporalPoint) {
		// Nothing to do here.
	}
	
	public List<EvolutionOperation> getEvolutionOperations() {
		return this.evolutionOperations;
	}
}
