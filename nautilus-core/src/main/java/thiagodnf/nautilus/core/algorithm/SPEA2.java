package thiagodnf.nautilus.core.algorithm;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class SPEA2<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2<S> implements AlgorithmListener{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	protected int maxEvaluations;
	
	protected int evaluations;
	
	public SPEA2(Builder builder) {
		super(builder.getProblem(), 
				1, 
				builder.getPopulationSize(), 
				builder.getCrossover(), 
				builder.getMutation(), 
				builder.getSelection(),
				new SequentialSolutionListEvaluator<S>());
		
		this.maxEvaluations = builder.getMaxEvaluations();
		this.initialPopulation = builder.getInitialPopulation();
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
	protected void initProgress() {
		evaluations = getMaxPopulationSize();
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		return evaluations >= maxEvaluations;
	}
	
	@Override
	protected void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
		
		evaluations += getMaxPopulationSize() ;
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}
}
