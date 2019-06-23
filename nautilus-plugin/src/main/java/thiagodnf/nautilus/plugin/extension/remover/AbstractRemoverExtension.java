package thiagodnf.nautilus.plugin.extension.remover;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.RemoverExtension;

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
