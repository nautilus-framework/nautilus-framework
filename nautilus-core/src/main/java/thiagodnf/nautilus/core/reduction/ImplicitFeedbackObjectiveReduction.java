package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class ImplicitFeedbackObjectiveReduction extends AbstractReduction {

	private double alpha = 0.5;
	
	private double epsilon = 0.1;
	
	@Override
	public String getName() {
		return "By Implicit Feedback";
	}

	@Override
	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
		
		List<NSolution<?>> visualizedSolutions = SolutionListUtils.getVisualizedSolutions(population);
		
		double[] rankingForUserSelection = new double[optimizedObjectives.size()];

		for (Solution<?> solution : visualizedSolutions) {
			
			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserSelection[i] += (solution.getObjective(i)+1);
			}
		}
		
		rankingForUserSelection = mean(rankingForUserSelection, visualizedSolutions.size());

		double[] rankingForUserFeedback = new double[optimizedObjectives.size()];

		for (NSolution<?> solution : visualizedSolutions) {

			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserFeedback[i] += (solution.getObjective(i)+1) * solution.getUserFeedback();
			}
		}
		
		rankingForUserFeedback = mean(rankingForUserFeedback, visualizedSolutions.size());
		
		double[] general = new double[optimizedObjectives.size()];
		
		for (int i = 0; i < optimizedObjectives.size(); i++) {
			general[i] = alpha * rankingForUserSelection[i] + (1.0 - alpha) * rankingForUserFeedback[i];
		}
		
		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < general.length; i++) {
			rankings.add(new RankingItem(optimizedObjectives.get(i), general[i]));
		}
		
		double sum = rankings.stream().map(e -> e.getValue()).reduce(Double::sum).get();
		double average = (double) sum / (double) rankings.size();
		
		System.out.println("-------------------");
		System.out.println(Arrays.toString(rankingForUserSelection));
		System.out.println(Arrays.toString(rankingForUserFeedback));
		System.out.println("sum: "+sum);
		System.out.println("average: "+average);
		
//		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue));
		
		rankings.get(0).selected = true;
		
		//double minRanking = rankings.get(0).getValue();
		
		for (RankingItem ranking : rankings) {
			
//			if (minRanking - ranking.getValue() <= epsilon) {
//				ranking.selected = true;
//				minRanking = ranking.getValue();
//			}
			
			if (ranking.getValue() <= average) {
				ranking.selected = true;
			}
		}
		
		return rankings;
	}

	
	protected double[] mean(double[] array, double size) {
		
		if (size == 0) {
			return array;
		}

		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = 1.0 - array[i] / size;
		}

		return newArray;
	}	
}
