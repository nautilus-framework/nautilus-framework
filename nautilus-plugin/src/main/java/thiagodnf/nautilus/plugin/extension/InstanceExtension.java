package thiagodnf.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.plugin.gui.Tab;

public interface InstanceExtension extends ExtensionPoint {

	public Instance getInstanceData(Path path);
	
	public List<Tab> getTabs(Instance data);
	
	public List<String> getProblemIds();
}
