package thiagodnf.nautilus.core.reduce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reducer.AbstractReducer.RankingItem;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class ObjectiveBasedReducer extends AbstractReduce{

	@Override
	public List<RankingItem> execute(Problem<?> problem, 
			InstanceData data, 
			List<AbstractObjective> allObjectives,
			List<AbstractObjective> selectedObjectives, 
			List<NSolution<?>> solutions) {
		
		
		List<NSolution<?>> visualizedSolutions = SolutionListUtils.getVisualizedSolutions(solutions);
		
		
		
		
		return null;
	}
	
	public List<String> execute(Problem<?> problem, List<? extends NSolution<?>> population){
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));

		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(population);
		
		int numberOfObjectives = selectedSolutions.get(0).getNumberOfObjectives();
		
		double[] preferences = new double[selectedSolutions.size()];

		for (int i = 0; i < selectedSolutions.size(); i++) {
			preferences[i] = SolutionUtils.getUserFeedback(selectedSolutions.get(i));
		}
		
		double sumOfPreferences = Arrays.stream(preferences).sum();
		
		System.out.println(toStringObjectives(optimizedObjectives));
		for(NSolution<?> solution : selectedSolutions) {
			System.out.println(toString(solution));
		}
		
		double[][] matrix = new double[selectedSolutions.size()][numberOfObjectives];
		
		for (int i = 0; i < selectedSolutions.size(); i++) {
			matrix[i] = selectedSolutions.get(i).getObjectives();
		}
		
		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] *= preferences[i];
			}
		}
		
		double[] sums = new double[numberOfObjectives];
		
		for (int j = 0; j < numberOfObjectives; j++) {

			double sum = 0.0;

			for (int i = 0; i < matrix.length; i++) {
				sum += matrix[i][j];
			}

			sums[j] = sum;
		}
		
		
		// Divide
		
		for(int i=0;i<sums.length;i++) {
			sums[i] = sums[i]/sumOfPreferences;
		}
		
		System.out.println(Arrays.toString(preferences));
		System.out.println(Arrays.toString(sums));
		
		
		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < sums.length; i++) {
			rankings.add(new RankingItem(optimizedObjectives.get(i), sums[i]));
		}
		
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		
		rankings.get(0).setSelected(true);
		
		//byAverage(rankings);
		byEpsilon(rankings);
		
		List<String> nextObjectiveIds = new ArrayList<>();
		
		for (RankingItem item : rankings) {
			
			System.out.println(item);
			
			if (item.isSelected()) {
				nextObjectiveIds.add(item.getObjectiveId());
			}
		}
		
		return nextObjectiveIds;
	}
	
	public void byEpsilon(List<RankingItem> items) {
		
		double epsion = 0.1;
		double lastValue = items.get(0).value;
		
		// Remove the already selected objective;
		
		items = items.stream().filter(e -> !e.isSelected()).collect(Collectors.toList());

		for (RankingItem item : items) {

			double currentValue = item.value;

			double diff = Math.abs(currentValue - lastValue);
			
			if (diff <= epsion) {
				item.setSelected(true);
				lastValue = currentValue;
			}
		}
	}
	
	public void byAverage(List<RankingItem> items) {
		
		// Remove the already selected objective;
		
		items = items.stream().filter(e -> !e.isSelected()).collect(Collectors.toList());
		
		double sum = items.stream().map(e -> e.getValue()).reduce(Double::sum).get();
		double average = (double) sum / (double) items.size();
		
		for (RankingItem item : items) {
			
			if (item.getValue() >= average) {
				item.setSelected(true);
			}
		}
	}
	
	
	public String toStringObjectives(List<String> objectives) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("feedback,");
		
		for(int i=0;i<objectives.size();i++) {
			builder.append(objectives.get(i));
			
			if(i+1 != objectives.size()) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
	
	public String toString(NSolution<?> solution) {
		
		double[] objectives = solution.getObjectives();
		
		StringBuilder builder = new StringBuilder();
		
		double feedback = (double) solution.getAttribute(SolutionAttribute.FEEDBACK);
		
		builder.append(Converter.round(feedback, 2)).append(",");
		
		for (int i = 0; i < objectives.length; i++) {
			
			builder.append(Converter.round(objectives[i], 2));
			
			if(i+1 != objectives.length) {
				builder.append(",");
			}
		}

		return builder.toString();
	}
			

	@Override
	public String getName() {
		return "Objective Based Reducer";
	}

}
