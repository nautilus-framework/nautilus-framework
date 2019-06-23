package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.normalize.AbstractNormalize;

public interface NormalizerExtension extends ExtensionPoint {

	public AbstractNormalize getNormalizer();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
