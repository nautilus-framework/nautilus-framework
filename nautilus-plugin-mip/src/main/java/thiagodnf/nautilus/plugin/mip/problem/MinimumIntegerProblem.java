package thiagodnf.nautilus.plugin.mip.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.plugin.mip.objective.MinimumNumberObjective;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public class MinimumIntegerProblem extends AbstractIntegerProblem {

	private static final long serialVersionUID = 5347625751550400087L;

	private int size;
	
	private int lowerBound = 1;
	
	private int upperBound= 2;
	
	private List<AbstractObjective<IntegerSolution>> objectives;

	public MinimumIntegerProblem(int size) {
		
		this.size = size;
		this.objectives = new ArrayList<>();
		this.objectives.add(new MinimumNumberObjective(1));
		this.objectives.add(new MinimumNumberObjective(2));

		// JMetal's Settings
		setNumberOfVariables(size);
		setNumberOfObjectives(objectives.size());
		setName(MinimumIntegerProblem.class.getSimpleName());
		
		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lowerBound);
			upperLimit.add(upperBound);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(IntegerSolution solution) {

		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(solution));
		}
	}

}