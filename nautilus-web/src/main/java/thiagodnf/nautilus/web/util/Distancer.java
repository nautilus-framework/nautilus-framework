package thiagodnf.nautilus.web.util;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.web.model.Solution;

public class Distancer {
	
	public static double getEuclideanDistance(List<Double> s1, List<Double> s2) {

		double sum = 0.0;

		for (int i = 0; i < s1.size(); i++) {
			sum += Math.pow(s1.get(i) - s2.get(i), 2);
		}

		return Math.sqrt(sum);
	}

	public static double getDistance(Solution s, List<Solution> solutions) {
		
		List<Solution> selectedSolutions = new ArrayList<>();
		
		for(Solution sol : solutions) {
			
			if(sol.getProperties().containsKey("selected")) {
				
				selectedSolutions.add(sol);
			}
		}
		
		double sumDistance = 0.0;
		
		for(Solution selected : selectedSolutions) {
			
			List<Double> objectives = new ArrayList<>(selected.getObjectives());
			
			double feedback = selected.getUserFeeback();
			
			if(feedback < 0) {
				
				for (int i = 0; i < selected.getNumberOfObjectives(); i++) {
				
					objectives.set(i, - objectives.get(i));
				}
			}
			
			sumDistance += getEuclideanDistance(s.getObjectives(), objectives);
		}
		
		return sumDistance;
	}
}
