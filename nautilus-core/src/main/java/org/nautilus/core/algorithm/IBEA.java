package org.nautilus.core.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings("unchecked")
public class IBEA<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.ibea.IBEA<S> implements AlgorithmListener{

	private static final long serialVersionUID = -3996333329840079517L;
	
	private OnProgressListener onProgressListener;
	
	protected List<S> initialPopulation;
	
	protected int evaluations;
	
	public IBEA(Builder builder) {
	    super(builder.getProblem(), 
            builder.getPopulationSize(), 
            builder.getPopulationSize(), 
            builder.getMaxEvaluations(),
            builder.getSelection(), 
            builder.getCrossover(),
            builder.getMutation()
        );
	   
		this.initialPopulation = (List<S>) builder.getInitialPopulation();
	}
	
	protected List<S> getInitialPopulation() {

		if (this.initialPopulation == null) {
			return createInitialPopulation();
		}

		while (initialPopulation.size() != populationSize) {
			((List<S>) this.initialPopulation).add(problem.createSolution());
		}

		return (List<S>) initialPopulation;
	}
	
	protected void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}
	
	protected List<S> createInitialPopulation() {
	    
	    List<S> solutionSet = new ArrayList<>(populationSize);
        //-> Create the initial solutionSet
        S newSolution;
        for (int i = 0; i < populationSize; i++) {
          newSolution = problem.createSolution();
          problem.evaluate(newSolution);
          evaluations++;
          solutionSet.add(newSolution);
        }
        
        return solutionSet;
	}
	
    @Override
    public void run() {
        
	    List<S> solutionSet, offSpringSolutionSet;

	    evaluations = 0;
	    
	    //Initialize the variables
	    archive = new ArrayList<>(archiveSize);
	    
	    solutionSet = getInitialPopulation();

	    while (evaluations < maxEvaluations) {
	      List<S> union = new ArrayList<>();
	      union.addAll(solutionSet);
	      union.addAll(archive);
	      calculateFitness(union);
	      archive = union;

	      while (archive.size() > populationSize) {
	        removeWorst(archive);
	      }
	      // Create a new offspringPopulation
	      offSpringSolutionSet = new ArrayList<>(populationSize);
	      S parent1;
	      S parent2;
	      while (offSpringSolutionSet.size() < populationSize) {
	        int j = 0;
	        do {
	          j++;
	          parent1 = selectionOperator.execute(archive);
	        } while (j < IBEA.TOURNAMENTS_ROUNDS);
	        int k = 0;
	        do {
	          k++;
	          parent2 = selectionOperator.execute(archive);
	        } while (k < IBEA.TOURNAMENTS_ROUNDS);

	        List<S> parents = new ArrayList<>(2);
	        parents.add(parent1);
	        parents.add(parent2);

	        //make the crossover
	        List<S> offspring = crossoverOperator.execute(parents);
	        mutationOperator.execute(offspring.get(0));
	        problem.evaluate(offspring.get(0));
	        //problem.evaluateConstraints(offSpring[0]);
	        offSpringSolutionSet.add(offspring.get(0));
	        evaluations++;
	        
	        updateProgress();
	      }
	      solutionSet = offSpringSolutionSet;
	    }
	  }
}
