package thiagodnf.nautilus.plugin.zdt.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.gui.Tab;
import thiagodnf.nautilus.core.gui.TableTabContent;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.util.InstanceReader;

public class TXTInstanceData extends Instance {

	private int numberOfVariables;
	
	public TXTInstanceData(Path path) {

		Preconditions.checkNotNull(path, "The path should not be null");
		Preconditions.checkArgument(Files.exists(path), "The path does not exists");

		InstanceReader reader = new InstanceReader(path);

		this.numberOfVariables = reader.getInteger();
	}
	
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	public void setNumberOfVariables(int numberOfVariables) {
		this.numberOfVariables = numberOfVariables;
	}
	
	@Override
    public List<Tab> getTabs(Instance data) {
        return Arrays.asList(getContentTab(data));
    }

    protected Tab getContentTab(Instance data) {

        TXTInstanceData d = (TXTInstanceData) data;

        TableTabContent table = new TableTabContent(Arrays.asList("Content", "Value"));

        table.getRows().add(Arrays.asList("Number of Variables", "" + d.getNumberOfVariables()));

        return new Tab("Content", table);
    }
}
