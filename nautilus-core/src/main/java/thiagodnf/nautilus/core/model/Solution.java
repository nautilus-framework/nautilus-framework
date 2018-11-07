package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;

public class Solution {
	
	private String type;
	
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
		this.type = new String(solution.getType());
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
	
	@Override
	public boolean equals(Object o) {
		
		Solution sol = (Solution) o;

		if (getNumberOfObjectives() != sol.getNumberOfObjectives()) {
			return false;
		}

		if (getVariables().size() != getVariables().size()) {
			return false;
		}
		
		// Binary
		
		
		for (int i = 0; i < getNumberOfVariables(); i++) {

			String v1 = getVariables().get(i).getValue();
			String v2 = sol.getVariables().get(i).getValue();

			if (!v1.equalsIgnoreCase(v2)) {
				return false;
			}
		}
		

		
		// Integer Solution
		
//		Map<String, Integer> mapS1 = new HashMap<>();
//		Map<String, Integer> mapS2 = new HashMap<>();
//		
//		for (int i = 0; i < getNumberOfVariables(); i++) {
//
//			String keyS1 = getVariables().get(i).getValue();
//			String keyS2 = sol.getVariables().get(i).getValue();
//			
//			if (!mapS1.containsKey(keyS1)) {
//				mapS1.put(keyS1, 0);
//			}
//
//			if (!mapS2.containsKey(keyS2)) {
//				mapS2.put(keyS2, 0);
//			}
//			
//			int totalS1 = mapS1.get(keyS1);
//			int totalS2 = mapS2.get(keyS2);
//			
//			mapS1.put(keyS1, ++totalS1);
//			mapS2.put(keyS2, ++totalS2);
//		}
//		
//		for(Entry<String, Integer> entry : mapS1.entrySet()){
//			
//			if(entry.getValue() != mapS2.get(entry.getKey())) {
//				return false;
//			}
//		}
		
		return true;
	}
}
