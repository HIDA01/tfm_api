package tfm.util;

public abstract class TemporalPoint {
	//TODO: Provide a richer interface (e,g, compare if before after etc.)
	
	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean before(TemporalPoint temporalPoint);
	
	public abstract boolean after(TemporalPoint temporalPoint);
	
	public abstract TemporalPoint clone();
	
	public abstract String toString();
}
