package thiagodnf.nautilus.core.operator;

import thiagodnf.nautilus.core.util.Converter;

public abstract class Operator {

	protected double distributionIndex;

	protected double probability;

	public double getProbability() {
		return probability;
	}

	public double getDistributionIndex() {
		return distributionIndex;
	}

	public void setDistributionIndex(double distributionIndex) {
		this.distributionIndex = distributionIndex;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public String getKey() {
		return Converter.toKey(getName());
	}
	
	public abstract String getName();
}
