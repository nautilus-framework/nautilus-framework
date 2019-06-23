package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.correlation.AbstractCorrelation;

public interface CorrelationExtension extends ExtensionPoint {

	public AbstractCorrelation getCorrelation();
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
