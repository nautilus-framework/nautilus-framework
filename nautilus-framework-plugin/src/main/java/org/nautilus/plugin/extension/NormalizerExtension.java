package org.nautilus.plugin.extension;

import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.plugin.annotations.ExtensionPoint;

public interface NormalizerExtension extends ExtensionPoint {

	public AbstractNormalize getNormalizer();
}
