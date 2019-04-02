package api.operation.feature;

import fm.Feature;
import fm.Group;

public class FeatureDetachOperation extends FeatureEvolutionOperation {

	protected Group oldParentGroup;
	protected Feature oldParentFeature;

	public Group getOldParentGroup() {
		return oldParentGroup;
	}

	public void setOldParentGroup(Group oldParentGroup) {
		this.oldParentGroup = oldParentGroup;
	}

	public Feature getOldParentFeature() {
		return oldParentFeature;
	}

	public void setOldParentFeature(Feature oldParentFeature) {
		this.oldParentFeature = oldParentFeature;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureDetachOperation clone = new FeatureDetachOperation();
		
		clone.setOldParentGroup(this.getOldParentGroup());
		clone.setOldParentFeature(this.getOldParentFeature());
		
		return clone;
	}
	
}
