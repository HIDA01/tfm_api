package api.tfm;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import api.base.TFMToFMAdapter;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalGroup;
import tfm.TemporalGroupVariationType;
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
 * @param <G>
 *            Type of notation specific Temporal Group
 */
public abstract class AbstractTFMViewWithFeatureAndGroupCaching<FM, F, G>
		extends AbstractTFMViewWithFeatureCaching<FM, F> {

	protected Map<G, TemporalGroup> wrappedTemporalGroupCache;
	
	protected Map<TemporalGroup, Map<TemporalPoint, Group>> wrappedGroupCache;
	protected Map<Group, TemporalGroup> groupToTemporalGroupCache;

	public AbstractTFMViewWithFeatureAndGroupCaching(String rootFeatureName) {
		super(rootFeatureName);
		initialize();
	}

	public AbstractTFMViewWithFeatureAndGroupCaching(FM notationSpecificTemporalFeatureModel) {
		super(notationSpecificTemporalFeatureModel);
		initialize();
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		wrappedTemporalGroupCache = new HashMap<G, TemporalGroup>();
		wrappedGroupCache = new HashMap<TemporalGroup, Map<TemporalPoint, Group>>();
		groupToTemporalGroupCache = new HashMap<Group, TemporalGroup>();
	}

	@Override
	protected List<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureChildGroups(
			F notationSpecificFeature) {
		
		List<TemporalRangeElementTuple<G>> notationSpecificChildGroups = doGetAllNotationSpecificFeatureNotationSpecificChildGroups(notationSpecificFeature);
		return createTemporalRangeTemporalGroupTuplesFromNotationSpecific(notationSpecificChildGroups);
	}

	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	protected abstract List<TemporalRangeElementTuple<G>> doGetAllNotationSpecificFeatureNotationSpecificChildGroups(
			F notationSpecificParentFeature);
	
	@Override
	protected Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureParentGroups(
			F notationSpecificFeature) {
		Collection<TemporalRangeElementTuple<G>> notationSpecificParentGroups = doGetAllNotationSpecificFeatureNotationSpecificParentGroups(notationSpecificFeature);
		
		return createTemporalRangeTemporalGroupTuplesFromNotationSpecific(notationSpecificParentGroups);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<G>> doGetAllNotationSpecificFeatureNotationSpecificParentGroups(
			F notationSpecificChildFeature);
	
	@Override
	protected List<TemporalRangeElementTuple<F>> doGetAllGroupNotationSpecificChildFeatures(TemporalGroup temporalGroup) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);
		
		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
		}
		
		return doGetAllNotationSpecificGroupNotationSpecificChildFeatures(notationSpecificGroup);
	}


	/**
	 * Order remains stable during evolution
	 * @param temporalParentFeature
	 * @return
	 */
	protected abstract List<TemporalRangeElementTuple<F>> doGetAllNotationSpecificGroupNotationSpecificChildFeatures(
			G notationSpecificParentGroup);
	
	@Override
	protected Collection<TemporalRangeElementTuple<F>> doGetAllGroupNotationSpecificParentFeatures(TemporalGroup temporalGroup) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);
		
		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
		}
		
		return doGetAllNotationSpecificGroupNotationSpecificParentFeatures(notationSpecificGroup);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<F>> doGetAllNotationSpecificGroupNotationSpecificParentFeatures(G notationSpecificChildGroup);

	@Override
	protected Collection<TemporalGroupVariationType> doGetAllGroupVariationTypes(TemporalGroup temporalGroup) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);

		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group " + temporalGroup.toString()
					+ ". Maybe it's unknown in the model.");
		}
		
		return doGetNotationSpecificGroupVariationTypes(notationSpecificGroup);
	}

	protected abstract Collection<TemporalGroupVariationType> doGetNotationSpecificGroupVariationTypes(
			G notationSpecificGroup);

	

	@Override
	protected Group doGetGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		Map<TemporalPoint, Group> temporalGroupToGroupCache = wrappedGroupCache.get(temporalGroup);
		TFMToFMAdapter adapter = getTFMToFMAdapter(temporalPoint);
		
		Group group = null;
		
		if(temporalGroupToGroupCache == null) {
			temporalGroupToGroupCache = new HashMap<TemporalPoint, Group>();
			wrappedGroupCache.put(temporalGroup, temporalGroupToGroupCache);
		}
		else {
			group = temporalGroupToGroupCache.get(temporalPoint);
		}
		
		if(group == null) {
			G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);

			if (notationSpecificGroup == null) {
				throwPreconditionViolatedException("Couldn't unwrap temporal group " + temporalGroup.toString()
						+ ". Maybe it's unknown in the model.");
			}

			group = new Group(adapter, notationSpecificGroup);
			temporalGroupToGroupCache.put(temporalPoint, group);
			groupToTemporalGroupCache.put(group, temporalGroup);
		}
		
		return group;
	}
	
	@Override
	protected TemporalGroup doGetTemporalGroup(Group group) {
		return groupToTemporalGroupCache.get(group);
	}

	

	@Override
	protected Collection<TemporalRangeElementTuple<TemporalGroup>> doGetAllNotationSpecificFeatureDefaultChildGroups(
			F notationSpecificFeature) {
		
		Collection<TemporalRangeElementTuple<G>> notationSpecificGroups = doGetAllNotationSpecificFeatureNotationSpecificDefaultChildGroups(notationSpecificFeature);

		return createTemporalRangeTemporalGroupTuplesFromNotationSpecific(notationSpecificGroups);
	}
	
	protected abstract Collection<TemporalRangeElementTuple<G>> doGetAllNotationSpecificFeatureNotationSpecificDefaultChildGroups(F notationSpecificFeature);
	
	// Write
	
	@Override
	protected TemporalGroup doCreateTemporalDefaultNotationSpecificFeatureChildGroupAt(F notationSpecificParentFeature,
			TemporalPoint temporalPoint) {
		G notationSpecificGroup = doCreateTemporalDefaultNotationSpecificFeatureNotationSpecificChildGroupAt(notationSpecificParentFeature, temporalPoint);
		
		return wrapTemporalGroup(notationSpecificGroup);
	}
	
	protected abstract G doCreateTemporalDefaultNotationSpecificFeatureNotationSpecificChildGroupAt(F notationSpecificParentFeature,
			TemporalPoint temporalPoint);
	
	@Override
	protected void doAddNotationSpecificFeatureToTemporalGroupAt(F feature, TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);

		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group " + temporalGroup.toString()
					+ ". Maybe it's unknown in the model.");
		}
		
		doAddNotationSpecificFeatureToNotationSpecificGroupAt(feature, notationSpecificGroup, temporalPoint);
	}
	
	protected abstract void doAddNotationSpecificFeatureToNotationSpecificGroupAt(F feature, G group, TemporalPoint temporalPoint);
	

	@Override
	protected void doAddTemporalGroupAt(TemporalGroup temporalGroup, F notationSpecificParentFeature,
			TemporalPoint temporalPoint) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);

		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group " + temporalGroup.toString()
					+ ". Maybe it's unknown in the model.");
		}
		
		doAddNotationSpecificGroupAt(notationSpecificGroup, notationSpecificParentFeature, temporalPoint);
	}
	
	protected abstract void doAddNotationSpecificGroupAt(G notationSpecificGroup, F notationSpecificParentFeature,
			TemporalPoint temporalPoint);
	
//	@Override
//	protected void doRemoveNotationSpecificFeatureFromTemporalGroupAt(F feature, TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
//		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);
//		
//		if (notationSpecificGroup == null) {
//			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
//		}
//		
//		doRemoveNotationSpecificFeatureFromNotationSpecificGroupAt(feature, notationSpecificGroup, temporalPoint);
//	}
//
//	/**
//	 *  Feature should not be contained by this group anymore. Used to move features to other groups
//	 * @param feature
//	 * @param group
//	 * @param temporalPoint
//	 */
//	protected abstract void doRemoveNotationSpecificFeatureFromNotationSpecificGroupAt(F feature, G group, TemporalPoint temporalPoint);

	@Override
	protected void doSetTemporalGroupVariationTypeAt(TemporalGroup temporalGroup, GroupVariationType groupVariationType, TemporalPoint temporalPoint) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);

		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
		}
		
		doSetNotationSpecificGroupVariationTypeAt(notationSpecificGroup, groupVariationType, temporalPoint);
	}

	protected abstract void doSetNotationSpecificGroupVariationTypeAt(G group, GroupVariationType groupVariationType, TemporalPoint temporalPoint);


	@Override
	protected TemporalGroup doCreateTemporalGroupAt(TemporalPoint temporalPoint) {
		G notationSpecificGroup = doCreateNotationSpecificGroupAt(temporalPoint);
		return wrapTemporalGroup(notationSpecificGroup);
	}
	
	protected abstract G doCreateNotationSpecificGroupAt(TemporalPoint temporalPoint);

	@Override
	protected void doRemoveTemporalGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);
		
		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
		}
		
		doRemoveNotationSpecificGroupAt(notationSpecificGroup, temporalPoint);
	}
	
	protected abstract void doRemoveNotationSpecificGroupAt(G notationSpecificGroup, TemporalPoint temporalPoint);
	

	@Override
	protected void doDetachTemporalGroupFromParentAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		G notationSpecificGroup = unwrapTemporalGroup(temporalGroup);
		
		if (notationSpecificGroup == null) {
			throwPreconditionViolatedException("Couldn't unwrap temporal group "+temporalGroup.toString()+". Maybe it's unknown in the model.");
		}
		
		doDetachNotationSpecificGroupFromParentAt(notationSpecificGroup, temporalPoint);
	}
	
	/**
	 * After executing this method, this group should not be contained in the feature model hierarchy anymore, i.e., it should not have a parent feature anymore.
	 * @param notationSpecificGroup
	 * @param temporalPoint
	 */
	protected abstract void doDetachNotationSpecificGroupFromParentAt(G notationSpecificGroup, TemporalPoint temporalPoint);
	
	// Auxiliary

	protected TemporalGroup wrapTemporalGroup(G notationSpecificGroup) {
		if (wrappedTemporalGroupCache.containsKey(notationSpecificGroup)) {
			return wrappedTemporalGroupCache.get(notationSpecificGroup);
		} else {
			TemporalGroup temporalGroup = new TemporalGroup(this, notationSpecificGroup);
			temporalGroup.setTemporalValidity(doGetTemporalValidityOfNotationSpecificGroup(notationSpecificGroup));
			
			wrappedTemporalGroupCache.put(notationSpecificGroup, temporalGroup);
			return temporalGroup;
		}
	}
	
	protected abstract TemporalRange doGetTemporalValidityOfNotationSpecificGroup(G notationSpecificGroup);
	
	@SuppressWarnings("unchecked")
	protected G unwrapTemporalGroup(TemporalGroup temporalGroup) {
		Object notationSpecificElement = temporalGroup.getNotationSpecificElement();
		if (wrappedTemporalGroupCache.containsKey(notationSpecificElement)) {
			return (G) notationSpecificElement;
		} else {
			throw new UnsupportedOperationException("Unknown Group");
		}
	}
	
	
//	protected Collection<TemporalRangeElementTuple<List<TemporalGroup>>> createTemporalRangeTemporalGroupListTuplesFromNotationSpecificList(
//			Collection<TemporalRangeElementTuple<List<G>>> rangeNotationSpecificGroupListTuples) {
//		
//		Collection<TemporalRangeElementTuple<List<TemporalGroup>>> groupTuples = new LinkedList<TemporalRangeElementTuple<List<TemporalGroup>>>();
//
//		for (TemporalRangeElementTuple<List<G>> rangeNotationSpecificGroupTuple : rangeNotationSpecificGroupListTuples) {
//			groupTuples.add(createTemporalRangeTemporalGroupListTupleFromNotationSpecificList(rangeNotationSpecificGroupTuple));
//		}
//
//		return groupTuples;
//	}
//
//	protected TemporalRangeElementTuple<List<TemporalGroup>> createTemporalRangeTemporalGroupListTupleFromNotationSpecificList(
//			TemporalRangeElementTuple<List<G>> rangeNotationSpecificGroupListTuple) {
//		
//		TemporalRangeElementTuple<List<TemporalGroup>> rangeGroupTuple = new TemporalRangeElementTuple<List<TemporalGroup>>();
//		rangeGroupTuple.setTemporalRange(rangeNotationSpecificGroupListTuple.getTemporalRange());
//		
//		List<TemporalGroup> wrappedTemporalGroups = new LinkedList<TemporalGroup>();
//		for (G notationSpecificGroup : rangeNotationSpecificGroupListTuple.getElement()) {
//			wrappedTemporalGroups.add(wrapTemporalGroup(notationSpecificGroup));
//		}
//		
//		rangeGroupTuple.setElement(wrappedTemporalGroups);
//
//		return rangeGroupTuple;
//	}
	

	/**
	 * Is order preserving
	 * @param rangeNotationSpecificFeatureTuples
	 * @return
	 */
	protected List<TemporalRangeElementTuple<TemporalGroup>> createTemporalRangeTemporalGroupTuplesFromNotationSpecific(
			Collection<TemporalRangeElementTuple<G>> rangeNotationSpecificGroupTuples) {
		List<TemporalRangeElementTuple<TemporalGroup>> groupTuples = new LinkedList<TemporalRangeElementTuple<TemporalGroup>>();

		for (TemporalRangeElementTuple<G> rangeNotationSpecificGroupTuple : rangeNotationSpecificGroupTuples) {
			groupTuples.add(createTemporalRangeTemporalGroupTupleFromNotationSpecific(rangeNotationSpecificGroupTuple));
		}

		return groupTuples;
	}

	protected TemporalRangeElementTuple<TemporalGroup> createTemporalRangeTemporalGroupTupleFromNotationSpecific(
			TemporalRangeElementTuple<G> rangeNotationSpecificGroupTuple) {
		TemporalRangeElementTuple<TemporalGroup> rangeGroupTuple = new TemporalRangeElementTuple<TemporalGroup>();
		rangeGroupTuple.setTemporalRange(rangeNotationSpecificGroupTuple.getTemporalRange());
		rangeGroupTuple.setElement(wrapTemporalGroup(rangeNotationSpecificGroupTuple.getElement()));

		return rangeGroupTuple;
	}
	
	@Override
	protected Collection<TemporalGroup> doGetAllTemporalGroups() {

		Collection<G> notationSpecificGroups = doGetAllNotationSpecificGroups();
		List<TemporalGroup> temporalGroups = new LinkedList<TemporalGroup>();

		for (G notationSpecificGroup : notationSpecificGroups) {
			temporalGroups.add(wrapTemporalGroup(notationSpecificGroup));
		}

		return temporalGroups;
	}
	
	protected abstract Collection<G> doGetAllNotationSpecificGroups();
}
