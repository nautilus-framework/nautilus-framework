package org.nautilus.core.algorithm;

import java.io.File;
import java.util.List;

import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.nautilus.core.util.Converter;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

@SuppressWarnings("unchecked")
public class WASFGA<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.wasfga.WASFGA<S> implements AlgorithmListener{

	private static final long serialVersionUID = -2060417835574893395L;

	private OnProgressListener onProgressListener;
	
	protected List<S> initialPopulation;
	
	public WASFGA(Builder builder) {
	    super(builder.getProblem(),
            builder.getPopulationSize(),
            builder.getMaxEvaluations(),
            builder.getCrossover(),
            builder.getMutation(),
            builder.getSelection(),
            new SequentialSolutionListEvaluator<S>(),
            builder.getEpsilon(),
            Converter.toDoubleList(builder.getReferencePoints().get(0).getObjectives()),
            getWeightVectorPath(builder)
        ); 
	    
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
	
	public static String getWeightVectorPath(Builder builder) {
	    
	    String dataFileName = "W" + builder.getProblem().getNumberOfObjectives() + "D_" + builder.getPopulationSize() + ".dat";

        String path = "files"+File.separator+"weight-vectors"+File.separator + dataFileName;
        
        return path;
	}

    @Override
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }
    
    @Override
    protected void updateProgress() {
        
        double progress = (((double) iterations) / ((double) maxIterations)) * 100.0;
        
        if (onProgressListener != null) {
            onProgressListener.onProgress(progress);
        }
        
        super.updateProgress();
    }
}
