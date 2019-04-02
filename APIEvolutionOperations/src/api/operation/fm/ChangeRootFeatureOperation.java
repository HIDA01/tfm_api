package api.operation.fm;

import api.operation.EvolutionOperation;
import fm.Feature;

public class ChangeRootFeatureOperation extends FeatureModelEvolutionOperation {

	protected Feature newRootFeature;
	protected Feature oldRootFeature;

	public Feature getNewRootFeature() {
		return newRootFeature;
	}

	public Feature getOldRootFeature() {
		return oldRootFeature;
	}

	public void setNewRootFeature(Feature newRootFeature) {
		this.newRootFeature = newRootFeature;
	}

	public void setOldRootFeature(Feature oldRootFeature) {
		this.oldRootFeature = oldRootFeature;
	}

	@Override
	protected EvolutionOperation doClone() {
		ChangeRootFeatureOperation clone = new ChangeRootFeatureOperation();
		
		clone.setNewRootFeature(this.getNewRootFeature());
		clone.setOldRootFeature(this.getOldRootFeature());
		
		return clone;
	}

	
}
