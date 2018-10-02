package thiagodnf.nautilus.core.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.RankingAndPreferenceSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.CrowdingDistanceComparator;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;
import org.uma.jmetal.util.solutionattribute.impl.PreferenceDistance;

import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class RNSGAIITHIAGO<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S>{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	protected List<Double> referencePoint;
	
	protected double epsilon = 0.0001;
	
	protected Random randomGenerator = new Random();
	
	protected List<Double> weights;
	
	public RNSGAIITHIAGO(Problem<S> problem, 
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
		
		this.weights = new ArrayList<>();

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			this.weights.add(1.0);
		}
	}
	
	public RNSGAIITHIAGO(Problem<S> problem, 
			int maxEvaluations, 
			int populationSize, 
			CrossoverOperator<S> crossoverOperator,
			MutationOperator<S> mutationOperator) {
		this(problem, 
			maxEvaluations, 
			populationSize, 
			crossoverOperator, 
			mutationOperator, 
			new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>()));
		
		this.initialPopulation = null;
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
	
	@Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
	    List<S> jointPopulation = new ArrayList<>();
	    jointPopulation.addAll(population);
	    jointPopulation.addAll(offspringPopulation);

	    RankingAndPreferenceSelection<S> rankingAndCrowdingSelection ;
	    rankingAndCrowdingSelection = new RNSGAIIRankingAndCrowdingSelection(getMaxPopulationSize(), referencePoint,epsilon) ;

	    return rankingAndCrowdingSelection.execute(jointPopulation) ;
	  }
	
	public class RNSGAIIRankingAndCrowdingSelection extends RankingAndPreferenceSelection<S>{

		private static final long serialVersionUID = 935192163828941635L;
		
		private int solutionsToSelect ;
		
		public RNSGAIIRankingAndCrowdingSelection(
			int solutionsToSelect, 
			List<Double> interestPoint, 
			double epsilon) {
			
			super(solutionsToSelect, interestPoint, epsilon);
		
			this.solutionsToSelect = solutionsToSelect;
		}

		@Override
		protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S>population) {
	        List<S> currentRankedFront = ranking.getSubfront(rank) ;

//	        for(S s : currentRankedFront) {
//	        	System.out.println(s.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()));
//	        }
//	        System.out.println("-------");
	        Collections.sort(currentRankedFront, new RNSGAIICrowdingDistanceComparator()) ;

//	        for(S s : currentRankedFront) {
//	        	System.out.println(s.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()));
//	        }
	        
	        //System.out.println(currentRankedFront.get(0).getAttribute(id));
	        int i = 0 ;
	        while (population.size() < solutionsToSelect) {
	            population.add(currentRankedFront.get(i)) ;
	            i++ ;
	        }
	    }

		
		
	}
	
	public class RNSGAIICrowdingDistanceComparator extends CrowdingDistanceComparator<S>{

		private static final long serialVersionUID = -1422201875417313777L;
	
		private final CrowdingDistance<S> crowdingDistance = new CrowdingDistance<S>() ;
		
		private String key = "class org.uma.jmetal.util.solutionattribute.impl.PreferenceDistance";
		
		@Override
		  public int compare(S solution1, S solution2) {
		    int result ;
		    if (solution1 == null) {
		      if (solution2 == null) {
		        result = 0;
		      } else {
		        result = -1 ;
		      }
		    } else if (solution2 == null) {
		      result = 1;
		    } else {
		      double distance1 = Double.MIN_VALUE ;
		      double distance2 = Double.MIN_VALUE ;

		      if (solution1.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()) != null) {
		        distance1 = (double) solution1.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier());
		      }

		      if (solution2.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()) != null) {
		        distance2 = (double) solution2.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier());
		      }

		      if (distance1 > distance2) {
		        result = 1;
		      } else  if (distance1 < distance2) {
		        result = -1;
		      } else {
		        result = 0;
		      }
		    }

		    return result ;
		  }
	}
//	
//	public class RNSGAIICrowdingDistance extends CrowdingDistance<S> {
//
//		private static final long serialVersionUID = 1608210368370929291L;
//
//		@Override
//		public void computeDensityEstimator(List<S> solutionList) {
//
//			int size = solutionList.size();
//
//			if (size == 0) {
//				return;
//			}
//
//			if (size == 1) {
//				solutionList.get(0).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
//				return;
//			}
//
//			if (size == 2) {
//				solutionList.get(0).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
//				solutionList.get(1).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
//
//				return;
//			}
//
//			// Use a new SolutionSet to avoid altering the original solutionSet
//			List<S> front = new ArrayList<>(size);
//			for (S solution : solutionList) {
//				front.add(solution);
//			}
//		}
//
//		@Override
//		public Object getAttributeIdentifier() {
//			return "DISTANCE";
//		}
//	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public void setReferencePoint(List<Double> referencePoint) {
		this.referencePoint = referencePoint;
	}
}
