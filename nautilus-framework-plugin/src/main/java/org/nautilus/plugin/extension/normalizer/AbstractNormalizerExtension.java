package org.nautilus.plugin.extension.normalizer;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.NormalizerExtension;

public abstract class AbstractNormalizerExtension implements NormalizerExtension {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	@Override
    public final String toString() {
        return getId();
    }
}
