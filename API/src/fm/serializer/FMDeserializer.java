package fm.serializer;

import java.io.File;

import fm.FeatureModel;

public interface FMDeserializer {
	public FeatureModel deserialize(File file);
	public FeatureModel deserialize(String serializedFeatureModel);
}
