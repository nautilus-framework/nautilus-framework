package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class RandomlyObjectivesReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Randomly";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<Solution<?>> solutions) {

		List<RankingItem> rankings = new ArrayList<>();

		// Randomly select the objectives to next execution 
		
		for (AbstractObjective objective : selectedObjectives) {

			if (JMetalRandom.getInstance().nextDouble() <= 0.5) {
				rankings.add(new RankingItem(objective.getId(), 1.0, true));
			} else {
				rankings.add(new RankingItem(objective.getId(), 1.0, false));
			}
		}
		
		// Verify if some objective was selected. Otherwise, we have to 
		// make sure at least one objective should be selected
		
		List<RankingItem> selectedRanking = rankings
				.stream()
				.filter(e -> e.selected)
				.collect(Collectors.toList());
		
		if (selectedRanking.isEmpty()) {
			rankings.get(JMetalRandom.getInstance().nextInt(0, selectedObjectives.size() - 1)).selected = true;
		}

		return rankings;
	}
}
