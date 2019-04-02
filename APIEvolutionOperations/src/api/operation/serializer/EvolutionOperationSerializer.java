package api.operation.serializer;

import java.io.File;
import java.util.Collection;

import api.operation.EvolutionOperation;

public interface EvolutionOperationSerializer {
	public void serialize(Collection<EvolutionOperation> evolutionOperations, File file);
	public String serialize(Collection<EvolutionOperation> evolutionOperations);
}
