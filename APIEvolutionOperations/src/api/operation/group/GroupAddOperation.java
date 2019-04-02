package api.operation.group;

import fm.Feature;

public class GroupAddOperation extends GroupEvolutionOperation {

	protected Feature newParentFeature;

	public Feature getNewParentFeature() {
		return newParentFeature;
	}

	public void setNewParentFeature(Feature newParentFeature) {
		this.newParentFeature = newParentFeature;
	}

	@Override
	protected GroupEvolutionOperation doCloneGroupEvolutionOperation() {
		GroupAddOperation clone = new GroupAddOperation();
		
		clone.setNewParentFeature(this.getNewParentFeature());
		
		return clone;
	}
	
}
