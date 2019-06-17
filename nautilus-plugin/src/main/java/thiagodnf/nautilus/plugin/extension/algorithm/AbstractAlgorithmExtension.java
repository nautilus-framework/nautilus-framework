package thiagodnf.nautilus.plugin.extension.algorithm;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;

public abstract class AbstractAlgorithmExtension implements AlgorithmExtension  {

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
}