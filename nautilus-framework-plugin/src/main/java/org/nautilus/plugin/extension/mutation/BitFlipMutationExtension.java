package org.nautilus.plugin.extension.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;

public class BitFlipMutationExtension extends AbstractMutationExtension {

	@Override
	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex) {
		return new BitFlipMutation(probability);
	}

	@Override
	public String getName() {
		return "Bit Flip Mutation";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return BinarySolution.class;
	}
}
