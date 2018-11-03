package thiagodnf.nautilus.core.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.solution.Solution;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractProblem<T extends Solution<?>> extends AbstractGenericProblem<T> {

	private static final long serialVersionUID = 1095619149906323822L;

	private List<AbstractObjective> objectives;
	
	private InstanceData data;
	
	public AbstractProblem() {
		this.data = null;
		this.objectives = new ArrayList<>();
	}
	
	public AbstractProblem(InstanceData data, List<AbstractObjective> objectives) {

		Preconditions.checkNotNull(data, "The data should not be null");
		Preconditions.checkNotNull(objectives, "The list of objectives should not be null");
		Preconditions.checkArgument(objectives.size() >= 1, "The list of objectives should be >= 1");

		this.data = data;
		this.objectives = objectives;

		setNumberOfObjectives(objectives.size());
	}

	public List<AbstractObjective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<AbstractObjective> objectives) {
		this.objectives = objectives;
	}
	
	public InstanceData getInstanceData() {
		return data;
	}

	public void setInstanceData(InstanceData data) {
		this.data = data;
	}
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public abstract String getName();
	
	@Override
	public void evaluate(T solution) {

		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(data, solution));
		}
	}
}
