package thiagodnf.nautilus.plugin.factory;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.solution.Solution;

public class MutationFactory {

	@SuppressWarnings("rawtypes")
	public static <T extends Solution<?>> MutationOperator getMutation(String name, double probability, double distributionIndex) {

		if (name.equalsIgnoreCase("IntegerPolynomialMutation")) {
			return new IntegerPolynomialMutation(probability, distributionIndex);
		} else {
			throw new IllegalArgumentException("Invalid Mutation Name");
		}
	}
}