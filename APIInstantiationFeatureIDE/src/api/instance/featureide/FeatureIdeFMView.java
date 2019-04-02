package api.instance.featureide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import api.fm.AbstractFMViewWithFeatureCaching;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.functional.Functional;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * We use IFeatureStructure as abstraction for groups.
 * @author Michael Nieke
 *
 */
public class FeatureIdeFMView extends AbstractFMViewWithFeatureCaching<IFeatureModel, IFeature> {

	protected static DefaultFeatureModelFactory factory;
	
	protected BiMap<Group, IFeatureStructure> groupStructureMap;
	protected Map<Group, DummyGroup> dummyGroupMap;
	
	public FeatureIdeFMView(String rootFeatureName) {
		super(rootFeatureName);
		
		groupStructureMap = HashBiMap.create();
		dummyGroupMap = new HashMap<>();
	}
	
	protected static DefaultFeatureModelFactory getFactory() {
		if (factory == null) {
			factory = DefaultFeatureModelFactory.getInstance();
		}
		
		return factory;
	}
	
	@Override
	protected Collection<IFeature> getNotationSpecificAllFeatures() {
		return Functional.toList(this.notationSpecificFeatureModel.getFeatures());
	}

	@Override
	protected IFeature getNotationSpecificFeature(String identifier) {
		return this.notationSpecificFeatureModel.getFeature(identifier);
	}

	@Override
	protected IFeature getNotationSpecificFeatureParentFeature(IFeature feature) {
		IFeatureStructure parentStructure = feature.getStructure().getParent();
		if (parentStructure == null) {
			return null;
		}
		
		return parentStructure.getFeature();
	}

	@Override
	protected IFeature getNotationSpecificRootFeature() {
		return notationSpecificFeatureModel.getStructure().getRoot().getFeature();
	}

	@Override
	protected String getNotationSpecificFeatureName(IFeature notationSpecificFeature) {
		return notationSpecificFeature.getName();
	}

	@Override
	protected FeatureVariationType getNotationSpecificFeatureVariationType(IFeature notationSpecificFeature) {
		IFeatureStructure structure = notationSpecificFeature.getStructure();
		
		IFeatureStructure parentStructure = structure.getParent();
		
		if (parentStructure == null) {
			return FeatureVariationType.MANDATORY;
		}
		
		if (parentStructure.isAlternative() || parentStructure.isOr()) {
			return FeatureVariationType.OPTIONAL;			
		}
		
		if (structure.isMandatory()) {
			return FeatureVariationType.MANDATORY;
		}
		else {
			return FeatureVariationType.OPTIONAL;
		}
	}

	@Override
	protected void setNotationSpecificRootFeature(IFeature notationSpecificFeature) {
		notationSpecificFeatureModel.getStructure().setRoot(notationSpecificFeature.getStructure());
	}

	@Override
	protected IFeature createNotationSpecificFeature(String identifier) {
		return getFactory().createFeature(notationSpecificFeatureModel, identifier);
	}

	@Override
	protected void removeNotationSpecificFeature(IFeature notationSpecificFeature) {
		notationSpecificFeatureModel.deleteFeature(notationSpecificFeature);
	}

	@Override
	protected void detachNotationSpecificFeatureFromParent(IFeature notationSpecificFeature) {
		IFeatureStructure featureStructure = notationSpecificFeature.getStructure();
		featureStructure.getParent().removeChild(featureStructure);
	}

	@Override
	protected void setNotationSpecificFeatureName(IFeature notationSpecificFeature, String name) {
		notationSpecificFeature.setName(name);
	}

	@Override
	protected void setNotationSpecificFeatureVariationType(IFeature notationSpecificFeature,
			FeatureVariationType variationType) {
		IFeatureStructure featureStructure = notationSpecificFeature.getStructure();
		
		switch (variationType) {
		case MANDATORY:
			featureStructure.setMandatory(true);
			break;
		case NON_STANDARD:
			throw new UnsupportedOperationException("NON_Standard feature type is not supported");
		case OPTIONAL:
			featureStructure.setMandatory(false);
			break;
		default:
			featureStructure.setMandatory(false);
			break;
		
		}
	}

	@Override
	protected Feature doWrapFeature(IFeature notationSpecificFeature) {
		return new Feature(this, notationSpecificFeature);
	}

	@Override
	protected IFeatureModel createNotationSpecificFeatureModel(String rootFeatureName) {
		
		IFeatureModel featureModel = getFactory().createFeatureModel();
		IFeature rootFeature = getFactory().createFeature(featureModel, rootFeatureName);
		
		featureModel.getStructure().setRoot(rootFeature.getStructure());
		
		return featureModel;
	}

	@Override
	protected FeatureModel doWrapFeatureModel(IFeatureModel notationSpecificFeatureModel) {
		return new FeatureModel(this, notationSpecificFeatureModel);
	}

	@Override
	protected Collection<Group> doGetAllGroups() {
		for (IFeature feature : notationSpecificFeatureModel.getFeatures()) {
			IFeatureStructure structure = feature.getStructure();
			if (structure.hasChildren()) {
				if (!groupStructureMap.containsValue(structure)) {
					groupStructureMap.put(new Group(this, structure), structure);
				}
			}
		}
		
		Set<Group> groups = new HashSet<>();
		groups.addAll(groupStructureMap.keySet());
		groups.addAll(dummyGroupMap.keySet());
		
		return groupStructureMap.keySet();
	}

	@Override
	protected GroupVariationType doGetGroupVariationType(Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		if (nativeGroup instanceof IFeatureStructure) {
			IFeatureStructure structure = (IFeatureStructure) nativeGroup;
			
			if (structure.isAnd()) {
				return GroupVariationType.AND;
			}
			else if (structure.isAlternative()) {
				return GroupVariationType.XOR;
			}
			else if (structure.isOr()) {
				return GroupVariationType.OR;
			}
			
			return GroupVariationType.NON_STANDARD;
		}
		else if (nativeGroup instanceof DummyGroup) {
			return ((DummyGroup)nativeGroup).getType();
		}
		
		return null;
	}

	@Override
	protected Group doCreateGroup() {
		DummyGroup dummyGroup = new DummyGroup();
		
		Group group = new Group(this, dummyGroup);
		dummyGroupMap.put(group, dummyGroup);
		
		return group;
	}

	@Override
	protected void doRemoveGroup(Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		
		if (nativeGroup instanceof IFeatureStructure) {
			IFeatureStructure structure = (IFeatureStructure) nativeGroup;
			structure.getChildren().clear();
			groupStructureMap.remove(group);
		}
	}

	@Override
	protected void detachGroupFromParent(Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		if (nativeGroup instanceof DummyGroup) {
			return;
		}
		
		IFeatureStructure featureStructure = (IFeatureStructure) nativeGroup;
		
		DummyGroup dummyGroup = new DummyGroup();
		dummyGroup.setType(group.getVariationType());
		
		List<IFeature> childFeatures = dummyGroup.getChildFeatures();
		
		featureStructure.getChildren().forEach(childStructure->childFeatures.add(childStructure.getFeature()));
		
		dummyGroupMap.put(group, dummyGroup);
		
		groupStructureMap.remove(group);
		
		featureStructure.getChildren().clear();
		
		group.setNativeElement(dummyGroup);
	}

	@Override
	protected void doSetGroupVariationType(Group group, GroupVariationType variationType) {
		Object nativeGroup = group.getNotationSpecificElement();
		if (nativeGroup instanceof DummyGroup) {
			((DummyGroup) nativeGroup).setType(variationType);
		}
		else {
			IFeatureStructure featureStructure = (IFeatureStructure) nativeGroup;
			switch (variationType) {
			case AND:
				featureStructure.setAnd();
				break;
			case NON_STANDARD:
				throw new UnsupportedOperationException("FeatureIDE does not support NON_Standard group type.");
			case OR:
				featureStructure.setOr();
				break;
			case XOR:
				featureStructure.setAlternative();
				break;
			default:
				featureStructure.setAnd();
				break;
			}
		}
	}

	@Override
	protected Group getNativeFeatureParentGroup(IFeature nativeFeature) {
		IFeatureStructure structure = nativeFeature.getStructure();
		IFeatureStructure parentStructure = structure.getParent();
		
		if (parentStructure == null) {
			return null;
		}
		
		Group parentGroup = groupStructureMap.inverse().get(parentStructure);
		
		if (parentGroup == null) {
			parentGroup = new Group(this, parentStructure);
			groupStructureMap.put(parentGroup, parentStructure);
		}
		
		return parentGroup;
	}

	@Override
	protected Group getNativeFeatureDefaultChildGroup(IFeature nativeFeature) {
		IFeatureStructure structure = nativeFeature.getStructure();
		Group group = groupStructureMap.inverse().get(structure);
		
		if (group == null && structure.hasChildren()) {
			group = new Group(this, structure);
			groupStructureMap.put(group, structure);
		}
		
		return group;
	}

	@Override
	protected List<Group> getNativeFeatureChildGroups(IFeature nativeFeature) {
		IFeatureStructure structure = nativeFeature.getStructure();
		
		Group group = groupStructureMap.inverse().get(structure);
		
		if (group == null && structure.hasChildren()) {
			group = new Group(this, structure);
			groupStructureMap.put(group, structure);
		}
		
		List<Group> groups = new ArrayList<Group>(1);
		if (group != null) {
			groups.add(group);
		}
		
		return groups;
	}

	@Override
	protected List<IFeature> getGroupNativeChildFeatures(Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		
		if (nativeGroup instanceof DummyGroup) {
			DummyGroup dummyGroup = (DummyGroup) nativeGroup;
			return dummyGroup.getChildFeatures();
		}
		else {
			List<IFeature> features = new LinkedList<>();
			IFeatureStructure structure = (IFeatureStructure) nativeGroup;
			structure.getChildren().forEach(child->features.add(child.getFeature()));
			return features;
		}
		
	}

	@Override
	protected IFeature getGroupNativeParentFeature(Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		
		if (nativeGroup instanceof DummyGroup) {
			return null;
		}
		
		IFeatureStructure structure = (IFeatureStructure) nativeGroup;
		return structure.getFeature();
	}

	@Override
	protected void addNativeFeature(IFeature nativeFeature, Group group) {
		Object nativeGroup = group.getNotationSpecificElement();
		
		if (nativeGroup instanceof DummyGroup) {
			DummyGroup dummyGroup = (DummyGroup) nativeGroup;
			
			dummyGroup.getChildFeatures().add(nativeFeature);
		}
		else {
			IFeatureStructure parentStructure = (IFeatureStructure) nativeGroup;
			IFeatureStructure childStructure = nativeFeature.getStructure();
			
			parentStructure.addChild(childStructure);
		}
	}

	@Override
	protected Group createNativeFeatureDefaultChildGroup(IFeature nativeFeature) {
//		IFeatureStructure structure = nativeFeature.getStructure();
//		structure.setAnd();
		
		Group group = new Group(this, new DummyGroup());
		
		return group;
	}

	@Override
	protected void addGroupToNativeFeature(Group group, IFeature nativeParentFeature) {
		Object nativeGroup = group.getNotationSpecificElement();
		
		if (nativeGroup instanceof DummyGroup) {
			DummyGroup dummyGroup = (DummyGroup) nativeGroup;
			
			IFeatureStructure parentStructure = nativeParentFeature.getStructure();
			dummyGroup.getChildFeatures().forEach(feature->parentStructure.addChild(feature.getStructure()));
			
			group.setNativeElement(nativeParentFeature.getStructure());
			
			dummyGroupMap.remove(group);
			groupStructureMap.put(group, parentStructure);
		}
		else {
			throw new UnsupportedOperationException("Group has already a parent: "+((IFeatureStructure) nativeGroup).getFeature().getName());
		}
	}
}
