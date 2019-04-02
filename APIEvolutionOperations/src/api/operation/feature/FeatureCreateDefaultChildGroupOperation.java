package api.operation.feature;

import fm.Group;

public class FeatureCreateDefaultChildGroupOperation extends FeatureEvolutionOperation {

	protected Group newDefaultChildGroup;

	public Group getNewDefaultChildGroup() {
		return newDefaultChildGroup;
	}

	public void setNewDefaultChildGroup(Group newDefaultChildGroup) {
		this.newDefaultChildGroup = newDefaultChildGroup;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureCreateDefaultChildGroupOperation clone = new FeatureCreateDefaultChildGroupOperation();
		
		clone.setNewDefaultChildGroup(this.getNewDefaultChildGroup());

		return clone;
	}
	
}
