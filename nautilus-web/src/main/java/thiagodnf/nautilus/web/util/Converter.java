package thiagodnf.nautilus.web.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerSolution;

public class Converter {
	
	public static List<thiagodnf.nautilus.web.model.Solution> toSolutions(List<? extends Solution<?>> population) {

		List<thiagodnf.nautilus.web.model.Solution> solutions = new ArrayList<>();

		for (Solution<?> s : population) {

			thiagodnf.nautilus.web.model.Solution solution = new thiagodnf.nautilus.web.model.Solution();

			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
				solution.getObjectives().add(s.getObjective(i));
			}

			for (int i = 0; i < s.getNumberOfVariables(); i++) {
				solution.getVariables().add(s.getVariableValueString(i));
			}

			solutions.add(solution);
		}

		return solutions;
	}
	
	public static Solution toSolutionWithOutObjectives(Problem problem, thiagodnf.nautilus.web.model.Solution solution) {
		
		Solution newSolution = (Solution) problem.createSolution();
		
		for (int i = 0; i < solution.getVariables().size(); i++) {
			if(newSolution instanceof DefaultIntegerSolution) {
				newSolution.setVariableValue(i, Integer.valueOf(solution.getVariables().get(i)));
			}
		}
		
		return newSolution;
	}
}
