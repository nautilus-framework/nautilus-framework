package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reducer.AbstractReducer.RankingItem;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class BestAndWorstObjectivesReducer extends AbstractReducer {

	protected Map<String, Double> confiability;
	
	protected double confiabilityLevel = 0.8;
	
	public BestAndWorstObjectivesReducer() {
		
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
		return "Best and Worst Objectives";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> optimizedObjectives,
			List<NSolution<?>> solutions) {
		
		return null;
	}
	
	public List<String> execute(Problem<?> problem, List<NSolution<?>> solutions){
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(solutions.get(0));
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions); 
		
		double[] worst = new double[optimizedObjectives.size()];
		double[] best = new double[optimizedObjectives.size()];
		
		for (NSolution<?> solution : selectedSolutions) {

			double feedback = solution.getUserFeedback();
			int objIndex = (int) solution.getAttribute(SolutionAttribute.USER_SELECTED_OBJECTIVE_INDEX);
			double objValue = (double) solution.getAttribute(SolutionAttribute.USER_SELECTED_OBJECTIVE_VALUE);

			if (objValue == 0.0) {
				best[objIndex] = feedback;
			} else if (objValue == 1.0) {
				worst[objIndex] = feedback;
			}
			
			System.out.println(Arrays.toString(solution.getObjectives())+ " "+objIndex+" "+objValue+" "+feedback);
		}
		
		System.out.println(Arrays.toString(worst));
		System.out.println(Arrays.toString(best));
		
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
		
		System.out.println("next-objective-ids");
		System.out.println(nextObjectiveIds);

		return nextObjectiveIds;
	}
			
			
}
