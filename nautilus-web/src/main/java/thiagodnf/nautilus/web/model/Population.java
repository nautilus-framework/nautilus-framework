package thiagodnf.nautilus.web.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.google.gson.Gson;

public class Population {

	@Id
	private String id;
	
	private String lastId;
	
	private long executionTime;
	
	private List<Solution> solutions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
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
