package thiagodnf.nautilus.core.problem;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.solution.IntegerSolution;

public abstract class IntegerProblem extends AbstractProblem<IntegerSolution>{

	public static class FakeIntegerProblem extends IntegerProblem {

		private static final long serialVersionUID = -8238549660654774370L;

		public FakeIntegerProblem(int numberOfObjectives, int numberOfVariables) {
			setNumberOfObjectives(numberOfObjectives);
			setNumberOfVariables(numberOfVariables);

			List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
			List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

			for (int i = 0; i < getNumberOfVariables(); i++) {
				lowerLimit.add(0);
				upperLimit.add(1);
			}

			setLowerLimit(lowerLimit);
			setUpperLimit(upperLimit);
		}

		@Override
		public String getName() {
			return "Fake Integer Problem";
		}
	}
	
	private static final long serialVersionUID = -8950253488872454839L;

	private List<Integer> lowerLimit;
	
	private List<Integer> upperLimit;
	
	public IntegerProblem() {
		this.lowerLimit = new ArrayList<>();
		this.upperLimit = new ArrayList<>();
	}
	
	public IntegerProblem(InstanceData data, List<AbstractObjective> objectives) {
		super(data, objectives);
		
		this.lowerLimit = new ArrayList<>();
		this.upperLimit = new ArrayList<>();
	}
	
	public Integer getUpperBound(int index) {
		return upperLimit.get(index);
	}

	public Integer getLowerBound(int index) {
		return lowerLimit.get(index);
	}

	protected void setLowerLimit(List<Integer> lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	protected void setUpperLimit(List<Integer> upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	@Override
	public IntegerSolution createSolution() {
		return new IntegerSolution(this);
	}
}
