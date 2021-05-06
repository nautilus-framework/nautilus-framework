package org.nautilus.plugin.extension.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

public class PolynomialMutationExtension extends AbstractMutationExtension {

	@Override
	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex) {
		return new PolynomialMutation(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Polynomial Mutation";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return DoubleSolution.class;
	}
}
