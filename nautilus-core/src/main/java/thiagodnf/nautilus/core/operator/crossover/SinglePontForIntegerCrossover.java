package thiagodnf.nautilus.core.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

public class SinglePontForIntegerCrossover extends Crossover<IntegerSolution> {

	private static final long serialVersionUID = 7599597315417258581L;

	private static final double DEFAULT_PROBABILITY = 0.001;

	private RandomGenerator<Double> crossoverRandomGenerator;

	private BoundedRandomGenerator<Integer> pointRandomGenerator;

	/** Constructor */
	public SinglePontForIntegerCrossover(double crossoverProbability) {
		this(crossoverProbability, () -> JMetalRandom.getInstance().nextDouble(),
				(a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	}

	/** Constructor */
	public SinglePontForIntegerCrossover(double crossoverProbability, RandomGenerator<Double> randomGenerator) {
		this(crossoverProbability, randomGenerator, BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
	}

	/** Constructor */
	public SinglePontForIntegerCrossover(double crossoverProbability, RandomGenerator<Double> crossoverRandomGenerator,
			BoundedRandomGenerator<Integer> pointRandomGenerator) {
		if (crossoverProbability < 0) {
			throw new JMetalException("Crossover probability is negative: " + crossoverProbability);
		}
		this.probability = crossoverProbability;
		this.crossoverRandomGenerator = crossoverRandomGenerator;
		this.pointRandomGenerator = pointRandomGenerator;
	}

	public SinglePontForIntegerCrossover() {
		this(DEFAULT_PROBABILITY);
	}

	@Override
	public List<IntegerSolution> execute(List<IntegerSolution> solutions) {
		
		if (solutions == null) {
			throw new JMetalException("Null parameter");
		} else if (solutions.size() != 2) {
			throw new JMetalException("There must be two parents instead of " + solutions.size());
		}

		return doCrossover(probability, solutions.get(0), solutions.get(1));
	}

	/**
	 * Perform the crossover operation.
	 *
	 * @param probability Crossover setProbability
	 * @param parent1     The first parent
	 * @param parent2     The second parent
	 * @return An array containing the two offspring
	 */
	public List<IntegerSolution> doCrossover(double probability, IntegerSolution parent1, IntegerSolution parent2) {
		
		List<IntegerSolution> offspring = new ArrayList<>(2);
		
		offspring.add((IntegerSolution) parent1.copy());
		offspring.add((IntegerSolution) parent2.copy());

		if (crossoverRandomGenerator.getRandomValue() < probability) {

			int numberOfBits = parent1.getNumberOfVariables();
			
			int pos = pointRandomGenerator.getRandomValue(0, numberOfBits - 1);

			// Generate the children
			for (int i = 0; i < numberOfBits; i++) {
				
				if (i <= pos) {
					offspring.get(0).setVariableValue(i, parent1.getVariableValue(i));
					offspring.get(1).setVariableValue(i, parent2.getVariableValue(i));
				} else {
					offspring.get(0).setVariableValue(i, parent2.getVariableValue(i));
					offspring.get(1).setVariableValue(i, parent1.getVariableValue(i));
				}
			}
		}
		
		return offspring;
	}

	@Override
	public int getNumberOfRequiredParents() {
		return 2;
	}

	@Override
	public int getNumberOfGeneratedChildren() {
		return 2;
	}

	@Override
	public String getName() {
		return "Single Point For Integer";
	}
}
