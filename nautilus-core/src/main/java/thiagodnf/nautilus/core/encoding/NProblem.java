package thiagodnf.nautilus.core.encoding;

import org.uma.jmetal.problem.Problem;

public abstract class NProblem<S> implements Problem<S>{

	private static final long serialVersionUID = -5610089904985994479L;

	private int numberOfVariables = 0;
	
	private int numberOfObjectives = 0;
	
	private int numberOfConstraints = 0;
	
	private String name = null;

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
}
