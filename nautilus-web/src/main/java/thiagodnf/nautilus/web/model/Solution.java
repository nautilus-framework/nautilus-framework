package thiagodnf.nautilus.web.model;

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
	private List<String> variables;
	
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

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
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

	public Solution copy() {
		return new Solution(this);
	}
}
