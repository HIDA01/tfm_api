package api.test.instance;

import java.util.LinkedList;
import java.util.List;

import api.fm.FMView;
import api.instance.deltaecore.DeltaEcoreFMView;

public class FMViewFactoryInstantiations {
	//TODO: Add factory for new FM instantiations here to integrate it into all tests.
	public static List<FMViewFactory> createFMViewFactories() {
		List<FMViewFactory> factories = new LinkedList<FMViewFactory>();
		
		factories.add(new FMViewFactory() {
			@Override
			public FMView createFMView(String rootFeatureName) {
				return new DeltaEcoreFMView(rootFeatureName);
			}
		});
		
		return factories;
	}
}
