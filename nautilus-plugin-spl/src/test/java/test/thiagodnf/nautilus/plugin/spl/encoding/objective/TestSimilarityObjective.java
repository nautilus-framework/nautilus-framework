package test.thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.spl.encoding.objective.SimilarityObjective;

public class TestSimilarityObjective {

	private static Path path = Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("instance-test.txt");
	
	private AbstractObjective objective = new SimilarityObjective();
	
//	public static InstanceData getInstanceData() {
//
//		InstanceDataExtension instanceDataExtension = new SPLInstanceDataExtension();
//
//		return instanceDataExtension.getInstanceData(path);
//	}
//	
//	private double evaluate(String binaryString) {
//
//		NBinarySolution solution = Converter.toBinarySolution(1, binaryString);
//
//		NBinaryProblem problem = new SPLProblem(getInstanceData(), Arrays.asList(objective));
//
//		problem.evaluate(solution);
//		
//		return solution.getObjective(0);
//	}
//	
//	@Test
//	public void shouldReturnOneIfThereIsNoSelectedProducts() {
//		
//		assertEquals(1.0, evaluate("00000"));
//	}
//
//	@Test
//	public void shouldReturnTheCorrectValueIfAllOfThemWereSelectedProducts() {
//		
//		assertEquals((7.4 * 2) / ((5) * (5 - 1)), evaluate("11111"), 0.0001);
//	}
//	
//	@Test
//	public void shouldReturnTheCorrectValues() {
//
//		assertEquals(0.75 , evaluate("11000"), 0.00001);
//		assertEquals(1.0, evaluate("10000"), 0.00001);
//		assertEquals((2.5 * 2) / 6.0, evaluate("10101"), 0.00001);
//	}
//	
//	@Test
//	public void shouldReturnTheCorrectMinAndMaxValues() {
//		
//		assertEquals(0.0, objective.getMinimumValue());
//		assertEquals(1.0, objective.getMaximumValue());
//	}
//	
//	@Test
//	public void shouldReturnNonEmptyNameAndGroupName() {
//
//		assertNotNull(objective.getName());
//		assertNotNull(objective.getGroupName());
//
//		assertNotEquals(0.0, objective.getName().length());
//		assertNotEquals(0.0, objective.getGroupName().length());
//	}
}
