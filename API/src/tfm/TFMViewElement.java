package tfm;

import api.tfm.TFMView;
import tfm.util.TemporalRange;

public abstract class TFMViewElement implements TemporalElement {
	private TFMView owningView;
	private Object notationSpecificElement;
	
	private TemporalRange temporalValidity;
	
	protected TFMViewElement(TFMView owningView, Object notationSpecificElement) {
		this.owningView = owningView;
		this.notationSpecificElement = notationSpecificElement;
	}
	
	public TFMView getOwningView() {
		return owningView;
	}
	
	public Object getNotationSpecificElement() {
		return notationSpecificElement;
	}
	
	public void setNativeElement(Object nativeElement) {
		this.notationSpecificElement = nativeElement;
	}
	
	@Override
	public TemporalRange getTemporalValidity() {
		return temporalValidity;
	}
	
	@Override
	public void setTemporalValidity(TemporalRange temporalValidity) {
		this.temporalValidity = temporalValidity;
	}

}
