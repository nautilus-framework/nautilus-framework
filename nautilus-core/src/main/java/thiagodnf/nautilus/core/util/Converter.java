package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.util.distance.DominanceDistance;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

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
	
	public static thiagodnf.nautilus.core.model.Solution toSolution(Solution<?> solution) {

		thiagodnf.nautilus.core.model.Solution newSolution = new thiagodnf.nautilus.core.model.Solution();

		newSolution.setType(solution.getClass().getName());
		
		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			newSolution.getObjectives().add(solution.getObjective(i));
		}

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			newSolution.getVariables().add(solution.getVariableValue(i));
		}

		return newSolution;
	}
	
	public static List<thiagodnf.nautilus.core.model.Solution> toSolutions(List<? extends Solution<?>> population) {

		List<thiagodnf.nautilus.core.model.Solution> solutions = new ArrayList<>();

		for (Solution<?> s : population) {
			solutions.add(toSolution(s));
		}

		return solutions;
	}
	
	public static PointSolution toPointSolution(thiagodnf.nautilus.core.model.Solution solution) {
		return PointSolutionUtils.createSolution(toDoubleArray(solution.getObjectives()));
	}
	
//	public static List<thiagodnf.nautilus.core.model.Solution> toSolutions(List<? extends Solution<?>> population) {
//
//		List<thiagodnf.nautilus.core.model.Solution> solutions = new ArrayList<>();
//
//		for (Solution<?> s : population) {
//
//			solutions.add(toSolution(s));
//			
//			thiagodnf.nautilus.core.model.Solution solution = new thiagodnf.nautilus.core.model.Solution();
//
//			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
//				solution.getObjectives().add(s.getObjective(i));
//			}
//
//			for (int i = 0; i < s.getNumberOfVariables(); i++) {
//
//				if (s instanceof IntegerSolution) {
//					solution.getVariables().add(new Variable(s.getVariableValueString(i)));
//				} else if (s instanceof BinarySolution) {
//
//					BinarySet binarySet = (BinarySet) s.getVariableValue(i);
//
//					for (int j = 0; j < binarySet.getBinarySetLength(); j++) {
//						solution.getVariables().add(new Variable(binarySet.get(j)));
//					}
//
//					solution.getProperties().put("binaryset-size-for-variable_" + i, String.valueOf(binarySet.getBinarySetLength()));
//				}
//			}
//			
//			solution.getProperties().put("number-of-variables", String.valueOf(s.getNumberOfVariables()));
//
//			solution.setType(s.getClass().getName());
//
//			solutions.add(solution);
//		}
//
//		return solutions;
//	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Solution toJMetalSolutionWithOutObjectives(Problem problem, thiagodnf.nautilus.core.model.Solution solution) {
//		
//		Solution newSolution = (Solution) problem.createSolution();
//		
//		if (newSolution instanceof IntegerSolution) {
//			for (int i = 0; i < solution.getVariables().size(); i++) {
//				newSolution.setVariableValue(i, Integer.valueOf(solution.getVariables().get(i).getValue()));
//			}
//		} else if (newSolution instanceof BinarySolution) {
//			
//			int numberOfVariables = Integer.valueOf(solution.getProperties().get("number-of-variables"));
//
//			int index = 0;
//			
//			for (int i = 0; i < numberOfVariables; i++) {
//
//				int nbits = Integer.valueOf(solution.getProperties().get("binaryset-size-for-variable_" + i));
//				
//				BinarySet binarySet = new BinarySet(nbits);
//
//				for (int j = 0; j < nbits; j++) {
//					binarySet.set(j, Boolean.valueOf(solution.getVariables().get(index++).getValue()));
//				}
//				
//				newSolution.setVariableValue(i, binarySet);
//			}
//		}
//		
//		return newSolution;
//	}
	
	public static Solution<?> toJMetalSolutionWithOutObjectives(Problem<?> problem, Solution<?> solution) {
		
		Solution<?> newSolution = (Solution<?>) problem.createSolution();

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (problem instanceof IntegerProblem) {
				((IntegerSolution) newSolution).setVariableValue(i, (Integer) solution.getVariableValue(i));
			}
			if (problem instanceof BinaryProblem) {
				((BinarySolution) newSolution).setVariableValue(i, (BinarySet) solution.getVariableValue(i));
			}
		}
		
//		for (Entry<String, String> entry : solution.getProperties().entrySet()) {
//			newSolution.setAttribute(entry.getKey(), entry.getValue());
//		}
		
		return newSolution;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Solution<?> toJMetalSolution(Problem problem, thiagodnf.nautilus.core.model.Solution solution) {

		Solution newSolution = (Solution) problem.createSolution();

		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			newSolution.setObjective(i, solution.getObjective(i));
		}

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			newSolution.setVariableValue(i, solution.getVariables().get(i));
		}

		for (Entry<String, String> entry : solution.getProperties().entrySet()) {
			newSolution.setAttribute(entry.getKey(), entry.getValue());
		}

		return newSolution;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<? extends Solution<?>> toJMetalSolutions(Problem problem, List<thiagodnf.nautilus.core.model.Solution> population) {

		List solutions = new ArrayList<>();

		for (thiagodnf.nautilus.core.model.Solution solution : population) {
			solutions.add(toJMetalSolution(problem, solution));
		}

		return solutions;
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

	public static List<? extends Solution<?>> clearAttributes(List<? extends Solution<?>> rawSolutions) {
		
		for(Solution<?> s : rawSolutions) {
			
			s.setAttribute(new CrowdingDistance<>().getAttributeIdentifier(), null);
			s.setAttribute(new DominanceRanking<>().getAttributeIdentifier(), null);
			
			
		}
		
		// TODO Auto-generated method stub
		return rawSolutions;
	}
}
