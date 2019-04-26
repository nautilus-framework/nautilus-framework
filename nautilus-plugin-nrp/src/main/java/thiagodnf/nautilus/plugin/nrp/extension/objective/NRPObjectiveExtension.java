package thiagodnf.nautilus.plugin.nrp.extension.objective;

import java.util.ArrayList;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.ImportanceObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.NumberOfRequirementsObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.ProfitObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.TasksObjective;
import thiagodnf.nautilus.plugin.nrp.extension.problem.NRPProblemExtension;

@Extension
public class NRPObjectiveExtension implements ObjectiveExtension {

	@Override
	public List<AbstractObjective> getObjectives() {

		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfRequirementsObjective());
		objectives.add(new CostObjective());
		objectives.add(new ProfitObjective());
		objectives.add(new ImportanceObjective());
		objectives.add(new TasksObjective());

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
		return Converter.toKey(new NRPProblemExtension().getName());
	}
}
