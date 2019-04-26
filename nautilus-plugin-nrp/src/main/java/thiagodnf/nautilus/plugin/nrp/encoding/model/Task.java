package thiagodnf.nautilus.plugin.nrp.encoding.model;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import thiagodnf.nautilus.core.util.Converter;

public class Task {
	
	protected static JMetalRandom random = JMetalRandom.getInstance();

	public double cost;
	
	public double profit;
	
	public double importance;
	
	public Task(double cost, double profit, double importance) {
		this.cost = cost;
		this.profit = profit;
		this.importance = importance;
	}
	
	public static Task getRandom() {
		
		return new Task(
			Converter.round(random.nextDouble(1, 10), 1),
			Converter.round(random.nextDouble(1, 10), 1),
			random.nextInt(1, 10)
		);
	}
	
	public String toString() {
		return Converter.toJson(this);
	}
}
