package thiagodnf.nautilus.core.reduction;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public class DontReduceObjectivesReduction extends AbstractReduction {

	@Override
	public String getName() {
		return "Don't Reduce";
	}

	@Override
	public List<RankingItem> execute(NProblem<?> problem, List<NSolution<?>> population, List<ItemForEvaluation> itemsForEvaluation) {
		
		List<String> optimizedObjectives = SolutionListUtils.getObjectives(population.get(0));
		
		List<RankingItem> rankings = new ArrayList<>();

		for (String objectiveId : optimizedObjectives) {
			rankings.add(new RankingItem(objectiveId, 1.0, true));
		}

		return rankings;
	}
}
