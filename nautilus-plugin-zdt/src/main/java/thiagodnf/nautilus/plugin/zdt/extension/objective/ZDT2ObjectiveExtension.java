package thiagodnf.nautilus.plugin.zdt.extension.objective;

import java.util.ArrayList;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F1Objective;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F2ForZDT2Objective;
import thiagodnf.nautilus.plugin.zdt.extension.problem.ZDT2ProblemExtension;

@Extension
public class ZDT2ObjectiveExtension implements ObjectiveExtension {

	@Override
	public List<AbstractObjective> getObjectives() {

		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new F1Objective());
		objectives.add(new F2ForZDT2Objective());

		return objectives;
	}

	@Override
	public String getProblemId() {
		return Converter.toKey(new ZDT2ProblemExtension().getName());
	}
}
