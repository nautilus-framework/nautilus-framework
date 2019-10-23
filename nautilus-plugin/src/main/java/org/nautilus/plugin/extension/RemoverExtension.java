package org.nautilus.plugin.extension;

import org.nautilus.core.remover.AbstractRemover;
import org.pf4j.ExtensionPoint;

public interface RemoverExtension extends ExtensionPoint {

    public AbstractRemover getRemover(ProblemExtension problemExtension);
    
	public String getName();
	
	public String getId();
	
	public String toString();
	
}
