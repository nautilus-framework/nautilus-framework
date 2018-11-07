package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

@SuppressWarnings("rawtypes")
public class SolutionListUtils {

	/**
	 * Remove the repeated solutions. To do that, we take into account
	 * the objective values into a solution. If the list contains solutions
	 * with the same objective values we removed it
	 * 
	 * @param list list of solutions we have to remove the repeated one
	 * @return a new list contains just non-repeated solutions.
	 */
//	public static List<Solution<?>> removeRepeated(List<? extends Solution<?>> list) {
//		
//		List<Solution<?>> nonRepeated = new ArrayList<>();
//
//		for (Solution solution : list) {
//
//			if (!contains(nonRepeated, solution)) {
//				nonRepeated.add(solution);
//			}
//		}
//
//		return nonRepeated;
//	}
	
	public static List<thiagodnf.nautilus.core.model.Solution> removeRepeated(List<thiagodnf.nautilus.core.model.Solution> list) {
		
		List<thiagodnf.nautilus.core.model.Solution> nonRepeated = new ArrayList<>();

		for (thiagodnf.nautilus.core.model.Solution solution : list) {

			if (!contains(nonRepeated, solution)) {
				nonRepeated.add(solution);
			}
		}

		return nonRepeated;
	}
	
	/**
	 * This method verifies if a given solution is in a list of solutions. 
	 * We taking into account the objective values for considering if a
	 * solution is in the list.
	 * 
	 * @param solutions list of solutions
	 * @param s a given solution
	 * @return true if the list of solutions contains the given solution. False, otherwise
	 */
	public static boolean contains(List<thiagodnf.nautilus.core.model.Solution> solutions, thiagodnf.nautilus.core.model.Solution s1) {
		
		for (thiagodnf.nautilus.core.model.Solution s2 : solutions) {
			
			if (s1.equals(s2)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * This method verifies if two solutions have the same value for
	 * all objectives. If they have then they are equals
	 * 
	 * @param a the solution 'a'
	 * @param b the solution 'b'
	 * @return true if the solution are the same. False, otherwise.
	 */
	public static boolean equals(Solution a, Solution b) {
		
		if (a.getNumberOfObjectives() != b.getNumberOfObjectives()) {
			return false;
		}

		for (int i = 0; i < a.getNumberOfObjectives(); i++) {
			
			if (a.getObjective(i) != b.getObjective(i)) {
				return false;
			}
		}

		return true;
	}

}
