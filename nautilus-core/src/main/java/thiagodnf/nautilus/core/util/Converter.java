package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;

public class Converter {
	
	public static double[] toDoubleArray(List<Double> list) {
		return list.stream().mapToDouble(x -> x).toArray();
	}
	
	public static List<Double> toDoubleList(double[] array) {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}
	
	public static String toKey(String text) {
		return text.replaceAll("[^A-Za-z0-9]", "-").toLowerCase();
	}
	
	public static PointSolution toPointSolution(Solution<?> solution) {
		return PointSolutionUtils.createSolution(solution.getObjectives());
	}
	
	public static List<PointSolution> toPointSolutions(List<? extends Solution<?>> solutions) {

		List<PointSolution> newSolutions = new ArrayList<>();

		for (Solution<?> solution : solutions) {
			newSolutions.add(toPointSolution(solution));
		}

		return newSolutions;
	}
	
	public static double[][] toDoubleMatrix(String[][] matrix) {
		
		double[][] result = new double[matrix.length][];

		for (int i = 0; i < matrix.length; i++) {
			
			result[i] = new double[matrix[i].length];

			for (int j = 0; j < matrix[i].length; j++) {				

				if (matrix[i][j] == null || matrix[i][j].isEmpty()) {
					result[i][j] = 0.0;
				} else {
					result[i][j] = Double.valueOf(matrix[i][j].trim());
				}
			}
		}

		return result;
	}

	public static int[][] toIntegerMatrix(String[][] matrix) {
		
		int[][] result = new int[matrix.length][];

		for (int i = 0; i < matrix.length; i++) {
			
			result[i] = new int[matrix[i].length];

			for (int j = 0; j < matrix[i].length; j++) {				

				if (matrix[i][j] == null || matrix[i][j].isEmpty()) {
					result[i][j] = 0;
				} else {
					result[i][j] = Integer.valueOf(matrix[i][j].trim());
				}
			}
		}

		return result;
	}

	public static List<PointSolution> toReferencePoints(List<List<Double>> referencePoints) {

		List<PointSolution> pointSolutions = new ArrayList<>();

		for (List<Double> referencePoint : referencePoints) {
			pointSolutions.add(PointSolutionUtils.createSolution(toDoubleArray(referencePoint)));
		}

		return pointSolutions;
	}
}
