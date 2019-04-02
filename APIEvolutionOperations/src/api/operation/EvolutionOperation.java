package api.operation;

import fm.FeatureModel;
import tfm.util.TemporalPoint;

public abstract class EvolutionOperation {

	protected int commandStackIndex;
	// TODO not so cool. Only thing of TFM API. Could be replaced by using an FMView (resp. TFMToFMAdapter). But seems necessary to store evoOps of TFM API calls
	protected TemporalPoint operationDate;
	
	protected boolean hasBeenExecuted;

	protected FeatureModel featureModel;

	public int getCommandStackIndex() {
		return commandStackIndex;
	}

	// TODO protected for setters sensible? Makes sense e.g., for "FeatureAddOperation" -> parent cannot be modified.
	public void setCommandStackIndex(int commandStackIndex) {
		this.commandStackIndex = commandStackIndex;
	}

	public TemporalPoint getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(TemporalPoint operationDate) {
		this.operationDate = operationDate;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setFeatureModel(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public boolean hasBeenExecuted() {
		return hasBeenExecuted;
	}

	public void setHasBeenExecuted(boolean hasBeenExecuted) {
		this.hasBeenExecuted = hasBeenExecuted;
	}
	
	public Object clone() {
		EvolutionOperation clone = doClone();
		
		clone.setFeatureModel(this.getFeatureModel());
		clone.setCommandStackIndex(this.getCommandStackIndex());
		clone.setHasBeenExecuted(this.hasBeenExecuted);
		clone.setOperationDate(this.getOperationDate());
		
		return clone;
	}
	
	protected abstract EvolutionOperation doClone();
	

	// TODO notes: Think about serialization. I have the impression that the mapping
	// between features and temporal features (resp. groups) and the mapping between
	// notation specific features and other features (resp. groups) have to be
	// serialized as well
}
