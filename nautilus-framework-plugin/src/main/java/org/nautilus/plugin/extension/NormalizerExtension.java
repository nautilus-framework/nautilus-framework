package org.nautilus.plugin.extension;

import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.plugin.annotations.Extension;

public interface NormalizerExtension extends Extension {

	public AbstractNormalize getNormalizer();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
