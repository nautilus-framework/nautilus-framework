package thiagodnf.nautilus.plugin.extension;

import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public interface ObjectiveExtension extends ExtensionPoint {

	public List<AbstractObjective> getObjectives(String problemId);
	
}
