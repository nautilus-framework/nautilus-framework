package thiagodnf.nautilus.plugin.extension.mutation;

import org.pf4j.Extension;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

@Extension
public class IntegerPolynomialMutationExtension implements MutationExtension {

	@Override
	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex) {
		return new IntegerPolynomialMutation(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Integer Polynomial Mutation";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}	
	
	@Override
	public boolean supports(ProblemExtension extension) {

		if (extension == null || extension.supports() == null) {
			return false;
		}

		if (IntegerProblem.class.equals(extension.supports())) {
			return true;
		}

		return false;
	}
}
