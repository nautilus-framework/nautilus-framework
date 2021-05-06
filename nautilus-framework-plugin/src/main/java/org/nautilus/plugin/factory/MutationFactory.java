package org.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.plugin.extension.MutationExtension;
import org.nautilus.plugin.extension.mutation.BitFlipMutationExtension;
import org.nautilus.plugin.extension.mutation.IntegerPolynomialMutationExtension;
import org.nautilus.plugin.extension.mutation.PolynomialMutationExtension;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

public class MutationFactory {
	
	private List<MutationExtension> extensions = new ArrayList<>();
	
	public MutationFactory() {
		extensions.add(new BitFlipMutationExtension());
		extensions.add(new IntegerPolynomialMutationExtension());
		extensions.add(new PolynomialMutationExtension());
	}

	public List<MutationExtension> getExtensions() {
		return extensions;
	}

	public MutationOperator<? extends Solution<?>> getMutation(String mutationId, double probability, double distributionIndex) {

		for (MutationExtension extension : getExtensions()) {

			if (mutationId.equalsIgnoreCase(extension.getId())) {
				return extension.getMutation(probability, distributionIndex);
			}
		}

		return null;
	}
}
