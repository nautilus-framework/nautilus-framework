package test.thiagodnf.nautilus.plugin.spl.encoding.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.util.Converter;
import org.nautilus.plugin.spl.encoding.objective.CostObjective;
import org.nautilus.plugin.spl.encoding.problem.SPLProblem;
import org.nautilus.plugin.spl.extension.problem.SPLProblemExtension;

public class TestCostObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new CostObjective();
	
	public static Instance getInstanceData() {

		SPLProblemExtension instanceDataExtension = new SPLProblemExtension();

		return instanceDataExtension.getInstance(path);
	}
	
	private double evaluate(String binaryString) {

		NBinarySolution solution = Converter.toBinarySolution(1, binaryString);
		
		NBinaryProblem problem = new SPLProblem(getInstanceData(), Arrays.asList(objective));

		problem.evaluate(solution);
		
		return solution.getObjective(0);
	}
	
	@Test
	public void shouldReturnZeroIfThereIsNoSelectedProducts() {
		
		assertNotEquals(0.0, evaluate("00000"));
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
