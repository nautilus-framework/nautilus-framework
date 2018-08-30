package thiagodnf.nautilus.web.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.web.model.Population;

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
	
	public static Population toParentoFront(long executionTime, List<? extends Solution<?>> population) {
		
		Population pf = new Population();
		
		List<thiagodnf.nautilus.web.model.Solution> solutions = new ArrayList<>();
		
		for(Solution<?> s : population) {
			
			thiagodnf.nautilus.web.model.Solution solution = new thiagodnf.nautilus.web.model.Solution();
			
			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
				solution.getObjectives().add(s.getObjective(i));
			}
			
			for (int i = 0; i < s.getNumberOfVariables(); i++) {
				solution.getVariables().add(s.getVariableValueString(i));
			}
			
			solutions.add(solution);
		}
		
		pf.setExecutionTime(executionTime);
		pf.setSolutions(solutions);
		
		return pf;
	}

}
