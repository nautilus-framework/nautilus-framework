package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class ConfiabilityBasedReduction extends AbstractReduction {

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
		return "By Confiability";
	}

	@Override
	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population) {
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(population); 
		
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
		
		System.out.println(Arrays.toString(worst));
		System.out.println(Arrays.toString(best));
		
		return rankings;
	}		
}
