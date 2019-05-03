package thiagodnf.nautilus.plugin.extension.problem;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

public abstract class AbstractProblemExtension implements ProblemExtension {

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
}
