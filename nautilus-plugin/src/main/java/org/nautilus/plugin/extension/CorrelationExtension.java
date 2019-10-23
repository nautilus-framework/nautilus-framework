package org.nautilus.plugin.extension;

import org.nautilus.core.correlation.AbstractCorrelation;
import org.pf4j.ExtensionPoint;

public interface CorrelationExtension extends ExtensionPoint {

	public AbstractCorrelation getCorrelation();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
