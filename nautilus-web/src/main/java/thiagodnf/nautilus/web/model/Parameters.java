package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

import com.google.gson.Gson;

public class Parameters {
	
	@DecimalMin("10")
	@DecimalMax("1000")
	private int populationSize = 100;
	
	@DecimalMin("10")
	@DecimalMax("10000000")
	private int maxEvaluations = 500000;
	
	private String pluginId;
	
	@NotEmpty
	private String algorithmId;
	
	private String problemId;
	
	private String filename;
	
	private List<String> objectiveKeys;
	
	@NotEmpty
	private String selectionId;
	
	@NotEmpty
	private String crossoverId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double crossoverProbability = 0.9;
	
	@DecimalMin("1.0")
	@DecimalMax("1000.0")
	private Double crossoverDistribution = 20.0;
	
	@NotEmpty
	private String mutationId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double mutationProbability = 0.005;
	
	@DecimalMin("1.0")
	@DecimalMax("1000.0")
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

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	
	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
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
	
	public void setSelectionId(String selectionId) {
		this.selectionId = selectionId;
	}

	public String getSelectionId() {
		return selectionId;
	}
	
	public String getCrossoverId() {
		return crossoverId;
	}

	public void setCrossoverId(String crossoverId) {
		this.crossoverId = crossoverId;
	}

	public String getMutationId() {
		return mutationId;
	}

	public void setMutationId(String mutationId) {
		this.mutationId = mutationId;
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

	public String getAlgorithmId() {
		return algorithmId;
	}

	public void setAlgorithmId(String algorithmName) {
		this.algorithmId = algorithmName;
	}
}
