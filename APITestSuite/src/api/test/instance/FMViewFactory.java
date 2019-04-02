package api.test.instance;

import api.fm.FMView;

public abstract class FMViewFactory {
	public abstract FMView createFMView(String rootFeatureName);
}
