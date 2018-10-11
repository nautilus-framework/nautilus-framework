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

				double index = 0;
				
				for (Solution solution : solutions) {
					
					Solution copy = solution.copy();
					
					copy.getVariables().add(variable);
					
					double x = objectives.get(i).evaluate(data, solution);
					double y = objectives.get(i).evaluate(data, copy);
					//x = index++;
					
					
					x = Normalizer.normalize(x, objectives.get(i).getMinimumValue(), objectives.get(i).getMaximumValue());
					y = Normalizer.normalize(y, objectives.get(i).getMinimumValue(), objectives.get(i).getMaximumValue());
					
					y = x - y;
					
					xs.add(index++);
					ys.add(y);
				}
				
				if(variable.getValue().equalsIgnoreCase("1")) {
					System.out.println(objectives.get(i).getName()+" - "+variable.getValue());
					System.out.println(xs);
					System.out.println(ys);
					System.out.println("-------");
				}
				
				double[] x = Converter.toDoubleArray(xs);
				double[] y = Converter.toDoubleArray(ys);

				item.getValues().add(getCorrelation(x, y));
			}
			
			items.add(item);
		}
		
		return items;
	}
	
	public Set<String> mapToVariabless(List<Solution> solutions) {

		Set<String> map = new HashSet<>();

		for (Solution s : solutions) {

			for (Variable v : s.getVariables()) {

//				if (!map.containsKey(v.getValue())) {
//					map.put(v.getValue(), index++);
//				}
			}
		}

		return map;
	}
	
	

	public abstract List<String> correlatVariables(List<AbstractObjective> objectives, List<Solution> solutions);
}
