package api.operation.feature;

import fm.Feature;
import fm.FeatureModel;
import fm.Group;
import tfm.util.TemporalPoint;

public class FeatureMoveOperation extends FeatureEvolutionOperation {

	protected FeatureAddOperation addOperation;
	protected FeatureDetachOperation detachOperation;

	public FeatureMoveOperation() {
		this.addOperation = new FeatureAddOperation();
		this.detachOperation = new FeatureDetachOperation();
	}
	
	@Override
	public void setFeatureModel(FeatureModel featureModel) {
		super.setFeatureModel(featureModel);
		detachOperation.setFeatureModel(featureModel);
		addOperation.setFeatureModel(featureModel);
	}

	@Override
	public void setHasBeenExecuted(boolean hasBeenExecuted) {
		super.setHasBeenExecuted(hasBeenExecuted);
		addOperation.setHasBeenExecuted(hasBeenExecuted);
		detachOperation.setHasBeenExecuted(hasBeenExecuted);
	}

	@Override
	public void setFeature(Feature feature) {
		super.setFeature(feature);
		addOperation.setFeature(feature);
		detachOperation.setFeature(feature);
	}
	
	@Override
	public void setOperationDate(TemporalPoint operationDate) {
		super.setOperationDate(operationDate);
		detachOperation.setOperationDate(operationDate);
		addOperation.setOperationDate(operationDate);
	}

	public Group getNewParentGroup() {
		return addOperation.getNewParentGroup();
	}

	public void setNewParentGroup(Group newParentGroup) {
		addOperation.setNewParentGroup(newParentGroup);
	}

	public Feature getNewParentFeature() {
		return addOperation.getNewParentFeature();
	}

	public void setNewParentFeature(Feature newParentFeature) {
		addOperation.setNewParentFeature(newParentFeature);
	}

	public Group getOldParentGroup() {
		return detachOperation.getOldParentGroup();
	}

	public void setOldParentGroup(Group oldParentGroup) {
		detachOperation.setOldParentGroup(oldParentGroup);
	}
	
	public Feature getOldParentFeature() {
		return detachOperation.getOldParentFeature();
	}
	
	public void setOldParentFeature(Feature oldParentFeature) {
		detachOperation.setOldParentFeature(oldParentFeature);
	}

	public FeatureAddOperation getAddOperation() {
		return addOperation;
	}

	public void setAddOperation(FeatureAddOperation addOperation) {
		this.addOperation = addOperation;
	}

	public FeatureDetachOperation getDetachOperation() {
		return detachOperation;
	}

	public void setDetachOperation(FeatureDetachOperation detachOperation) {
		this.detachOperation = detachOperation;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureMoveOperation clone = new FeatureMoveOperation();
		
		clone.setNewParentFeature(this.getNewParentFeature());
		clone.setNewParentGroup(this.getNewParentGroup());
		clone.setOldParentFeature(this.getOldParentFeature());
		clone.setOldParentGroup(this.getOldParentGroup());
		
		return clone;
	}
}
