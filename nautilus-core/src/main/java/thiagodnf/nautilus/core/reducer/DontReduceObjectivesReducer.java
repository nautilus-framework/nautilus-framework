package thiagodnf.nautilus.core.reducer;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class DontReduceObjectivesReducer extends AbstractReducer {

	@Override
	public String getName() {
		return "Don't Reduce";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<AbstractObjective> selectedObjectives,
			List<NSolution<?>> solutions) {

		List<RankingItem> rankings = new ArrayList<>();

		for (AbstractObjective objective : selectedObjectives) {
			rankings.add(new RankingItem(objective.getId(), 1.0, true));
		}

		return rankings;
	}
}
