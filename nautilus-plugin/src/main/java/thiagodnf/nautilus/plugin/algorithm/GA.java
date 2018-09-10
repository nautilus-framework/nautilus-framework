package thiagodnf.nautilus.plugin.algorithm;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.plugin.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class GA<S extends Solution<?>> extends org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm<S>{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	private double maxEvaluations;
	
	private int evaluations;
	
	public GA(Problem<S> problem, 
			int maxEvaluations, 
			int populationSize, 
			CrossoverOperator<S> crossoverOperator,
			MutationOperator<S> mutationOperator, 
			SelectionOperator<List<S>, S> selectionOperator) {
		super(problem, 
			maxEvaluations, 
			populationSize, 
			crossoverOperator, 
			mutationOperator, 
			selectionOperator, 
			new SequentialSolutionListEvaluator<S>());
		
		this.maxEvaluations = maxEvaluations;
	}
	
	public GA(Problem<S> problem, 
			int maxEvaluations, 
			int populationSize, 
			CrossoverOperator<S> crossoverOperator,
			MutationOperator<S> mutationOperator,
			List<?> initialPopulation) {
		this(problem, 
			maxEvaluations, 
			populationSize, 
			crossoverOperator, 
			mutationOperator, 
			new BinaryTournamentSelection<S>());
		
		this.initialPopulation = initialPopulation;
	}
	
	@Override
	protected List<S> createInitialPopulation() {

		if (this.initialPopulation == null) {
			return super.createInitialPopulation();
		}

		while (initialPopulation.size() != maxPopulationSize) {
			((List<S>) this.initialPopulation).add(problem.createSolution());
		}

		return (List<S>) initialPopulation;
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		return (evaluations >= maxEvaluations);
	}
	
	@Override
	public void initProgress() {
		evaluations = getMaxPopulationSize();
	}

	@Override
	public void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
		
		evaluations += getMaxPopulationSize();
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}
}
