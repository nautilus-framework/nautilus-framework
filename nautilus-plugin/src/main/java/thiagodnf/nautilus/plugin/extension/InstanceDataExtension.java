package thiagodnf.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.gui.Tab;

public interface InstanceDataExtension extends ExtensionPoint {

	public InstanceData getInstanceData(Path path);
	
	public List<Tab> getTabs(InstanceData data);
	
	public List<String> getProblemIds();
}
