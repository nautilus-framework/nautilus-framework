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
import thiagodnf.nautilus.plugin.spl.encoding.objective.AliveMutantsObjective;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;

public class TestAliveMutantsObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new AliveMutantsObjective();
	
	public static TXTInstanceData getInstanceData() {

		InstanceDataExtension instanceDataExtension = new SPLInstanceDataExtension();

		return (TXTInstanceData) instanceDataExtension.getInstanceData(path);
	}
	
	@Test
	public void shouldReturnOneIfThereIsNoSelectedProducts() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "00000");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(1.0, value);
	}
	
	@Test
	public void shouldReturnZeroIfAllProductsWereSelected() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "11111");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(0.0, value);
	}
	
	@Test
	public void shouldReturnSixtyPercentIfJustProductThreeWasSelected() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "00010");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(0.6, value);
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

		assertNotEquals(0.0, objective.getName());
		assertNotEquals(0.0, objective.getGroupName());
	}
}
