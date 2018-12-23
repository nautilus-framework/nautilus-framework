package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class ImplicitFeedbackObjectiveReducer extends AbstractReducer {

	private double alpha = 0.5;
	
	private double epsilon = 0.1;
	
	@Override
	public String getName() {
		return "By Implicit Feedback";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data, 
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<NSolution<?>> solutions) {
		
		List<NSolution<?>> visualizedSolutions = SolutionListUtils.getVisualizedSolutions(solutions);
		
		double[] rankingForUserSelection = new double[selectedObjectives.size()];

		for (Solution<?> solution : visualizedSolutions) {
			
			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserSelection[i] += solution.getObjective(i);
			}
		}
		
		rankingForUserSelection = mean(rankingForUserSelection, visualizedSolutions.size());

		double[] rankingForUserFeedback = new double[selectedObjectives.size()];

		for (NSolution<?> solution : visualizedSolutions) {

			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserFeedback[i] += solution.getObjective(i) * solution.getUserFeedback();
			}
		}
		
		rankingForUserFeedback = mean(rankingForUserFeedback, visualizedSolutions.size());
		
		double[] general = new double[selectedObjectives.size()];
		
		for (int i = 0; i < selectedObjectives.size(); i++) {
			general[i] = alpha * rankingForUserSelection[i] + (1.0 - alpha) * rankingForUserFeedback[i];
		}
		
		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < general.length; i++) {
			rankings.add(new RankingItem(selectedObjectives.get(i).getId(), general[i]));
		}
		
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		
		rankings.get(0).selected = true;
		
		double minRanking = rankings.get(0).getValue();
		
		for (RankingItem ranking : rankings) {
			
			if (minRanking - ranking.getValue() <= epsilon) {
				ranking.selected = true;
				minRanking = ranking.getValue();
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
