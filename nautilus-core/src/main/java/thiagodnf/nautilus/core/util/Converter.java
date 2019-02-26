package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.point.PointSolution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.serializer.BinarySetDeserializer;
import thiagodnf.nautilus.core.serializer.BinarySetSerializer;
import thiagodnf.nautilus.core.serializer.NSolutionDeserializer;
import thiagodnf.nautilus.core.serializer.NSolutionSerializer;

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
	
	public static NBinarySolution toBinarySolution(int numberOfObjectives, String binaryString) {

		NBinarySolution solution = new NBinarySolution(numberOfObjectives, 1, binaryString.length());

		String[] parts = binaryString.split("");

		for (int i = 0; i < parts.length; i++) {

			if (parts[i].equalsIgnoreCase("0")) {
				solution.getVariableValue(0).set(i, false);
			} else {
				solution.getVariableValue(0).set(i, true);
			}
		}

		return solution;
	}
	
	public static double round(double value, int places) {
	    
		if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	  
	    value = value * factor;
	    
	    long tmp = Math.round(value);
	    
	    return (double) tmp / factor;
	}
	
	public static String toJson(Object object) {
		
		Gson gson = new GsonBuilder()
				.serializeSpecialFloatingPointValues()
				.registerTypeAdapter(NSolution.class, new NSolutionSerializer())
				.registerTypeAdapter(BinarySet.class, new BinarySetSerializer())
				.create();
		
		return gson.toJson(object);
	}
	
	public static <T> T fromJson(String content, Class<T> classOfT) {
		
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(NSolution.class, new NSolutionDeserializer())
				.registerTypeAdapter(BinarySet.class, new BinarySetDeserializer())
				.create();
		
		return gson.fromJson(content, classOfT);
	}
	
	public static String getObjectivesValuesAsString(Solution<?> solution, String separator) {
		
//		require(solution, notNullValue());
		
		return Arrays.stream(solution.getObjectives())
				.mapToObj(i -> String.valueOf(i))
				.reduce((i, a) -> i + separator + a)
				.get();
	}
	
	
	public static Solution<?> toSolutionWithOutObjectives(Problem<?> problem, Solution<?> solution) {

		Solution<?> newSolution = (Solution<?>) problem.createSolution();

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			if(problem instanceof IntegerProblem) {
				((IntegerSolution) newSolution).setVariableValue(i, ((IntegerSolution) solution).getVariableValue(i));
			}else if(problem instanceof BinaryProblem) {
				((BinarySolution) newSolution).setVariableValue(i, ((BinarySolution)solution).getVariableValue(i));
			}
		}

		return newSolution;
	}
}
