package thiagodnf.nautilus.core.operator.mutation;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import thiagodnf.nautilus.core.solution.BinarySolution;

public class BitFlipForIntegerMutation extends Mutation<BinarySolution> {

	private static final long serialVersionUID = -4586446093935335704L;

	private double mutationProbability;
	private RandomGenerator<Double> randomGenerator;

	/** Constructor */
	  public BitFlipForIntegerMutation(double mutationProbability) {
		  this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
	  }

	/** Constructor */
	  public BitFlipForIntegerMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
	    if (mutationProbability < 0) {
	      throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
	    }
	    this.mutationProbability = mutationProbability;
	    this.randomGenerator = randomGenerator ;
	  }

	public BitFlipForIntegerMutation() {
		this(0.01, () -> JMetalRandom.getInstance().nextDouble());
	}

	/* Getter */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/* Setters */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/** Execute() method */
	@Override
	public BinarySolution execute(BinarySolution solution) {
		if (null == solution) {
			throw new JMetalException("Null parameter");
		}

		doMutation(mutationProbability, solution);
		return solution;
	}

	/**
	 * Perform the mutation operation
	 *
	 * @param probability Mutation setProbability
	 * @param solution    The solution to mutate
	 */
	public void doMutation(double probability, BinarySolution solution) {
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			for (int j = 0; j < solution.getVariableValue(i).getBinarySetLength(); j++) {
				if (randomGenerator.getRandomValue() <= probability) {
					solution.getVariableValue(i).flip(j);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Bit Flip Mutation";
	}
}
