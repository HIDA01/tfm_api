package api.operation.serializer;

import java.io.File;
import java.util.List;

import api.operation.EvolutionOperation;
import fm.FeatureModel;
import tfm.TemporalFeatureModel;

public interface EvolutionOperationDeserializer {
	public List<EvolutionOperation> deserialize(File file, TemporalFeatureModel temporalFeatureModel);
	public List<EvolutionOperation> deserialize(File file, FeatureModel featureModel);
	public List<EvolutionOperation> deserialize(String serializedEvolutionOperations, FeatureModel featureModel);
	public List<EvolutionOperation> deserialize(String serializedEvolutionOperations, TemporalFeatureModel temporalFeatureModel);
}
