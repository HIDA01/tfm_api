package api.fm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.Feature;
import fm.FeatureVariationType;
import fm.Group;

/**
 * 
 * @author Michael Nieke, Christoph Seidl
 *
 * @param <FM> Type of notation specific Feature Model
 * @param <F> Type of notation specific Feature
 */
public abstract class AbstractFMViewWithFeatureCaching<FM, F> extends AbstractFMView<FM> {

	//TODO: These should be weak references
	protected Map<F, Feature> wrappedFeatureCache;

	public AbstractFMViewWithFeatureCaching(String rootFeatureName) {
		super(rootFeatureName);
		initialize();
	}
	
	public AbstractFMViewWithFeatureCaching(FM notationSpecificFeatureModel) {
		super(notationSpecificFeatureModel);
		initialize();
	}
	
	private void initialize() {
		wrappedFeatureCache = new HashMap<F, Feature>();
	}
	
	
	// Read
	
	@Override
	protected Collection<Feature> doGetAllFeatures() {
		Collection<Feature> features = createFeatureList();
		
		for (F notationSpecificFeature : getNotationSpecificAllFeatures()) {
			features.add(wrapFeature(notationSpecificFeature));
		}
		
		return features;
	}
	
	protected abstract Collection<F> getNotationSpecificAllFeatures();
	
	
	@Override
	protected Feature doGetFeature(String identifier) {
		F notationSpecificFeature = getNotationSpecificFeature(identifier);
		return wrapFeature(notationSpecificFeature);
	}
	
	protected abstract F getNotationSpecificFeature(String identifier);
	
	@Override
	protected Feature doGetFeatureParentFeature(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		F notationSpecificParentFeature = getNotationSpecificFeatureParentFeature(notationSpecificFeature);
		Feature parentFeature = wrapFeature(notationSpecificParentFeature);
		return parentFeature;
	}
	
	protected abstract F getNotationSpecificFeatureParentFeature(F feature);
	
	
	@Override
	protected Feature doGetRootFeature() {
		F notationSpecificRootFeature = getNotationSpecificRootFeature();
		return wrapFeature(notationSpecificRootFeature);
	}
	
	protected abstract F getNotationSpecificRootFeature();
	
	
	@Override
	protected boolean areIdenticalFeatures(Feature feature1, Feature feature2) {
		if (feature1 == null) {
			return false;
		}
		
		return feature1.equals(feature2);
	}
	
	
	@Override
	protected String doGetFeatureName(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		return getNotationSpecificFeatureName(notationSpecificFeature);
	}
	
	protected abstract String getNotationSpecificFeatureName(F notationSpecificFeature);

	
	@Override
	protected FeatureVariationType doGetFeatureVariationType(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		return getNotationSpecificFeatureVariationType(notationSpecificFeature);
	}
	
	protected abstract FeatureVariationType getNotationSpecificFeatureVariationType(F notationSpecificFeature);
	
	
	
	// Write
	@Override
	protected void doSetRootFeature(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		setNotationSpecificRootFeature(notationSpecificFeature);
	}
	
	protected abstract void setNotationSpecificRootFeature(F notationSpecificFeature);
	
	
	@Override
	protected Feature doCreateFeature(String identifier) {
		F notationSpecificFeature = createNotationSpecificFeature(identifier);
		Feature feature = wrapFeature(notationSpecificFeature);
		return feature;
	}

	protected abstract F createNotationSpecificFeature(String identifier);
	
	
	@Override
	protected void doRemoveFeature(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		removeNotationSpecificFeature(notationSpecificFeature);
		
		wrappedFeatureCache.remove(notationSpecificFeature);
	}
	
	protected abstract void removeNotationSpecificFeature(F notationSpecificFeature);
	
	
	protected void detachFeatureFromParent(Feature feature) {
		F notationSpecificFeature = unwrapFeature(feature);
		detachNotationSpecificFeatureFromParent(notationSpecificFeature);
	}
	
	protected abstract void detachNotationSpecificFeatureFromParent(F notationSpecificFeature);
	
	
	@Override
	protected void doSetFeatureName(Feature feature, String name) {
		F notationSpecificFeature = unwrapFeature(feature);
		setNotationSpecificFeatureName(notationSpecificFeature, name);
	}
	
	protected abstract void setNotationSpecificFeatureName(F notationSpecificFeature, String name);

	
	@Override
	protected void doSetFeatureVariationType(Feature feature, FeatureVariationType variationType) {
		F notationSpecificFeature = unwrapFeature(feature);
		setNotationSpecificFeatureVariationType(notationSpecificFeature, variationType);
	}
	
	protected abstract void setNotationSpecificFeatureVariationType(F notationSpecificFeature, FeatureVariationType variationType);
	
	// Group related methods
	@Override
	protected Group doGetFeatureParentGroup(Feature feature) {
		F nativeFeature = unwrapFeature(feature);
		return getNativeFeatureParentGroup(nativeFeature);
	}
	
	protected abstract Group getNativeFeatureParentGroup(F nativeFeature);
	
	@Override
	protected Group doGetFeatureDefaultChildGroup(Feature feature) {
		F nativeFeature = unwrapFeature(feature);
		return getNativeFeatureDefaultChildGroup(nativeFeature);
	}
	
	protected abstract Group getNativeFeatureDefaultChildGroup(F nativeFeature);
	
	@Override
	protected List<Group> doGetFeatureChildGroups(Feature feature) {
		F nativeFeature = unwrapFeature(feature);
		return getNativeFeatureChildGroups(nativeFeature);
	}
	
	protected abstract List<Group> getNativeFeatureChildGroups(F nativeFeature);

	@Override
	protected List<Feature> doGetGroupChildFeatures(Group group) {
		List<F> nativeFeatures = getGroupNativeChildFeatures(group);
		List<Feature> features = new ArrayList<Feature>(nativeFeatures.size());
		
		nativeFeatures.forEach(nativeFeature->features.add(wrapFeature(nativeFeature)));
		
		return features;
	}
	
	protected abstract List<F> getGroupNativeChildFeatures(Group group);

	@Override
	protected Feature doGetGroupParentFeature(Group group) {
		F nativeParentFeature = getGroupNativeParentFeature(group);
		return wrapFeature(nativeParentFeature);
	}
	
	protected abstract F getGroupNativeParentFeature(Group group);

	@Override
	protected void doAddFeature(Feature feature, Group parentGroup) {
		addNativeFeature(unwrapFeature(feature), parentGroup);
	}
	
	protected abstract void addNativeFeature(F nativeFeature, Group group);

	@Override
	protected Group doCreateDefaultChildGroup(Feature feature) {
		return createNativeFeatureDefaultChildGroup(unwrapFeature(feature));
	}
	
	protected abstract Group createNativeFeatureDefaultChildGroup(F nativeFeature);

	@Override
	protected void doAddGroup(Group group, Feature parentFeature) {
		addGroupToNativeFeature(group, unwrapFeature(parentFeature));
	}
	
	protected abstract void addGroupToNativeFeature(Group group, F nativeParentFeature);
	
	// Auxiliary
	
	protected Feature wrapFeature(F notationSpecificFeature) {
		if (notationSpecificFeature == null) {
			return null;
		}
		
		if (wrappedFeatureCache.containsKey(notationSpecificFeature)) {
			return wrappedFeatureCache.get(notationSpecificFeature);
		} else {
			Feature wrappedFeature = doWrapFeature(notationSpecificFeature);
			wrappedFeatureCache.put(notationSpecificFeature, wrappedFeature);
			return wrappedFeature;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected F unwrapFeature(Feature feature) {
		if (feature == null) {
			return null;
		}
		
		Object notationSpecificElement = feature.getNotationSpecificElement();
		if (wrappedFeatureCache.containsKey(notationSpecificElement)) {
			return (F) notationSpecificElement;			
		} else {
			throw new UnsupportedOperationException("Unknown feature.");
		}
	}
		
	protected abstract Feature doWrapFeature(F notationSpecificFeature);
}
