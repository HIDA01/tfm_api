package tfm.util;

public class RevisionTemporalPoint extends TemporalPoint {
	//TODO: Find a more appropriate representation than String.
	private String revision;

	public RevisionTemporalPoint(String revision) {
		this.revision = revision;
	}

	public String getRevision() {
		return revision;
	}

	@Override
	public boolean equals(Object o) {
		return revision.equals(o);
	}

	@Override
	public int hashCode() {
		return revision.hashCode();
	}

	@Override
	public boolean before(TemporalPoint temporalPoint) {
		// TODO need a sensible checking for that. Maybe leave it abstract as revisions seem to me rather domain specific? (e.g.: v1, v2 or 1.0, 2.0 or A, B or...)
		return false;
	}

	@Override
	public boolean after(TemporalPoint temporalPoint) {
		// TODO need a sensible checking for that. Maybe leave it abstract as revisions seem to me rather domain specific? (e.g.: v1, v2 or 1.0, 2.0 or A, B or...)
		return false;
	}
	
	@Override
	public TemporalPoint clone() {
		RevisionTemporalPoint clone = new RevisionTemporalPoint(revision);
		return clone;
	}

	@Override
	public String toString() {
		return revision;
	}
}
