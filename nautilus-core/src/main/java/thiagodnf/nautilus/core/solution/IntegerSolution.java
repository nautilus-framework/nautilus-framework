package thiagodnf.nautilus.core.solution;

import java.util.HashMap;

import org.uma.jmetal.solution.impl.AbstractGenericSolution;

import thiagodnf.nautilus.core.problem.IntegerProblem;
import thiagodnf.nautilus.core.problem.IntegerProblem.FakeIntegerProblem;

public class IntegerSolution extends AbstractGenericSolution<Integer, IntegerProblem>{

	private static final long serialVersionUID = 8180243734556495910L;

	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 */
	public IntegerSolution(int numberOfObjectives, int numberOfVariables) {
		super(new FakeIntegerProblem(numberOfObjectives, numberOfVariables));

		initializeIntegerVariables();
		initializeObjectiveValues();
	}
	/**
	 * Constructor
	 * 
	 * @param problem The addressed problem
	 */
	public IntegerSolution(IntegerProblem problem) {
		super(problem);

		initializeIntegerVariables();
		initializeObjectiveValues();
	}

	/**
	 * Constructor
	 * 
	 * @param solution The solution that should be copied
	 */
	public IntegerSolution(IntegerSolution solution) {
		super(solution.problem);

		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}
		
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariableValue(i, solution.getVariableValue(i));
		}

		super.attributes = new HashMap<Object, Object>(solution.attributes);
	}

	public Integer getUpperBound(int index) {
		return problem.getUpperBound(index);
	}

	public Integer getLowerBound(int index) {
		return problem.getLowerBound(index);
	}

	public IntegerSolution copy() {
		return new IntegerSolution(this);
	}
	
	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}

	protected void initializeIntegerVariables() {

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			setVariableValue(i, randomGenerator.nextInt(getLowerBound(i), getUpperBound(i)));
		}
	}
}
