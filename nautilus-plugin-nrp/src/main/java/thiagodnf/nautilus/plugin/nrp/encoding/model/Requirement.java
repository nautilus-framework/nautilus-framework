package thiagodnf.nautilus.plugin.nrp.encoding.model;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.util.Converter;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Requirement {

	protected static JMetalRandom random = JMetalRandom.getInstance();

	public List<Task> tasks;

	public Requirement(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public static Requirement getRandom() {
		
		int numberOfTasks = JMetalRandom.getInstance().nextInt(1, 5);
		
		List<Task> tasks = new ArrayList<>();
		
		for(int i=0;i<numberOfTasks;i++) {
			tasks.add(Task.getRandom());
		}
		
		return new Requirement(tasks);
	}
	
	public double getCost() {

		double sum = 0.0;

		for (Task task : tasks) {
			sum += task.cost;
		}

		return sum;
	}

	public double getProfit() {

		double sum = 0.0;

		for (Task task : tasks) {
			sum += task.profit;
		}

		return sum;
	}
	
	public double getImportance() {

		double sum = 0.0;

		for (Task task : tasks) {
			sum += task.importance;
		}

		return sum;
	}
	
	public String toString() {
		return Converter.toJson(this);
	}
}
