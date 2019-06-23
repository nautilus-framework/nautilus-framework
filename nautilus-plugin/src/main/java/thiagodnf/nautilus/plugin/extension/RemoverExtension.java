package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.remover.AbstractRemover;

public interface RemoverExtension extends ExtensionPoint {

    public AbstractRemover getRemover(ProblemExtension problemExtension);
    
	public String getName();
	
	public String getId();
	
	public String toString();
	
}
