package thiagodnf.nautilus.plugin.extension.mutation;

import org.pf4j.Extension;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

@Extension
public class PolynomialMutationExtension implements MutationExtension {

	@Override
	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex) {
		return new PolynomialMutation(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Polynomial Mutation";
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

		if (DoubleProblem.class.equals(extension.supports())) {
			return true;
		}

		return false;
	}
}
