package api.fm;

import api.fm.listener.FMViewListener;

//NOTE: When adding/modifying methods in the FM API, also make adequate changes to:
//- the listener support at FMViewListener and FMViewCoreLogic
//- the abstract implementations in AbstractFMView (e.g., the do...() methods)
//- the TFM API at TFMView
//- the reference implementations (DeltaEcore etc.)

/**
 * Convenience interface providing both read and write support for feature models.
 * 
 * @author Christoph Seidl, Michael Nieke
 */
public interface FMView extends FMReadView, FMWriteView {
	// Listener support
	public void addListener(FMViewListener listener);
	public void removeListener(FMViewListener listener);
	
	// Worker methods are the sum of those of the extended interfaces.
}
