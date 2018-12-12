package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.Correlation.CorrelationItem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.problem.BinaryProblem;
import thiagodnf.nautilus.core.problem.IntegerProblem;
import thiagodnf.nautilus.core.solution.BinarySolution;
import thiagodnf.nautilus.core.solution.IntegerSolution;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

@Service
public class CorrelationService {

	public List<CorrelationItem> correlateObjectives(Correlation correlation, List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();

		for (int i = 0; i < objectives.size(); i++) {

			CorrelationItem item = new CorrelationItem(objectives.get(i).getName());

			for (int j = 0; j < objectives.size(); j++) {

				double[] x = new double[solutions.size()];
				double[] y = new double[solutions.size()];

				for (int k = 0; k < solutions.size(); k++) {
					x[k] = solutions.get(k).getObjective(i);
					y[k] = solutions.get(k).getObjective(j);
				}

				item.getValues().add(correlation.getCorrelation(x, y));
			}

			items.add(item);
		}

		return items;
	}

	public List<CorrelationItem> correlateVariables(Problem<?> p, InstanceData data, List<AbstractObjective> objectives, List<? extends Solution<?>> solutions){
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();
		
		if (p instanceof IntegerProblem) {

			IntegerProblem problem = (IntegerProblem) p;

			int min = problem.getLowerBound(0);
			int max = problem.getUpperBound(0);
			
			for (int i = min; i <= max; i++) {

				CorrelationItem item = new CorrelationItem(i);

				for (int j = 0; j < objectives.size(); j++) {
					item.getValues().add(calculateForIntegerSolution(p, data, objectives.get(j), solutions, i));
				}

				items.add(item);
			}
		}else if (p instanceof BinaryProblem) {
			
			BinaryProblem problem = (BinaryProblem) p;
			
			int min = 0;
			int max = problem.getNumberOfBits(0) - 1;
		
			for (int i = min; i <= max; i++) {

				CorrelationItem item = new CorrelationItem(i);

				for (int j = 0; j < objectives.size(); j++) {
					item.getValues().add(calculateForBinary(p, data, objectives.get(j), solutions, i));
				}

				items.add(item);
			}
		}
		
		return items;
	}
	
	public double calculateForIntegerSolution(Problem problem, InstanceData data, AbstractObjective objective, List<? extends Solution<?>> solutions, int value) {
		
		double sum = 0.0;
		
		for (Solution solution : solutions) {

			IntegerSolution sol = (IntegerSolution) solution;
			
			double originalValue = objective.evaluate(data, sol);
			
			for (int i = 0; i < sol.getNumberOfVariables(); i++) {

				if (sol.getVariableValue(i) != value) {
					sol.setVariableValue(i, value);
					break;
				}
			}

			double newValue = objective.evaluate(data, sol);

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
	
	public double calculateForBinary(Problem problem, InstanceData data, AbstractObjective objective, List<? extends Solution<?>> solutions, int pos) {
		
		double sum = 0.0;

		for (Solution solution : solutions) {

			BinarySolution sol = (BinarySolution) solution;
			
			double originalValue = objective.evaluate(data, sol);
			
			sol.getVariableValue(0).set(pos);
			
			double newValue = objective.evaluate(data, sol);
			
			originalValue = Normalizer.normalize(originalValue, objective.getMinimumValue(), objective.getMaximumValue());
			newValue = Normalizer.normalize(newValue, objective.getMinimumValue(), objective.getMaximumValue());
			
			double diff = (newValue - originalValue);

			if (originalValue == 0) {
				sum += diff;
			} else {
				sum += diff / originalValue;
			}
		}

		return (double) sum / (double) solutions.size();
	}
	
	public List<CorrelationItem> normalize(List<CorrelationItem> correlationItems, List<AbstractObjective> objectives){
		
		List<CorrelationItem> normalized = new ArrayList<>();

		for (CorrelationItem item : correlationItems) {
			normalized.add(item.copy());
		}
		
		for (int i = 0; i < objectives.size(); i++) {
			
			double minValue = Double.MAX_VALUE;
			double maxValue = Double.MIN_VALUE;
			
			for (CorrelationItem item : normalized) {

				double value = item.getValues().get(i);

				if (value < minValue) {
					minValue = value;
				}
				if (value > maxValue) {
					maxValue = value;
				}
			}
			
			for (CorrelationItem item : normalized) {
				
				double value = item.getValues().get(i);
				
				if(objectives.get(i).isMaximize()) {
					item.getValues().set(i, Normalizer.normalize(value, -1.0, 1.0, minValue, maxValue));
				}else {
					item.getValues().set(i, Normalizer.normalize(value, -1.0, 1.0, maxValue, minValue));
				}				
			}
		}
		
		return normalized;
	}
}
