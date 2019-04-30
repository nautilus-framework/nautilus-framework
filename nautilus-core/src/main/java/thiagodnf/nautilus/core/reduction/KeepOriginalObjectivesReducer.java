package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class KeepOriginalObjectivesReducer extends AbstractReduction {

	@Override
	public String getName() {
		return "Keep Original Objectives";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<NSolution<?>> population) {

		List<RankingItem> rankings = new ArrayList<>();

		for (AbstractObjective objective : allObjectives) {
			rankings.add(new RankingItem(objective.getId(), 1.0, true));
		}

		return rankings;
	}
}
