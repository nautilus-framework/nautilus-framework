package test.thiagodnf.nautilus.plugin.spl.encoding.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.spl.encoding.instance.TXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnselectedFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;

public class TestUnselectedFeaturesObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new UnselectedFeaturesObjective();
	
	private static TXTInstanceData getInstanceData() {

		InstanceDataExtension instanceDataExtension = new SPLInstanceDataExtension();

		return (TXTInstanceData) instanceDataExtension.getInstanceData(path);
	}
	
	private double evaluate(String binaryString) {

		NBinarySolution solution = Converter.toBinarySolution(1, binaryString);

		return objective.evaluate(getInstanceData(), solution);
	}
	
	@Test
	public void shouldReturnOneIfThereIsNoSelectedProducts() {
		
		assertEquals(1.0, evaluate("00000"));
	}
	
	@Test
	public void shouldReturnZeroIfAllProductsWereSelected() {
		
		assertEquals(0.0, evaluate("11111"));
	}
	
	@Test
	public void shouldReturnTheCorrectValues() {

		assertEquals(0.0, evaluate("00011"), 0.00001);
		assertEquals(0.0, evaluate("01010"), 0.00001);
		assertEquals(0.0, evaluate("00010"), 0.00001);
		assertEquals(0.4, evaluate("10001"), 0.00001);
		assertEquals(0.4, evaluate("10000"), 0.00001);
		assertEquals(0.2, evaluate("00101"), 0.00001);
	}
	
	@Test
	public void shouldReturnTheCorrectMinAndMaxValues() {
		
		assertEquals(0.0, objective.getMinimumValue());
		assertEquals(1.0, objective.getMaximumValue());
	}
	
	@Test
	public void shouldReturnNonEmptyNameAndGroupName() {

		assertNotNull(objective.getName());
		assertNotNull(objective.getGroupName());

		assertNotEquals(0.0, objective.getName().length());
		assertNotEquals(0.0, objective.getGroupName().length());
	}
}
