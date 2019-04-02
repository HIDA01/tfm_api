package tfm;

import tfm.util.TemporalRange;

public interface TemporalElement {

	public TemporalRange getTemporalValidity();
	public void setTemporalValidity(TemporalRange temporalValidity); // TODO -> seems to me non-consistent with the TFMView -> removeFeatureAt
	
}
