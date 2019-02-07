package test.thiagodnf.nautilus.plugin.spl.encoding.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.spl.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;

public class TestCostObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new CostObjective();
	
	public static InstanceData getInstanceData() {

		InstanceDataExtension instanceDataExtension = new SPLInstanceDataExtension();

		return instanceDataExtension.getInstanceData(path);
	}
	
	private double evaluate(String binaryString) {

		NBinarySolution solution = Converter.toBinarySolution(1, binaryString);

		return objective.evaluate(getInstanceData(), solution);
	}
	
	@Test
	public void shouldReturnZeroIfThereIsNoSelectedProducts() {
		
		assertEquals(0.0, evaluate("00000"), 0.00001);
	}

	@Test
	public void shouldReturnOneIfAllOfThemWereSelectedProducts() {
		
		assertEquals(1.0, evaluate("11111"), 0.00001);
	}
	
	@Test
	public void shouldReturnTheCorrectValues() {

		assertEquals(13.0 / 33.0, evaluate("01001"), 0.00001);
		assertEquals(3.0 / 33.0, evaluate("10000"), 0.00001);
		assertEquals(11.0 / 33.0, evaluate("10101"), 0.00001);
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
