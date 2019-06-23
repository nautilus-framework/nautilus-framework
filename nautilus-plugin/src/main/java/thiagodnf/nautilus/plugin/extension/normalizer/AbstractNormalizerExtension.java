package thiagodnf.nautilus.plugin.extension.normalizer;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.NormalizerExtension;

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
