package thiagodnf.nautilus.core.model;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;

public class Variable {
	
	@NotNull
	private String value;
	
	@NotNull
	private Map<String, String> properties;
	
	public Variable() {
		this("");
	}
	
	public Variable(int value) {
		this(String.valueOf(value));
	}

	public Variable(boolean value) {
		this(String.valueOf(value));
	}

	public Variable(String value) {
		this(value, new HashMap<>());
	}

	public Variable(String value, Map<String, String> properties) {
		this.value = value;
		this.properties = properties;
	}
	
	public Variable(Variable solution) {
		this.value = new String(solution.getValue());
		this.properties = new HashMap<>(solution.getProperties());
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Variable copy() {
		return new Variable(this);
	}
	
	public double getUserFeeback() {

		String feedback = getProperties().get("feedback");

		if (feedback == null) {
			return 0.0;
		}

		return Double.valueOf(feedback);
	}

	public void setUserFeedback(double value) {
		getProperties().put("feedback", String.valueOf(value));
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
