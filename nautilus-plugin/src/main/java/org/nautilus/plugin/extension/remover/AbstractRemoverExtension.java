package org.nautilus.plugin.extension.remover;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.RemoverExtension;

public abstract class AbstractRemoverExtension implements RemoverExtension {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	@Override
    public final String toString() {
        return getId();
    }
}
