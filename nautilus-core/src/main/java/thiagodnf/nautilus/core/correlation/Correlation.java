package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class Correlation {
	
	public static class CorrelationItem {

		private String name;
		
		private List<Double> values;

		public CorrelationItem() {
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
	
	public abstract List<CorrelationItem> correlateObjectives(List<AbstractObjective> objectives, List<Solution> solutions);

	public abstract List<String> correlatVariables(List<AbstractObjective> objectives, List<Solution> solutions);
}
