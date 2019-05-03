package thiagodnf.nautilus.core.encoding.problem;

import java.util.List;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.solution.NDoubleSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public abstract class NDoubleProblem extends NProblem<DoubleSolution> implements DoubleProblem {

	private static final long serialVersionUID = 1234593199794358192L;

	private List<Double> lowerBounds;
	
	private List<Double> upperBounds;
	
	public NDoubleProblem(Instance data, List<AbstractObjective> objectives) {
		super(data, objectives);
	}

	@Override
	public Double getUpperBound(int index) {
		return upperBounds.get(index);
	}

	@Override
	public Double getLowerBound(int index) {
		return lowerBounds.get(index);
	}

	protected void setLowerBounds(List<Double> lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	protected void setUpperBounds(List<Double> upperBounds) {
		this.upperBounds = upperBounds;
	}
	
	protected List<Double> getLowerBounds() {
		return lowerBounds;
	}
	
	protected List<Double> getUpperBounds() {
		return upperBounds;
	}
	
	@Override
	public void evaluate(DoubleSolution solution) {
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
		}
	}
	
	@Override
	public DoubleSolution createSolution() {
		return new NDoubleSolution(
			getNumberOfObjectives(), 
			getNumberOfVariables(),
			getLowerBounds(), 
			getUpperBounds()
		);
	}
}
