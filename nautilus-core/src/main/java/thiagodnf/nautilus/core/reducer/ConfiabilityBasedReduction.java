package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class ConfiabilityBasedReduction extends AbstractReducer {

	protected Map<String, Double> confiability;

	protected double confiabilityLevel;

	public ConfiabilityBasedReduction() {
		this(0.8);
	}
	
	public ConfiabilityBasedReduction(double confiabilityLevel) {
		
		this.confiabilityLevel = confiabilityLevel;
		this.confiability = new HashMap<>();
		
		this.confiability.put("1.0_1.0", 0.0);
		this.confiability.put("1.0_0.0", 0.2);
		this.confiability.put("1.0_-1.0", 0.0);

		this.confiability.put("0.0_1.0", 0.8);
		this.confiability.put("0.0_0.0", 0.5);
		this.confiability.put("0.0_-1.0", 0.2);

		this.confiability.put("-1.0_1.0", 1.0);
		this.confiability.put("-1.0_0.0", 0.8);
		this.confiability.put("-1.0_-1.0", 1.0);
	}
	
	@Override
	public String getName() {
		return "Confiability-based Reduction";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> optimizedObjectives,
			List<NSolution<?>> solutions) {
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions); 
		
		List<List<Double>> feedbacksForWorst = new ArrayList<>();
		List<List<Double>> feedbacksForBest = new ArrayList<>();

		for (int i = 0; i < optimizedObjectives.size(); i++) {
			feedbacksForWorst.add(new ArrayList<>());
			feedbacksForBest.add(new ArrayList<>());
		}

		for (NSolution<?> solution : selectedSolutions) {

			for (int i = 0; i < problem.getNumberOfObjectives(); i++) {

				Object value = solution.getAttribute(SolutionAttribute.FEEDBACK_FOR_OBJECTIVE + i);

				if (value != null) {

					if (solution.getObjective(i) == 0.0) {
						feedbacksForBest.get(i).add((double) value);
					} else if (solution.getObjective(i) == 1.0) {
						feedbacksForWorst.get(i).add((double) value);
					}
				}
			}
		}
		
		double[] worst = new double[optimizedObjectives.size()];
		double[] best = new double[optimizedObjectives.size()];
		
		for (int i = 0; i < optimizedObjectives.size(); i++) {
			
			if(!feedbacksForWorst.get(i).isEmpty()) {
				worst[i] = Collections.max(feedbacksForWorst.get(i));
			}
			
			if(!feedbacksForBest.get(i).isEmpty()) {
				best[i] = Collections.max(feedbacksForBest.get(i));
			}
		}
		
		List<RankingItem> rankings = new ArrayList<>();

		for (int i = 0; i < best.length; i++) {
			rankings.add(new RankingItem(optimizedObjectives.get(i).getName(), confiability.get(best[i] + "_" + worst[i])));
		}
		
		for (RankingItem ranking : rankings) {

			if (ranking.getValue() >= confiabilityLevel) {
				ranking.selected = false;
			} else {
				ranking.selected = true;
			}
		}
		
		return rankings;
	}
	
	public List<String> execute(NProblem<?> problem, List<NSolution<?>> solutions){
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(solutions.get(0));
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions); 
		
		List<List<Double>> feedbacksForWorst = new ArrayList<>();
		List<List<Double>> feedbacksForBest = new ArrayList<>();

		for (int i = 0; i < optimizedObjectives.size(); i++) {
			feedbacksForWorst.add(new ArrayList<>());
			feedbacksForBest.add(new ArrayList<>());
		}

		for (NSolution<?> solution : selectedSolutions) {

			for (int i = 0; i < problem.getNumberOfObjectives(); i++) {

				Object value = solution.getAttribute(SolutionAttribute.FEEDBACK_FOR_OBJECTIVE + i);

				if (value != null) {

					if (solution.getObjective(i) == 0.0) {
						feedbacksForBest.get(i).add((double) value);
					} else if (solution.getObjective(i) == 1.0) {
						feedbacksForWorst.get(i).add((double) value);
					}
				}
			}
		}
		
		double[] worst = new double[optimizedObjectives.size()];
		double[] best = new double[optimizedObjectives.size()];
		
		for (int i = 0; i < optimizedObjectives.size(); i++) {
			
			if(!feedbacksForWorst.get(i).isEmpty()) {
				worst[i] = Collections.max(feedbacksForWorst.get(i));
			}
			
			if(!feedbacksForBest.get(i).isEmpty()) {
				best[i] = Collections.max(feedbacksForBest.get(i));
			}
		}
		
		List<RankingItem> rankings = new ArrayList<>();

		for (int i = 0; i < best.length; i++) {
			rankings.add(new RankingItem(optimizedObjectives.get(i), confiability.get(best[i] + "_" + worst[i])));
		}
		
		for (RankingItem ranking : rankings) {

			if (ranking.getValue() >= confiabilityLevel) {
				ranking.selected = false;
			} else {
				ranking.selected = true;
			}
		}

		List<String> nextObjectiveIds = new ArrayList<>();

		for (RankingItem item : rankings) {

			if (item.selected) {
				nextObjectiveIds.add(item.objectiveId);
			}
		}
		
		System.out.println(Arrays.toString(worst));
		System.out.println(Arrays.toString(best));
		
		System.out.println("next-objective-ids");
		System.out.println(nextObjectiveIds);

		return nextObjectiveIds;
	}		
}

