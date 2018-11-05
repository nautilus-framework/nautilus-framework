package thiagodnf.nautilus.core.algorithm;

import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.operator.crossover.Crossover;
import thiagodnf.nautilus.core.operator.mutation.Mutation;
import thiagodnf.nautilus.core.operator.selection.Selection;

@SuppressWarnings("rawtypes")
public class Builder {
	
	private Problem problem;
	
	private List<?> initialPopulation;
	
	private int populationSize;
	
	private int maxEvaluations;
	
	private Selection selection;
	
	private Crossover crossover;
	
	private Mutation mutation;

	private List<PointSolution> referencePoints;
	
	private double epsilon;
	
	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public List<?> getInitialPopulation() {
		return initialPopulation;
	}

	public void setInitialPopulation(List<?> initialPopulation) {
		this.initialPopulation = initialPopulation;
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

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}

	public Crossover getCrossover() {
		return crossover;
	}

	public void setCrossover(Crossover crossover) {
		this.crossover = crossover;
	}

	public Mutation getMutation() {
		return mutation;
	}

	public void setMutation(Mutation mutation) {
		this.mutation = mutation;
	}

	public List<PointSolution> getReferencePoints() {
		return referencePoints;
	}

	public void setReferencePoints(List<PointSolution> referencePoints) {
		this.referencePoints = referencePoints;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
}
