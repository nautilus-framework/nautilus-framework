package thiagodnf.nautilus.plugin.toy.extension.instance;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.plugin.extension.InstanceExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.toy.encoding.instance.TXTInstance;
import thiagodnf.nautilus.plugin.toy.extension.problem.ToyProblemExtension;

@Extension
public class ToyInstanceDataExtension implements InstanceExtension {

	@Override
	public Instance getInstanceData(Path path) {
		return new TXTInstance(path);
	}
	
	@Override
	public List<String> getProblemIds() {
		return Arrays.asList(
			new ToyProblemExtension().getId()
		);
	}

	@Override
	public List<Tab> getTabs(Instance data) {
		return Arrays.asList(getContentTab(data));
	}

	protected Tab getContentTab(Instance data) {

		TXTInstance d = (TXTInstance) data;

		TableTabContent table = new TableTabContent("Key", "Value");

		table.addRow("Lower Bound", d.getLowerBound());
		table.addRow("Upper Bound", d.getUpperBound());
		table.addRow("Number of Variables", d.getNumberOfVariables());

		return new Tab("Content", table);
	}

	
}
