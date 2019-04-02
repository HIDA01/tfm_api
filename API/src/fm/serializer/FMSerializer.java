package fm.serializer;

import java.io.File;

import fm.FeatureModel;

public interface FMSerializer {
	public void serialize(FeatureModel featureModel, File file);
	public String serialize(FeatureModel featureModel);
}
