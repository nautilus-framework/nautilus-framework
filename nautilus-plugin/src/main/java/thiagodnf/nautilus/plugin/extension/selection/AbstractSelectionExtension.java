package thiagodnf.nautilus.plugin.extension.selection;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;

public abstract class AbstractSelectionExtension implements SelectionExtension {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	public boolean supports(ProblemExtension extension) {
		
		if (extension == null || extension.supports() == null) {
			return false;
		}

		return true;
	}
}
