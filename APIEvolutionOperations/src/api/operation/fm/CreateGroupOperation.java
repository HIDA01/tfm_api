package api.operation.fm;

import api.operation.EvolutionOperation;
import fm.Group;

public class CreateGroupOperation extends EvolutionOperation {

	protected Group group;
	
	public Group getGroup() {
		return this.group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	protected EvolutionOperation doClone() {
		CreateGroupOperation clone = new CreateGroupOperation();
		clone.setGroup(this.getGroup());
		return clone;
	}
}
