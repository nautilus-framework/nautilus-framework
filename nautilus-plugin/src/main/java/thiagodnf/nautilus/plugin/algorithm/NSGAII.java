package thiagodnf.nautilus.plugin.algorithm;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.plugin.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class NSGAII<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S>{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	public NSGAII(Problem<S> problem, 
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
			new DominanceComparator<>(), 
			new SequentialSolutionListEvaluator<S>());
	}
	
	public NSGAII(Problem<S> problem, 
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
			new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>()));
		
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
	protected void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
		
		super.updateProgress();
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}
}
