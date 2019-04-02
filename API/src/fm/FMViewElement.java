package fm;

import api.fm.FMView;

public abstract class FMViewElement {
	private FMView owningView;
	private Object notationSpecificElement;
	
	protected FMViewElement(FMView owningView, Object notationSpecificElement) {
		this.owningView = owningView;
		this.notationSpecificElement = notationSpecificElement;
	}
	
	public FMView getOwningView() {
		return owningView;
	}
	
	public Object getNotationSpecificElement() {
		return notationSpecificElement;
	}
	
	public void setNativeElement(Object nativeElement) {
		this.notationSpecificElement = nativeElement;
	}
}
