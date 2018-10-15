package thiagodnf.nautilus.core.ranking;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class RankingItem {

	private String name;
	
	private String id;

	private double value;

	public RankingItem(AbstractObjective objective, double value) {
		this(objective.getId(), objective.getName(), value);
	}

	public RankingItem(String id, String name, double value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return getName() + ", " + getId() + ", " + getValue();
	}
}
