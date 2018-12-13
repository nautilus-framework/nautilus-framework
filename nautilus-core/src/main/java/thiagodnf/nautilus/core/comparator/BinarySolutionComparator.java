package thiagodnf.nautilus.core.comparator;

import java.util.Comparator;

import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.GenericSolution;

public class BinarySolutionComparator implements Comparator<GenericSolution>{

	@Override
	public int compare(GenericSolution s1, GenericSolution s2) {

		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return 0;
		}

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return 0;
		}

		for (int i = 0; i < s1.getNumberOfVariables(); i++) {

			BinarySet binarySet1 = (BinarySet) s1.getVariables().get(i);
			BinarySet binarySet2 = (BinarySet) s2.getVariables().get(i);

			if (!binarySet1.equals(binarySet2)) {
				return 0;
			}
		}

		return 1;
	}
}
