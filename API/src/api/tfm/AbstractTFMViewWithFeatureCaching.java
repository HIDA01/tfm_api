package api.tfm;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import api.base.TFMToFMAdapter;
import fm.Feature;
import fm.FeatureVariationType;
import tfm.TemporalFeature;
import tfm.TemporalFeatureVariationType;
import tfm.TemporalGroup;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRange;
import tfm.util.TemporalRangeElementTuple;

/**
 * 
 * @author Michael Nieke
 *
 * @param <FM>
 *            Type of notation specific Temporal Feature Model
 * @param <F>
 *            Type of notation specific Temporal Feature
 */
public abstract class AbstractTFMViewWithFeatureCaching<FM, F> extends AbstractTFMView<FM> {

	protected Map<F, TemporalFeature> wrappedTemporalFeatureCache;
	
	protected Map<TemporalPoint, TFMToFMAdapter> adapterMap;
	
	protected Map<TemporalFeature, Map<TemporalPoint, Feature>> wrappedFeatureCache;
	protected Map<Feature, TemporalFeature> featureToTemporalFeatureCache;
	
	protected Map<TemporalFeatureVariationType, Map<TemporalPoint, FeatureVariationType>> wrappedFeatureVariationTypeCache;
	protected Map<FeatureVariationType, TemporalFeatureVariationType> featureVariationTypeToTemporalFeatureVariationTypeCache;

	public AbstractTFMViewWithFeatureCaching(String rootFeatureName) {
		super(rootFeatureName);
		initialize();
	}

	public AbstractTFMViewWithFeatureCaching(FM notationSpecificTemporalFeatureModel) {
		super(notationSpecificTemporalFeatureModel);
		initialize();
	}

	protected void initialize() {
		adapterMap = new HashMap<TemporalPoint, TFMToFMAdapter>();
		
		wrappedTemporalFeatureCache = new HashMap<F, TemporalFeature>();
		wrappedFeatureCache = new HashMap<TemporalFeature, Map<TemporalPoint,Feature>>();
		featureToTemporalFeatureCache = new HashMap<Feature, TemporalFeature>();
		
		wrappedFeatureVariationTypeCache = new HashMap<TemporalFeatureVariationType, Map<TemporalPoint, FeatureVariationType>>();
		featureVariationTypeToTemporalFeatureVariationTypeCache = new HashMap<FeatureVariationType, TemporalFeatureVariationType>();
	}
	
	@Override
	protected boolean areIdenticalFeatures(TemporalFeature feature1, TemporalFeature feature2) {
		F f1 = unwrapTemporalFeature(feature1);
		F f2 = unwrapTemporalFeature(feature2);

		if (f1 == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+feature1.toString());
		}

		if (f2 == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+feature2.toString());
		}
		
		return areIndenticalNotationSpecificFeatures(f1, f2);
	}
	
	protected abstract boolean areIndenticalNotationSpecificFeatures(F feature1, F feature2);
	
	@Override
	protected Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllRootFeatures() {
		Collection<TemporalRangeElementTuple<F>> notationSpecificRootFeatures = doGetAllNotationSpecificRootFeatures();

		Collection<TemporalRangeElementTuple<TemporalFeature>> rootFeatures = createTemporalRangeTemporalFeatureTuplesFromNotationSpecific(
				notationSpecificRootFeatures);
		
		return rootFeatures;
	}

	protected abstract Collection<TemporalRangeElementTuple<F>> doGetAllNotationSpecificRootFeatures();
	
	@Override
	protected Collection<TemporalRangeElementTuple<String>> doGetAllFeatureNames(TemporalFeature temporalFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		return doGetAllNotationSpecificFeatureNames(notationSpecificFeature);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<String>> doGetAllNotationSpecificFeatureNames(F notationSpecificFeature);
	
	@Override
	protected Collection<TemporalFeatureVariationType> doGetAllFeatureVariationTypes(TemporalFeature temporalFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		return doGetAllNotationSpecificFeatureVariationTypes(notationSpecificFeature);
	}
	
	protected abstract Collection<TemporalFeatureVariationType> doGetAllNotationSpecificFeatureVariationTypes(F notationSpecificFeature);

	@Override
	protected Collection<TemporalFeature> doGetAllTemporalFeatures() {

		Collection<F> notationSpecificFeatures = doGetAllNotationSpecificFeatures();
		List<TemporalFeature> temporalFeatures = new LinkedList<TemporalFeature>();

		for (F notationSpecificFeature : notationSpecificFeatures) {
			temporalFeatures.add(wrapTemporalFeature(notationSpecificFeature));
		}

		return temporalFeatures;
	}

	protected abstract Collection<F> doGetAllNotationSpecificFeatures();
	
	@Override
	protected TemporalFeature doGetTemporalFeature(Feature feature) {
		return featureToTemporalFeatureCache.get(feature);
	}
	
	@Override
	protected Feature doGetFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		Map<TemporalPoint, Feature> temporalFeatureToFeatureCache = wrappedFeatureCache.get(temporalFeature);
		TFMToFMAdapter adapter = getTFMToFMAdapter(temporalPoint);
		
		Feature feature = null;
		
		if(temporalFeatureToFeatureCache == null) {
			temporalFeatureToFeatureCache = new HashMap<TemporalPoint, Feature>();
			wrappedFeatureCache.put(temporalFeature, temporalFeatureToFeatureCache);
		}
		else {
			feature = temporalFeatureToFeatureCache.get(temporalPoint);
		}
		
		if(feature == null) {
			F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);

			if (notationSpecificFeature == null) {
				throwPreconditionViolatedException("Cannot unwrap temporal feature "+temporalFeature.toString()+". Maybe it's defined in another feature model");
			}
			
			feature = new Feature(adapter, notationSpecificFeature);
			temporalFeatureToFeatureCache.put(temporalPoint, feature);
			featureToTemporalFeatureCache.put(feature, temporalFeature);
		}
		
		return feature;
	}
	
	@Override
	protected Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllGroupParentFeatures(
			TemporalGroup temporalGroup) {
		Collection<TemporalRangeElementTuple<F>> groupNotationSpecificParentFeatures = doGetAllGroupNotationSpecificParentFeatures(temporalGroup);
		
		return createTemporalRangeTemporalFeatureTuplesFromNotationSpecific(groupNotationSpecificParentFeatures);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<F>> doGetAllGroupNotationSpecificParentFeatures(TemporalGroup temporalGroup);
	
	@Override
	protected List<TemporalRangeElementTuple<TemporalFeature>> doGetAllGroupChildFeatures(
			TemporalGroup temporalGroup) {
		List<TemporalRangeElementTuple<F>> notationSpecificChildFeatures = doGetAllGroupNotationSpecificChildFeatures(temporalGroup);
		
		return createTemporalRangeTemporalFeatureTuplesFromNotationSpecific(notationSpecificChildFeatures);
	}

	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	protected abstract List<TemporalRangeElementTuple<F>> doGetAllGroupNotationSpecificChildFeatures(TemporalGroup temporalGroup);

	@Override
	protected Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureParentGroups(
			TemporalFeature temporalFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		return doGetAllNotationSpecificFeatureParentGroups(notationSpecificFeature);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureParentGroups(
			F notationSpecificFeature);
	
	@Override
	protected List<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureChildGroups(
			TemporalFeature parentFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(parentFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+parentFeature.toString());
		}
		
		return doGetAllNotationSpecificFeatureChildGroups(notationSpecificFeature);
	}

	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	protected abstract List<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureChildGroups(
			F notationSpecificFeature);
	
	@Override
	protected Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllFeatureDefaultChildGroups(
			TemporalFeature parentTemporalFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(parentTemporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+parentTemporalFeature.toString());
		}
		
		return doGetAllNotationSpecificFeatureDefaultChildGroups(notationSpecificFeature);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureDefaultChildGroups(
			F notationSpecificFeature);
	
	@Override
	protected Collection<TemporalRangeElementTuple<TemporalFeature>> doGetAllFeatureParentFeatures(
			TemporalFeature temporalFeature) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		Collection<TemporalRangeElementTuple<F>> notationSpecificParentFeatures = doGetAllFeatureParentFeatures(notationSpecificFeature);
		return createTemporalRangeTemporalFeatureTuplesFromNotationSpecific(notationSpecificParentFeatures);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<F>> doGetAllFeatureParentFeatures(
			F notationSpecificFeature);
	
	// Write

//	@Override
//	public TemporalFeature createTemporalFeature() {
//		return wrapTemporalFeature(doCreateNotationSpecificFeature());
//	}


	@Override
	protected void doAddTemporalFeatureAt(TemporalFeature temporalFeature, TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);

		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		doAddNotationSpecificFeatureToTemporalGroupAt(notationSpecificFeature, temporalGroup, temporalPoint);
	}
	
	protected abstract void doAddNotationSpecificFeatureToTemporalGroupAt(F feature, TemporalGroup temporalGroup, TemporalPoint temporalPoint);

	@Override
	protected void doRemoveTemporalFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		doRemoveNotationSpecificFeatureAt(notationSpecificFeature, temporalPoint);
	}
	
	protected abstract void doRemoveNotationSpecificFeatureAt(F feature, TemporalPoint temporalPoint);
	
//	@Override
//	protected void doRemoveTemporalFeatureFromTemporalGroupAt(TemporalFeature temporalFeature, TemporalGroup temporalGroup,
//			TemporalPoint temporalPoint) {
//		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
//		
//		if (notationSpecificFeature == null) {
//			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
//		}
//		
//		doRemoveNotationSpecificFeatureFromTemporalGroupAt(notationSpecificFeature, temporalGroup, temporalPoint);
//	}
//	
//	/**
//	 *  Feature should not be contained by this group anymore. Used to move features to other groups
//	 * @param feature
//	 * @param temporalGroup
//	 * @param temporalPoint
//	 */
//	protected abstract void doRemoveNotationSpecificFeatureFromTemporalGroupAt(F feature, TemporalGroup temporalGroup, TemporalPoint temporalPoint);
	
	@Override
	protected void doSetTemporalFeatureNameAt(TemporalFeature temporalFeature, String name, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		doSetNotationSpecificFeatureNameAt(notationSpecificFeature, name, temporalPoint);
	}
	
	protected abstract void doSetNotationSpecificFeatureNameAt(F feature, String name, TemporalPoint temporalPoint);

	@Override
	protected void doSetTemporalFeatureVariationTypeAt(TemporalFeature temporalFeature,
			FeatureVariationType featureVariationType, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		doSetNotationSpecificFeatureVariationTypeAt(notationSpecificFeature, featureVariationType, temporalPoint);
	}
	
	protected abstract void doSetNotationSpecificFeatureVariationTypeAt(F feature, FeatureVariationType featureVariationType, TemporalPoint temporalPoint);

	@Override
	protected void doSetTemporalRootFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		doSetNotationSpecificRootFeatureAt(notationSpecificFeature, temporalPoint);
	}
	
	protected abstract void doSetNotationSpecificRootFeatureAt(F feature, TemporalPoint temporalPoint);
	
	@Override
	protected TemporalFeature doCreateTemporalFeatureAt(String identifier, TemporalPoint temporalPoint) {
		F notationSpecificFeature = doCreateNotationSpecificFeatureAt(identifier, temporalPoint);
		return wrapTemporalFeature(notationSpecificFeature);
	}
	
	protected abstract F doCreateNotationSpecificFeatureAt(String identifier, TemporalPoint temporalPoint);
	
	@Override
	protected void doDetachTemporalFeatureFromParentAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalFeature.toString());
		}
		
		detachNotationSpecificFeatureFromParentAt(notationSpecificFeature, temporalPoint);
	}
	
	protected abstract void detachNotationSpecificFeatureFromParentAt(F notationSpecificFeature, TemporalPoint temporalPoint);

	@Override
	protected void doAddTemporalGroupAt(TemporalGroup temporalGroup, TemporalFeature temporalParentFeature,
			TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(temporalParentFeature);
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+temporalParentFeature.toString());
		}
		
		doAddTemporalGroupAt(temporalGroup, notationSpecificFeature, temporalPoint);
	}
	
	protected abstract void doAddTemporalGroupAt(TemporalGroup temporalGroup, F notationSpecificParentFeature, TemporalPoint temporalPoint);
	
	@Override
	protected TemporalGroup doCreateTemporalDefaultChildGroupAt(TemporalFeature parentFeature,
			TemporalPoint temporalPoint) {
		F notationSpecificFeature = unwrapTemporalFeature(parentFeature);

		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Cannot unwrap temporal feature "+parentFeature.toString()+". Maybe it's defined in another feature model");
		}
		
		if (notationSpecificFeature == null) {
			throwPreconditionViolatedException("Could not retrieve notation specific feature for temporal feature "+parentFeature.toString());
		}
		
		return doCreateTemporalDefaultNotationSpecificFeatureChildGroupAt(notationSpecificFeature, temporalPoint);
	}
	
	protected abstract TemporalGroup doCreateTemporalDefaultNotationSpecificFeatureChildGroupAt(F notationSpecificParentFeature, TemporalPoint temporalPoint);
	
	
	
	// Auxiliary

	/**
	 * This method should be used to retrieve temporal features from a notation specific feature
	 * @param notationSpecificFeature
	 * @return
	 */
	protected TemporalFeature wrapTemporalFeature(F notationSpecificFeature) {
		if (wrappedTemporalFeatureCache.containsKey(notationSpecificFeature)) {
			return wrappedTemporalFeatureCache.get(notationSpecificFeature);
		} else {
			TemporalFeature temporalFeature = new TemporalFeature(this, notationSpecificFeature);
			temporalFeature.setTemporalValidity(doGetTemporalValidityOfNotationSpecificFeature(notationSpecificFeature));

			wrappedTemporalFeatureCache.put(notationSpecificFeature, temporalFeature);
			return temporalFeature;
		}
	}
	
	protected abstract TemporalRange doGetTemporalValidityOfNotationSpecificFeature(F notationSpecificFeature);

	@SuppressWarnings("unchecked")
	protected F unwrapTemporalFeature(TemporalFeature temporalFeature) {
		Object notationSpecificElement = temporalFeature.getNotationSpecificElement();
		if (wrappedTemporalFeatureCache.containsKey(notationSpecificElement)) {
			return (F) notationSpecificElement;
		} else {
			return null;
		}
	}
	
	protected TFMToFMAdapter getTFMToFMAdapter(TemporalPoint temporalPoint) {
		TFMToFMAdapter adapter = adapterMap.get(temporalPoint);
		
		if(adapter == null) {
			adapter = new TFMToFMAdapter(this, temporalPoint);
			adapterMap.put(temporalPoint, adapter);
		}
		
		return adapter;
	}
	
//	protected Collection<TemporalRangeElementTuple<List<TemporalFeature>>> createTemporalRangeTemporalFeatureListTuplesFromNotationSpecificList(
//			Collection<TemporalRangeElementTuple<List<F>>> rangeNotationSpecificFeatureListTuples) {
//		
//		Collection<TemporalRangeElementTuple<List<TemporalFeature>>> groupTuples = new LinkedList<TemporalRangeElementTuple<List<TemporalFeature>>>();
//
//		for (TemporalRangeElementTuple<List<F>> rangeNotationSpecificFeatureTuple : rangeNotationSpecificFeatureListTuples) {
//			groupTuples.add(createTemporalRangeTemporalFeatureListTupleFromNotationSpecificList(rangeNotationSpecificFeatureTuple));
//		}
//
//		return groupTuples;
//	}
//
//	protected TemporalRangeElementTuple<List<TemporalFeature>> createTemporalRangeTemporalFeatureListTupleFromNotationSpecificList(
//			TemporalRangeElementTuple<List<F>> rangeNotationSpecificFeatureListTuple) {
//		
//		TemporalRangeElementTuple<List<TemporalFeature>> rangeFeatureTuple = new TemporalRangeElementTuple<List<TemporalFeature>>();
//		rangeFeatureTuple.setTemporalRange(rangeNotationSpecificFeatureListTuple.getTemporalRange());
//		
//		List<TemporalFeature> wrappedTemporalFeatures = new LinkedList<TemporalFeature>();
//		for (F notationSpecificFeature : rangeNotationSpecificFeatureListTuple.getElement()) {
//			wrappedTemporalFeatures.add(wrapTemporalFeature(notationSpecificFeature));
//		}
//		
//		rangeFeatureTuple.setElement(wrappedTemporalFeatures);
//
//		return rangeFeatureTuple;
//	}
	
	/**
	 * Is order preserving
	 * @param rangeNotationSpecificFeatureTuples
	 * @return
	 */
	protected List<TemporalRangeElementTuple<TemporalFeature>> createTemporalRangeTemporalFeatureTuplesFromNotationSpecific(
			Collection<TemporalRangeElementTuple<F>> rangeNotationSpecificFeatureTuples) {
		List<TemporalRangeElementTuple<TemporalFeature>> featureTuples = new LinkedList<TemporalRangeElementTuple<TemporalFeature>>();

		for (TemporalRangeElementTuple<F> rangeNotationSpecificFeatureTuple : rangeNotationSpecificFeatureTuples) {
			featureTuples.add(
					createTemporalRangeTemporalFeatureTupleFromNotationSpecific(rangeNotationSpecificFeatureTuple));
		}

		return featureTuples;
	}

	protected TemporalRangeElementTuple<TemporalFeature> createTemporalRangeTemporalFeatureTupleFromNotationSpecific(
			TemporalRangeElementTuple<F> rangeNotationSpecificFeatureTuple) {
		TemporalRangeElementTuple<TemporalFeature> rangeFeatureTuple = new TemporalRangeElementTuple<TemporalFeature>();
		rangeFeatureTuple.setTemporalRange(rangeNotationSpecificFeatureTuple.getTemporalRange());
		rangeFeatureTuple.setElement(wrapTemporalFeature(rangeNotationSpecificFeatureTuple.getElement()));

		return rangeFeatureTuple;
	}

}
