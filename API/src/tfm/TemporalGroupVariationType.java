package tfm;

import fm.GroupVariationType;
import tfm.util.TemporalRange;

public class TemporalGroupVariationType implements TemporalElement {
	
	private GroupVariationType variationType;
	private TemporalRange temporalValidity;

	public TemporalGroupVariationType(GroupVariationType variationType, TemporalRange temporalValidity) {
		setVariationType(variationType);
		setTemporalValidity(temporalValidity);
	}

	public GroupVariationType getVariationType() {
		return variationType;
	}

	public void setVariationType(GroupVariationType variationType) {
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
