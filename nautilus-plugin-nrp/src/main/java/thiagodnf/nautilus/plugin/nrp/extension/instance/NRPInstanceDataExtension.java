package thiagodnf.nautilus.plugin.nrp.extension.instance;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.plugin.extension.InstanceExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import thiagodnf.nautilus.plugin.nrp.extension.problem.NRPProblemExtension;

@Extension
public class NRPInstanceDataExtension implements InstanceExtension {

	@Override
	public Instance getInstanceData(Path path) {
		return new TXTInstance(path);
	}
	
	@Override
	public List<String> getProblemIds() {
		return Arrays.asList(
			new NRPProblemExtension().getId()
		);
	}
	
	@Override
	public List<Tab> getTabs(Instance data) {

		TXTInstance c = (TXTInstance) data;

		List<Tab> tabs = new ArrayList<>();

		tabs.add(getRequirementsTab(c));
		
		return tabs;
	}

	protected Tab getRequirementsTab(TXTInstance data) {

		TableTabContent table = new TableTabContent(Arrays.asList("Requirement", "Cost", "Profit", "Importance"));

		for (int i = 0; i < data.getNumberOfRequirements(); i++) {
			table.getRows().add(Arrays.asList(
					"" + i,
					"" + data.getCost(i),
					"" + data.getProfit(i),
					"" + data.getImportance(i)
			));
		}

		return new Tab("Requirements", table);
	}
}
