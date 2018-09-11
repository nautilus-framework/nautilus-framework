package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.google.gson.Gson;

public class Parameters {
	
	@DecimalMin("10")
	@DecimalMax("10000")
	private int populationSize = 100;
	
	@DecimalMin("10")
	@DecimalMax("10000000")
	private int maxEvaluations = 100000;
	
	private String problemKey;
	
	private String filename;
	
	private List<String> objectiveKeys;
	
	private String crossoverName;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double crossoverProbability = 0.9;
	
	@DecimalMin("1.0")
	private Double crossoverDistribution = 20.0;
	
	private String mutationName;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double mutationProbability = 0.01;
	
	@DecimalMin("1.0")
	private Double mutationDistribution = 20.0;
	
	private String lastExecutionId;

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

	public String getLastExecutionId() {
		return lastExecutionId;
	}

	public void setLastExecutionId(String lastExecutionId) {
		this.lastExecutionId = lastExecutionId;
	}

	public String getCrossoverName() {
		return crossoverName;
	}

	public void setCrossoverName(String crossoverName) {
		this.crossoverName = crossoverName;
	}

	public String getMutationName() {
		return mutationName;
	}

	public void setMutationName(String mutationName) {
		this.mutationName = mutationName;
	}

	public Double getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(Double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public Double getCrossoverDistribution() {
		return crossoverDistribution;
	}

	public void setCrossoverDistribution(Double crossoverDistribution) {
		this.crossoverDistribution = crossoverDistribution;
	}

	public Double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(Double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public Double getMutationDistribution() {
		return mutationDistribution;
	}

	public void setMutationDistribution(Double mutationDistribution) {
		this.mutationDistribution = mutationDistribution;
	}
}
