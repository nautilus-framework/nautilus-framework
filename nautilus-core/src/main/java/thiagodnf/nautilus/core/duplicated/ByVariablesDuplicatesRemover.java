package thiagodnf.nautilus.core.duplicated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Is;

public class ByVariablesDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "by Variables Values";
	}

	@Override
	public boolean equals(Solution<?> s1, Solution<?> s2) {

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}
		
		if (s1 instanceof GenericSolution && s2 instanceof GenericSolution) {

			GenericSolution sol1 = (GenericSolution) s1;
			GenericSolution sol2 = (GenericSolution) s2;

			List<Object> varS1 = sol1.getVariables();
			List<Object> varS2 = sol2.getVariables();

			if (Is.binarySolution(s1) && Is.binarySolution(s2)) {
				return equalsBinarySets(Converter.toBinarySetList(varS1), Converter.toBinarySetList(varS2));
			} else if (Is.integerSolution(s1) && Is.integerSolution(s2)) {
				return equalsIntegers(Converter.toIntegerList(varS1), Converter.toIntegerList(varS2));
			}
		}else if (Is.binarySolution(s1) && Is.binarySolution(s2)) {
			return equalsBinarySets(getVariables((BinarySolution) s1), getVariables((BinarySolution) s2));
		} else if (Is.integerSolution(s1) && Is.integerSolution(s2)) {
			return equalsIntegers(getVariables((IntegerSolution) s1), getVariables((IntegerSolution) s2));
		}

		return true;
	}
	
	private List<BinarySet> getVariables(BinarySolution s) {

		List<BinarySet> variables = new ArrayList<>();

		for (int i = 0; i < s.getNumberOfVariables(); i++) {
			variables.add(s.getVariableValue(i));
		}

		return variables;
	}
	
	private List<Integer> getVariables(IntegerSolution s) {

		List<Integer> variables = new ArrayList<>();

		for (int i = 0; i < s.getNumberOfVariables(); i++) {
			variables.add(s.getVariableValue(i));
		}

		return variables;
	}

	private boolean equalsBinarySets(List<BinarySet> s1, List<BinarySet> s2) {

		for (int i = 0; i < s1.size(); i++) {

			BinarySet binarySet1 = s1.get(i);
			BinarySet binarySet2 = s2.get(i);

			if (!binarySet1.equals(binarySet2)) {
				return false;
			}
		}

		return true;
	}
	
	private boolean equalsIntegers(List<Integer> s1, List<Integer> s2) {
		
		Map<Integer, Integer> mapS1 = new HashMap<>();
		Map<Integer, Integer> mapS2 = new HashMap<>();
		
		for (int i = 0; i < s1.size(); i++) {
			mapS1.merge(s1.get(i), 1, Integer::sum);
			mapS2.merge(s2.get(i), 1, Integer::sum);
		}
		
		for(Entry<Integer, Integer> entry : mapS1.entrySet()){
			
			if(entry.getValue() != mapS2.get(entry.getKey())) {
				return false;
			}
		}

		return true;
	}
}
