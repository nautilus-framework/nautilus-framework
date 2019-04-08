package thiagodnf.nautilus.plugin.toy.extension.objective;

import java.util.ArrayList;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.toy.encoding.objective.NumberOfObjective;
import thiagodnf.nautilus.plugin.toy.extension.problem.ToyProblemExtension;

@Extension
public class ToyObjectiveExtension implements ObjectiveExtension {

	@Override
	public List<AbstractObjective> getObjectives() {

		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfObjective(1));
		objectives.add(new NumberOfObjective(2));
		objectives.add(new NumberOfObjective(3));
		objectives.add(new NumberOfObjective(4));
		objectives.add(new NumberOfObjective(5));
		objectives.add(new NumberOfObjective(6));
		objectives.add(new NumberOfObjective(7));
		objectives.add(new NumberOfObjective(8));
		objectives.add(new NumberOfObjective(9));
		objectives.add(new NumberOfObjective(10));
		objectives.add(new NumberOfObjective(11));
		objectives.add(new NumberOfObjective(12));
		objectives.add(new NumberOfObjective(13));
		objectives.add(new NumberOfObjective(14));
		objectives.add(new NumberOfObjective(15));
		objectives.add(new NumberOfObjective(16));

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
		return Converter.toKey(new ToyProblemExtension().getName());
	}
}
