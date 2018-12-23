package thiagodnf.nautilus.core.encoding;

import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public abstract class NProblem<S> implements Problem<S>{

	private static final long serialVersionUID = -5610089904985994479L;

	protected List<AbstractObjective> objectives;
	
	protected InstanceData instance;
	
	private int numberOfVariables = 0;
	
	private int numberOfObjectives = 0;
	
	private int numberOfConstraints = 0;
	
	private String name = null;
	
	public NProblem(InstanceData instance, List<AbstractObjective> objectives) {
		this.instance = instance;
		this.objectives = objectives;
		
		// JMetal's Settings
		setNumberOfObjectives(objectives.size());
				
	}

	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public int getNumberOfObjectives() {
		return numberOfObjectives;
	}

	@Override
	public int getNumberOfConstraints() {
		return numberOfConstraints;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setNumberOfVariables(int numberOfVariables) {
		this.numberOfVariables = numberOfVariables;
	}

	protected void setNumberOfObjectives(int numberOfObjectives) {
		this.numberOfObjectives = numberOfObjectives;
	}

	protected void setNumberOfConstraints(int numberOfConstraints) {
		this.numberOfConstraints = numberOfConstraints;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public List<AbstractObjective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<AbstractObjective> objectives) {
		this.objectives = objectives;
	}

	public InstanceData getInstance() {
		return instance;
	}

	public void setInstance(InstanceData instance) {
		this.instance = instance;
	}
}
