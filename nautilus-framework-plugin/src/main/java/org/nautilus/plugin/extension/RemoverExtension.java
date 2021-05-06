package org.nautilus.plugin.extension;

import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.plugin.annotations.Extension;

public interface RemoverExtension extends Extension {

    public AbstractRemover getRemover(ProblemExtension problemExtension);
    
	public String getName();
	
	public String getId();
	
	public String toString();
	
}
