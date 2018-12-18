package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class AlphaReducer extends AbstractReducer {

	private double alpha = 0.5;
	
	private double range = 0.1;
	
	@Override
	public String getName() {
		return "Alpha Reducer";
	}

	@Override
	public List<Ranking> execute(List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<Solution<?>> solutions) {
		
		List<Solution<?>> selectedSolutions = SolutionListUtils.getVisualizedSolutions(solutions);
		
		double[] rankingForUserSelection = new double[selectedObjectives.size()];

		for (Solution<?> solution : selectedSolutions) {
			
			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserSelection[i] += solution.getObjective(i);
			}
		}
		
		rankingForUserSelection = mean(rankingForUserSelection, selectedSolutions.size());

		double[] rankingForUserFeedback = new double[selectedObjectives.size()];

		for (Solution<?> solution : selectedSolutions) {

			double normalizedValue = SolutionUtils.getUserFeedback((GenericSolution) solution);

			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				rankingForUserFeedback[i] += solution.getObjective(i) * normalizedValue;
			}
		}
		
		rankingForUserFeedback = mean(rankingForUserFeedback, selectedSolutions.size());
		
		double[] general = new double[selectedObjectives.size()];
		
		for (int i = 0; i < selectedObjectives.size(); i++) {
			general[i] = alpha * rankingForUserSelection[i] + (1.0 - alpha) * rankingForUserFeedback[i];
		}
		
		List<Ranking> rankings = new ArrayList<>();
		
		for (int i = 0; i < general.length; i++) {
			rankings.add(new Ranking(selectedObjectives.get(i).getId(), general[i]));
		}
		
		Collections.sort(rankings, Comparator.comparing(Ranking::getValue).reversed());
		
		rankings.get(0).selected = true;
		
		double minRanking = rankings.get(0).getValue();
		
		for (Ranking ranking : rankings) {
			
			if (minRanking - ranking.getValue() <= range) {
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
