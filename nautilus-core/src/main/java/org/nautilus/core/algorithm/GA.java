package org.nautilus.core.algorithm;

import java.util.List;

import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

@SuppressWarnings("unchecked")
public class GA<S extends Solution<?>> extends org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm<S> implements AlgorithmListener{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	private double maxEvaluations;
	
	private int evaluations;
	
	public GA(Builder builder) {
		super(builder.getProblem(), 
			builder.getMaxEvaluations(), 
			builder.getPopulationSize(), 
			builder.getCrossover(), 
			builder.getMutation(), 
			builder.getSelection(), 
			new SequentialSolutionListEvaluator<S>());
		
		if (builder.getProblem().getNumberOfObjectives() > 1) {
			throw new RuntimeException("GA should optimize just one objective");
		}
		
		this.initialPopulation = builder.getInitialPopulation();
		this.maxEvaluations = builder.getMaxEvaluations();
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
