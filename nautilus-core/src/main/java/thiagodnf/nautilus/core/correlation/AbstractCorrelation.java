package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractCorrelation {
	
	public static class CorrelationItem {

		private String name;
		
		private List<Double> values;

		public CorrelationItem(String name, List<Double> values) {
			this.name = name;
			this.values = values;
		}
		
		public CorrelationItem(int name) {
			this(String.valueOf(name));
		}
		
		public CorrelationItem(String name) {
			this(name, new ArrayList<>());
		}
		
		public CorrelationItem(CorrelationItem item) {
			this.name = new String(item.getName());
			this.values = new ArrayList<>(item.getValues());
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
		
		public String toString() {
			return getName() + ";" + getValues();
		}

		public CorrelationItem copy() {
			return new CorrelationItem(this);
		}
	}

	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public List<CorrelationItem> execute(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		
		List<CorrelationItem> items = new ArrayList<>();
		
		if(!isEnabled()) {
			return null;
		}
		
		for (int i = 0; i < objectives.size(); i++) {

			CorrelationItem item = new CorrelationItem(objectives.get(i).getName());

			for (int j = 0; j < objectives.size(); j++) {

				double[] x = new double[solutions.size()];
				double[] y = new double[solutions.size()];

				for (int k = 0; k < solutions.size(); k++) {
					x[k] = solutions.get(k).getObjective(i);
					y[k] = solutions.get(k).getObjective(j);
				}
				
				double value = getCorrelation(x, y);

				if (Double.isNaN(value)) {
					value = 0.0;
				}

				item.getValues().add(value);
			}

			items.add(item);
		}
		
		return items;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public abstract String getName() ;
	
	public abstract double getCorrelation(final double[] x, final double[] y);	
}
