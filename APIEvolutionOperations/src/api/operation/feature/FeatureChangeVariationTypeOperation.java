package api.operation.feature;

import fm.FeatureVariationType;

public class FeatureChangeVariationTypeOperation extends FeatureEvolutionOperation {

	protected FeatureVariationType oldVariationType;
	protected FeatureVariationType newVariationType;
	
	public FeatureVariationType getOldVariationType() {
		return oldVariationType;
	}
	
	public void setOldVariationType(FeatureVariationType oldVariationType) {
		this.oldVariationType = oldVariationType;
	}
	
	public FeatureVariationType getNewVariationType() {
		return newVariationType;
	}
	
	public void setNewVariationType(FeatureVariationType newVariationType) {
		this.newVariationType = newVariationType;
	}

	@Override
	protected FeatureEvolutionOperation doCloneFeatureEvolutionOperation() {
		FeatureChangeVariationTypeOperation clone = new FeatureChangeVariationTypeOperation();
		
		clone.setOldVariationType(this.getOldVariationType());
		clone.setNewVariationType(this.getNewVariationType());

		return clone;
	}
	
}
