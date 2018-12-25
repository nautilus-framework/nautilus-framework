package thiagodnf.nautilus.core.encoding.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import thiagodnf.nautilus.core.encoding.NSolution;

public class NDoubleSolution extends NSolution<Double> implements DoubleSolution {

	private static final long serialVersionUID = 8545681239007623730L;
	
	protected List<Double> lowerBounds;
	
	protected List<Double> upperBounds;

	public NDoubleSolution() {
		super();
		
		this.lowerBounds = new ArrayList<>();
		this.upperBounds = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param numberOfObjectives The number of objectives
	 * @param numberOfVariables The number of variables
	 */
	public NDoubleSolution(
			int numberOfObjectives, 
			int numberOfVariables, 
			List<Double> lowerBounds,
			List<Double> upperBounds) {
		super(numberOfObjectives, numberOfVariables);

		Preconditions.checkNotNull(lowerBounds, "The lower bounds should not be null");
		Preconditions.checkNotNull(upperBounds, "The upper bounds should not be null");
		Preconditions.checkArgument(lowerBounds.size() == numberOfVariables, "The lower bounds shound have the same number of variables");
		Preconditions.checkArgument(upperBounds.size() == numberOfVariables, "The upper bounds shound have the same number of variables");
		
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;

		initializeDoubleVariables();
	}
	
	public NDoubleSolution(int numberOfObjectives, int numberOfVariables) {
		this(numberOfObjectives, 
			numberOfVariables, 
			Collections.nCopies(numberOfVariables, 0.0),
			Collections.nCopies(numberOfVariables, 1.0));
	}
	
	/**
	 * Copy constructor
	 */
	public NDoubleSolution(NDoubleSolution solution) {
		super(solution.getNumberOfObjectives(), solution.getNumberOfVariables());

		setLowerBounds(new ArrayList<>(solution.getLowerBounds()));
		setUpperBounds(new ArrayList<>(solution.getUpperBounds()));
		
		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariableValue(i, new Double(solution.getVariableValue(i)));
		}

		setAttributes(new HashMap<Object, Object>(solution.getAttributes()));
	}
	
	protected void initializeDoubleVariables() {
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			setVariableValue(i, JMetalRandom.getInstance().nextDouble(getLowerBound(i), getUpperBound(i)));
		}
	}

	@Override
	public NDoubleSolution copy() {
		return new NDoubleSolution(this);
	}
	
	public List<Double> getLowerBounds() {
		return lowerBounds;
	}

	public void setLowerBounds(List<Double> lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	public List<Double> getUpperBounds() {
		return upperBounds;
	}

	public void setUpperBounds(List<Double> upperBounds) {
		this.upperBounds = upperBounds;
	}

	public void setLowerBound(int index, Double value) {
		this.lowerBounds.set(index, value);
	}
	
	@Override
	public Double getLowerBound(int index) {
		return lowerBounds.get(index);
	}
	
	public void setUpperBound(int index, Double value) {
		this.upperBounds.set(index, value);
	}

	@Override
	public Double getUpperBound(int index) {
		return upperBounds.get(index);
	}
	
	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
