package org.nautilus.plugin.extension.selection;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.SelectionExtension;

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
