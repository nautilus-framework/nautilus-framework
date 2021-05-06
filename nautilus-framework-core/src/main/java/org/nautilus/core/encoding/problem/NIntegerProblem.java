package org.nautilus.core.encoding.problem;

import java.util.List;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.solution.NIntegerSolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

public abstract class NIntegerProblem extends NProblem<IntegerSolution> implements IntegerProblem {

	public NIntegerProblem(Instance data, List<AbstractObjective> objectives) {
		super(data, objectives);
	}

	private static final long serialVersionUID = 9124593199794358192L;

	private List<Integer> lowerBounds;
	
	private List<Integer> upperBounds;

	@Override
	public Integer getUpperBound(int index) {
		return upperBounds.get(index);
	}

	@Override
	public Integer getLowerBound(int index) {
		return lowerBounds.get(index);
	}

	protected void setLowerBounds(List<Integer> lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	protected void setUpperBounds(List<Integer> upperBounds) {
		this.upperBounds = upperBounds;
	}
	
	protected List<Integer> getLowerBounds() {
		return lowerBounds;
	}
	
	protected List<Integer> getUpperBounds() {
		return upperBounds;
	}
	
	@Override
	public void evaluate(IntegerSolution solution) {
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
		}
	}
	
	@Override
	public IntegerSolution createSolution() {
		return new NIntegerSolution(
			getNumberOfObjectives(), 
			getNumberOfVariables(),
			getLowerBounds(), 
			getUpperBounds()
		);
	}
}
