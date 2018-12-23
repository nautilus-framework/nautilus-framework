package thiagodnf.nautilus.plugin.problem;

import java.util.List;

import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.plugin.solution.NIntegerSolution;

public abstract class NIntegerProblem extends NProblem<IntegerSolution> implements IntegerProblem {

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
	public IntegerSolution createSolution() {
		return new NIntegerSolution(
			getNumberOfObjectives(), 
			getNumberOfVariables(),
			getLowerBounds(), 
			getUpperBounds()
		);
	}
}
