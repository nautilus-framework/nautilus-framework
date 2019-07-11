package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.uma.jmetal.util.comparator.ObjectiveComparator;

import thiagodnf.nautilus.core.encoding.NSolution;

public class SolutionListUtils {
	
	@SuppressWarnings("unchecked")
	public static List<String> getObjectives(NSolution<?> solution){
		
		String value = (String) solution.getAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES);
		
		return Converter.fromJson(value, List.class);
	}
	
	public static List<NSolution<?>> getSelectedSolutions(List<? extends NSolution<?>> solutions) {
		return getSelectedSolutions(solutions, true);
	}
	
	public static List<NSolution<?>> getSelectedSolutions(List<? extends NSolution<?>> solutions, boolean isCopied) {

		List<NSolution<?>> selectedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {

			Object value = sol.getAttribute(SolutionAttribute.SELECTED);

			if (value != null && (boolean) value == true) {

				if (isCopied) {
					selectedSolutions.add((NSolution<?>) sol.copy());
				} else {
					selectedSolutions.add(sol);
				}
			}
		}

		return selectedSolutions;
	}

	public static List<NSolution<?>> getVisualizedSolutions(List<NSolution<?>> solutions) {

		List<NSolution<?>> visualizedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.VISUALIZED);

			if (value != null && (boolean) value == true) {
				visualizedSolutions.add((NSolution<?>) sol.copy());
			}
		}

		return visualizedSolutions;
	}
	
    public static List<List<Double>> getDefaultReferencePoints(int numberOfObjectives){
	    
	    List<List<Double>> referencePoints = new ArrayList<>();
	    
	    referencePoints.add(Collections.nCopies(numberOfObjectives, 0.5));
	    
	    return referencePoints;   
	}
    
    public static double[] getMinimumObjectiveValues(List<? extends NSolution<?>> population) {

        int numberOfObjectives = population.get(0).getNumberOfObjectives();

        double[] fmin = new double[numberOfObjectives];

        for (int i = 0; i < numberOfObjectives; i++) {

            Collections.sort(population, new ObjectiveComparator<NSolution<?>>(i));

            fmin[i] = population.get(0).getObjective(i);
        }

        return fmin;
    }

    public static double[] getMaximumObjectiveValues(List<? extends NSolution<?>> population) {

        int numberOfObjectives = population.get(0).getNumberOfObjectives();

        double[] fmax = new double[numberOfObjectives];

        for (int i = 0; i < numberOfObjectives; i++) {

            Collections.sort(population, new ObjectiveComparator<NSolution<?>>(i));

            fmax[i] = population.get(population.size() - 1).getObjective(i);
        }

        return fmax;
    }
}
