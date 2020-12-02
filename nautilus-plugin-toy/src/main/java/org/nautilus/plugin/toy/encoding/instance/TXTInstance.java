package org.nautilus.plugin.toy.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.util.InstanceReader;

import com.google.common.base.Preconditions;

public class TXTInstance extends Instance {

	protected int lowerBound;
	
	protected int upperBound;
	
	protected int numberOfVariables;
	
	public TXTInstance(Path path) {

		Preconditions.checkNotNull(path, "The path should not be null");
		Preconditions.checkArgument(Files.exists(path), "The path does not exists");

		InstanceReader reader = new InstanceReader(path);

		this.lowerBound = reader.getInteger();
		this.upperBound = reader.getInteger();
		this.numberOfVariables = reader.getInteger();
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
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

        TXTInstance d = (TXTInstance) data;

        TableTabContent table = new TableTabContent("Key", "Value");

        table.addRow("Lower Bound", d.getLowerBound());
        table.addRow("Upper Bound", d.getUpperBound());
        table.addRow("Number of Variables", d.getNumberOfVariables());

        return new Tab("Content", table);
    }
}
