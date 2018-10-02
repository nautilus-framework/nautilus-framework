package thiagodnf.nautilus.core.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.comparator.RankingComparator;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import thiagodnf.nautilus.core.algorithm.RNSGAII.RDominanceComparator;
import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class RNSGAII<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S>{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	private List<?> initialPopulation;
	
	protected List<double[]> referencePoint;
	
	protected double epsilon = 0.0001;
	
	protected Random randomGenerator = new Random();
	
	protected List<Double> weight;
	
	public static class RDominanceComparator<S extends Solution<?>> implements Comparator<S>, Serializable {

	    private ConstraintViolationComparator<S> constraintViolationComparator;

	    /**
	     * Constructor
	     */
	    public RDominanceComparator() {
	        this(new OverallConstraintViolationComparator<S>(), 0.0);
	    }

	    /**
	     * Constructor
	     */
	    public RDominanceComparator(double epsilon) {
	        this(new OverallConstraintViolationComparator<S>(), epsilon);
	    }

	    /**
	     * Constructor
	     */
	    public RDominanceComparator(ConstraintViolationComparator<S> constraintComparator) {
	        this(constraintComparator, 0.0);
	    }

	    /**
	     * Constructor
	     */
	    public RDominanceComparator(ConstraintViolationComparator<S> constraintComparator, double epsilon) {
	        constraintViolationComparator = constraintComparator;
	    }

	    /**
	     * Compares two solutions.
	     *
	     * @param solution1 Object representing the first <code>Solution</code>.
	     * @param solution2 Object representing the second <code>Solution</code>.
	     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
	     * non-dominated, or solution1 is dominated by solution2, respectively.
	     */
	    @Override
	    public int compare(S solution1, S solution2) {
	        if (solution1 == null) {
	            throw new JMetalException("Solution1 is null");
	        } else if (solution2 == null) {
	            throw new JMetalException("Solution2 is null");
	        } else if (solution1.getNumberOfObjectives() != solution2.getNumberOfObjectives()) {
	            throw new JMetalException("Cannot compare because solution1 has " + solution1.getNumberOfObjectives()
	                    + " objectives and solution2 has " + solution2.getNumberOfObjectives());
	        }
	        int result;
	        result = constraintViolationComparator.compare(solution1, solution2);
	        if (result == 0) {
	            result = dominanceTest(solution1, solution2);
	        }

	        return result;
	    }

	    private int dominanceTest(Solution<?> solution1, Solution<?> solution2) {
	        int dominate1 = 0; // dominate1 indicates if some objective of solution1
	        // dominates the same objective in solution2. dominate2
	        int dominate2 = 0; // is the complementary of dominate1.
	        int flag; // stores the result of the comparison
	        double value1, value2;

	        for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
	            value1 = solution1.getObjective(i);
	            value2 = solution2.getObjective(i);
	            if (value1 < value2) {
	                flag = -1;
	            } else if (value1 > value2) {
	                flag = 1;
	            } else {
	                flag = 0;
	            }

	            if (flag == -1) {
	                dominate1 = 1;
	            }

	            if (flag == 1) {
	                dominate2 = 1;
	            }
	        }

	        if (dominate1 == dominate2) {
	            
	            if (getDistanceFromClosestReferencePoint(solution1) > getDistanceFromClosestReferencePoint(solution2)) {
	                return 1;
	            } else if (getDistanceFromClosestReferencePoint(solution1) < getDistanceFromClosestReferencePoint(solution2)) {
	                return -1;
	            } else {
	                return 0;
	            }
	        }
	        if (dominate1 == 1) {
	            return -1;
	        }
	        return 1;
	    }
	}
	
	/**
	 *
	 * @author Jakubovski Filho
	 */
	 class RCrowdingDistanceComparator<S extends Solution<?>> implements Comparator<S>, Serializable {

	    //private final CrowdingDistance<S> crowdingDistance = new CrowdingDistance<S>();
	    RankingComparator<S> ranking = new RankingComparator();
	    
	    /**
	     * Compare two solutions.
	     *
	     * @param solution1 Object representing the first <code>Solution</code>.
	     * @param solution2 Object representing the second <code>Solution</code>.
	     * @return -1, or 0, or 1 if solution1 is has greater, equal, or less
	     * distance value than solution2, respectively.
	     */
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
	            int flag = ranking.compare(solution1, solution2);
	            if(flag != 0){
	                return flag;
	            }
	            int distance1 = Integer.MIN_VALUE;
	            int distance2 = Integer.MIN_VALUE;

	            distance1 = (int) getDistanceFromClosestReferencePoint(solution1);
	            distance2 = (int) getDistanceFromClosestReferencePoint(solution2);

	            if (distance1 < distance2) {
	                result = -1;
	            } else if (distance1 > distance2) {
	                result = 1;
	            } else {
	                result = 0;
	            }
	        }

	        return result;
	    }
	}
	
	class IndexManagement {

        double distance;
        int indexofSol;

        public IndexManagement() {
            distance = 0.0;
            indexofSol = -1;
        }

        public IndexManagement(double dis, int index) {
            distance = dis;
            indexofSol = index;
        }

        void set(double distance, int index) {
            this.distance = distance;
            this.indexofSol = index;
        }

        int getIndexOfSol() {
            return indexofSol;
        }

        double getdistance() {
            return distance;
        }

    }

    class CustomComparator implements Comparator<IndexManagement> {

        @Override
        public int compare(IndexManagement o1, IndexManagement o2) {
            if (o1.getdistance() < o2.getdistance()) {
                return -1;
            }
            if (o1.getdistance() > o2.getdistance()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
	
	public RNSGAII(Problem<S> problem, 
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
		
		this.weight = new ArrayList<>();

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			this.weight.add(1.0);
		}
	}
	
	public RNSGAII(Problem<S> problem, 
			int maxEvaluations, 
			int populationSize, 
			CrossoverOperator<S> crossoverOperator,
			MutationOperator<S> mutationOperator) {
		this(problem, 
			maxEvaluations, 
			populationSize, 
			crossoverOperator, 
			mutationOperator, 
			new BinaryTournamentSelection<>(new RDominanceComparator<>()));
		
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
	
	protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
		List<S> currentRankedFront = ranking.getSubfront(rank);

		Collections.sort(currentRankedFront, new RCrowdingDistanceComparator<S>());

		int i = 0;
		while (population.size() < getMaxPopulationSize()) {
			population.add(currentRankedFront.get(i));
			i++;
		}
	}
	
	protected void addRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
        List<S> front;

        front = ranking.getSubfront(rank);

        for (S solution : front) {
            population.add(solution);
        }
    }
	
	protected Ranking<S> computeRanking(List<S> solutionList) {
        Ranking<S> ranking = new DominanceRanking<>();
        ranking.computeRanking(solutionList);

        return ranking;
    }
	
	protected List<S> crowdingDistanceSelection(Ranking<S> ranking) {
        // CrowdingDistance<S> crowdingDistance = new CrowdingDistance<>();
        List<S> population = new ArrayList<>(getMaxPopulationSize());
        int rankingIndex = 0;
        while (populationIsNotFull(population)) {
            //crowdingDistance.computeDensityEstimator(ranking.getSubfront(rankingIndex));
            if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
                addRankedSolutionsToPopulation(ranking, rankingIndex, population);
                rankingIndex++;
            } else {
                addLastRankedSolutionsToPopulation(ranking, rankingIndex, population);
            }
        }

        return population;
    }
	
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	@Override
    protected List<S> evaluatePopulation(List<S> population) {
        population = evaluator.evaluate(population, getProblem());

        return population;
    }
	
	protected boolean populationIsNotFull(List<S> population) {
        return population.size() < getMaxPopulationSize();
    }
	
	
	
	public List<double[]> getReferencePoint() {
		return referencePoint;
	}

	public void setReferencePoint(List<double[]> referencePoint) {
		this.referencePoint = referencePoint;
	}

	@Override
    protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
        List<S> jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        Ranking<S> ranking = computeRanking(jointPopulation);

        assingDistancePreference(jointPopulation, epsilon);

        return crowdingDistanceSelection(ranking);
    }

    protected boolean subfrontFillsIntoThePopulation(Ranking<S> ranking, int rank, List<S> population) {
        return ranking.getSubfront(rank).size() < (getMaxPopulationSize() - population.size());
    }
    
    protected List<S> assingDistancePreference(List<S> population, double epsilon) {
        int numberOfObjectives = population.get(0).getNumberOfObjectives();
        double[] objetiveMax = new double[numberOfObjectives];
        double[] objetiveMin = new double[numberOfObjectives];

        List<S> front = new ArrayList<>(population.size());
        for (S solution : population) {
            front.add(solution);
        }

        for (int objNo = 0; objNo < numberOfObjectives; objNo++) {
            Collections.sort(front, new ObjectiveComparator<S>(objNo));
            objetiveMin[objNo] = front.get(0).getObjective(objNo);
            objetiveMax[objNo] = front.get(front.size() - 1).getObjective(objNo);
        }

        

        int[][] prefDistanceMat = new int[population.size()][referencePoint.size()];
        double aux1 = 0, aux2 = 0, result = 0;
        List<IndexManagement> arrayIndex;
        for (int i = 0; i < referencePoint.size(); i++) {
            arrayIndex = new ArrayList<>();
            double refPoint[] = referencePoint.get(i);
            for (int j = 0; j < population.size(); j++) {
                double distance = 0.0;
                for (int objNo = 0; objNo < numberOfObjectives; objNo++) {
                    aux1 = population.get(j).getObjective(objNo) - refPoint[objNo];
                    if ((objetiveMax[objNo] == 0) && (objetiveMin[objNo] == 0)) {
                        result = aux1;
                    } else {
                        aux2 = objetiveMax[objNo] - objetiveMin[objNo];
                        result = aux1 / aux2;
                    }
                    distance += weight.get(objNo) * (Math.pow(result, 2));
                }
                distance = Math.sqrt(distance);
                IndexManagement im = new IndexManagement(distance, j);
                arrayIndex.add(im);
            }
            Collections.sort(arrayIndex, new CustomComparator());

            for (int z = 1; z <= arrayIndex.size(); z++) {
                prefDistanceMat[arrayIndex.get(z - 1).getIndexOfSol()][i] = z;
            }
        }

        for (int i = 0; i < population.size(); i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < referencePoint.size(); j++) {
                if (prefDistanceMat[i][j] < min) {
                    min = prefDistanceMat[i][j];
                }
            }
            setDistanceFromClosestReferencePoint(population.get(i), min);
        }

        List<Integer> index = new ArrayList<Integer>(population.size());
        for (int i = 0; i < population.size(); i++) {
            index.add(i, i);
        }
        aux1 = 0;
        aux2 = 0;
        result = 0;
        while (!index.isEmpty()) {
            int currentIndex = index.get(0);
            List<Integer> indexOfSameGroup = new ArrayList<Integer>();
            indexOfSameGroup.add(currentIndex);

            for (int j = 1; j < index.size(); j++) {
                double disIJSol = 0;
                for (int objNo = 0; objNo < numberOfObjectives; objNo++) {
                    aux1 = population.get(currentIndex).getObjective(objNo)
                            - population.get(index.get(j)).getObjective(objNo);
                    if ((objetiveMax[objNo] == 0) && (objetiveMin[objNo] == 0)) {
                        result = aux1;
                    } else {
                        aux2 = objetiveMax[objNo] - objetiveMin[objNo];
                        result = aux1 / aux2;
                    }
                    disIJSol += weight.get(objNo) * (Math.pow(result, 2));
                }

                disIJSol = Math.sqrt(disIJSol);
                if (disIJSol < epsilon) {
                    indexOfSameGroup.add(j);
                }
            }

            int randomIndex = randomGenerator.nextInt(indexOfSameGroup.size());
            setDistancePreference(population.get(indexOfSameGroup.get(randomIndex)),
            		getDistanceFromClosestReferencePoint(population.get(indexOfSameGroup.get(randomIndex))
            ));
            //System.out.println(indexOfSameGroup.size());
            for (int z = 0; z < indexOfSameGroup.size(); z++) {
                if (z == randomIndex) {
                    continue;
                }
                setDistancePreference(population.get(indexOfSameGroup.get(z)),Integer.MAX_VALUE);
                setDistanceFromClosestReferencePoint(population.get(indexOfSameGroup.get(z)),Integer.MAX_VALUE);
            }
            for (int z = 0; z < indexOfSameGroup.size(); z++) {
                index.remove(indexOfSameGroup.get(z));
            }
        }

        //System.out.println("-----------------------------------");
        return population;
    }
    
    public static void setDistancePreference(Solution s, int value) {
    	s.setAttribute("distancePreference", value);
    }
    
    public static int getDistanceFromClosestReferencePoint(Solution s) {
    	Object res = s.getAttribute("distanceFromClosestReferencePoint_");
    	
    	if(res == null) {
    		return 0;
    	}
    	
    	return (int) res;
    }
    
    public static void setDistanceFromClosestReferencePoint(Solution s, int value) {
    	s.setAttribute("distanceFromClosestReferencePoint_", value);
 
    }
}
