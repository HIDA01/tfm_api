package api.operation.feature;

import fm.Feature;
import fm.Group;

public class FeatureRemoveOperation extends FeatureEvolutionOperation {

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
		FeatureRemoveOperation clone = new FeatureRemoveOperation();
		
		clone.setOldParentFeature(this.getOldParentFeature());
		clone.setOldParentGroup(this.getOldParentGroup());
		
		return clone;
	}
}
