package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;

public class Solution {
	
	@NotNull
	private List<Double> objectives;
	
	@NotNull
	private List<Variable> variables;
	
	@NotNull
	private Map<String, String> properties;
	
	public Solution() {
		this.objectives = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.properties = new HashMap<>();
	}
	
	public Solution(Solution solution) {
		this.objectives = new ArrayList<>(solution.getObjectives());
		this.variables = new ArrayList<>(solution.getVariables());
		this.properties = new HashMap<>(solution.getProperties());
	}

	public List<Double> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Double> objectives) {
		this.objectives = objectives;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
	
	public int getNumberOfObjectives() {
		return getObjectives().size();
	}
	
	public double getObjective(int index) {
		return getObjectives().get(index);
	}
	
	public void setObjective(int index, double value) {
		getObjectives().set(index, value);
	}

	public Solution copy() {
		return new Solution(this);
	}
	
	public boolean isSelected() {
		return getProperties().containsKey("selected");
	}
	
	public void setUserFeedback(double value) {
		getProperties().put("feedback", String.valueOf(value));
	}
	
	public double getUserFeeback() {

		String feedback = getProperties().get("feedback");

		if (feedback == null) {
			return 0.0;
		}

		return Double.valueOf(feedback);
	}
}
