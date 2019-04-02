package api.fm;

import fm.FeatureModel;

//TODO: Find name that reflects that this is the first class in the hierarchy to connect to native FM notations. 
/**
 * 
 * @author Michael Nieke, Christoph Seidl
 *
 * @param <FM> Type of notation specific Feature Model
 */
public abstract class AbstractFMView<FM> extends FMViewCoreLogic {

	protected FeatureModel featureModel;
	protected FM notationSpecificFeatureModel;
	
	//TODO: Cannot force these constructors in the interface. Also hard to define a core logic with proper notification support
	//TODO: Also causes problems with instantiation order (e.g., the caches may not be initialized during constructor calls).
	/**
	 * Be sure to send notifications for creation of the root feature, setting of the root feature, change of the feature set and change of the feature model.
	 * 
	 * @param rootFeatureName
	 */
	public AbstractFMView(String rootFeatureName) {
		this.notationSpecificFeatureModel = createNotationSpecificFeatureModel(rootFeatureName);
		wrapFeatureModel(notationSpecificFeatureModel);
	}
	
	public AbstractFMView(FM notationSpecificFeatureModel) {
		this.notationSpecificFeatureModel = notationSpecificFeatureModel;
		wrapFeatureModel(notationSpecificFeatureModel);
	}
	
	/**
	 * Root Feature should be created automatically (but not using API methods. This won't work as feature model has not been initialized in the API)
	 */
	protected abstract FM createNotationSpecificFeatureModel(String rootFeatureName);
	protected abstract FeatureModel doWrapFeatureModel(FM notationSpecificFeatureModel);
	
	protected FeatureModel wrapFeatureModel(FM notationSpecificFeatureModel) {
		this.featureModel = doWrapFeatureModel(notationSpecificFeatureModel);
		return this.featureModel;
	}
	
	
	@Override
	protected FeatureModel doGetFeatureModel() {
		return featureModel;
	}

	protected FM getNotationSpecificFeatureModel() {
		return notationSpecificFeatureModel;
	}
}
