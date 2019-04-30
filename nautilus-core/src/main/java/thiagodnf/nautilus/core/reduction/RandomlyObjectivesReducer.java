package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class RandomlyObjectivesReducer extends AbstractReduction {

	@Override
	public String getName() {
		return "Randomly";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<NSolution<?>> population) {
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));

		List<RankingItem> rankings = new ArrayList<>();

		// Randomly select the objectives to next execution 
		
		for (String objectiveId : optimizedObjectives) {

			if (JMetalRandom.getInstance().nextDouble() <= 0.5) {
				rankings.add(new RankingItem(objectiveId, 1.0, true));
			} else {
				rankings.add(new RankingItem(objectiveId, 1.0, false));
			}
		}
		
		// Verify if some objective was selected. Otherwise, we have to 
		// make sure at least one objective should be selected
		
		List<RankingItem> selectedRanking = rankings
				.stream()
				.filter(e -> e.selected)
				.collect(Collectors.toList());
		
		if (selectedRanking.isEmpty()) {
			rankings.get(JMetalRandom.getInstance().nextInt(0, optimizedObjectives.size() - 1)).selected = true;
		}

		return rankings;
	}
}
