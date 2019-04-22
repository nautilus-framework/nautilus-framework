package thiagodnf.nautilus.plugin.spl.extension.objective;

import java.util.ArrayList;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.spl.encoding.objective.AliveMutantsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NumberOfProductsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.SimilarityObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UncoveredPairsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnimportantFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnselectedFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.extension.problem.SPLProblemExtension;

@Extension
public class SPLObjectiveExtension implements ObjectiveExtension {

	@Override
	public List<AbstractObjective> getObjectives() {

		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfProductsObjective());
		objectives.add(new AliveMutantsObjective());
		objectives.add(new UncoveredPairsObjective());
		objectives.add(new SimilarityObjective());
		objectives.add(new CostObjective());
		objectives.add(new UnselectedFeaturesObjective());
		objectives.add(new UnimportantFeaturesObjective());
		
		return objectives;
	}
	
	public List<AbstractObjective> getObjectives(List<String> objectiveIds) {

		List<AbstractObjective> selected = new ArrayList<>();

		for (AbstractObjective objective : getObjectives()) {

			for (String id : objectiveIds) {

				if (objective.getId().equalsIgnoreCase(id)) {
					selected.add(objective);
				}
			}
		}

		return selected;
	}

	@Override
	public String getProblemId() {
		return Converter.toKey(new SPLProblemExtension().getName());
	}
}
