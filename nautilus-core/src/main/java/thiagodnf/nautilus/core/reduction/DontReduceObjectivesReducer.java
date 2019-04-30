package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class DontReduceObjectivesReducer extends AbstractReduction {

	@Override
	public String getName() {
		return "Don't Reduce";
	}

	@Override
	public List<RankingItem> execute(Problem<?> problem,
			InstanceData data,
			List<AbstractObjective> allObjectives, 
			List<NSolution<?>> population) {
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
		
		List<RankingItem> rankings = new ArrayList<>();

		for (String objectiveId : optimizedObjectives) {
			rankings.add(new RankingItem(objectiveId, 1.0, true));
		}

		return rankings;
	}
}
