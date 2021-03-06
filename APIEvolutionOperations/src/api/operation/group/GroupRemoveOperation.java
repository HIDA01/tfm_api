package api.operation.group;

import fm.Feature;

public class GroupRemoveOperation extends GroupEvolutionOperation {

	protected Feature oldParentFeature;

	public Feature getOldParentFeature() {
		return oldParentFeature;
	}

	public void setOldParentFeature(Feature oldParentFeature) {
		this.oldParentFeature = oldParentFeature;
	}

	@Override
	protected GroupEvolutionOperation doCloneGroupEvolutionOperation() {
		GroupRemoveOperation clone = new GroupRemoveOperation();
		
		clone.setOldParentFeature(this.getOldParentFeature());
		
		return clone;
	}
	
}
