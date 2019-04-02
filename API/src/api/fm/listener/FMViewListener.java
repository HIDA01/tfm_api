package api.fm.listener;

import api.fm.listener.individual.FMViewFeatureListener;
import api.fm.listener.individual.FMViewFeatureModelListener;
import api.fm.listener.individual.FMViewGroupListener;

//TODO: This has to be mirrored in feature/group/fm abstraction
public interface FMViewListener extends FMViewFeatureModelListener, FMViewFeatureListener, FMViewGroupListener {
	//This is the sum of all listeners for the feature model, features and groups. Harmonizes listener handling for API and its abstraction (as feature model, features and groups).
}