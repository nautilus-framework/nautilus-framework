package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.reduction.AbstractReduction;
import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.AlgorithmExtension;

public abstract class AbstractAlgorithmExtension implements AlgorithmExtension  {

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
	
	@Override
    public final String toString() {
        return getId();
    }
	
	public AbstractReduction getReduction() {
	    return null;
	}
}
