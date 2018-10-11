package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

public abstract class Correlation {
	
	public static class CorrelationItem {

		private String name;
		
		private List<Double> values;

		public CorrelationItem(String name) {
			this.name = name;
			this.values = new ArrayList<>();
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Double> getValues() {
			return values;
		}

		public void setValues(List<Double> values) {
			this.values = values;
		}
	}

	public String getKey() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract String getName() ;
	
	public abstract double getCorrelation(final double[] x, final double[] y);
	
	public List<CorrelationItem> correlateObjectives(List<AbstractObjective> objectives, List<Solution> solutions) {
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();

		for (int i = 0; i < objectives.size(); i++) {

			CorrelationItem item = new CorrelationItem(objectives.get(i).getName());

			for (int j = 0; j < objectives.size(); j++) {

				List<Double> xs = new ArrayList<>();
				List<Double> ys = new ArrayList<>();

				for (Solution s : solutions) {
					xs.add(s.getObjective(i));
					ys.add(s.getObjective(j));
				}

				double[] x = Converter.toDoubleArray(xs);
				double[] y = Converter.toDoubleArray(ys);

				item.getValues().add(getCorrelation(x, y));
			}

			items.add(item);
		}

		return items;
	}
	
	public List<CorrelationItem> correlateVariables(Normalize normalizer, InstanceData data, List<Variable> variables, List<AbstractObjective> objectives, List<Solution> solutions){
		
		Preconditions.checkNotNull(solutions, "The solution list should not be null");
		Preconditions.checkNotNull(objectives, "The objective list should not be null");
		
		List<CorrelationItem> items = new ArrayList<>();
		
		for (Variable variable : variables) {

			CorrelationItem item = new CorrelationItem(variable.getValue());

			for (int i = 0; i < objectives.size(); i++) {

				List<Double> xs = new ArrayList<>();
				List<Double> ys = new ArrayList<>();

				for (int j = 1; j <= 10; j++) {
					xs.add(Double.valueOf(j));
					ys.add(teste(objectives.get(i),data,solutions, variable, j));
				}
				
				double[] x = Converter.toDoubleArray(xs);
				double[] y = Converter.toDoubleArray(ys);

				item.getValues().add(getCorrelation(x, y));
			}
			
			items.add(item);
		}
		
		return items;
	}
	
	public double teste(AbstractObjective objective, InstanceData data, List<Solution> solutions, Variable variable, int size) {
		
		double sum = 0.0;

		for (Solution solution : solutions) {

			Solution copy = solution.copy();

			for (int i = 0; i < size; i++) {
				copy.getVariables().add(variable);
			}
			
			double value = objective.evaluate(data, copy);

			sum += Normalizer.normalize(value, objective.getMinimumValue(), objective.getMaximumValue());
		}

		return sum;
	}
	
	public abstract List<String> correlatVariables(List<AbstractObjective> objectives, List<Solution> solutions);
}
