package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.google.gson.Gson;

public class Parameters {
	
	@DecimalMin("10")
	@DecimalMax("1000")
	private int populationSize = 10;
	
	@DecimalMin("10")
	@DecimalMax("1000000")
	private int maxEvaluations = 100000;
	
	private String problemKey;
	
	private String filename;
	
	private List<String> objectiveKeys;

	public Parameters() {
		this.objectiveKeys = new ArrayList<>();
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public void setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
	}
	
	public List<String> getObjectiveKeys() {
		return objectiveKeys;
	}

	public void setObjectiveKeys(List<String> objectiveKeys) {
		this.objectiveKeys = objectiveKeys;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

	public String getProblemKey() {
		return problemKey;
	}

	public void setProblemKey(String problemKey) {
		this.problemKey = problemKey;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
