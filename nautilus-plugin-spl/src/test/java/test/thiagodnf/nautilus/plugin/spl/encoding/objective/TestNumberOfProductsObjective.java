package test.thiagodnf.nautilus.plugin.spl.encoding.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.spl.encoding.instance.TXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NumberOfProductsObjective;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;

public class TestNumberOfProductsObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new NumberOfProductsObjective();
	
	public static TXTInstanceData getInstanceData() {

		InstanceDataExtension instanceDataExtension = new SPLInstanceDataExtension();

		return (TXTInstanceData) instanceDataExtension.getInstanceData(path);
	}
	
	@Test
	public void shouldReturnZeroIfThereIsNoSelectedProducts() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "00000");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(0.0, value);
	}

	@Test
	public void shouldReturnOneIfAllOfThemWereSelectedProducts() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "11111");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(1.0, value);
	}
	
	@Test
	public void shouldReturnSixtyPercentIfThreeProductsWereSelected() {
		
		NBinarySolution solution = Converter.toBinarySolution(1, "00111");
		
		double value = objective.evaluate(getInstanceData(), solution);
		
		assertEquals(0.6, value);
	}
}
