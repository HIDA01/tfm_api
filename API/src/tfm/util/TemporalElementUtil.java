package tfm.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import tfm.TemporalElement;

public class TemporalElementUtil {

	public static <T extends TemporalElement> Collection<T> getValidTemporalElements(Collection<T> elements, TemporalPoint temporalPoint) {
		List<T> validTemporalElements = new LinkedList<T>();
		
		if(elements == null) {
			return validTemporalElements;
		}
		
		for(T element: elements) {
			if(element.getTemporalValidity().contains(temporalPoint)) {
				validTemporalElements.add(element);
			}
		}
		
		return validTemporalElements;
	}
	
	public static <T> Collection<T> getValidElementsFromTemporalRangeElementTuples(Collection<TemporalRangeElementTuple<T>> tuples, TemporalPoint temporalPoint) {
		List<T> validElements = new LinkedList<T>();
		
		if(tuples == null) {
			return validElements;
		}
		
		for(TemporalRangeElementTuple<T> tuple: tuples) {
			if(tuple.getTemporalRange().contains(temporalPoint)) {
				validElements.add(tuple.getElement());
			}
		}
		
		return validElements;
	}
	
}
