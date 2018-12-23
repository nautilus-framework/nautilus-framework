package thiagodnf.nautilus.core.encoding.solution;

import java.util.HashMap;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import thiagodnf.nautilus.core.encoding.NSolution;

public class NBinarySolution extends NSolution<BinarySet> implements BinarySolution {

	private static final long serialVersionUID = -4755282254256775513L;

	public NBinarySolution() {
		super();
	}
	
	/**
	 * Copy constructor
	 */
	public NBinarySolution(NBinarySolution solution) {
		super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariableValue(i, (BinarySet) solution.getVariableValue(i).clone());
		}

		setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
	}
	
	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 */
	public NBinarySolution(int numberOfObjectives, int numberOfVariables) {
		super(numberOfObjectives, numberOfVariables);

		initializeBinaryVariables();
		initializeObjectiveValues();
	}
		
	@Override
	public Solution<BinarySet> copy() {
		return new NBinarySolution(this);
	}

	@Override
	public int getNumberOfBits(int index) {
		return getVariableValue(index).getBinarySetLength() ;
	}

	@Override
	public int getTotalNumberOfBits() {
		int sum = 0;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			sum += getVariableValue(i).getBinarySetLength();
		}

		return sum;
	}
	
	protected void initializeBinaryVariables() {
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			setVariableValue(i, createNewBitSet(getNumberOfBits(i)));
		}
	}
	
	protected BinarySet createNewBitSet(int numberOfBits) {
		
		BinarySet bitSet = new BinarySet(numberOfBits);

		for (int i = 0; i < numberOfBits; i++) {

			double rnd = JMetalRandom.getInstance().nextDouble();
			
			if (rnd < 0.5) {
				bitSet.set(i);
			} else {
				bitSet.clear(i);
			}
		}
		return bitSet;
	}
}
