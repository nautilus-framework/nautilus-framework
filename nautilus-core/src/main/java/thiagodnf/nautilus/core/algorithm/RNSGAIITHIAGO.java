package thiagodnf.nautilus.core.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.RankingAndPreferenceSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.comparator.RankingComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;
import org.uma.jmetal.util.solutionattribute.impl.PreferenceDistance;

import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class RNSGAIITHIAGO<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S>{

	private static Object KEY = new CrowdingDistance().getAttributeIdentifier();
	
	public static class DistanceToRPComparator<S extends Solution<?>> implements Comparator<S> {

		private int rpIndex;

		public DistanceToRPComparator(int rpIndex) {
			this.rpIndex = rpIndex;
		}

		@Override
		public int compare(S o1, S o2) {

			double v1 = (double) o1.getAttribute("distance_to_rp_" + rpIndex);
			double v2 = (double) o2.getAttribute("distance_to_rp_" + rpIndex);

			return Double.compare(v1, v2);
		}
	}
	
	public static class ReferencePoint {

		private List<Double> values;

		public ReferencePoint(List<Double> values) {
			this.values = values;
		}

		public ReferencePoint(double... values) {
			this(Arrays.stream(values).boxed().collect(Collectors.toList()));
		}

		public List<Double> getValues() {
			return values;
		}

		public void setValues(List<Double> values) {
			this.values = values;
		}

		@Override
		public String toString() {
			return getValues().toString();
		}
	}
	
	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	protected List<ReferencePoint> referencePoints;
	
	protected double epsilon = 0.01;
	
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
			MutationOperator<S> mutationOperator,
			List<ReferencePoint> referencePoints) {
		this(problem, 
			maxEvaluations, 
			populationSize, 
			crossoverOperator, 
			mutationOperator, 
			new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceWithPreferenceComparator<>()));
		
		this.referencePoints = referencePoints;
		this.initialPopulation = null;
	}
	
	public List<ReferencePoint> getReferencePoints() {
		return referencePoints;
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
	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
		List<S> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		RankingAndPreferenceSelection<S> rankingAndCrowdingSelection;
		rankingAndCrowdingSelection = new RNSGAIIRankingAndCrowdingSelection(getMaxPopulationSize(), referencePoints, epsilon);

		return rankingAndCrowdingSelection.execute(jointPopulation);
	}
	
	public class RNSGAIIRankingAndCrowdingSelection extends RankingAndPreferenceSelection<S>{

		private static final long serialVersionUID = 935192163828941635L;
		
		private int solutionsToSelect ;
		
		private List<ReferencePoint> referencePoints;
		
		public RNSGAIIRankingAndCrowdingSelection(
			int solutionsToSelect, 
			List<ReferencePoint> referencePoints, 
			double epsilon) {
		super(
			solutionsToSelect, 
			new ArrayList<>(), 
			epsilon);
		
			this.referencePoints = referencePoints;
			this.solutionsToSelect = solutionsToSelect;
		}
		
		@Override
		public List<S> execute(List<S> solutionList) {
			
			if (null == solutionList) {
				throw new JMetalException("The solution list is null");
			} else if (solutionList.isEmpty()) {
				throw new JMetalException("The solution list is empty");
			} else if (solutionList.size() < solutionsToSelect) {
				throw new JMetalException("The population size (" + solutionList.size() + ") is smaller than"
						+ "the solutions to selected (" + solutionsToSelect + ")");
			}

			Ranking<S> ranking = new DominanceRanking<S>();
			ranking.computeRanking(solutionList);

			return preferenceDistanceSelection(solutionList, ranking, solutionList.get(0).getNumberOfObjectives());
		}
		
		protected List<S> calculate(List<S> solutionList) {
			
			int dim = solutionList.get(0).getNumberOfObjectives();
			
			double[] fmin = new double[dim];
			double[] fmax = new double[dim];

			for (int i = 0; i < dim; i++) {
				fmin[i] = Double.MAX_VALUE;
				fmax[i] = Double.MIN_VALUE;
			}
			
			for (S s : solutionList) {
				
				for (int i = 0; i < dim; i++) {
					
					double value = s.getObjective(i);
					
					if(value < fmin[i]) {
						fmin[i] = value;
					}
					if(value > fmax[i]) {
						fmax[i] = value;
					}
				}
			}
			
			
			
			
			
			for (int i = 0; i < referencePoints.size(); i++) {

				ReferencePoint rp = referencePoints.get(i);

				for (S s : solutionList) {

					ReferencePoint solution = new ReferencePoint(s.getObjectives());

					double sum = 0.0; 
							
					//Normalization Euclidean Distance
					
					for (int j = 0; j < dim; j++) {

						double fi = s.getObjective(j);
						double r = rp.getValues().get(j);
						
						sum += Math.pow((fi-r)/(fmax[j] - fmin[j]), 2.0);
					}
						
					double distance = Math.sqrt(sum);
							//EuclideanDistance.calculate(solution.getValues(), rp.getValues());

					s.setAttribute("distance_to_rp_" + i, distance);
				}
			}
			
			for (int i = 0; i < referencePoints.size(); i++) {
				
				Collections.sort(solutionList, new DistanceToRPComparator<S>(i)) ;
				
				Map<Double, Double> positions = new HashMap<>();
				
				double pos = 1;
				
				for (int j = 0; j < solutionList.size(); j++) {

					double value = (double) solutionList.get(j).getAttribute("distance_to_rp_" + i);
					
					if(!positions.containsKey(value)) {
						positions.put(value, pos++);
					}
				}
				
				for (S s : solutionList) {

					double value = (double) s.getAttribute("distance_to_rp_" + i);

					double posi = positions.get(value);

					s.setAttribute("ranking_to_rp_" + i, posi);
				}
			}
			
			for (S s : solutionList) {
				
				
				
				double minPos = Double.MAX_VALUE;
				
				for (int i = 0; i < referencePoints.size(); i++) {
					
					double pos = (double) s.getAttribute("ranking_to_rp_" + i);
					
					if(pos < minPos) {
						minPos = pos;
					}
				}

				s.setAttribute(KEY, minPos);
				
				//System.out.println(Arrays.toString(s.getObjectives())+", "+s.getAttribute("ranking_to_rp_0")+", "+s.getAttribute("ranking_to_rp_1")+", "+minPos);
				
			}
			
			return solutionList;
		}
		
		protected List<S> preferenceDistanceSelection(List<S> solutionList, Ranking<S> ranking, int numberOfObjectives) {
			
			int nInteresPoint = referencePoints.size();

			List<S> population = new ArrayList<>(solutionsToSelect);
			
			int rankingIndex = 0;
			
			
			int index = 0;
			
            while (population.size() < solutionsToSelect) {
            	if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
                    addRankedSolutionsToPopulation(ranking, rankingIndex, population);
                    rankingIndex++;
                    
                    printFinalSolutionSet(ranking.getSubfront(rankingIndex), index++);
                } else {
                	
                	List<S> sub = ranking.getSubfront(rankingIndex);
                	
                	printFinalSolutionSet(sub, index++);
                	
                	List<S> possible = calculate(sub);
        			
        			Collections.sort(possible, new CrowdingDistanceWithPreferenceComparator<S>());
        			
        			for (S s : possible) {
        				
        				double pos = (double) s.getAttribute(KEY);
        				
        				System.out.println(Arrays.toString(s.getObjectives())+" - "+pos);
        				
        			}
                    
        			int i = 0;
        			
        			while (population.size() < solutionsToSelect) {
        				population.add(possible.get(i));
        				i++;
        			}
        			
//                	int i = possible.size()-1;
//        			
//        			while (population.size() < solutionsToSelect) {
//        				population.add(possible.get(i));
//        				i--;
//        			}
                }
        }
            
            
			

//			while (population.size() < solutionsToSelect) {
//				int indexPoint = 0;
//				for (int n = 0; (n < nInteresPoint) && (population.size() < solutionsToSelect); n++) {
//					List<S> auxPopulation = new ArrayList<>(solutionsToSelect / nInteresPoint);
//					List<Double> auxInterestPoint = nextInterestPoint(indexPoint, numberOfObjectives);
//					indexPoint += numberOfObjectives;
//					PreferenceDistance<S> preferenceDistance = new PreferenceDistance<>(auxInterestPoint, epsilon);
//					int rankingIndex = 0;
//					while ((auxPopulation.size() < (solutionsToSelect / nInteresPoint))
//							&& (population.size() < solutionsToSelect)) {
//						if (subfrontFillsIntoThePopulation(ranking, rankingIndex, auxPopulation)) {
//							addRankedSolutionsToPopulation(ranking, rankingIndex, auxPopulation);
//							rankingIndex++;
//						} else {
//							preferenceDistance.computeDensityEstimator(ranking.getSubfront(rankingIndex));
//							addLastRankedSolutionsToPopulation(ranking, rankingIndex, auxPopulation);
//						}
//					}
//					population.addAll(auxPopulation);
//				}
//			}
//			PreferenceDistance<S> preferenceDistance = new PreferenceDistance<>(new ArrayList<>(), epsilon);
			population = epsilonClean(population);
			return population;
		}

		@Override
		protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S>population) {
	        List<S> currentRankedFront = ranking.getSubfront(rank) ;

//	        for(S s : currentRankedFront) {
//	        	System.out.println(s.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()));
//	        }
//	        System.out.println("-------");
	        //Collections.sort(currentRankedFront, new RNSGAIICrowdingDistanceComparator()) ;

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
	
//	public class RNSGAIICrowdingDistanceComparator extends CrowdingDistanceComparator<S>{
//
//		private static final long serialVersionUID = -1422201875417313777L;
//	
//		private final CrowdingDistance<S> crowdingDistance = new CrowdingDistance<S>() ;
//		
//		private String key = "class org.uma.jmetal.util.solutionattribute.impl.PreferenceDistance";
//		
//		@Override
//		  public int compare(S solution1, S solution2) {
//		    int result ;
//		    if (solution1 == null) {
//		      if (solution2 == null) {
//		        result = 0;
//		      } else {
//		        result = -1 ;
//		      }
//		    } else if (solution2 == null) {
//		      result = 1;
//		    } else {
//		      double distance1 = Double.MIN_VALUE ;
//		      double distance2 = Double.MIN_VALUE ;
//
//		      if (solution1.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()) != null) {
//		        distance1 = (double) solution1.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier());
//		      }
//
//		      if (solution2.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier()) != null) {
//		        distance2 = (double) solution2.getAttribute(new PreferenceDistance<>(referencePoint, epsilon).getAttributeIdentifier());
//		      }
//
//		      if (distance1 > distance2) {
//		        result = 1;
//		      } else  if (distance1 < distance2) {
//		        result = -1;
//		      } else {
//		        result = 0;
//		      }
//		    }
//
//		    return result ;
//		  }
//	}
//	
	public static class RankingAndCrowdingDistanceWithPreferenceComparator<S extends Solution<?>>
	implements Comparator<S>, Serializable {

		private static final long serialVersionUID = 1608210368370929291L;

		private final Comparator<S> rankComparator = new RankingComparator<S>();
		private final Comparator<S> crowdingDistanceComparator = new CrowdingDistanceWithPreferenceComparator<S>();

		/**
		 * Compares two solutions.
		 *
		 * @param solution1 Object representing the first solution
		 * @param solution2 Object representing the second solution.
		 * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than
		 *         solution2, respectively.
		 */
		@Override
		public int compare(S solution1, S solution2) {
			int result = this.rankComparator.compare(solution1, solution2);
			if (result == 0) {
				result = this.crowdingDistanceComparator.compare(solution1, solution2);
			}

			return result;
		}
	}
	
	public static class CrowdingDistanceWithPreferenceComparator<S extends Solution<?>> implements Comparator<S>, Serializable {

		private static final long serialVersionUID = 6963872366235365730L;
		
		private final CrowdingDistance<S> crowdingDistance = new CrowdingDistance<S>() ;

		@Override
		public int compare(S solution1, S solution2) {
			int result;
			if (solution1 == null) {
				if (solution2 == null) {
					result = 0;
				} else {
					result = 1;
				}
			} else if (solution2 == null) {
				result = -1;
			} else {
				double distance1 = Double.MAX_VALUE;
				double distance2 = Double.MAX_VALUE;

				if (solution1.getAttribute(KEY) != null) {
					distance1 = (double) solution1.getAttribute(KEY);
				}

				if (solution2.getAttribute(KEY) != null) {
					distance2 = (double) solution2.getAttribute(KEY);
				}

				if (distance1 > distance2) {
					result = -1;
				} else if (distance1 < distance2) {
					result = 1;
				} else {
					result = 0;
				}
			}

			return result;
		}
	}
	
	public List<S> epsilonClean(List<S> solutionList) {
		
		List<S> preference = new ArrayList<>();
		
		List<S> temporalList = new LinkedList();
		temporalList.addAll(solutionList);
		
		int numerOfObjectives = solutionList.get(0).getNumberOfObjectives();

		while (!temporalList.isEmpty()) {
			int indexRandom = JMetalRandom.getInstance().nextInt(0, temporalList.size() - 1);// 0

			S randomSolution = temporalList.get(indexRandom);

			preference.add(randomSolution);
			temporalList.remove(indexRandom);

			for (int indexOfSolution = 0; indexOfSolution < temporalList.size(); indexOfSolution++) {
				double sum = 0;
				double sum2 = 0;

				for (int indexOfObjective = 0; indexOfObjective < numerOfObjectives; indexOfObjective++) {
					Collections.sort(temporalList, new ObjectiveComparator<S>(indexOfObjective));
					double objetiveMinn = temporalList.get(0).getObjective(indexOfObjective);
					double objetiveMaxn = temporalList.get(temporalList.size() - 1).getObjective(indexOfObjective);
					
					double fi = randomSolution.getObjective(indexOfObjective);
					double r = temporalList.get(indexOfSolution).getObjective(indexOfObjective);
					
					sum += Math.pow((fi-r)/(objetiveMaxn - objetiveMinn), 2.0);
				
					
				//
					
				sum2 = sum2 + ((Math.abs(randomSolution.getObjective(indexOfObjective)
							- temporalList.get(indexOfSolution).getObjective(indexOfObjective)))
							/ (objetiveMaxn - objetiveMinn));

				}
				
				double distance = Math.sqrt(sum);
				
				//System.out.println("distance: "+sum+", "+sum2+", "+distance);

				if (sum2 < epsilon) {
					temporalList.get(indexOfSolution).setAttribute(KEY, Double.MAX_VALUE);
					preference.add(temporalList.get(indexOfSolution));
					temporalList.remove(indexOfSolution);
				}
			}
		}
		return preference;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public void setReferencePoints(List<ReferencePoint> referencePoints) {
		this.referencePoints = referencePoints;
	}
	
	public static void printFinalSolutionSet(List<? extends Solution<?>> population, int index) {

	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("output/FUN_"+index+".tsv"))
	        .print();
	}
}
