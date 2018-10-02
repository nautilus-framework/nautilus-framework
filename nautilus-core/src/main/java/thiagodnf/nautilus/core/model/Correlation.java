package thiagodnf.nautilus.core.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Correlation {
	
	private String variable;
	
	private List<Double> values;
	
	public Correlation() {
		this.values = new ArrayList<>();
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
