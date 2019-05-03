package thiagodnf.nautilus.plugin.toy.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.util.InstanceReader;

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
}
