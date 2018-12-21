package thiagodnf.nautilus.core.util;

import java.util.List;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;

import thiagodnf.nautilus.core.model.GenericSolution;

public class Is {
	
	private Is() throws InstantiationException {
		throw new InstantiationException("Instances of this type are forbidden");
	}
	
	public static boolean equal(BinarySet a, BinarySet b) {
		return a.equals(b);
	}

	public static boolean notEqual(BinarySet a, BinarySet b) {
		return !equal(a, b);
	}
	
	public static boolean equal(double[] a, double[] b) {
		return equal(Doubles.asList(a), Doubles.asList(b));
	}
	
	public static boolean equal(List<?> a, List<?> b) {

		Preconditions.checkNotNull(a, "The list 'a' should not be null");
		Preconditions.checkNotNull(b, "The list 'b' should not be null");

		// To be equal, the two lists should have the same size
		if (a.size() != b.size()) {
			return false;
		}

		// To be equal, all contents should be the same
		// and in the same position
		for (int i = 0; i < a.size(); i++) {

			if (!a.get(i).equals(b.get(i))) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean equal(BinarySolution s1, BinarySolution s2) {

		Preconditions.checkNotNull(s1, "The solution s1 should not be null");
		Preconditions.checkNotNull(s2, "The solution s2 should not be null");

		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return false;
		}
		
		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}

		for (int i = 0; i < s1.getNumberOfVariables(); i++) {

			if (notEqual(s1.getVariableValue(i), s2.getVariableValue(i))) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean equal(IntegerSolution s1, IntegerSolution s2) {

		Preconditions.checkNotNull(s1, "The solution s1 should not be null");
		Preconditions.checkNotNull(s2, "The solution s2 should not be null");

		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return false;
		}
		
		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}

		for (int i = 0; i < s1.getNumberOfVariables(); i++) {

			if (s1.getVariableValue(i) != s2.getVariableValue(i)) {
				return false;
			}
		}

		return true;
	}

	public static boolean binarySolution(Solution<?> s) {

		if (s instanceof GenericSolution) {

			try {

				Class<?> cls = Class.forName(((GenericSolution) s).getType());

				return BinarySolution.class.isAssignableFrom(cls);
			} catch (Exception e) {
				return false;
			}
		}

		return s instanceof BinarySolution;
	}
	
	public static boolean integerSolution(Solution<?> s) {

		if (s instanceof GenericSolution) {

			try {

				Class<?> cls = Class.forName(((GenericSolution) s).getType());

				return IntegerSolution.class.isAssignableFrom(cls);
			} catch (Exception e) {
				return false;
			}
		}

		return s instanceof IntegerSolution;
	}
}
