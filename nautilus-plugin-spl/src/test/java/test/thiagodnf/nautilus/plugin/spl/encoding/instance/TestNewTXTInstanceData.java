package test.thiagodnf.nautilus.plugin.spl.encoding.instance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.extension.problem.SPLProblemExtension;
class TestNewTXTInstanceData {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	public static NewTXTInstanceData getInstanceData() {

		SPLProblemExtension instanceDataExtension = new SPLProblemExtension();

		return (NewTXTInstanceData) instanceDataExtension.getInstance(path);
	}
	
	@Test
	public void shouldReturnTheCorrectValues() {

		NewTXTInstanceData instance = getInstanceData();
		
		assertEquals(5, instance.getNumberOfProducts());
		assertEquals(5, instance.getNumberOfFeatures());
		assertEquals(5, instance.getNumberOfMutants());
		assertEquals(5, instance.getNumberOfPairs());
		
		assertNotNull(instance.getFeatures());
		assertNotEquals(0, instance.getFeatures().size());
		
		assertEquals("TEST", instance.getFeatures().get(0));
		assertEquals("User", instance.getFeatures().get(1));
		assertEquals("DAPI", instance.getFeatures().get(2));
		assertEquals("Interface", instance.getFeatures().get(3));
		assertEquals("GUI", instance.getFeatures().get(4));
		
		assertEquals(0, (int) instance.getProduct(0).get(0));
		assertEquals(1, (int) instance.getProduct(0).get(1));
		assertEquals(4, (int) instance.getProduct(0).get(2));
		
		assertEquals(0, (int) instance.getProduct(1).get(0));
		assertEquals(1, (int) instance.getProduct(1).get(1));
		assertEquals(2, (int) instance.getProduct(1).get(2));
		assertEquals(4, (int) instance.getProduct(1).get(3));
		
		assertEquals(0, (int) instance.getProduct(2).get(0));
		assertEquals(1, (int) instance.getProduct(2).get(1));
		assertEquals(3, (int) instance.getProduct(2).get(2));
		assertEquals(4, (int) instance.getProduct(2).get(3));
		
		assertEquals(0, (int) instance.getProduct(3).get(0));
		assertEquals(1, (int) instance.getProduct(3).get(1));
		assertEquals(2, (int) instance.getProduct(3).get(2));
		assertEquals(3, (int) instance.getProduct(3).get(3));
		assertEquals(4, (int) instance.getProduct(3).get(4));
		
		assertEquals(0, (int) instance.getProduct(4).get(0));
		assertEquals(1, (int) instance.getProduct(4).get(1));
		assertEquals(4, (int) instance.getProduct(4).get(2));
	}
}
