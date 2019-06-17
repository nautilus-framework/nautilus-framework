package thiagodnf.nautilus.plugin.extension.crossover;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

public abstract class AbstractCrossoverExtension implements CrossoverExtension  {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	public boolean supports(ProblemExtension extension) {
		
		if (extension == null || extension.supports() == null) {
			return false;
		}

		if (supports() == null) {
			return false;
		}
		
		if (supports().equals(extension.supports())) {
			return true;
		}

		return false;
	}
}
