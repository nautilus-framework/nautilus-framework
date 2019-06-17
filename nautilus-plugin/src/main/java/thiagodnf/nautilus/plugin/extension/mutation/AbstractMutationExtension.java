package thiagodnf.nautilus.plugin.extension.mutation;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

public abstract class AbstractMutationExtension implements MutationExtension {

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
