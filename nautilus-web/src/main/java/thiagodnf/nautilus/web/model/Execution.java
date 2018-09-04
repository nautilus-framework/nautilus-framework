package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.google.gson.Gson;

public class Execution {

	@Id
	private String id;
	
	private long executionTime;
	
	@NotNull
	private Parameters parameters;
	
	@NotNull
	private List<Solution> solutions;
	
	public Execution() {
		this.parameters = new Parameters();
		this.solutions = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
