package thiagodnf.nautilus.core.reduce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class ObjectiveBasedReducer extends AbstractReduce{

	@Override
	public List<RankingItem> execute(Problem<?> problem, 
			InstanceData data, 
			List<AbstractObjective> allObjectives,
			List<AbstractObjective> selectedObjectives, 
			List<NSolution<?>> solutions) {
		
		
		List<NSolution<?>> visualizedSolutions = SolutionListUtils.getVisualizedSolutions(solutions);
		
		
		
		
		return null;
	}
	
	
	
	
	
	
	public double[][] correlationForBinary(NProblem<?> problem) {

		NBinaryProblem p = (NBinaryProblem) problem;
		
		int num = p.getNumberOfBits(0);

		double[][] correlation = new double[num+1][p.getNumberOfObjectives()];

		for (int i = 0; i <= num; i++) {

			BinarySolution sol = (BinarySolution) problem.createSolution();

			BinarySet bin = new BinarySet(num);
			
			// Clear Variables
			for (int j = 0; j < num; j++) {
				bin.set(j, false);
			}
			
			bin.set(i, true);
			
			sol.setVariableValue(0, bin);
			
			p.evaluate(sol);
			
			correlation[i] = sol.getObjectives();
		}
		
		System.out.println(toString(correlation));
		System.out.println("-------");
		System.out.println(toString(normalizeByColumns(correlation, p.getNumberOfObjectives())));
		System.out.println("-----------");
		
		return normalizeByColumns(correlation, p.getNumberOfObjectives());
	}
	
	
	
	
	public double[][] correlationForInteger(NProblem<?> problem) {

		NIntegerProblem p = (NIntegerProblem) problem;

		int min = p.getLowerBound(0);
		int max = p.getUpperBound(0);
		
		double[][] correlation = new double[(max-min)+1][p.getNumberOfObjectives()];

		for (int i = min; i <= max; i++) {

			IntegerSolution sol = (IntegerSolution) problem.createSolution();

			// Clear Variables
			for (int j = 0; j < sol.getNumberOfVariables(); j++) {
				sol.setVariableValue(j, 0);
			}
			
			sol.setVariableValue(0, i);

			p.evaluate(sol);
			
			correlation[i-1] = sol.getObjectives();
		}
		
//		System.out.println(toString(correlation));
//		System.out.println("-------");
//		System.out.println(toString(normalizeByColumns(correlation, p.getNumberOfObjectives())));
//		System.out.println("-----------");
		
		return normalizeByColumns(correlation, p.getNumberOfObjectives());
	}
	
	public double[][] normalizeByColumns(double[][] matrix, int numberOfColumns) {
		
		double[][] m = new double[matrix.length][numberOfColumns];
				
		for (int j = 0; j < numberOfColumns; j++) {

			double[] column = getColumn(matrix, j);
			
			double min = Arrays.stream(column).min().getAsDouble();
			double max = Arrays.stream(column).max().getAsDouble();
			
			for (int i = 0; i < column.length; i++) {
				m[i][j] = Normalizer.normalize(column[i], 1, 0, min, max);
			}
		}
		
		return m;
	}
	
	public double[] getColumn(double[][] matrix, int columnId) {

		if (columnId < 0) {
			throw new IllegalArgumentException("The columnId should be >= 0");
		}
		
		double[] column = new double[matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			column[i] = matrix[i][columnId];
		}

		return column;
	}
	
	public String toString(double[][] matrix) {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[i].length; j++) {

				builder.append(Converter.round(matrix[i][j],1));

				if (j + 1 != matrix[i].length) {
					builder.append(",");
				}
			}

			if (i + 1 != matrix.length) {
				builder.append("\n");
			}
		}

		return builder.toString();
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<String> executeByVariables(Problem<?> problem, List<? extends NSolution<?>> population){
		
		List<String> optimizedObjectivesAsString = SolutionListUtils.getObjectives(population.get(0));
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(population);
		
		int numberOfObjectives = selectedSolutions.get(0).getNumberOfObjectives();
		
		double[][] correlation = correlationForBinary((NProblem<?>) problem);
		
		for(NSolution<?> solution : selectedSolutions) {
			System.out.println(toStringVariable(solution));
		}
		System.out.println("-----------");
		
		
		
		
//		System.out.println(toString(correlation));
		
		
		
		
		
		
		double[] grade = new double[optimizedObjectivesAsString.size()];

		for (int i = 0; i < grade.length; i++) {
			grade[i] = 1;
		}
		
		for (NSolution<?> solution : selectedSolutions) {

			double feedback = SolutionUtils.getUserFeedback(solution);
			
			for (int i = 0; i < solution.getNumberOfVariables(); i++) {

//				int variable = (int) solution.getVariableValue(i) - 1;
//				
//				for (int j = 0; j < grade.length; j++) {
//					grade[j] += correlation[variable][j] * feedback;
//				}
			}
		}
		
		System.out.println(Arrays.toString(grade));
		
		double minGrade = Arrays.stream(grade).min().getAsDouble();
		double maxGrade = Arrays.stream(grade).max().getAsDouble();
		
		for (int i = 0; i < grade.length; i++) {
			if(minGrade == maxGrade) {
				grade[i] = Normalizer.normalize(grade[i], 1, 0, minGrade+0.000001, maxGrade);
			}else {
				grade[i] = Normalizer.normalize(grade[i], 1, 0, minGrade, maxGrade);
			}
		}
		
		System.out.println(Arrays.toString(grade));

		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < grade.length; i++) {
			rankings.add(new RankingItem(optimizedObjectivesAsString.get(i), grade[i]));
		}
		
//		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue));
		
		for (RankingItem item : rankings) {
			item.setSelected(true);
		}
		
		if(minGrade != maxGrade) {
			byNormalized(rankings);
		}
		
		List<String> nextObjectiveIds = new ArrayList<>();
		
		for (RankingItem item : rankings) {
			
			System.out.println(item);
			
			if (item.isSelected()) {
				nextObjectiveIds.add(item.getObjectiveId());
			}
		}
		
		return nextObjectiveIds;
	}
	
	
	
	
	
	
	
	
	
	
	
	public List<String> execute(Problem<?> problem, List<? extends NSolution<?>> population){
		
		List<String> optimizedObjectivesAsString = SolutionListUtils.getObjectives(population.get(0));
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(population);
		
		int numberOfObjectives = selectedSolutions.get(0).getNumberOfObjectives();
		
		double[] preferences = new double[selectedSolutions.size()];

		for (int i = 0; i < selectedSolutions.size(); i++) {
			preferences[i] = SolutionUtils.getUserFeedback(selectedSolutions.get(i));
		}
		
		double sumOfPreferences = Arrays.stream(preferences).sum();
		
		List<AbstractObjective> objectives = ((NProblem) problem).getObjectives();
		
		System.out.println(toStringObjectives(optimizedObjectivesAsString));
		for(NSolution<?> solution : selectedSolutions) {
			System.out.println(toString(solution));
		}
		System.out.println("-----------");
		
//		selectedSolutions = new ByParetoFrontValuesNormalize().normalize(objectives, selectedSolutions);
//		
//		
//		System.out.println(toStringObjectives(optimizedObjectivesAsString));
//		for(NSolution<?> solution : selectedSolutions) {
//			//System.out.println(PopulationUtils.toString(solution));
//		}
//		System.out.println("-----------");
		
		for(NSolution<?> solution : selectedSolutions) {
			System.out.println(toStringVariable(solution));
		}
		System.out.println("-----------");
		
		double[] minFeedback = new double[optimizedObjectivesAsString.size()];
		double[] maxFeedback = new double[optimizedObjectivesAsString.size()];	
		
		for (int i = 0; i < optimizedObjectivesAsString.size(); i++) {

			List<Double> min = new ArrayList<>();
			List<Double> max = new ArrayList<>();

			for (NSolution<?> solution : selectedSolutions) {

				double preference = SolutionUtils.getUserFeedback(solution);
				double value = solution.getObjective(i);
				
				if (value == 0.0) {
					min.add(preference);
				} else if (value == 1.0) {
					max.add(preference);
				}
			}
			
			if (min.isEmpty()) {
				minFeedback[i] = 0;
			} else {
				minFeedback[i] = min.stream().mapToDouble(a -> a).average().getAsDouble();
			}
			if (max.isEmpty()) {
				maxFeedback[i] = 0;
			} else {
				maxFeedback[i] = max.stream().mapToDouble(a -> a).average().getAsDouble();
			}
		}
		
		System.out.println(Arrays.toString(minFeedback));
		System.out.println(Arrays.toString(maxFeedback));
		
		double[] grade = new double[optimizedObjectivesAsString.size()];
		
		for (int i = 0; i < optimizedObjectivesAsString.size(); i++) {
			if(minFeedback[i] < maxFeedback[i]) {
				grade[i] = Math.min(minFeedback[i], maxFeedback[i]);
			}else {
				grade[i] = Math.max(minFeedback[i], maxFeedback[i]);
			}
		}
		
		System.out.println(Arrays.toString(grade));
		
		double minGrade = Arrays.stream(grade).min().getAsDouble();
		double maxGrade = Arrays.stream(grade).max().getAsDouble();
		
		for (int i = 0; i < grade.length; i++) {
			
			if(minGrade == maxGrade) {
				grade[i] = minGrade;
			}else {
				grade[i] = Normalizer.normalize(grade[i], 0, 1, minGrade, maxGrade);
			}
		}
		
		System.out.println(Arrays.toString(grade));
		
		List<RankingItem> rankings = new ArrayList<>();
		
		for (int i = 0; i < grade.length; i++) {
			rankings.add(new RankingItem(optimizedObjectivesAsString.get(i), grade[i]));
		}
		
//		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue).reversed());
		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue));
		
		for (RankingItem item : rankings) {
			item.setSelected(true);
		}
		
		
		//byAverage(rankings);
		//byEpsilon(rankings);
		
		if(minGrade != maxGrade) {
			byNormalized(rankings);
		}
		
		List<String> nextObjectiveIds = new ArrayList<>();
		
		for (RankingItem item : rankings) {
			
			System.out.println(item);
			
			if (item.isSelected()) {
				nextObjectiveIds.add(item.getObjectiveId());
			}
		}
		
		return nextObjectiveIds;
	}
	
	public void byNormalized(List<RankingItem> items) {
		
		for (RankingItem item : items) {

			if(item.value == 1.0) {
				item.setSelected(false);
			}
		}
	}
	
	public void byEpsilon(List<RankingItem> items) {
		
		double epsion = 0.1;
		double lastValue = items.get(0).value;
		
		// Remove the already selected objective;
		
		items = items.stream().filter(e -> !e.isSelected()).collect(Collectors.toList());

		for (RankingItem item : items) {

			double currentValue = item.value;

			double diff = Math.abs(currentValue - lastValue);
			
			if (diff <= epsion) {
				item.setSelected(true);
				lastValue = currentValue;
			}
		}
	}
	
	public void byAverage(List<RankingItem> items) {
		
		// Remove the already selected objective;
		
		items = items.stream().filter(e -> !e.isSelected()).collect(Collectors.toList());
		
		double sum = items.stream().map(e -> e.getValue()).reduce(Double::sum).get();
		double average = (double) sum / (double) items.size();
		
		for (RankingItem item : items) {
			
			if (item.getValue() >= average) {
				item.setSelected(true);
			}
		}
	}
	
	
	public String toStringObjectives(List<String> objectives) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("feedback,");
		
		for(int i=0;i<objectives.size();i++) {
			builder.append(objectives.get(i));
			
			if(i+1 != objectives.size()) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
	
	public String toStringVariable(NSolution<?> solution) {
	
		StringBuilder builder = new StringBuilder();
		
//		double feedback = (double) solution.getAttribute(SolutionAttribute.FEEDBACK);
//		
//		builder.append(Converter.round(feedback, 2)).append(",");
//		
//		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
//			
//			builder.append(solution.getVariableValue(i));
//			
//			if(i+1 != solution.getNumberOfVariables()) {
//				builder.append(",");
//			}
//		}

		return builder.toString();
	}
		
	public static String toString(NSolution<?> solution) {
		
		double[] objectives = solution.getObjectives();
		
		StringBuilder builder = new StringBuilder();
		
		double feedback =SolutionUtils.getUserFeedback(solution);
		
		builder.append(Converter.round(feedback, 2)).append(",");
		
		for (int i = 0; i < objectives.length; i++) {
			
			builder.append(Converter.round(objectives[i], 2));
			
			if(i+1 != objectives.length) {
				builder.append(",");
			}
		}

		return builder.toString();
	}

	@Override
	public String getName() {
		return "Objective Based Reducer";
	}

}
