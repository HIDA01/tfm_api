package api.test;

import org.junit.Test;

import api.fm.FMView;
import api.fm.listener.FMViewAbstractListener;
import api.test.instance.FMViewFactory;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;

public class FMViewListenerTest extends FMViewAbstractTest {
	public FMViewListenerTest(FMViewFactory viewFactory) {
		super(viewFactory);
	}
	
	@Test
	public void testListenerSupport() {
		FMView fmView = getFMView();
		fmView.addListener(new FMViewAbstractListener() {
			@Override
			public void onFeatureVariationTypeChanged(Feature feature, FeatureVariationType oldVariationType, FeatureVariationType newVariationType) {
				//TODO: Log all messages
				System.out.println("variation type changed");
			}
		});
		
		FeatureModel featureModel = fmView.getFeatureModel();
		
		Feature turtleBotFeature = featureModel.createFeature("TurtleBot");
		turtleBotFeature.setVariationType(FeatureVariationType.MANDATORY);
		turtleBotFeature.createDefaultChildGroup(); //TODO: This seems problematic!
		featureModel.setRootFeature(turtleBotFeature);
		
		
	}
	
//	protected void expectMessages() {
//		
//	}
}
