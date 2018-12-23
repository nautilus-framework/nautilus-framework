package thiagodnf.nautilus.plugin.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.encoding.NSolution;

public class NIntegerSolution extends NSolution<Integer> implements IntegerSolution {

	private static final long serialVersionUID = 8564568059007623730L;
	
	protected List<Integer> lowerBounds;
	
	protected List<Integer> upperBounds;

	public NIntegerSolution() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 */
	public NIntegerSolution(
			int numberOfObjectives, 
			int numberOfVariables, 
			List<Integer> lowerBounds,
			List<Integer> upperBounds) {
		super(numberOfObjectives, numberOfVariables);

		Preconditions.checkNotNull(lowerBounds, "The lower bounds should not be null");
		Preconditions.checkNotNull(upperBounds, "The upper bounds should not be null");
		Preconditions.checkArgument(lowerBounds.size() == numberOfVariables, "The lower bounds shound have the same number of variables");
		Preconditions.checkArgument(upperBounds.size() == numberOfVariables, "The upper bounds shound have the same number of variables");
		
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;

		initializeIntegerVariables();
		initializeObjectiveValues();
	}
	
	public NIntegerSolution(int numberOfObjectives, int numberOfVariables) {
		this(numberOfObjectives, 
			numberOfVariables, 
			Collections.nCopies(numberOfVariables, 0),
			Collections.nCopies(numberOfVariables, 10));
	}
	
	/**
	 * Copy constructor
	 */
	public NIntegerSolution(NIntegerSolution solution) {
		super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

		setLowerBounds(new ArrayList<>(solution.getLowerBounds()));
		setUpperBounds(new ArrayList<>(solution.getUpperBounds()));
		
		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariableValue(i, new Integer(solution.getVariableValue(i)));
		}

		setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
	}
	
	protected void initializeIntegerVariables() {
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			setVariableValue(i, JMetalRandom.getInstance().nextInt(getLowerBound(i), getUpperBound(i)));
		}
	}

	@Override
	public NIntegerSolution copy() {
		return new NIntegerSolution(this);
	}
	
	public List<Integer> getLowerBounds() {
		return lowerBounds;
	}

	public void setLowerBounds(List<Integer> lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	public List<Integer> getUpperBounds() {
		return upperBounds;
	}

	public void setUpperBounds(List<Integer> upperBounds) {
		this.upperBounds = upperBounds;
	}

	public void setLowerBound(int index, Integer value) {
		this.lowerBounds.set(index, value);
	}
	
	@Override
	public Integer getLowerBound(int index) {
		return lowerBounds.get(index);
	}
	
	public void setUpperBound(int index, Integer value) {
		this.upperBounds.set(index, value);
	}

	@Override
	public Integer getUpperBound(int index) {
		return upperBounds.get(index);
	}
	
	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}
}
