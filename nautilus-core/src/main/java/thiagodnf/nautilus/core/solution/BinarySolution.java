package thiagodnf.nautilus.core.solution;

import java.util.HashMap;

import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.problem.BinaryProblem;
import thiagodnf.nautilus.core.problem.BinaryProblem.FakeBinaryProblem;

public class BinarySolution extends AbstractGenericSolution<BinarySet, BinaryProblem> {

	private static final long serialVersionUID = 8181344734556495910L;

	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 */
	public BinarySolution(int numberOfObjectives, int numberOfVariables) {
		super(new FakeBinaryProblem(numberOfObjectives, numberOfVariables));

		initializeBinaryVariables();
		initializeObjectiveValues();
	}
	
	/** 
	 * Constructor 
	 */
	public BinarySolution(BinaryProblem problem) {
		super(problem);

		initializeBinaryVariables();
		initializeObjectiveValues();
	}

	/** 
	 * Constructor 
	 */
	public BinarySolution(BinarySolution solution) {
		super(solution.problem);

		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}
		
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariableValue(i, solution.getVariableValue(i));
		}

		super.attributes = new HashMap<Object, Object>(solution.attributes);
	}

	protected BinarySet createNewBitSet(int numberOfBits) {

		BinarySet binarySet = new BinarySet(numberOfBits);

		for (int i = 0; i < numberOfBits; i++) {

			if (randomGenerator.nextDouble() < 0.5) {
				binarySet.set(i);
			} else {
				binarySet.clear(i);
			}
		}

		return binarySet;
	}

	public int getNumberOfBits(int index) {
		return getVariableValue(index).getBinarySetLength();
	}

	public BinarySolution copy() {
		return new BinarySolution(this);
	}

	public int getTotalNumberOfBits() {
		
		int sum = 0;
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			sum += getVariableValue(i).getBinarySetLength();
		}

		return sum;
	}

	@Override
	public String getVariableValueString(int index) {
		
		String result = "";
		
		for (int i = 0; i < getVariableValue(index).getBinarySetLength(); i++) {
			if (getVariableValue(index).get(i)) {
				result += "1";
			} else {
				result += "0";
			}
		}
		
		return result;
	}

	protected void initializeBinaryVariables() {
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			setVariableValue(i, createNewBitSet(problem.getNumberOfBits(i)));
		}
	}
}
