package thiagodnf.nautilus.plugin.zdt.encoding.problem;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.problem.NDoubleProblem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.zdt.encoding.instance.TXTInstanceData;

public class ZDT2Problem extends NDoubleProblem {

	private static final long serialVersionUID = -7233594822176588853L;

	/**
	 * Creates a new instance of problem ZDT1.
	 *
	 * @param numberOfVariables Number of variables.
	 */
	public ZDT2Problem(InstanceData data, List<AbstractObjective> objectives) {
		super(data, objectives);
		
		TXTInstanceData d = (TXTInstanceData) data;

		// JMetal's Settings
		setNumberOfObjectives(objectives.size());
		setNumberOfVariables(d.getNumberOfVariables());

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(0.0);
			upperLimit.add(1.0);
		}

		setLowerBounds(lowerLimit);
		setUpperBounds(upperLimit);
	}		
}
