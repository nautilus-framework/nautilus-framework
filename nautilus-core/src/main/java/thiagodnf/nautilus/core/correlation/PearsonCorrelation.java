package thiagodnf.nautilus.core.correlation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;

public class PearsonCorrelation extends Correlation{
	
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
	
//	public double count(String variable, Solution s) {
//
//		double counter = 0;
//
//		for (Variable v : s.getVariables()) {
//
//			if (v.getValue().equalsIgnoreCase(variable)) {
//				counter++;
//			}
//		}
//
//		return counter;
//	}
	
//	public double count(CorrelationItem item, String searchFor) {
//
//		double counter = 0;
////
////		for (String v : item.getVariables()) {
////
////			if (v.equalsIgnoreCase(searchFor)) {
////				counter++;
////			}
////		}
////
//		return counter;
//	}
	
//	public List<Correlation> calculate(Map<String, Integer> map, List<CorrelationItem> items, int numberOfObjectives, List<Solution> solutions) {
//		
//		List<Correlation> correlations = new ArrayList<>();
//		
////		//Map<String, Integer> map = mapToVariables(solutions);
////		
////		
////		for(Entry<String, Integer> entry : map.entrySet()) {
////			
////			Correlation c = new Correlation();
////			
////			c.setVariable(entry.getKey());
////			
////			System.out.println(entry.getKey());
////			for (int i = 0; i < numberOfObjectives; i++) {
////
////				List<Double> xValues = new ArrayList<>();
////				List<Double> yValues = new ArrayList<>();
////
////				String v = entry.getKey();
////
////				for (CorrelationItem item : items) {
////					
////					xValues.add(item.getObjectives().get(i));
////					yValues.add(count(item, v));
////				}
////				
////				System.out.println(xValues);
////				System.out.println(yValues);
////				System.out.println("-------");
////
////				double[] x = xValues.stream().mapToDouble(el -> el).toArray();
////				double[] y = yValues.stream().mapToDouble(el -> el).toArray();
////
//////				PearsonsCorrelation pc = new PearsonsCorrelation();
////				SpearmansCorrelation pc = new SpearmansCorrelation();
////				
////				double result = pc.correlation(x, y);
////				
////				c.getValues().add(result);
////				System.out.println(result);
////			}
////			
////			correlations.add(c);
////		}
////		
//		return correlations;
//	}

	@Override
	public String getName() {
		return "Pearson's Correlation";
	}

	@Override
	public double getCorrelation(double[] x, double[] y) {

		PearsonsCorrelation correlation = new PearsonsCorrelation();

		return correlation.correlation(x, y);
	}
}
