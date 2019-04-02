package api.operation.group;

import fm.GroupVariationType;

public class GroupChangeVariationTypeOperation extends GroupEvolutionOperation {

	protected GroupVariationType oldVariationType;
	protected GroupVariationType newVariationType;
	
	public GroupVariationType getOldVariationType() {
		return oldVariationType;
	}
	
	public void setOldVariationType(GroupVariationType oldVariationType) {
		this.oldVariationType = oldVariationType;
	}
	
	public GroupVariationType getNewVariationType() {
		return newVariationType;
	}
	
	public void setNewVariationType(GroupVariationType newVariationType) {
		this.newVariationType = newVariationType;
	}

	@Override
	protected GroupEvolutionOperation doCloneGroupEvolutionOperation() {
		GroupChangeVariationTypeOperation clone = new GroupChangeVariationTypeOperation();
		clone.setOldVariationType(this.getOldVariationType());
		clone.setNewVariationType(this.getNewVariationType());
		return clone;
	}
	
}
