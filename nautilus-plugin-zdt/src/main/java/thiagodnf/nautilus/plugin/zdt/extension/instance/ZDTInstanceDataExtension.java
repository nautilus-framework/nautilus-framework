package thiagodnf.nautilus.plugin.zdt.extension.instance;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.zdt.encoding.instance.TXTInstanceData;
import thiagodnf.nautilus.plugin.zdt.extension.problem.ZDT1ProblemExtension;
import thiagodnf.nautilus.plugin.zdt.extension.problem.ZDT2ProblemExtension;
import thiagodnf.nautilus.plugin.zdt.extension.problem.ZDT3ProblemExtension;

@Extension
public class ZDTInstanceDataExtension implements InstanceDataExtension {

	@Override
	public InstanceData getInstanceData(Path path) {
		return new TXTInstanceData(path);
	}
	
	@Override
	public List<String> getProblemIds() {
		return Arrays.asList(
			new ZDT1ProblemExtension().getId(),
			new ZDT2ProblemExtension().getId(),
			new ZDT3ProblemExtension().getId()
		);
	}

	@Override
	public List<Tab> getTabs(InstanceData data) {
		return Arrays.asList(getContentTab(data));
	}

	protected Tab getContentTab(InstanceData data) {

		TXTInstanceData d = (TXTInstanceData) data;

		TableTabContent table = new TableTabContent(Arrays.asList("Content", "Value"));

		table.getRows().add(Arrays.asList("Number of Variables", "" + d.getNumberOfVariables()));

		return new Tab("Content", table);
	}
}