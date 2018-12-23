package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.correlation.AbstractCorrelation.CorrelationItem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class VariableBasedReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Variable-based Reducer";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<NSolution<?>> solutions) {
		
		int min = 1;
		int max = 100;
		
		if (problem instanceof IntegerProblem) {
			min = ((IntegerProblem) problem).getLowerBound(0);
			max = ((IntegerProblem) problem).getUpperBound(0);
		} else if (problem instanceof BinaryProblem) {
			min = 0;
			max = ((BinaryProblem) problem).getNumberOfBits(0) - 1;
		}
		
		List<CorrelationItem> correlations = correlateVariables(data, selectedObjectives, solutions, min, max);
		
		List<CorrelationItem> normalized = normalize(correlations, selectedObjectives);
		
		Map<String, CorrelationItem> map = new HashMap<>();

		for (CorrelationItem item : normalized) {
			map.put(item.getName(), item);
		}
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions); 
		
		double r[] = new double[selectedObjectives.size()];
		
		for (NSolution<?> selectedSolution : selectedSolutions) {

			double feedback = selectedSolution.getUserFeedback();
			
			List<String> variables = SolutionUtils.getVariablesAsList(selectedSolution);
			
			for (String variable : variables) {
				
				CorrelationItem item = map.get(variable);

				for (int j = 0; j < r.length; j++) {

					double distance = 0.0;
					double minDistance = item.getValues().get(j);

					if (feedback == 0) {
						distance = minDistance;
					} else if (feedback > 0) {
						distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
					} else {
						distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
					}

					if (Double.isNaN(distance)) {
						distance = minDistance;
					}

					r[j] += distance;
				}
			}
		}
		
		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < r.length; i++) {
			rankings.add(new RankingItem(selectedObjectives.get(i).getId(), r[i]));
		}
		
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		
		rankings.get(0).selected = true;
		
		double sum = rankings.stream().map(e -> e.getValue()).reduce(Double::sum).get();
		double average = (double) sum / (double) rankings.size();
		
		for (RankingItem ranking : rankings) {
			
			if (ranking.getValue() >= average) {
				ranking.selected = true;
			}
		}
				
		return rankings;
	}
	
	public List<CorrelationItem> correlateVariables(InstanceData data, List<AbstractObjective> objectives, List<NSolution<?>> solutions, int min, int max){
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();
		
		for (int i = min; i <= max; i++) {

			CorrelationItem item = new CorrelationItem(i);

			for (int j = 0; j < objectives.size(); j++) {
				item.getValues().add(calculateSolution(i, data, objectives.get(j), solutions));
			}

			items.add(item);
		}
		
		return items;
	}
	
	public double calculateSolution(int value, InstanceData data, AbstractObjective objective, List<NSolution<?>> solutions) {
		
		double sum = 0.0;
		
		for (NSolution<?> solution : solutions) {

			NSolution<?> orginalSolution = solution;
			NSolution<?> shiftedSolution = null;

			if (solution instanceof NIntegerSolution) {
				shiftedSolution = shift(value, (NIntegerSolution) orginalSolution);
			} else if (solution instanceof NBinarySolution) {
				shiftedSolution = shift(value, (NBinarySolution) orginalSolution);
			}
			
			double originalValue = objective.evaluate(data, orginalSolution);
			double newValue = objective.evaluate(data, shiftedSolution);

			originalValue = Normalizer.normalize(originalValue, objective.getMinimumValue(), objective.getMaximumValue());
			newValue = Normalizer.normalize(newValue, objective.getMinimumValue(), objective.getMaximumValue());

			double diff = (originalValue - newValue);

			if (originalValue == 0) {
				sum += diff;
			} else {
				sum += diff / originalValue;
			}
		}

		return (double) sum / (double) solutions.size();
	}
	
	public NSolution<?> shift(int value, NIntegerSolution sol) {

		NIntegerSolution solution = (NIntegerSolution) sol.copy();

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (solution.getVariableValue(i) != value) {
				solution.setVariableValue(i, value);
				break;
			}
		}

		return solution;
	}
	
	public NSolution<?> shift(int value, NBinarySolution sol) {

		NBinarySolution solution = (NBinarySolution) sol.copy();

		solution.getVariableValue(0).set(value);

		return solution;
	}
	
	public List<CorrelationItem> normalize(List<CorrelationItem> correlationItems, List<AbstractObjective> objectives){
		
		for (int i = 0; i < objectives.size(); i++) {
			
			double minValue = Double.MAX_VALUE;
			double maxValue = Double.MIN_VALUE;
			
			for (CorrelationItem item : correlationItems) {

				double value = item.getValues().get(i);

				if (value < minValue) {
					minValue = value;
				}
				if (value > maxValue) {
					maxValue = value;
				}
			}
			
			for (CorrelationItem item : correlationItems) {
				
				double value = item.getValues().get(i);
				
				if(objectives.get(i).isMaximize()) {
					item.getValues().set(i, Normalizer.normalize(value, -1.0, 1.0, minValue, maxValue));
				}else {
					item.getValues().set(i, Normalizer.normalize(value, -1.0, 1.0, maxValue, minValue));
				}				
			}
		}
		
		return correlationItems;
	}
}
