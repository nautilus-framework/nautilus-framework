package org.nautilus.plugin.extension;

import org.nautilus.core.normalize.AbstractNormalize;
import org.pf4j.ExtensionPoint;

public interface NormalizerExtension extends ExtensionPoint {

	public AbstractNormalize getNormalizer();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
