package thiagodnf.nautilus.plugin.extension.correlation;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.CorrelationExtension;

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
