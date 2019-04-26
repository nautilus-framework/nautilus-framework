package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.correlation.AbstractCorrelation.CorrelationItem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class InfluenceOfVariableBasedReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Influence of Variable-based Reducer";
	}
	
	public static List<CorrelationItem> normalizeByColumns(List<CorrelationItem> correlations, int numberOfColumns, boolean inverse) {
		
		List<CorrelationItem> normalizedCorrelations = new ArrayList<>();
		
		for (CorrelationItem correlation : correlations) {
			normalizedCorrelations.add(correlation.copy());
		}
		
		for (int j = 0; j < numberOfColumns; j++) {

			List<Double> values = new ArrayList<>();

			for (CorrelationItem correlation : correlations) {
				values.add(correlation.getValues().get(j));
			}

			double sum = values.stream().mapToDouble(e -> e).sum();
			double min = values.stream().mapToDouble(e -> e).min().getAsDouble();
			double max = values.stream().mapToDouble(e -> e).max().getAsDouble();
			
			for (int i = 0; i < correlations.size(); i++) {

				double value = 0.0;

				if (min == max) {
					if(inverse) {
						value = Normalizer.normalize(max, 1, 0, 0, sum);
					}else {
						value = Normalizer.normalize(max, 0, 1, 0, sum);
					}
				} else {
					if(inverse) {
						value = Normalizer.normalize(values.get(i), 1, 0, 0, sum);
					}else {
						value = Normalizer.normalize(values.get(i), 0, 1, 0, sum);
					}
				}

				normalizedCorrelations.get(i).getValues().set(j, value);
			}
		}
		
		return normalizedCorrelations;
	}
	
	public List<CorrelationItem> getCorrelationForBinaryProblem(NBinaryProblem p) {

		int num = p.getNumberOfBits(0);

		List<CorrelationItem> correlations = new ArrayList<>();

		for (int i = 0; i < num; i++) {

			BinarySolution sol = (BinarySolution) p.createSolution();

			BinarySet bin = new BinarySet(num);

			bin.set(i, true);

			sol.setVariableValue(0, bin);

			p.evaluate(sol);

			List<Double> values = new ArrayList<>();

			for (int j = 0; j < sol.getNumberOfObjectives(); j++) {
				values.add(sol.getObjective(j));
			}

			correlations.add(new CorrelationItem(String.valueOf(i), values));
		}
		
		correlations = normalizeByColumns(correlations, p.getNumberOfObjectives(), true);
		
		return correlations;
	}
	
	public List<CorrelationItem> getCorrelationForIntegerProblem(NIntegerProblem p) {

		List<CorrelationItem> correlations = new ArrayList<>();

		for (int i = p.getLowerBound(0); i <= p.getUpperBound(0); i++) {

			IntegerSolution sol = (IntegerSolution) p.createSolution();

			for (int j = 0; j < p.getNumberOfVariables(); j++) {
				sol.setVariableValue(j, 0);
			}
			
			sol.setVariableValue(0, i);

			p.evaluate(sol);

			List<Double> values = new ArrayList<>();

			for (int j = 0; j < sol.getNumberOfObjectives(); j++) {
				values.add(sol.getObjective(j));
			}

			correlations.add(new CorrelationItem(String.valueOf(i), values));
		}
		
		correlations = normalizeByColumns(correlations, p.getNumberOfObjectives(), true);
		
		return correlations;
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<NSolution<?>> solutions) {
		
		List<CorrelationItem> correlations = null;
		
		if (problem instanceof IntegerProblem) {
			correlations = getCorrelationForIntegerProblem((NIntegerProblem) problem);
		} else if (problem instanceof BinaryProblem) {
			correlations = getCorrelationForBinaryProblem((NBinaryProblem) problem);
		}
		
		Map<String, CorrelationItem> map = new HashMap<>();
		
		for (CorrelationItem item : correlations) {
			map.put(item.getName(), item);
		}
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions); 
		
		double[] grade = new double[selectedObjectives.size()];
		
		for (NSolution<?> selectedSolution : selectedSolutions) {
			
			double feedback = selectedSolution.getUserFeedback();
			
			List<String> variables = SolutionUtils.getVariablesAsList(selectedSolution);
			
			for (String variable : variables) {

				CorrelationItem item = map.get(variable);

				for (int i = 0; i < grade.length; i++) {
					grade[i] += item.getValues().get(i) * feedback;
				}
			}
		}
		
		System.out.println(Arrays.toString(grade));
		
		double min = Arrays.stream(grade).min().getAsDouble();
		double max = Arrays.stream(grade).max().getAsDouble();

		if(min == max) {
			return getRanking(selectedObjectives, grade);
		}
		
		for (int i = 0; i < grade.length; i++) {
			grade[i] = Normalizer.normalize(grade[i], 0, 1, min, max);
		}
		
		List<RankingItem> rankings = getRanking(selectedObjectives, grade);

		Collections.sort(rankings, Comparator.comparing(RankingItem::getValue));
		
		for (RankingItem ranking : rankings) {

			if (ranking.getValue() == 1.0) {
				ranking.selected = false;
			}
		}
		
		return rankings;
	
	}
	
	protected List<RankingItem> getRanking(List<AbstractObjective> selectedObjectives, double[] grade){
		
		List<RankingItem> rankings = new ArrayList<>();

		for (int i = 0; i < grade.length; i++) {
			rankings.add(new RankingItem(selectedObjectives.get(i).getId(), grade[i]));
		}
		
		for (RankingItem ranking : rankings) {
			ranking.selected = true;
		}
		
		return rankings;
	}
}
