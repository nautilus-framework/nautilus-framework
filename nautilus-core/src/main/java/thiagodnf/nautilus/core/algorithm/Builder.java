package thiagodnf.nautilus.core.algorithm;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

@SuppressWarnings("rawtypes")
public class Builder {
	
	private Problem problem;
	
	private List<? extends Solution<?>> initialPopulation;
	
	private int populationSize;
	
	private int maxEvaluations;
	
	private SelectionOperator selection;
	
	private CrossoverOperator crossover;
	
	private MutationOperator mutation;

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

	public void setInitialPopulation(List<? extends Solution<?>> initialPopulation) {
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

	public SelectionOperator getSelection() {
		return selection;
	}

	public void setSelection(SelectionOperator selection) {
		this.selection = selection;
	}

	public CrossoverOperator getCrossover() {
		return crossover;
	}

	public void setCrossover(CrossoverOperator crossover) {
		this.crossover = crossover;
	}

	public MutationOperator getMutation() {
		return mutation;
	}

	public void setMutation(MutationOperator mutation) {
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
