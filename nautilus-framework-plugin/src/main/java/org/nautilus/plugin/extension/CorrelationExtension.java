package org.nautilus.plugin.extension;

import org.nautilus.core.correlation.AbstractCorrelation;
import org.nautilus.plugin.annotations.Extension;

public interface CorrelationExtension extends Extension {

	public AbstractCorrelation getCorrelation();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
