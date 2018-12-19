package thiagodnf.nautilus.core.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.DensityEstimator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;

import com.google.common.base.Preconditions;

public class RNSGAII<S extends Solution<?>> extends NSGAII<S>{

	private static final long serialVersionUID = -2060417835574892395L;

	private double epsilon;
	
	private List<PointSolution> referencePoints;
	
	public RNSGAII(Builder builder) {
		super(builder);
		
		Preconditions.checkNotNull(builder.getReferencePoints(), "The reference point list should not be null");
		Preconditions.checkArgument(!builder.getReferencePoints().isEmpty(), "The reference point list should not be empty");
		
		for (PointSolution rp : builder.getReferencePoints()) {
			Preconditions.checkArgument(rp.getNumberOfObjectives() == builder.getProblem().getNumberOfObjectives(), "The reference point list should have points the same number of objectives to problem");
		}
		
		this.referencePoints = builder.getReferencePoints();
		this.epsilon = builder.getEpsilon();
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
	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {

		List<S> jointPopulation = new ArrayList<>();

		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		int solutionToSelect = getMaxPopulationSize();
		
		return new RankingAndPreferenceReplacement<S>(referencePoints, epsilon, solutionToSelect).execute(jointPopulation);
	}
	
	public static class RankingAndPreferenceReplacement<S extends Solution<?>> extends RankingAndCrowdingSelection<S> {
		
		private static final long serialVersionUID = 2840694213029065690L;

		private int solutionsToSelect;
		
		private Comparator<S> dominanceComparator ;
		
		private List<PointSolution> referencePoints;
		
		private double epsilon;
		
		public RankingAndPreferenceReplacement(List<PointSolution> referencePoints, double epsilon, int solutionsToSelect) {
			this(referencePoints, epsilon, solutionsToSelect, new DominanceComparator<S>());
		}

		public RankingAndPreferenceReplacement(List<PointSolution> referencePoints, double epsilon, int solutionsToSelect, Comparator<S> dominanceComparator) {
			super(solutionsToSelect, dominanceComparator);

			this.epsilon = epsilon;
			this.referencePoints = referencePoints;
			this.dominanceComparator = dominanceComparator;
			this.solutionsToSelect = solutionsToSelect;
		}

		/** Execute() method */
		public List<S> execute(List<S> solutionList) throws JMetalException {
			
			Preconditions.checkNotNull(solutionList, "The solution list should not be null");
			Preconditions.checkArgument(solutionList.size() >= solutionsToSelect, "The solution list should be >= "+ solutionsToSelect);
			
			Ranking<S> ranking = new DominanceRanking<S>(dominanceComparator);
			ranking.computeRanking(solutionList);

			return preferenceDistanceSelection(solutionList, ranking);
		}
		
		protected List<S> preferenceDistanceSelection(List<S> solutionList, Ranking<S> ranking) {
			
			Preconditions.checkNotNull(ranking, "The ranking should not be null");
			Preconditions.checkNotNull(solutionList, "The solution list should not be null");
			Preconditions.checkArgument(solutionList.size() >= solutionsToSelect, "The solution list should be >= "+ solutionsToSelect);
			
			int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
			
			double[] fmin = new double[numberOfObjectives];
			double[] fmax = new double[numberOfObjectives];

			for (int i = 0; i < numberOfObjectives; i++) {

				Collections.sort(solutionList, new ObjectiveComparator<S>(i));

				fmin[i] = solutionList.get(0).getObjective(i);
				fmax[i] = solutionList.get(solutionList.size() - 1).getObjective(i);
			}
			
			PreferenceDistance<S> preferenceDistance = new PreferenceDistance<S>(referencePoints, fmin, fmax);
			
			//calculate  preference  distance  of  each  fronts' individual using nitching strategy specified in Fig. 2
			preferenceDistance.computeDensityEstimator(solutionList);
			
			solutionList = clearEpsilon(solutionList, fmin, fmax);
			
			List<S> population = new ArrayList<>(solutionsToSelect);
			
			int rankingIndex = 0;
			
			while (population.size() < solutionsToSelect) {
				
				if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
					addRankedSolutionsToPopulation(ranking, rankingIndex, population);
					rankingIndex++;
				} else {
					addLastRankedSolutionsToPopulation(ranking, rankingIndex, population, fmin, fmax);
				}
			}
			
			return population;
		}
		
		protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population, double[] fmin, double[] fmax) {
			
			List<S> currentRankedFront = ranking.getSubfront(rank);
			
			Collections.sort(currentRankedFront, new PreferenceDistanceComparator<S>());
			
			int i = 0;
			
			while (population.size() < solutionsToSelect) {
				population.add(currentRankedFront.get(i));
				i++;
			}
		}
		
		protected List<S> clearEpsilon(List<S> population, double[] fmin, double[] fmax) {
			
			List<S> nextPopulation = new ArrayList<>();
			
			List<S> temporalList = new LinkedList<S>(population);
			
			while (!temporalList.isEmpty()) {
				
				int indexRandom = JMetalRandom.getInstance().nextInt(0, temporalList.size() - 1);

				S randomSolution = temporalList.get(indexRandom);
				
				nextPopulation.add(randomSolution);
				temporalList.remove(indexRandom);
				
				PointSolution p = PointSolutionUtils.createSolution(randomSolution.getObjectives());
				
				List<S> group = new ArrayList<>();

				for (int i = 0; i < temporalList.size(); i++) {

					PointSolution q = PointSolutionUtils.createSolution(temporalList.get(i).getObjectives());

					double sum = 0.0;

					for (int j = 0; j < q.getNumberOfObjectives(); j++) {
						sum += (Math.abs(p.getObjective(j) - q.getObjective(j)) / (fmax[j] - fmin[j]));
					}
					
					if (sum <= epsilon) {
						group.add(temporalList.get(i));
					}
				}

				for (S s : group) {
					
					int value = (int) s.getAttribute(PreferenceDistance.KEY);
					
					s.setAttribute(PreferenceDistance.KEY, value+population.size());
					
					nextPopulation.add(s);
					
					temporalList.remove(s);
				}
			}
			
			return nextPopulation;
		}
	}
	
	public static class DistanceToRPComparator<S extends Solution<?>> implements Comparator<S> {
		
		private int rpIndex;
		
		public final static String KEY = "distance_to_rp_";

		public DistanceToRPComparator(int rpIndex) {
			this.rpIndex = rpIndex;
		}

		@Override
		public int compare(S o1, S o2) {

			double v1 = (double) o1.getAttribute(KEY + rpIndex);
			double v2 = (double) o2.getAttribute(KEY + rpIndex);

			return Double.compare(v1, v2);
		}
	}
	
	public static class PreferenceDistanceComparator<S extends Solution<?>> implements Comparator<S> {

		@Override
		public int compare(S solution1, S solution2) {

			int result;

			if (solution1 == null) {
				if (solution2 == null) {
					result = 0;
				} else {
					result = -1;
				}
			} else if (solution2 == null) {
				result = 1;
			} else {
				
				double distance1 = Double.MIN_VALUE;
				double distance2 = Double.MIN_VALUE;

				if (solution1.getAttribute(PreferenceDistance.KEY) != null) {
					distance1 = (int) solution1.getAttribute(PreferenceDistance.KEY);
				}

				if (solution2.getAttribute(PreferenceDistance.KEY) != null) {
					distance2 = (int) solution2.getAttribute(PreferenceDistance.KEY);
				}

				if (distance1 > distance2) {
					result = 1;
				} else if (distance1 < distance2) {
					result = -1;
				} else {
					result = 0;
				}
			}

			return result;
		}

	}
	
	public static class PreferenceDistance<S extends Solution<?>> extends GenericSolutionAttribute<S, Double> implements DensityEstimator<S> {

		private static final long serialVersionUID = -7384332658172205041L;
		
		public static final String KEY = "preference_distance";
		
		private List<PointSolution> referencePoints;
		
		private double[] fmin;
		
		private double[] fmax;
		
		public PreferenceDistance(List<PointSolution> referencePoints, double[] fmin, double[] fmax) {
			
			Preconditions.checkNotNull(fmin, "The fmin should not be null");
			Preconditions.checkNotNull(fmin, "The fmax should not be null");
			Preconditions.checkNotNull(referencePoints, "The reference point list should not be null");
			Preconditions.checkArgument(!referencePoints.isEmpty(), "The reference point list should not be empty");
			Preconditions.checkArgument(fmin.length >= 2, "The fmin length should be >= 2");
			Preconditions.checkArgument(fmax.length >= 2, "The fmax length should be >= 2");
			
			this.referencePoints = referencePoints;
			this.fmin = fmin;
			this.fmax = fmax;
		}

		@Override
		public void computeDensityEstimator(List<S> solutionSet) {
			
			Preconditions.checkNotNull(solutionSet, "The solution set should not be null");
			Preconditions.checkArgument(!solutionSet.isEmpty(), "The solution set should not be empty");
			
			for (int i = 0; i < referencePoints.size(); i++) {

				PointSolution r = referencePoints.get(i);

				for (S s : solutionSet) {

					PointSolution x = PointSolutionUtils.createSolution(s.getObjectives());
					
					double distance = EuclideanDistanceUtils.calculate(x, r, fmin, fmax);
					
					s.setAttribute(DistanceToRPComparator.KEY + i, distance);
				}
				
				Collections.sort(solutionSet, new DistanceToRPComparator<S>(i));
				
				Map<Double, Integer> positions = new HashMap<>();
				
				int rankingPos = 1;
				
				for (S s : solutionSet) {

					double  value = (double) s.getAttribute(DistanceToRPComparator.KEY + i);
					
					if(!positions.containsKey(value)) {
						positions.put(value, rankingPos++);
					}
				}
				
				for (S s : solutionSet) {
					
					double value = (double) s.getAttribute(DistanceToRPComparator.KEY + i);
					
					int pos = positions.get(value);
					
					s.setAttribute("ranking_to_rp_" + i, pos);
				}
			}
			
			for (S s : solutionSet) {
				
				int minPos = Integer.MAX_VALUE;
				
				for (int i = 0; i < referencePoints.size(); i++) {
					
					int pos = (int) s.getAttribute("ranking_to_rp_" + i);
					
					if(pos < minPos) {
						minPos = pos;
					}
				}

				s.setAttribute(KEY, minPos);
			}
		}
		
	}
	
	public static class PointSolutionUtils {

		public static PointSolution createSolution(double... objectives) {

			Preconditions.checkArgument(objectives.length > 0, "the objective array should be > 0");

			PointSolution solution = new PointSolution(objectives.length);

			for (int i = 0; i < objectives.length; i++) {
				solution.setObjective(i, objectives[i]);
			}

			return solution;
		}

		public static <S extends Solution<?>> List<PointSolution> convert(List<S> population) {
			
			Preconditions.checkNotNull(population, "The population should not be null");
			
			List<PointSolution> points = new ArrayList<>();

			for (S s : population) {
				points.add(createSolution(s.getObjectives()));
			}

			return points;
		}
		
		public static PointSolution shift(PointSolution s, PointSolution vector) {

			PointSolution newPointSolution = s.copy();

			for (int i = 0; i < newPointSolution.getNumberOfObjectives(); i++) {
				newPointSolution.setObjective(i, s.getObjective(i) + vector.getObjective(i));
			}

			return newPointSolution;
		}
		
		public static List<PointSolution> copy(List<PointSolution> solutions) {

			List<PointSolution> list = new LinkedList<>();

			for (PointSolution solution : solutions) {
				list.add(solution.copy());
			}

			return list;
		}
	}
	
	private static class EuclideanDistanceUtils {
		
		private EuclideanDistanceUtils() throws InstantiationException {
			throw new InstantiationException("Instances of this type are forbidden.");
		}
		
		public static double calculate(PointSolution x, PointSolution r, double[] fmin, double[] fmax) {

			Preconditions.checkNotNull(x, "The solution x should not be null");
			Preconditions.checkNotNull(r, "The solution x should not be null");
			Preconditions.checkArgument(x.getNumberOfObjectives() == r.getNumberOfObjectives(), "The x and r points should be the same number of objectives. Found"+x.getNumberOfObjectives()+" and "+r.getNumberOfObjectives());
			Preconditions.checkArgument(fmin.length > 0, "The fmin length should be > 0");
			Preconditions.checkArgument(fmax.length > 0, "The fmax length should be > 0");
			Preconditions.checkArgument(fmin.length == x.getNumberOfObjectives(), "The fmin should have the same size of x");
			Preconditions.checkArgument(fmax.length == x.getNumberOfObjectives(), "The fmax should have the same size of x");
			
			int nObj = x.getNumberOfObjectives();

			double sum = 0.0;

			for (int i = 0; i < nObj; i++) {
				sum += Math.pow((x.getObjective(i) - r.getObjective(i)) / (fmax[i] - fmin[i]), 2);
			}

			return Math.sqrt(sum);
		}	
	}
}
