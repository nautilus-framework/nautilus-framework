package thiagodnf.nautilus.core.distance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardD {

	public static double calculate(List<String> set1, List<String> set2) {

		if (set1.isEmpty() && set2.isEmpty()) {
			return 1.0;
		}
		
		Set<String> unionXY = new HashSet<>(set1);
        unionXY.addAll(set2);

        Set<String> intersectionXY = new HashSet<>(set1);
        intersectionXY.retainAll(set2);
        
        return (double) intersectionXY.size() / (double) unionXY.size();

	}
}
