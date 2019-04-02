package api.operation.fm;

import api.operation.EvolutionOperation;
import fm.Feature;

public class CreateFeatureOperation extends FeatureModelEvolutionOperation {

	protected String featureName;
	protected Feature feature;
	
	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	@Override
	protected EvolutionOperation doClone() {
		CreateFeatureOperation clone = new CreateFeatureOperation();
		
		clone.setFeatureName(this.getFeatureName());
		clone.setFeature(this.getFeature());
		
		return clone;
	}

	
}
