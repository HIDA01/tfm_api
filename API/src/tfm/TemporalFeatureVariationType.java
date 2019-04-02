package tfm;

import fm.FeatureVariationType;
import tfm.util.TemporalRange;

public class TemporalFeatureVariationType implements TemporalElement {
	
	private FeatureVariationType variationType;
	private TemporalRange temporalValidity;

	public TemporalFeatureVariationType(FeatureVariationType variationType, TemporalRange temporalValidity) {
		setVariationType(variationType);
		setTemporalValidity(temporalValidity);
	}

	public FeatureVariationType getVariationType() {
		return variationType;
	}

	public void setVariationType(FeatureVariationType variationType) {
		this.variationType = variationType;
	}

	@Override
	public TemporalRange getTemporalValidity() {
		return this.temporalValidity;
	}

	@Override
	public void setTemporalValidity(TemporalRange temporalValidity) {
		this.temporalValidity = temporalValidity;
	}
}
