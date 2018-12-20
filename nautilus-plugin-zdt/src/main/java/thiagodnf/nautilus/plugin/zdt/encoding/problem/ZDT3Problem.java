package thiagodnf.nautilus.plugin.zdt.encoding.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.zdt.encoding.instance.TXTInstanceData;

public class ZDT3Problem extends AbstractDoubleProblem {

	private static final long serialVersionUID = -7233594822176588853L;

	private List<AbstractObjective> objectives;
	
	private InstanceData data;
	
	/**
	 * Creates a new instance of problem ZDT1.
	 *
	 * @param numberOfVariables Number of variables.
	 */
	public ZDT3Problem(InstanceData data, List<AbstractObjective> objectives) {

		this.data = data;
		this.objectives = objectives;

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

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}
	
	@Override
	public void evaluate(DoubleSolution solution) {
	
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(data, solution));
		}
	}

	public List<AbstractObjective> getObjectives() {
		return objectives;
	}	
}
