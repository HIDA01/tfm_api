package api.tfm;

import api.tfm.listener.TFMViewListener;

/**
 * Convenience interface providing both read and write support for temporal feature models.
 * 
 * NOTE: When implementing this interface, use a caching mechanism to ensure that temporal features are not generated multiple times for the same notation specific elements.
 * Otherwise, inconsistencies when using the API may occur.
 * 
 * For instance, use @link{AbstractTFMViewWithFeatureAndGroupCaching}.
 * 
 * @author Michael Nieke, Christoph Seidl
 */
public interface TFMView extends TFMReadView, TFMWriteView {
	//Interface methods are the sum of those of the extended interfaces.
	public void addListener(TFMViewListener listener);
	public void removeListener(TFMViewListener listener);
}
