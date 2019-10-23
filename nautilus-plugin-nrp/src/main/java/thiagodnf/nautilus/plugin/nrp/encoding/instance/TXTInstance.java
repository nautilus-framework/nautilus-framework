package thiagodnf.nautilus.plugin.nrp.encoding.instance;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.util.InstanceReader;

import thiagodnf.nautilus.plugin.nrp.encoding.model.Requirement;
import thiagodnf.nautilus.plugin.nrp.encoding.model.Task;

public class TXTInstance extends Instance {
	
	protected int numberOfRequirements;
	
	protected double sumOfCosts;
	
	protected double sumOfProfits;
	
	protected double sumOfImportances;
	
	protected double sumOfTasks;
	
	protected List<Double> requirementsCosts;
	
	protected List<Double> requirementsProfits;
	
	protected List<Double> requirementsImportances;
	
	protected List<Integer> numberOfTasks;
	
	protected List<Requirement> requirements;
	
	public TXTInstance(Path path) {
		
		this.requirements = new ArrayList<>();
		this.requirementsCosts = new ArrayList<>();
		this.requirementsProfits = new ArrayList<>();
		this.requirementsImportances = new ArrayList<>();
		
		InstanceReader reader = new InstanceReader(path, " ");
		
		reader.ignoreLine();
		this.numberOfRequirements = reader.readIntegerValue();
		
		reader.ignoreLine();
		this.numberOfTasks = reader.readIntegerValues();
		
		for (int i = 0; i < numberOfRequirements; i++) {
		
			reader.ignoreLine();
			
			List<Task> tasks = new ArrayList<>();

			for (int j = 0; j < numberOfTasks.get(i); j++) {
				
				List<Double> values = reader.readDoubleValues();
				
				Task task = new Task(
					values.get(0),
					values.get(1),
					values.get(2)
				);
				
				tasks.add(task);
			}
			
			this.requirements.add(new Requirement(tasks));		
		}
		
		for (Requirement requirement : requirements) {
			this.requirementsCosts.add(requirement.getCost());
			this.requirementsProfits.add(requirement.getProfit());
			this.requirementsImportances.add(requirement.getImportance());
		}

		this.sumOfCosts = this.requirementsCosts.stream().mapToDouble(e -> e).sum();
		this.sumOfProfits = this.requirementsProfits.stream().mapToDouble(e -> e).sum();
		this.sumOfImportances = this.requirementsImportances.stream().mapToDouble(e -> e).sum();
		this.sumOfTasks = this.numberOfTasks.stream().mapToDouble(e -> e).sum();
	}
	
	public int getNumberOfRequirements() {
		return numberOfRequirements;
	}

	public double getSumOfCosts() {
		return this.sumOfCosts;
	}
	
	public double getSumOfProfits() {
		return this.sumOfProfits;
	}
	
	public double getSumOfImportances() {
		return this.sumOfImportances;
	}
	
	public double getSumOfTasks() {
		return this.sumOfTasks;
	}

	public double getCost(int requirementId) {
		return this.requirementsCosts.get(requirementId);
	}
	
	public double getProfit(int requirementId) {
		return this.requirementsProfits.get(requirementId);
	}

	public double getImportance(int requirementId) {
		return this.requirementsImportances.get(requirementId);
	}

	public List<Task> getTasks(int requirementId) {
		return this.requirements.get(requirementId).tasks;
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
