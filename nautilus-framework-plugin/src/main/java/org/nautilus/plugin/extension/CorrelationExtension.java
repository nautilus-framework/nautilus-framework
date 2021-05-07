package org.nautilus.plugin.extension;

import org.nautilus.core.correlation.AbstractCorrelation;
import org.nautilus.plugin.annotations.ExtensionPoint;

public interface CorrelationExtension extends ExtensionPoint {

	public AbstractCorrelation getCorrelation();	
	
}
