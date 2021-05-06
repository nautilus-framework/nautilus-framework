package org.nautilus.plugin.extension.correlation;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.CorrelationExtension;

public abstract class AbstractCorrelationExtension implements CorrelationExtension {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	@Override
    public final String toString() {
        return getId();
    }
}
