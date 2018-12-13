package thiagodnf.nautilus.core.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import thiagodnf.nautilus.core.model.GenericSolution;

public class IntegerSolutionComparator implements Comparator<GenericSolution>{

	@Override
	public int compare(GenericSolution s1, GenericSolution s2) {

		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return 0;
		}

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return 0;
		}

		Map<Integer, Integer> mapS1 = new HashMap<>();
		Map<Integer, Integer> mapS2 = new HashMap<>();
		
		for (int i = 0; i < s1.getNumberOfVariables(); i++) {

			Integer keyS1 = (Integer) s1.getVariables().get(i);
			Integer keyS2 = (Integer) s2.getVariables().get(i);

			if (!mapS1.containsKey(keyS1)) {
				mapS1.put(keyS1, 0);
			}

			if (!mapS2.containsKey(keyS2)) {
				mapS2.put(keyS2, 0);
			}

			int totalS1 = mapS1.get(keyS1);
			int totalS2 = mapS2.get(keyS2);

			mapS1.put(keyS1, ++totalS1);
			mapS2.put(keyS2, ++totalS2);
		}
		
		for(Entry<Integer, Integer> entry : mapS1.entrySet()){
			
			if(entry.getValue() != mapS2.get(entry.getKey())) {
				return 0;
			}
		}

		return 1;
	}
}
