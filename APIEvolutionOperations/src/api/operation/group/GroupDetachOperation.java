package api.operation.group;

import fm.Feature;

public class GroupDetachOperation extends GroupEvolutionOperation {

	protected Feature oldParentFeature;

	public Feature getOldParentFeature() {
		return oldParentFeature;
	}

	public void setOldParentFeature(Feature oldParentFeature) {
		this.oldParentFeature = oldParentFeature;
	}

	@Override
	protected GroupEvolutionOperation doCloneGroupEvolutionOperation() {
		GroupDetachOperation clone = new GroupDetachOperation();
		
		clone.setOldParentFeature(this.getOldParentFeature());
		
		return clone;
	}
}
