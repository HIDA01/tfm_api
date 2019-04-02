package api.tfm;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import api.base.TFMToFMAdapter;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;
import tfm.TemporalElement;
import tfm.TemporalFeature;
import tfm.TemporalFeatureModel;
import tfm.TemporalFeatureVariationType;
import tfm.TemporalGroup;
import tfm.TemporalGroupVariationType;
import tfm.util.TemporalElementUtil;
import tfm.util.TemporalPoint;
import tfm.util.TemporalRange;
import tfm.util.TemporalRangeElementTuple;

/**
 * 
 * @author Michael Nieke
 *
 * @param <FM> Type of notation specific Feature Model
 */
public abstract class AbstractTFMView<FM> extends TFMViewCoreLogic {

	protected TemporalFeatureModel temporalFeatureModel;
	protected FM notationSpecificTemporalFeatureModel;
	
	//TODO: Cannot force these constructors in the interface. Also hard to define a core logic with proper notification support
	//TODO: Also causes problems with instantiation order (e.g., the caches may not be initialized during constructor calls).
	/**
	 * Be sure to send notifications for creation of the root feature, setting of the root feature, change of the feature set and change of the feature model.
	 * 
	 * @param rootFeatureName
	 */
	public AbstractTFMView(String rootFeatureName) {
		this.notationSpecificTemporalFeatureModel = createNotationSpecificTemporalFeatureModel(rootFeatureName);
		wrapTemporalFeatureModel(notationSpecificTemporalFeatureModel);
	}
	
	public AbstractTFMView(FM notationSpecificTemporalFeatureModel) {
		this.notationSpecificTemporalFeatureModel = notationSpecificTemporalFeatureModel;
		wrapTemporalFeatureModel(notationSpecificTemporalFeatureModel);
	}
	
	/**
	 * Root Feature should be created automatically (but not using API methods. This won't work as feature model has not been initialized in the API)
	 */
	protected abstract FM createNotationSpecificTemporalFeatureModel(String rootFeatureName);
	
	protected TemporalFeatureModel wrapTemporalFeatureModel(FM notationSpecificTemporalFeatureModel) {
		this.temporalFeatureModel = new TemporalFeatureModel(this, notationSpecificTemporalFeatureModel);
		this.temporalFeatureModel.setTemporalValidity(doGetTemporalValidityOfNotationSpecificFeatureModel(notationSpecificTemporalFeatureModel));
		
		return this.temporalFeatureModel;
	}
	
	/**
	 * Is only relevant if there is a limited life span of the feature model. May also be null
	 * @param notationSpecificFeatureModel
	 * @return
	 */
	protected abstract TemporalRange doGetTemporalValidityOfNotationSpecificFeatureModel(FM notationSpecificFeatureModel);
	
	@Override
	protected TemporalFeatureModel doGetTemporalFeatureModel() {
		return this.temporalFeatureModel;
	}

	@Override
	protected FeatureModel doGetFeatureModelAt(TemporalPoint temporalPoint) {
		TFMToFMAdapter adapter = new TFMToFMAdapter(this, temporalPoint);
		FeatureModel featureModel = new FeatureModel(adapter, this.notationSpecificTemporalFeatureModel);
		return featureModel;
	}
	
	
	@Override
	protected List<TemporalPoint> doGetAllTemporalPoints() {
		Collection<TemporalPoint> temporalPoints = new HashSet<TemporalPoint>();
		
		// Add temporal points for each changed root feature
		for(TemporalRangeElementTuple<TemporalFeature> rangeRootFeatureTuple: getAllRootFeatures()) {
			addTemporalPointsOfTemporalRangeToList(rangeRootFeatureTuple.getTemporalRange(), temporalPoints);
		}
		
		HashSet<TemporalGroup> checkedGroups = new HashSet<TemporalGroup>();
		
		// Add temporal points for each added or remove feature
		for(TemporalFeature temporalFeature: getAllTemporalFeatures()) {
			addTemporalPointsOfTemporalElementToList(temporalFeature, temporalPoints);
			
			for(TemporalFeatureVariationType featureVariationType: getAllFeatureVariationTypes(temporalFeature)) {
				addTemporalPointsOfTemporalElementToList(featureVariationType, temporalPoints);
			}
			
			
			
			for(TemporalRangeElementTuple<TemporalGroup> rangeChildGroupListTuple: getAllFeatureChildGroups(temporalFeature)) {
				addTemporalPointsOfTemporalRangeToList(rangeChildGroupListTuple.getTemporalRange(), temporalPoints);
				
				TemporalGroup group = rangeChildGroupListTuple.getElement();
				if(checkedGroups.contains(group)) {
					continue;
				}
				addTemporalPointsOfGroupsToList(group, temporalPoints);
			}
			
			for(TemporalRangeElementTuple<TemporalGroup> rangeParentGroupTuple: getAllFeatureParentGroups(temporalFeature)) {
				addTemporalPointsOfTemporalRangeToList(rangeParentGroupTuple.getTemporalRange(), temporalPoints);
				
				TemporalGroup group = rangeParentGroupTuple.getElement();
				// Duplicate group checks may occur as features may have the same child groups if groups have been moved.
				if(checkedGroups.contains(group)) {
					continue;
				}
				addTemporalPointsOfGroupsToList(group, temporalPoints);
			}
			
			// Note checking child features or parent features of groups is not necessary as it's covered by the child and parent groups of the features. 
		}
		
		return new LinkedList<TemporalPoint>(temporalPoints);
	}
	
	private void addTemporalPointsOfGroupsToList(TemporalGroup group, Collection<TemporalPoint> temporalPoints) {
		addTemporalPointsOfTemporalElementToList(group, temporalPoints);
		
		for(TemporalGroupVariationType groupVariationType: getAllGroupVariationTypes(group)) {
			addTemporalPointsOfTemporalElementToList(groupVariationType, temporalPoints);
		}
	}
	
	private void addTemporalPointsOfTemporalElementToList(TemporalElement temporalElement, Collection<TemporalPoint> collection) {
		TemporalRange range = temporalElement.getTemporalValidity();
		addTemporalPointsOfTemporalRangeToList(range, collection);
	}
	
	private void addTemporalPointsOfTemporalRangeToList(TemporalRange range, Collection<TemporalPoint> collection) {
		if(range != null) {
			TemporalPoint lowerBound = range.getLowerBound();
			TemporalPoint upperBound = range.getUpperBound();
			if(lowerBound != null) {
				collection.add(lowerBound);
			}
			if(upperBound != null) {
				collection.add(upperBound);
			}
		}
	}
	
	// Implemented methods to retrieve features at a certain point in time.

	@Override
	protected Feature doGetRootFeatureAt(TemporalPoint temporalPoint) {
		
		Collection<TemporalFeature> validRootFeatures = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(getAllRootFeatures(),temporalPoint);
		
		switch(validRootFeatures.size()) {
		case 0:
			//TODO inconsistency if no root feature.
			return null;
		case 1:
			for(TemporalFeature temporalFeature: validRootFeatures) {
				return getFeatureAt(temporalFeature, temporalPoint);
			}
		default:
			//TODO inconsistency if more than one.
			return null;
		}
	}
	
	@Override
	protected void doSetRootFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		doSetTemporalRootFeatureAt(temporalFeature, temporalPoint);
	}

	protected abstract void doSetTemporalRootFeatureAt(TemporalFeature feature, TemporalPoint temporalPoint);

	@Override
	protected Feature doGetFeatureAt(String identifier, TemporalPoint temporalPoint) {
		for (TemporalFeature temporalFeature : TemporalElementUtil.getValidTemporalElements(getAllTemporalFeatures(),
				temporalPoint)) {

			// TODO should be an inconsistency if more than one valid name?
			Collection<String> validNames = TemporalElementUtil
					.getValidElementsFromTemporalRangeElementTuples(getAllFeatureNames(temporalFeature), temporalPoint);
			for (String name : validNames) {
				if (identifier.equals(name)) {
					return getFeatureAt(temporalFeature, temporalPoint);
				}
			}
		}

		return null;
	}

	@Override
	protected  Collection<Feature> doGetAllFeaturesAt(TemporalPoint temporalPoint) {
		Collection<TemporalFeature> validTemporalFeatures = TemporalElementUtil.getValidTemporalElements(getAllTemporalFeatures(), temporalPoint);
		
		Set<Feature> features = new HashSet<Feature>();

		
		for (TemporalFeature temporalFeature : validTemporalFeatures) {
			features.add(getFeatureAt(temporalFeature, temporalPoint));
		}
		
		return features;
	}
	
	@Override
	protected Collection<Group> doGetAllGroupsAt(TemporalPoint temporalPoint) {
		Collection<TemporalGroup> validTemporalGroups = TemporalElementUtil.getValidTemporalElements(getAllTemporalGroups(), temporalPoint);
		
		Set<Group> groups = new HashSet<Group>();
		
		for (TemporalGroup temporalGroup : validTemporalGroups) {
			groups.add(getGroupAt(temporalGroup, temporalPoint));
		}
		
		return groups;
	}
	
	@Override
	protected  List<Group> doGetFeatureChildGroupsAt(Feature parentFeature, TemporalPoint temporalPoint) {
		List<TemporalRangeElementTuple<TemporalGroup>> temporalGroupListTuples = getAllFeatureChildGroups(getTemporalFeature(parentFeature));
		
		Collection<TemporalGroup> validTemporalGroups = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(temporalGroupListTuples, temporalPoint);
		
		List<Group> validGroups = new LinkedList<Group>();
		
		for(TemporalGroup temporalGroup: validTemporalGroups) {
			validGroups.add(getGroupAt(temporalGroup, temporalPoint));
		}
		
		return validGroups;
	}
	
	@Override
	protected Group doGetFeatureDefaultChildGroupAt(Feature parentFeature, TemporalPoint temporalPoint) {
		TemporalFeature parentTemporalFeature = getTemporalFeature(parentFeature);
		Collection<TemporalRangeElementTuple<TemporalGroup>> defaultGroups = getAllFeatureDefaultChildGroups(parentTemporalFeature);
		
		Collection<TemporalGroup> validDefaultGroups = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(defaultGroups, temporalPoint);
		
		if (validDefaultGroups.size() == 0) {
			return null;
		}
		if (validDefaultGroups.size() > 1) {
			throwPreconditionViolatedException("Multiple valid default group at temporal point " + temporalPoint.toString()
			+ " for parent feature " + parentFeature.getName());
		}
		
		TemporalGroup defaultTemporalGroup = null;
		for (TemporalGroup temporalGroup : validDefaultGroups) {
			defaultTemporalGroup = temporalGroup;
		}
		
		Group defaultGroup = getGroupAt(defaultTemporalGroup, temporalPoint);
		
		return defaultGroup;
	}
	

	@Override
	protected  Group doGetFeatureParentGroupAt(Feature feature, TemporalPoint temporalPoint) {
		Collection<TemporalRangeElementTuple<TemporalGroup>> parentGroups = getAllFeatureParentGroups(getTemporalFeature(feature));
		
		Collection<TemporalGroup> validTemporalGroups = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(parentGroups, temporalPoint);
		
		switch(validTemporalGroups.size()) {
			case 0:
				//TODO inconsistency if no parent group feature. Except for root feature.
				return null;
			case 1:
				for(TemporalGroup temporalGroup: validTemporalGroups) {
					return getGroupAt(temporalGroup, temporalPoint);
				}
			default:
				//TODO inconsistency if more than one.
				return null;
		}
	}
	
	@Override
	protected Feature doGetFeatureParentFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		Group parentGroup = doGetFeatureParentGroupAt(feature, temporalPoint);
		return getGroupParentFeatureAt(parentGroup, temporalPoint);
	}
	
//	protected abstract Group get(TemporalGroup temporalGroup, TemporalPoint temporalPoint);

	@Override
	protected  List<Feature> doGetGroupChildFeaturesAt(Group group, TemporalPoint temporalPoint) {
		List<TemporalRangeElementTuple<TemporalFeature>> temporalChildFeatures = getAllGroupChildFeatures(getTemporalGroup(group));
		
		
		Collection<TemporalFeature> validTemporalChildFeatures = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(temporalChildFeatures, temporalPoint);
		
		List<Feature> childFeatures = new LinkedList<Feature>();
		
		for(TemporalFeature temporalFeature: validTemporalChildFeatures) {
			childFeatures.add(getFeatureAt(temporalFeature, temporalPoint));
		}
		
		return childFeatures;
	}

	@Override
	protected Feature doGetGroupParentFeatureAt(Group group, TemporalPoint temporalPoint) {
		Collection<TemporalRangeElementTuple<TemporalFeature>> temporalParentFeatures = getAllGroupParentFeatures(getTemporalGroup(group));
		Collection<TemporalFeature> validTemporalParentFeatures = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(temporalParentFeatures, temporalPoint);
		
		switch(validTemporalParentFeatures.size()) {
		case 0:
			//TODO inconsistency if no parent feature.
			return null;
		case 1:
			for(TemporalFeature temporalFeature: validTemporalParentFeatures) {
				return getFeatureAt(temporalFeature, temporalPoint);
			}
		default:
			//TODO inconsistency if more than one.
			return null;
		}
	}

	@Override
	protected Feature doCreateFeatureAt(String identifier, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = doCreateTemporalFeatureAt(identifier, temporalPoint);
		
		TemporalRange temporalRange = new TemporalRange(temporalPoint, null);
		temporalFeature.setTemporalValidity(temporalRange);
		
		return getFeatureAt(temporalFeature, temporalPoint);
	}

	protected abstract TemporalFeature doCreateTemporalFeatureAt(String identifier, TemporalPoint temporalPoint);
	
//	protected abstract Feature getFeatureAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint);

	@Override
	protected void doAddFeatureAt(Feature feature, Feature parentFeature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		
		Group defaultGroup = getFeatureDefaultChildGroupAt(parentFeature, temporalPoint);
		TemporalGroup temporalGroup = getTemporalGroup(defaultGroup);
		
		doAddTemporalFeatureAt(temporalFeature, temporalGroup, temporalPoint);
	}

	@Override
	protected void doAddFeatureAt(Feature feature, Group parentGroup, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		TemporalGroup temporalGroup = getTemporalGroup(parentGroup);
		
		doAddTemporalFeatureAt(temporalFeature, temporalGroup, temporalPoint);
	}

	@Override
	protected void doAddGroupAt(Group group, Feature parentFeature, TemporalPoint temporalPoint) {
		TemporalGroup temporalGroup = getTemporalGroup(group);
		TemporalFeature temporalParentFeature = getTemporalFeature(parentFeature);
		
		doAddTemporalGroupAt(temporalGroup, temporalParentFeature, temporalPoint);
	}
	
	protected abstract void doAddTemporalGroupAt(TemporalGroup temporalGroup, TemporalFeature temporalParentFeature, TemporalPoint temporalPoint);

	@Override
	protected void doRemoveFeatureAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		
		doRemoveTemporalFeatureAt(temporalFeature, temporalPoint);
	}
	
	protected abstract void doRemoveTemporalFeatureAt(TemporalFeature feature, TemporalPoint temporalPoint);
	

	@Override
	protected void doRemoveGroupAt(Group group, TemporalPoint temporalPoint) {
		TemporalGroup temporalGroup = getTemporalGroup(group);
		doRemoveTemporalGroupAt(temporalGroup, temporalPoint);
	}
	
	protected abstract void doRemoveTemporalGroupAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint);

//	@Override
//	protected void doMoveFeatureAt(Feature feature, Group targetGroup, TemporalPoint temporalPoint) {
//		TemporalFeature temporalFeature = getTemporalFeature(feature);
//		
//		TemporalGroup oldParentGroup = getTemporalGroup(getFeatureParentGroupAt(feature, temporalPoint));
//		TemporalGroup newParentGroup = getTemporalGroup(targetGroup);
//		
//		removeTemporalFeatureFromTemporalGroupAt(temporalFeature, oldParentGroup, temporalPoint);
//		addTemporalFeatureAt(temporalFeature, newParentGroup, temporalPoint);
//	}
	
	protected abstract void doAddTemporalFeatureAt(TemporalFeature feature, TemporalGroup group, TemporalPoint temporalPoint);

//	protected abstract void doRemoveTemporalFeatureFromTemporalGroupAt(TemporalFeature feature, TemporalGroup group, TemporalPoint temporalPoint);

	@Override
	protected void doDetachFeatureFromParentAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		
		doDetachTemporalFeatureFromParentAt(temporalFeature, temporalPoint);
	}
	
	protected abstract void doDetachTemporalFeatureFromParentAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint);
	

	@Override
	protected void doDetachGroupFromParentAt(Group group, TemporalPoint temporalPoint) {
		TemporalGroup temporalGroup = getTemporalGroup(group);
		doDetachTemporalGroupFromParentAt(temporalGroup, temporalPoint);
	}
	
	protected abstract void doDetachTemporalGroupFromParentAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint);
	
//	@Override
//	protected void doMoveFeatureAt(Feature feature, Feature targetFeature, TemporalPoint temporalPoint) {
//		
//		
//		//TODO: FIXME
//		throw new UnsupportedOperationException();
//	}
	
	@Override
	protected String doGetFeatureNameAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		Collection<String> validNames = TemporalElementUtil.getValidElementsFromTemporalRangeElementTuples(getAllFeatureNames(temporalFeature), temporalPoint);
		
		switch(validNames.size()) {
		case 0:
			// TODO inconsistency -> no valid name (at least if feature is valid then as well?)
			return null;
		case 1:
			for(String name: validNames) {
				return name;
			}
		default:
			// TODO should be an inconsistency if more than one valid name?
			return null;
		}
	}

	@Override
	protected void doSetFeatureNameAt(Feature feature, String name, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		doSetTemporalFeatureNameAt(temporalFeature, name, temporalPoint);;
	}

	protected abstract void doSetTemporalFeatureNameAt(TemporalFeature temporalFeature, String name, TemporalPoint temporalPoint);

	@Override
	protected FeatureVariationType doGetFeatureVariationTypeAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		
		TemporalFeatureVariationType temporalFeatureVariationType = getTemporalFeatureVariationTypeAt(temporalFeature, temporalPoint);
		if(temporalFeatureVariationType == null) {
			return null;
		}
		else {
			return temporalFeatureVariationType.getVariationType();
		}
	}

	@Override
	protected void doSetFeatureVariationTypeAt(Feature feature, FeatureVariationType variationType, TemporalPoint temporalPoint) {
		TemporalFeature temporalFeature = getTemporalFeature(feature);
		doSetTemporalFeatureVariationTypeAt(temporalFeature, variationType, temporalPoint);
	}

	protected abstract void doSetTemporalFeatureVariationTypeAt(TemporalFeature temporalFeature, FeatureVariationType featureVariationType, TemporalPoint temporalPoint);
	
	@Override
	protected GroupVariationType doGetGroupVariationTypeAt(Group group, TemporalPoint temporalPoint) {
		TemporalGroup temporalGroup = getTemporalGroup(group);
		
		TemporalGroupVariationType temporalGroupVariationType = getTemporalGroupVariationTypeAt(temporalGroup, temporalPoint);
		if(temporalGroupVariationType == null) {
			return null;
		}
		else {
			return temporalGroupVariationType.getVariationType();
		}
	}

	@Override
	protected void doSetGroupVariationTypeAt(Group group, GroupVariationType variationType, TemporalPoint temporalPoint) {
		TemporalGroup temporalGroup = getTemporalGroup(group);
		doSetTemporalGroupVariationTypeAt(temporalGroup, variationType, temporalPoint);
	}

	protected abstract void doSetTemporalGroupVariationTypeAt(TemporalGroup temporalGroup, GroupVariationType groupVariationType, TemporalPoint temporalPoint);
	
	@Override
	protected Group doCreateGroupAt(TemporalPoint temporalPoint) {
		
		TemporalGroup temporalGroup = doCreateTemporalGroupAt(temporalPoint);
		
		return getGroupAt(temporalGroup, temporalPoint);
	}
	
	protected abstract TemporalGroup doCreateTemporalGroupAt(TemporalPoint temporalPoint);
	

	@Override
	protected Group doCreateDefaultChildGroupAt(Feature feature, TemporalPoint temporalPoint) {
		TemporalFeature temporalParentFeature = getTemporalFeature(feature);
		TemporalGroup temporalDefaultGroup = doCreateTemporalDefaultChildGroupAt(temporalParentFeature, temporalPoint);
		
		Group defaultGroup = getGroupAt(temporalDefaultGroup, temporalPoint);
		
		return defaultGroup;
	}
	
	protected abstract TemporalGroup doCreateTemporalDefaultChildGroupAt(TemporalFeature parentFeature, TemporalPoint temporalPoint);
	
//	protected abstract TemporalGroup createTemporalGroupAt(TemporalFeature parentFeature, TemporalPoint temporalPoint);
	
	protected TemporalGroupVariationType getTemporalGroupVariationTypeAt(TemporalGroup temporalGroup, TemporalPoint temporalPoint) {
		Collection<TemporalGroupVariationType> variationTypes = getAllGroupVariationTypes(temporalGroup);
		Collection<TemporalGroupVariationType> validVariationTypes = TemporalElementUtil.getValidTemporalElements(variationTypes, temporalPoint);
		

		switch(validVariationTypes.size()) {
		case 0:
			// TODO inconsistency -> no valid name (at least if feature is valid then as well?)
			return null;
		case 1:
			for(TemporalGroupVariationType temporalVariationType: validVariationTypes) {
				return temporalVariationType;
			}
		default:
			// TODO should be an inconsistency if more than one?
			return null;
		}
	}
	
	protected TemporalFeatureVariationType getTemporalFeatureVariationTypeAt(TemporalFeature temporalFeature, TemporalPoint temporalPoint) {
		Collection<TemporalFeatureVariationType> variationTypes = getAllFeatureVariationTypes(temporalFeature);
		
		Collection<TemporalFeatureVariationType> validVariationTypes = TemporalElementUtil.getValidTemporalElements(variationTypes, temporalPoint);
		
		switch(validVariationTypes.size()) {
		case 0:
			// TODO inconsistency -> no valid name (at least if feature is valid then as well?)
			return null;
		case 1:
			for(TemporalFeatureVariationType temporalVariationType: validVariationTypes) {
				return temporalVariationType;
			}
		default:
			// TODO should be an inconsistency if more than one?
			return null;
		}
	}
}
