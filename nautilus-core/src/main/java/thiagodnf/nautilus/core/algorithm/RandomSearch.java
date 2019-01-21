package thiagodnf.nautilus.core.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class RandomSearch<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S> implements AlgorithmListener{

	private static final long serialVersionUID = -399662429840079517L;
	
	private OnProgressListener onProgressListener;
	
	protected List<S> initialPopulation;
	
	public RandomSearch(Builder builder) {
		super(builder.getProblem(), 
				builder.getMaxEvaluations(), 
				builder.getPopulationSize(), 
				builder.getCrossover(), 
				builder.getMutation(), 
				builder.getSelection(),
			new DominanceComparator<>(), 
			new SequentialSolutionListEvaluator<S>());
		
		this.initialPopulation = (List<S>) builder.getInitialPopulation();
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
	
	@Override
	public void run() {

		updateProgress(0.0);
		
		population = createRandomSolutions();
		
		population = evaluatePopulation(population);
		
		updateProgress(100.0);
	}
	
	protected void updateProgress(double value) {
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(value);
		}
	}
	
	public List<S> createRandomSolutions() {

		List<S> population = new ArrayList<>();

		double progress = 0.0;
		
		while (population.size() != maxPopulationSize) {

			population.add(problem.createSolution());
			
			progress = (((double) population.size()) / ((double) maxPopulationSize)) * 100.0;
			
			updateProgress(progress);
		}

		return population;
	}

	@Override
	public List<S> getResult() {
		return SolutionListUtils.getNondominatedSolutions(getPopulation());
	}
}
