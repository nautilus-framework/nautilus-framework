package org.nautilus.plugin.toy.encoding.problem;

import java.util.Collections;
import java.util.List;

import org.nautilus.core.encoding.problem.NIntegerProblem;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.toy.encoding.instance.TXTInstance;

public class ToyProblem extends NIntegerProblem {

	private static final long serialVersionUID = -7233594822176588853L;

	public ToyProblem(Instance instance, List<AbstractObjective> objectives) {
		super(instance, objectives);
		
		TXTInstance d = (TXTInstance) instance;
		
		setNumberOfVariables(d.getNumberOfVariables());
		
		setLowerBounds(Collections.nCopies(getNumberOfVariables(), d.getLowerBound()));
		setUpperBounds(Collections.nCopies(getNumberOfVariables(), d.getUpperBound()));
	}
}
