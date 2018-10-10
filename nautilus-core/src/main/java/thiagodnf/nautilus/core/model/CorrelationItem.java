package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CorrelationItem {
	
	private List<String> variables;
	
	private List<Double> objectives;
	
	public CorrelationItem() {
		this.variables = new ArrayList<>();
		this.objectives = new ArrayList<>();
	}
	
	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

	public List<Double> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Double> objectives) {
		this.objectives = objectives;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
