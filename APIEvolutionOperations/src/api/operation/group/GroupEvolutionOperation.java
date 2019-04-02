package api.operation.group;

import api.operation.EvolutionOperation;
import fm.Group;

public abstract class GroupEvolutionOperation extends EvolutionOperation {

	protected Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	@Override
	protected EvolutionOperation doClone() {
		GroupEvolutionOperation clone = doCloneGroupEvolutionOperation();
		clone.setGroup(this.getGroup());
		
		return clone;
	}
	
	protected abstract GroupEvolutionOperation doCloneGroupEvolutionOperation();
}
