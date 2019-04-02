package tfm.util;

public class TemporalRange {
	// TODO does null mean unbounded?
	private TemporalPoint lowerBound;
	private TemporalPoint upperBound; // TODO Not included?! (right open interval?) 
	
	//TODO: Enforce that both bounds are of the same concrete type?
	public TemporalRange(TemporalPoint lowerBound, TemporalPoint upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public TemporalPoint getLowerBound() {
		return lowerBound;
	}

	public TemporalPoint getUpperBound() {
		return upperBound;
	}
	
	public boolean contains(TemporalPoint temporalPoint) {
		if(temporalPoint == null) { // TODO makes sense if null means unbounded or exists forever
			return true;
		}

		if (lowerBound == null || lowerBound.equals(temporalPoint) || lowerBound.before(temporalPoint)) {

			if (upperBound == null || upperBound.after(temporalPoint)) {
				return true;
			}

		}

		return false;
	}
	
	public TemporalRange clone() {
		TemporalPoint clonedLowerBound = null;
		TemporalPoint clonedUpperBound = null;
		
		if(lowerBound != null) {
			clonedLowerBound = lowerBound.clone();
		}
		
		if(upperBound != null) {
			clonedUpperBound = upperBound.clone();
		}
		
		TemporalRange clone = new TemporalRange(clonedLowerBound, clonedUpperBound);
		return clone;
	}
}
