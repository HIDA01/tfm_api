package api.fm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.Feature;
import fm.Group;
import fm.GroupVariationType;

/**
 * 
 * @author Michael Nieke, Christoph Seidl
 *
 * @param <FM> Type of notation specific Feature Model
 * @param <F> Type of notation specific Feature
 * @param <G> Type of notation specific Group
 */
public abstract class AbstractFMViewWithFeatureAndGroupCaching<FM, F, G> extends AbstractFMViewWithFeatureCaching<FM, F> {

	protected Map<G, Group> wrappedGroupCache;
	
	public AbstractFMViewWithFeatureAndGroupCaching(String rootFeatureName) {
		super(rootFeatureName);
		initialize();
	}
	
	public AbstractFMViewWithFeatureAndGroupCaching(FM notationSpecificFeatureModel) {
		super(notationSpecificFeatureModel);
		initialize();
	}
	
	private void initialize() {
		wrappedGroupCache = new HashMap<G, Group>();
	}
	
	
	
	// Read
	
	@Override
	protected Collection<Group> doGetAllGroups() {
		Collection<Group> groups = createGroupList();
		
		for (G notationSpecificGroup : getNotationSpecificAllGroups()) {
			groups.add(wrapGroup(notationSpecificGroup));
		}
		
		return groups;
	}
	
	protected abstract Collection<G> getNotationSpecificAllGroups();
	
	@Override
	protected Group getNativeFeatureParentGroup(F nativeFeature) {
		G notationSpecificGroup = getNotationSpecificFeatureParentGroup(nativeFeature);
		
		return wrapGroup(notationSpecificGroup);
	}
	
	protected abstract G getNotationSpecificFeatureParentGroup(F notationSpecificFeature);
	
	
	@Override
	protected Feature doGetFeatureParentFeature(Feature feature) {
		Group parentGroup = feature.getParentGroup();
		
		return getGroupParentFeature(parentGroup);
	}
	

	@Override
	protected Group getNativeFeatureDefaultChildGroup(F nativeFeature) {
		G notationSpecificDefaultGroup = getNotationSpecificFeatureDefaultChildGroup(nativeFeature);
		return wrapGroup(notationSpecificDefaultGroup);
	}
	
	/**
	 * This method is part of the read-API. Therefore, the default group MUST NOT be created lazily (because there would be a write in the read-API).
	 * If no default group exists, this should return null.
	 * 
	 * @param feature
	 * @return
	 */
	protected abstract G getNotationSpecificFeatureDefaultChildGroup(F notationSpecificFeature);
	
	
	@Override
	protected List<Group> getNativeFeatureChildGroups(F nativeFeature) {
		List<G> notationSpecificGroups = getNotationSpecificFeatureChildGroups(nativeFeature);
		
		List<Group> groups = createGroupList();
		for (G notationSpecificGroup : notationSpecificGroups) {
			groups.add(wrapGroup(notationSpecificGroup));
		}
		
		return groups;
	}
	
	protected abstract List<G> getNotationSpecificFeatureChildGroups(F notationSpecificFeature);
	
	
	@Override
	protected List<F> getGroupNativeChildFeatures(Group group) {
		G notationSpecificGroup = unwrapGroup(group);
		return getNotationSpecificGroupChildFeatures(notationSpecificGroup);
	}
	
	protected abstract List<F> getNotationSpecificGroupChildFeatures(G notationSpecificGroup);
	
	
	@Override
	protected F getGroupNativeParentFeature(Group group) {
		G notationSpecificGroup = unwrapGroup(group);
		return getNotationSpecificGroupParentFeature(notationSpecificGroup);
	}
	
	protected abstract F getNotationSpecificGroupParentFeature(G notationSpecificGroup);
	
	
	@Override
	protected GroupVariationType doGetGroupVariationType(Group group) {
		G notationSpecificGroup = unwrapGroup(group);
		return getNotationSpecificGroupVariationType(notationSpecificGroup);
	}
	
	protected abstract GroupVariationType getNotationSpecificGroupVariationType(G notationSpecificGroup);
	
	
	
	// Write
	
	@Override
	protected void addNativeFeature(F nativeFeature, Group parentGroup) {
		G notationSpecificGroup = unwrapGroup(parentGroup);
		
		addNotationSpecificFeatureToGroup(nativeFeature, notationSpecificGroup);
	}
	
	
	protected abstract void addNotationSpecificFeatureToGroup(F notationSpecificFeature, G notationSpecificParentGroup);
	
	
	
	@Override
	protected Group doCreateGroup() {
		G notationSpecificGroup = createNotationSpecificGroup();
		Group group = wrapGroup(notationSpecificGroup);
		
		return group;
	}

	protected abstract G createNotationSpecificGroup();
	
	
	@Override
	protected Group createNativeFeatureDefaultChildGroup(F nativeFeature) {
		G notationSpecificDefaultChildGroup = createNotationSpecificDefaultChildGroup(nativeFeature);
		
		return wrapGroup(notationSpecificDefaultChildGroup);
	}
	
	protected abstract G createNotationSpecificDefaultChildGroup(F notationSpecificFeature);
	
	
	@Override
	protected void addGroupToNativeFeature(Group group, F nativeParentFeature) {
		G notationSpecificGroup = unwrapGroup(group);
		
		addNotationSpecificGroup(notationSpecificGroup, nativeParentFeature);
	}
	
	protected abstract void addNotationSpecificGroup(G notationSpecificGroup, F notationSpecificParentFeature);
	
	
	@Override
	protected void doRemoveGroup(Group group) {
		G notationSpecificGroup = unwrapGroup(group);
		removeNotationSpecificGroup(notationSpecificGroup);
	}
	
	protected abstract void removeNotationSpecificGroup(G notationSpecificGroup);
	
	
	@Override
	protected void detachGroupFromParent(Group group) {
		G notationSpecificGroup = unwrapGroup(group);
		detachNotationSpecificGroupFromParent(notationSpecificGroup);
	}
	
	protected abstract void detachNotationSpecificGroupFromParent(G notationSpecificGroup);

	@Override
	protected void doSetGroupVariationType(Group group, GroupVariationType VariationType) {
		G notationSpecificGroup = unwrapGroup(group);
		setNotationSpecificGroupVariationType(notationSpecificGroup, VariationType);
	}
	
	protected abstract void setNotationSpecificGroupVariationType(G notationSpecificGroup, GroupVariationType variationType);
	

	
	
	// Auxiliary
	
	protected Group wrapGroup(G notationSpecificGroup) {
		if (notationSpecificGroup == null) {
			return null;
		}
		
		if(wrappedGroupCache.containsKey(notationSpecificGroup)) {
			return wrappedGroupCache.get(notationSpecificGroup);
		}
		else {
			Group wrappedGroup = doWrapGroup(notationSpecificGroup);
			wrappedGroupCache.put(notationSpecificGroup, wrappedGroup);
			return wrappedGroup;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected G unwrapGroup(Group group) {
		if (group == null) {
			return null;
		}
		
		Object notationSpecificElement = group.getNotationSpecificElement();
		if(wrappedGroupCache.containsKey(notationSpecificElement)) {
			return (G) notationSpecificElement;			
		}
		else {
			throw new UnsupportedOperationException("Unknown Group");
		}
	}
	
	// Abstract Template Methods
	protected abstract Group doWrapGroup(G notationSpecificGroup);
}
