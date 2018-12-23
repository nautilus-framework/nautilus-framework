package thiagodnf.nautilus.plugin.toy.encoding.problem;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.toy.encoding.instance.TXTInstanceData;

public class ToyProblem extends NIntegerProblem {

	private static final long serialVersionUID = -7233594822176588853L;

	public ToyProblem(InstanceData instance, List<AbstractObjective> objectives) {
		super(instance, objectives);
		
		TXTInstanceData d = (TXTInstanceData) instance;
		
		// JMetal's Settings
		setNumberOfObjectives(objectives.size());
		setNumberOfVariables(d.getNumberOfVariables());
		
		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(d.getLowerBound());
			upperLimit.add(d.getUpperBound());
		}

		setLowerBounds(lowerLimit);
		setUpperBounds(upperLimit);
	}
}
