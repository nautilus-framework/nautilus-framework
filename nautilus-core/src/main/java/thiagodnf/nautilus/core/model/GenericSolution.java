package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import thiagodnf.nautilus.core.util.SolutionAttribute;

public class GenericSolution implements Solution<Object> {
	
	private static final long serialVersionUID = 1635059184622430041L;

	private double[] objectives;
	
	private List<Object> variables;
	
	private Map<Object, Object> attributes;
	
	private String type;
	
	public GenericSolution() {
		this.variables = new ArrayList<>();
		this.attributes = new HashMap<>();
		this.type = "";
	}
	
	public GenericSolution(int numberOfObjectives, int numberOfVariables) {
		
		Preconditions.checkArgument(numberOfObjectives >= 1, "The number of objectives should be >= 1");
		Preconditions.checkArgument(numberOfVariables >= 1, "The number of variables should be >= 1");
		
		this.objectives = new double[numberOfObjectives];
		this.variables = new ArrayList<>(numberOfVariables);
		this.type = ""; 

		for (int i = 0; i < numberOfVariables; i++) {
			variables.add(i, null);
		}

		this.attributes = new HashMap<>();
	}
	
	public GenericSolution(GenericSolution solution) {
		this(solution.getNumberOfObjectives(), solution.getNumberOfVariables());
				
		for (int i = 0; i < getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < getNumberOfVariables(); i++) {

			if (solution.getVariableValue(i) instanceof BinarySet) {
				setVariableValue(i, ((BinarySet) solution.getVariableValue(i)).clone());
			} else {
				setVariableValue(i, solution.getVariableValue(i));
			}
		}
		
		this.type = new String(solution.type);
		this.attributes = new HashMap<>(solution.attributes);
	}

	public GenericSolution copy() {
		return new GenericSolution(this);
	}
	
	public List<Object> getVariables() {
		return variables;
	}

	public void setVariables(List<Object> variables) {
		this.variables = variables;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public int getNumberOfObjectives() {
		return objectives.length;
	}
	
	public int getNumberOfVariables() {
		return variables.size();
	}

	@Override
	public double[] getObjectives() {
		return objectives;
	}

	@Override
	public Object getVariableValue(int index) {
		return variables.get(index);
	}

	@Override
	public void setVariableValue(int index, Object value) {
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public double getUserFeedback() {

		double sum = 0.0;
		double total = 0.0;

		for (Entry<Object, Object> entry : getAttributes().entrySet()) {

			String key = entry.getKey().toString();

			if (key.startsWith(SolutionAttribute.FEEDBACK_FOR_VARIABLE)) {
				sum += (Double) entry.getValue();
				total++;
			}
		}

		if (total == 0.0) {
			return 0.0;
		}

		return sum / total;
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
}
