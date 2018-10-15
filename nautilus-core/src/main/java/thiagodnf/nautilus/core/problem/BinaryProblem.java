package thiagodnf.nautilus.core.problem;

import java.util.List;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.solution.BinarySolution;

public abstract class BinaryProblem extends AbstractProblem<BinarySolution> {

	public static class FakeBinaryProblem extends BinaryProblem {

		private static final long serialVersionUID = -8238549660654774370L;

		protected int numberOfVariables;
		
		public FakeBinaryProblem(int numberOfObjectives, int numberOfVariables) {
			
			this.numberOfVariables = numberOfVariables;
			
			setNumberOfObjectives(numberOfObjectives);
			setNumberOfVariables(numberOfVariables);			
		}

		@Override
		public String getName() {
			return "Fake Binary Problem";
		}

		@Override
		protected int getBitsPerVariable(int index) {
			return numberOfVariables;
		}
	}
	
	private static final long serialVersionUID = -119627891767481150L;

	public BinaryProblem() {
		
	}

	public BinaryProblem(InstanceData data, List<AbstractObjective> objectives) {
		super(data, objectives);
	}

	public int getNumberOfBits(int index) {
		return getBitsPerVariable(index);
	}

	public int getTotalNumberOfBits() {
		int count = 0;
		
		for (int i = 0; i < this.getNumberOfVariables(); i++) {
			count += this.getBitsPerVariable(i);
		}

		return count;
	}
	
	protected abstract int getBitsPerVariable(int index) ;

	@Override
	public BinarySolution createSolution() {
		return new BinarySolution(this);
	}
}
