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
import org.nautilus.plugin.spl.encoding.objective.UnimportantFeaturesObjective;
import org.nautilus.plugin.spl.encoding.problem.SPLProblem;
import org.nautilus.plugin.spl.extension.problem.SPLProblemExtension;

public class TestUnimportantFeaturesObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new UnimportantFeaturesObjective();
	
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
	
//	@Test
//	public void shouldReturnOneIfThereIsNoSelectedProducts() {
//		
//		assertEquals(1.0, evaluate("00000"));
//	}

	@Test
	public void shouldReturnZeroIfAllOfThemWereSelectedProducts() {
		
		assertEquals(0.0, evaluate("11111"));
	}
	
	@Test
	public void shouldReturnTheCorrectValues() {

		assertEquals(1.0 - (1.35 / 3.8), evaluate("01001"), 0.0001 );
		assertEquals(1.0 - 0.6 / 3.8, evaluate("10000"), 0.0001 );
		assertEquals(1.0 - 2.05 / 3.8, evaluate("10101"), 0.0001 );
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
