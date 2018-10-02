package thiagodnf.nautilus.core.correlation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import thiagodnf.nautilus.core.model.Correlation;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;

public class PearsonCorrelation {
	
	public Map<String, Integer> mapToVariables(List<Solution> solutions) {

		int index = 1;

		Map<String, Integer> map = new HashMap<>();

		for (Solution s : solutions) {

			for (Variable v : s.getVariables()) {

				if (!map.containsKey(v.getValue())) {
					map.put(v.getValue(), index++);
				}
			}
		}

		return map;
	}
	
	public double count(String variable, Solution s) {

		double counter = 0;

		for (Variable v : s.getVariables()) {

			if (v.getValue().equalsIgnoreCase(variable)) {
				counter++;
			}
		}

		return counter;
	}
	
	public List<Correlation> calculate(int numberOfObjectives, List<Solution> solutions) {
		
		List<Correlation> correlations = new ArrayList<>();
		
		Map<String, Integer> map = mapToVariables(solutions);
		
		
		for(Entry<String, Integer> entry : map.entrySet()) {
			
			Correlation c = new Correlation();
			
			c.setVariable(entry.getKey());
			
			for (int i = 0; i < numberOfObjectives; i++) {

				List<Double> xValues = new ArrayList<>();
				List<Double> yValues = new ArrayList<>();

				String v = entry.getKey();

				for (Solution s : solutions) {
					
					xValues.add(s.getObjective(i));
					yValues.add(count(v, s));
				}

				double[] x = xValues.stream().mapToDouble(el -> el).toArray();
				double[] y = yValues.stream().mapToDouble(el -> el).toArray();

				PearsonsCorrelation pc = new PearsonsCorrelation();

				double result = pc.correlation(x, y);
				
				c.getValues().add(result);
//				System.out.println(result);
			}
			
			correlations.add(c);
		}
		
		return correlations;
	}

}
