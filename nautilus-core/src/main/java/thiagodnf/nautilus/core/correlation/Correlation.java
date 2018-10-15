package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.util.Converter;

public abstract class Correlation {
	
	public static class CorrelationItem {

		private String name;
		
		private List<Double> values;

		public CorrelationItem(int name) {
			this(String.valueOf(name));
		}
		
		public CorrelationItem(String name) {
			this.name = name;
			this.values = new ArrayList<>();
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

	public String getKey() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract String getName() ;
	
	public abstract double getCorrelation(final double[] x, final double[] y);
}
