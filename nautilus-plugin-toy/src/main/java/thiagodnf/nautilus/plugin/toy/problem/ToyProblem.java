package thiagodnf.nautilus.plugin.toy.problem;

import java.util.List;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ToyProblem extends AbstractIntegerProblem {

	private static final long serialVersionUID = -7233594822176588853L;

	private List<AbstractObjective> objectives;
	
	private InstanceData data;
	
	public ToyProblem(InstanceData data, List<AbstractObjective> objectives) {
		
		this.data = data;
		this.objectives = objectives;
	}
	
	@Override
	public void evaluate(IntegerSolution solution) {
	
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(data, solution));
		}
	}

}
