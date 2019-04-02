package api.operation.feature;

import api.operation.EvolutionOperation;
import fm.Feature;

public abstract class FeatureEvolutionOperation extends EvolutionOperation {

	protected Feature feature;

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	@Override
	protected EvolutionOperation doClone() {
		FeatureEvolutionOperation clone = doCloneFeatureEvolutionOperation();
		clone.setFeature(this.getFeature());
		
		return clone;
	}
	
	protected abstract FeatureEvolutionOperation doCloneFeatureEvolutionOperation();
	
}
