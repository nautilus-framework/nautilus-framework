package thiagodnf.nautilus.core.operator.mutation;

import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

public class BitFlipForIntegerMutation extends Mutation<IntegerSolution> {

	private static final long serialVersionUID = -4586446093935335704L;

	private static final double DEFAULT_PROBABILITY = 0.001;

	private RandomGenerator<Double> randomGenerator;

	/** 
	 * Constructor 
	 */
	public BitFlipForIntegerMutation() {
		this(DEFAULT_PROBABILITY, () -> JMetalRandom.getInstance().nextDouble());
	}

	/** Constructor */
	public BitFlipForIntegerMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {

		if (mutationProbability < 0) {
			throw new JMetalException("Mutation probability is negative: " + mutationProbability);
		} else if (distributionIndex < 0) {
			throw new JMetalException("Distribution index is negative: " + distributionIndex);
		}

		this.probability = mutationProbability;

		this.randomGenerator = randomGenerator;
	}

	/** Execute() method */
	public IntegerSolution execute(IntegerSolution solution) throws JMetalException {

		if (null == solution) {
			throw new JMetalException("Null parameter");
		}

		doMutation(probability, solution);

		return solution;
	}

	/** Perform the mutation operation */
	private void doMutation(double probability, IntegerSolution solution) {

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (randomGenerator.getRandomValue() <= probability) {

				if (solution.getVariableValue(i) == 0) {
					solution.setVariableValue(i, 1);
				} else {
					solution.setVariableValue(i, 0);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "BitFlip For Integer";
	}
}
