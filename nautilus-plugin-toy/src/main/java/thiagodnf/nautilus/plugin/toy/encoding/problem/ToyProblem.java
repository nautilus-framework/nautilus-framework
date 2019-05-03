package thiagodnf.nautilus.plugin.toy.encoding.problem;

import java.util.Collections;
import java.util.List;

import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.toy.encoding.instance.TXTInstance;

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
