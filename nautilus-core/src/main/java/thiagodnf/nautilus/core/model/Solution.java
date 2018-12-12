package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;

import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.gson.Gson;

public class Solution {
	
	private String type;
	
	@NotNull
	private List<Double> objectives;
	
	@NotNull
	private List<Object> variables;
	
	@NotNull
	private Map<String, String> attributes;
	
	public Solution() {
		this.objectives = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.attributes = new HashMap<>();
	}
	
	public Solution(Solution solution) {
		this.type = new String(solution.getType());
		this.objectives = new ArrayList<>(solution.getObjectives());
		this.variables = new ArrayList<>(solution.getVariables());
		this.attributes = new HashMap<>(solution.getAttributes());
	}

	public List<Double> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Double> objectives) {
		this.objectives = objectives;
	}

	public List<Object> getVariables() {
		return variables;
	}

	public void setVariables(List<Object> variables) {
		this.variables = variables;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> properties) {
		this.attributes = properties;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
	
	public int getNumberOfObjectives() {
		return getObjectives().size();
	}
	
	public int getNumberOfVariables() {
		return getVariables().size();
	}
	
	public double getObjective(int index) {
		return getObjectives().get(index);
	}
	
	public void setObjective(int index, double value) {
		getObjectives().set(index, value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Solution copy() {
		return new Solution(this);
	}
	
	public boolean isSelected() {
		return getAttributes().containsKey("selected");
	}
	
	public double getUserFeeback() {

		double sum = 0.0;
		double total = 0.0;

		for (Entry<String, String> entry : getAttributes().entrySet()) {

			if (entry.getKey().startsWith("feedback-for-variable-")) {
				sum += Double.valueOf(entry.getValue());
				total++;
			}
		}

		if (total == 0.0) {
			return 0.0;
		}

		return sum / total;
	}
	
	public List<String> getVariablesValueAsList() {

		List<String> variables = new ArrayList<>();

		for (int i = 0; i < getNumberOfVariables(); i++) {

			Object object = getVariables().get(i);

			if (object instanceof BinarySet) {

				BinarySet binarySet = (BinarySet) object;

				for (int j = 0; j < binarySet.getBinarySetLength(); j++) {

					if (binarySet.get(j)) {
						variables.add(String.valueOf(j));
					}
				}
			} else if (object instanceof Integer) {
				variables.add(String.valueOf(object));
			}
		}

		return variables;
	}
}
