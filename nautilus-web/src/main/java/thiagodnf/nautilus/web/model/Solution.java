package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class Solution {
	
	@NotNull
	private List<Double> objectives;
	
	@NotNull
	private List<String> variables;
	
	public Solution() {
		this.objectives = new ArrayList<>();
		this.variables = new ArrayList<>();
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
}
