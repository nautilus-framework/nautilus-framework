package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.problem.Problem;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.Correlation.CorrelationItem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.problem.AbstractProblem;
import thiagodnf.nautilus.core.problem.BinaryProblem;
import thiagodnf.nautilus.core.problem.IntegerProblem;
import thiagodnf.nautilus.core.solution.BinarySolution;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

@Service
public class CorrelationService {

	@Autowired
	private PluginService pluginService;
	
	public List<CorrelationItem> correlateObjectives(Correlation correlation, List<AbstractObjective> objectives, List<Solution> solutions) {
		
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

	public List<CorrelationItem> correlateVariables(AbstractProblem<?> p, InstanceData data, List<AbstractObjective> objectives, List<Solution> solutions){
		
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

					double s = testeInteiro(p, objectives.get(j), j, data, solutions, i);
					
					item.getValues().add(s);
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

					double s = testeBinary(p, objectives.get(j), j, data, solutions, i);
					
					item.getValues().add(s);
				}
				
				items.add(item);
			}
		}
		
		return items;
	}
	
	public double testeInteiro(Problem p, AbstractObjective objective, int objectiveId, InstanceData data, List<Solution> solutions, int value) {
		
		double sum = 0.0;
		
		for (Solution solution : solutions) {

			double originalValue  = solution.getObjective(objectiveId);
			
			originalValue = Normalizer.normalize(originalValue, objective.getMinimumValue(), objective.getMaximumValue());
			
			Solution copy = solution.copy();

			for (int i = 0; i < copy.getVariables().size(); i++) {
				
				if (!copy.getVariables().get(i).getValue().equalsIgnoreCase(String.valueOf(value))) {
					copy.getVariables().set(i, new Variable(value));
					break;
				}
			}
			
			org.uma.jmetal.solution.Solution<?> sJmetal = Converter.toJMetalSolution(copy);

			double newValue = objective.evaluate(p, sJmetal);

			newValue = Normalizer.normalize(newValue, objective.getMinimumValue(), objective.getMaximumValue());

			double diff = (originalValue - newValue);

			double result = 0.0;

			if (originalValue == 0) {
				result = diff;
			} else {
				result = diff / originalValue;
			}

			sum += result;
		}

		return (double) sum / (double) solutions.size();
	}
	
	public double testeBinary(Problem p, AbstractObjective objective, int objectiveId, InstanceData data, List<Solution> solutions, int pos) {
		
		double sum = 0.0;

		for (Solution solution : solutions) {

			double originalValue  = solution.getObjective(objectiveId);
			
			originalValue = Normalizer.normalize(originalValue, objective.getMinimumValue(), objective.getMaximumValue());
			
			Solution copy = solution.copy();

			org.uma.jmetal.solution.Solution<?> sJmetal = Converter.toJMetalSolution(copy);

			BinarySolution bin = (BinarySolution) sJmetal;
			
			bin.getVariableValue(0).set(pos);
			
			double newValue = objective.evaluate(p, sJmetal);
			
			newValue = Normalizer.normalize(newValue, objective.getMinimumValue(), objective.getMaximumValue());
			
			double diff = (newValue-originalValue);
			
			double result = 0.0;

			if (originalValue == 0) {
				result = diff;
			} else {
				result = diff / originalValue;
			}
			
			sum += result;
		}

		return (double) sum / (double) solutions.size();
	}
	
	public List<CorrelationItem> normalize(List<CorrelationItem> correlationItems){
		
		List<CorrelationItem> normalized = new ArrayList<>();

		for (CorrelationItem item : correlationItems) {
			normalized.add(item.copy());
		}
		
		int size = normalized.get(0).getValues().size();

		for (int i = 0; i < size; i++) {
			
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
				
				item.getValues().set(i, Normalizer.normalize(value, maxValue, minValue));
			}
		}
		
		return normalized;
	}
}
