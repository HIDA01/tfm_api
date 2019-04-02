package tfm.util;

import java.util.Date;

public class DateTemporalPoint extends TemporalPoint {
	private Date date;

	public DateTemporalPoint() {
		this (new Date());
	}
	
	public DateTemporalPoint(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public boolean equals(Object o) {
		if(date == null) {
			if(o == null) {
				return true;
			}
			else {
				return false;
			}
		}
		
		if(o instanceof DateTemporalPoint) {
			Date when = ((DateTemporalPoint) o).getDate();
			
			return date.equals(when);
		}
		else {
			return false;
		}
		
	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}

	@Override
	public boolean before(TemporalPoint temporalPoint) {
		if(date == null || temporalPoint == null) {
			return false;
		}
		
		if(temporalPoint instanceof DateTemporalPoint) {
			Date parameterDate = ((DateTemporalPoint) temporalPoint).getDate();
			return date.before(parameterDate);
		}
		
		throw new UnsupportedOperationException("Cannot compare Date with non Dates"); // TODO more sensible handling, e.g., if compared with revisions?
	}

	@Override
	public boolean after(TemporalPoint temporalPoint) {
		if(date == null || temporalPoint == null) {
			return false;
		}
		
		if(temporalPoint instanceof DateTemporalPoint) {
			Date parameterDate = ((DateTemporalPoint) temporalPoint).getDate();
			return date.after(parameterDate);
		}
		
		throw new UnsupportedOperationException("Cannot compare Date with non Dates"); // TODO more sensible handling, e.g., if compared with revisions?
	}

	@Override
	public TemporalPoint clone() {
		Date clonedDate = null;
		if(date != null) {
			clonedDate = (Date)getDate().clone();
		}
		DateTemporalPoint clone = new DateTemporalPoint(clonedDate);
		return clone;
	}
	
	public static DateTemporalPoint minimum(DateTemporalPoint temporalPoint1, DateTemporalPoint temporalPoint2) {
		if (temporalPoint1 == null) {
			return temporalPoint2;
		}

		if (temporalPoint2 == null) {
			return temporalPoint1;
		}

		if (temporalPoint1.before(temporalPoint2)) {
			return temporalPoint1;
		} else {
			return temporalPoint2;
		}
	}
	
	public static DateTemporalPoint maximum(DateTemporalPoint temporalPoint1, DateTemporalPoint temporalPoint2) {
		if (temporalPoint1 == null) {
			return temporalPoint2;
		}

		if (temporalPoint2 == null) {
			return temporalPoint1;
		}

		if (temporalPoint1.after(temporalPoint2)) {
			return temporalPoint1;
		} else {
			return temporalPoint2;
		}
	}

	@Override
	public String toString() {
		if (date == null) {
			return "null";
		}
		else {
			return Long.toString(date.getTime());
		}
	}
}
