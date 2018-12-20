package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class KeepCurrentObjectivesReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Keep Current Objectives";
	}

	@Override
	public List<RankingItem> execute(List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<Solution<?>> solutions) {

		List<RankingItem> rankings = new ArrayList<>();

		for (AbstractObjective objective : selectedObjectives) {
			rankings.add(new RankingItem(objective.getId(), 1.0, true));
		}

		return rankings;
	}
}