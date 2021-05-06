package org.nautilus.core.encoding.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.nautilus.core.encoding.NSolution;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class NBinarySolution extends NSolution<BinarySet> implements BinarySolution {

	private static final long serialVersionUID = -4755282254256775513L;

	protected List<Integer> bitsPerVariable;
	
	public NBinarySolution() {
		super();
		
		this.bitsPerVariable = new ArrayList<>();
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

		setBitsPerVariable(new ArrayList<>(solution.getBitsPerVariable()));
		setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
	}
	
	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 * @param bitsPerVariable 
	 */
	public NBinarySolution(int numberOfObjectives, int numberOfVariables, List<Integer> bitsPerVariable) {
		super(numberOfObjectives, numberOfVariables);

		Preconditions.checkNotNull(bitsPerVariable, "The bits per variable should not be null");
		Preconditions.checkArgument(bitsPerVariable.size() == numberOfVariables, "The bits per variable shound have the same number of variables");
		
		this.bitsPerVariable = bitsPerVariable;
		
		initializeBinaryVariables();
	}
	
	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 * @param bitsForVariables The number of bits for all variables
	 */
	public NBinarySolution(int numberOfObjectives, int numberOfVariables, int bitsForVariables) {
		this(numberOfObjectives, numberOfVariables, Collections.nCopies(numberOfVariables, bitsForVariables));
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
			sum += getNumberOfBits(i);
		}

		return sum;
	}
	
	public List<Integer> getBitsPerVariable() {
		return bitsPerVariable;
	}

	public void setBitsPerVariable(List<Integer> bitsPerVariable) {
		this.bitsPerVariable = bitsPerVariable;
	}

	protected void initializeBinaryVariables() {
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			setVariableValue(i, createNewBitSet(bitsPerVariable.get(i)));
		}
	}
	
	protected BinarySet createNewBitSet(int numberOfBits) {

		BinarySet bitSet = new BinarySet(numberOfBits);

		for (int i = 0; i < numberOfBits; i++) {

			if (JMetalRandom.getInstance().nextDouble() < 0.5) {
				bitSet.set(i);
			} else {
				bitSet.clear(i);
			}
		}

		return bitSet;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
