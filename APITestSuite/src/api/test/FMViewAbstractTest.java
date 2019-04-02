package api.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import api.fm.FMView;
import api.test.instance.FMViewFactory;
import api.test.instance.FMViewFactoryInstantiations;
import fm.Feature;
import fm.FeatureModel;
import fm.FeatureVariationType;
import fm.Group;
import fm.GroupVariationType;

/**
 * For now, this project references all known instantiations of the API to test them in bunch. 
 * The general idea for the future is that each instantiation delivers its own concrete parameterization 
 * of the test suite but currently JUnit causes obstacles that should not slow down development.
 * 
 * @author Christoph Seidl
 */
@RunWith(Parameterized.class)
public abstract class FMViewAbstractTest {
	private FMView fmView;

	private FMViewFactory viewFactory;
	
	public FMViewAbstractTest(FMViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
	
	@Parameters
	public static List<FMViewFactory> createFMViewFactories() {
		return FMViewFactoryInstantiations.createFMViewFactories();
	}
	
	@Before
	public void setUp() {
		fmView = viewFactory.createFMView("Root");
	}
	
	@After
	public void tearDown() {
		fmView = null;
	}
	
	protected FMView getFMView() {
		return fmView;
	}
	
	
	protected static void assertFeatureSet(FeatureModel featureModel, Feature ... expectedFeaturesArray) {
		Collection<Feature> features = featureModel.getAllFeatures();
		assertFeatureSet(features, expectedFeaturesArray);
	}
	
	protected static void assertFeatureSet(Collection<Feature> features, Feature ... expectedFeaturesArray) {
		Collection<Feature> expectedFeatures = Arrays.asList(expectedFeaturesArray);
		assertFeatureSet(features, expectedFeatures);
	}
	
	protected static void assertFeatureSet(Collection<Feature> features, Collection<Feature> expectedFeatures) {
		assertEquals(expectedFeatures, features);
	}
	
	protected static void assertFeatureIntegrity(Feature feature, String expectedName, FeatureVariationType expectedVariationType, Group expectedParentGroup, Feature expectedParentFeature) {
		String name = feature.getName();
		assertEquals(expectedName, name);
		
		FeatureVariationType variationType = feature.getVariationType();
		assertEquals(expectedVariationType, variationType);
		
		Group parentGorup = feature.getParentGroup();
		assertEquals(expectedParentGroup, parentGorup);

		Feature parentFeature = feature.getParentFeature();
		assertEquals(expectedParentFeature, parentFeature);
	}
	
	protected static void assertGroupIntegrity(Group group, GroupVariationType expectedVariationType, Feature expectedParentFeature) {
		GroupVariationType variationType = group.getVariationType();
		assertEquals(expectedVariationType, variationType);
		
		Feature parentFeature = group.getParentFeature();
		assertEquals(expectedParentFeature, parentFeature);
	}
}
