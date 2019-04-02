package fm.util;

import java.util.List;

import fm.FMViewElement;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * Debug class to format a feature model as string.
 * 
 * @author Christoph Seidl
 */
public class FMPrinter {
	public String print(FMViewElement element) {
		if (element instanceof FeatureModel) {
			FeatureModel featureModel = (FeatureModel) element;
			return printFeatureModel(featureModel);
		}
		
		if (element instanceof Feature) {
			Feature feature = (Feature) element;
			return printFeature(feature);
		}
		
		if (element instanceof Group) {
			Group group = (Group) element;
			return printGroup(group);
		}
		
		throw new UnsupportedOperationException();
	}
	
	public String printFeatureModel(FeatureModel featureModel) {
		Feature rootFeature = featureModel.getRootFeature();
		return doPrintFeature(rootFeature, 0);
	}
	
	public String printFeature(Feature feature) {
		return doPrintFeature(feature, 0);
	}
	
	protected String doPrintFeature(Feature feature, int level) {
		String output = indent(level);
		
		if (level > 0) {
			output += "-";
		}
		
		output += "[" + printFeatureVariationType(feature.getVariationType()) + "] ";
		output += feature.getName();
		output += System.lineSeparator();
		
		List<Group> groups = feature.getChildGroups();
		
		for (Group group : groups) {
			output += doPrintGroup(group, level + 1); 
		}
		
		return output;
	}
	
	protected String printFeatureVariationType(FeatureVariationType variationType) {
		switch (variationType) {
			case OPTIONAL:
				return "?";
			case MANDATORY:
				return "!";
			case NON_STANDARD:
				return "¿¡";
		}
		
		throw new UnsupportedOperationException();
	}
	
	
	public String printGroup(Group group) {
		return doPrintGroup(group, 0);
	}
	
	protected String doPrintGroup(Group group, int level) {
		String output = indent(level);
		
		if (level > 0) {
			output += "-";
		}
		
		output += "[" + printGroupVariationType(group.getVariationType()) + "] ";
		output += System.lineSeparator();
		
		List<Feature> features = group.getChildFeatures();
		
		for (Feature feature : features) {
			output += doPrintFeature(feature, level + 1); 
		}
		
		return output;
	}
	
	protected String printGroupVariationType(GroupVariationType variationType) {
		switch (variationType) {
			case AND:
				return "AND";
			case OR:
				return "OR";
			case XOR:
				return "XOR";
			case NON_STANDARD:
				return "¿¡";
		}
		
		throw new UnsupportedOperationException();
	}
	
	
	protected String indent(int level) {
		return indent(level, "|");
	}
	
	protected String indent(int level, String indentationMarker) {
		String output = "";
		
		for (int i = 0; i < level; i++) {
			output += indentationMarker;
		}
		
		return output;
	}
}
