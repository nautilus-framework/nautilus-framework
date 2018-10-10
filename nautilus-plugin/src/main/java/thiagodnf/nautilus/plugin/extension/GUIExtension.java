package thiagodnf.nautilus.plugin.extension;

import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.gui.Tab;

public interface GUIExtension extends ExtensionPoint {

	public List<Tab> getTabs(InstanceData data);
}
