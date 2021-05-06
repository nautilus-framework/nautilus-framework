package org.nautilus.core.encoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nautilus.core.util.SolutionAttribute;
import org.nautilus.core.util.SolutionUtils;
import org.uma.jmetal.solution.Solution;

import com.google.common.base.Preconditions;

public abstract class NSolution <T> implements Solution<T> {
	
	private static final long serialVersionUID = -6306703378886449928L;

	protected double[] objectives;
	
	protected List<T> variables;
	
	protected Map<Object, Object> attributes;
	
	public NSolution() {
		this.variables = new ArrayList<>();
		this.attributes = new HashMap<>();
	}
	
	public NSolution(int numberOfObjectives, int numberOfVariables) {

		Preconditions.checkArgument(numberOfObjectives >= 1, "The number of objectives should be >= 1");
		Preconditions.checkArgument(numberOfVariables >= 1, "The number of variables should be >= 1");

		this.objectives = new double[numberOfObjectives];
		this.variables = new ArrayList<>(numberOfVariables);
		
		for (int i = 0; i < getNumberOfObjectives(); i++) {
			objectives[i] = 0.0;
		}

		for (int i = 0; i < numberOfVariables; i++) {
			variables.add(i, null);
		}

		this.attributes = new HashMap<>();
	}
	
	public List<T> getVariables() {
		return variables;
	}

	public void setVariables(List<T> variables) {
		this.variables = variables;
	}
	
	public int getNumberOfObjectives() {
		return objectives.length;
	}
	
	public int getNumberOfVariables() {
		return variables.size();
	}
	
	public void setObjectives(double[] objectives) {
		this.objectives = objectives;
	}

	@Override
	public double[] getObjectives() {
		return objectives;
	}

	@Override
	public T getVariableValue(int index) {
		return variables.get(index);
	}

	@Override
	public void setVariableValue(int index, T value) {
		variables.set(index, value);
	}

	@Override
	public String getVariableValueString(int index) {
		return getVariableValue(index).toString();
	}

	@Override
	public void setAttribute(Object id, Object value) {
		 this.attributes.put(id, value) ;
	}

	@Override
	public Object getAttribute(Object id) {
		return attributes.get(id) ;
	}
	
	public Map<Object, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Object, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setObjective(int index, double value) {
		this.objectives[index] = value;
	}

	@Override
	public double getObjective(int index) {
		return objectives[index];
	}
	
	
	public double getUserFeedback() {
		return SolutionUtils.getUserFeedback(this);
	}
	
	public void setVariableUserFeedback(int index, double value) {
		setAttribute(SolutionAttribute.FEEDBACK_FOR_VARIABLE + index, value);
	}
	
	public double getVariableUserFeedback(int index) {

		if (getAttribute(SolutionAttribute.FEEDBACK_FOR_VARIABLE + index) == null) {
			return 0.0;
		}

		return (double) getAttribute(SolutionAttribute.FEEDBACK_FOR_VARIABLE + index);
	}
	
	public abstract String toString();
}
