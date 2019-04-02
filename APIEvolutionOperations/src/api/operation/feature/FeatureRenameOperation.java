package api.operation.feature;

public class FeatureRenameOperation extends FeatureEvolutionOperation {

	protected String oldName;
	protected String newName;
	
	public String getOldName() {
		return oldName;
	}
	
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void setNewName(String newName) {
		this.newName = newName;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureRenameOperation clone = new FeatureRenameOperation();
		
		clone.setOldName(this.getOldName());
		clone.setNewName(clone.getNewName());
		
		return clone;
	}
	
}
