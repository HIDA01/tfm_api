package api.operation.group;

import fm.Feature;
import fm.FeatureModel;
import fm.Group;
import tfm.util.TemporalPoint;

public class GroupMoveOperation extends GroupEvolutionOperation {

	protected GroupDetachOperation detachOperation;
	protected GroupAddOperation addOperation;
	
	public GroupMoveOperation() {
		this.detachOperation = new GroupDetachOperation();
		this.addOperation = new GroupAddOperation();
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
	public void setGroup(Group group) {
		super.setGroup(group);
		detachOperation.setGroup(group);
		addOperation.setGroup(group);
	}
	
	@Override
	public void setOperationDate(TemporalPoint operationDate) {
		super.setOperationDate(operationDate);
		detachOperation.setOperationDate(operationDate);
		addOperation.setOperationDate(operationDate);
	}

	public Feature getNewParentFeature() {
		return addOperation.getNewParentFeature();
	}

	public void setNewParentFeature(Feature newParentFeature) {
		addOperation.setNewParentFeature(newParentFeature);
	}
	
	public Feature getOldParentFeature() {
		return detachOperation.getOldParentFeature();
	}
	
	public void setOldParentFeature(Feature oldParentFeature) {
		detachOperation.setOldParentFeature(oldParentFeature);
	}
	
	public GroupDetachOperation getDetachOperation() {
		return detachOperation;
	}
	
	public void setDetachOperation(GroupDetachOperation detachOperation) {
		this.detachOperation = detachOperation;
	}
	
	public GroupAddOperation getAddOperation() {
		return addOperation;
	}
	
	public void setAddOperation(GroupAddOperation addOperation) {
		this.addOperation = addOperation;
	}

	@Override
	protected GroupEvolutionOperation doCloneGroupEvolutionOperation() {
		GroupMoveOperation clone = new GroupMoveOperation();
		
		clone.setOldParentFeature(this.getOldParentFeature());
		clone.setNewParentFeature(this.getNewParentFeature());
		
		return clone;
	}
	
}
