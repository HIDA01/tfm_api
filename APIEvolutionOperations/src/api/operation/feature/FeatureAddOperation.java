package api.operation.feature;

import fm.Feature;
import fm.Group;

public class FeatureAddOperation extends FeatureEvolutionOperation {

	protected Group newParentGroup;
	protected Feature newParentFeature;

	public Group getNewParentGroup() {
		return newParentGroup;
	}

	public void setNewParentGroup(Group newParentGroup) {
		this.newParentGroup = newParentGroup;
	}

	public Feature getNewParentFeature() {
		return newParentFeature;
	}

	public void setNewParentFeature(Feature newParentFeature) {
		this.newParentFeature = newParentFeature;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureAddOperation clone = new FeatureAddOperation();
		clone.setNewParentFeature(this.getNewParentFeature());
		clone.setNewParentGroup(this.getNewParentGroup());
		
		return clone;
	}
}
