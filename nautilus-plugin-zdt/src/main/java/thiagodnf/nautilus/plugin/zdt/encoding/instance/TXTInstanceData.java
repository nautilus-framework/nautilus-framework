package thiagodnf.nautilus.plugin.zdt.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.util.InstanceReader;

public class TXTInstanceData extends InstanceData {

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
}
