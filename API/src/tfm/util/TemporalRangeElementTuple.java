package tfm.util;

public class TemporalRangeElementTuple<T> {

	private TemporalRange temporalRange;
	private T element;
	
	public TemporalRangeElementTuple() {
		
	}
	
	public TemporalRangeElementTuple(T element, TemporalRange temporalRange) {
		setElement(element);
		setTemporalRange(temporalRange);
	}
	
	public TemporalRange getTemporalRange() {
		return temporalRange;
	}
	public void setTemporalRange(TemporalRange temporalRange) {
		this.temporalRange = temporalRange;
	}
	public T getElement() {
		return element;
	}
	public void setElement(T element) {
		this.element = element;
	}
}
