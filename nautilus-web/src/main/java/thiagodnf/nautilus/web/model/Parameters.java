package thiagodnf.nautilus.web.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.google.gson.Gson;

public class Parameters {
	
	@DecimalMin("10")
	@DecimalMax("1000")
	private int populationSize = 100;
	
	@DecimalMin("10")
	@DecimalMax("1000000")
	private int maxEvaluations = 1000;

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
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
