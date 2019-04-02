package api.instance.featureide;

import java.util.LinkedList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeature;
import fm.GroupVariationType;

public class DummyGroup {

	private GroupVariationType type;
	private List<IFeature> childFeatures;
	
	public DummyGroup() {
		this.type = GroupVariationType.AND;
		childFeatures = new LinkedList<IFeature>();
	}

	public GroupVariationType getType() {
		return type;
	}

	public void setType(GroupVariationType type) {
		this.type = type;
	}

	public List<IFeature> getChildFeatures() {
		return childFeatures;
	}	
}
