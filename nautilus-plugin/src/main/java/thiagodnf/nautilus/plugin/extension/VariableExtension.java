package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;

public interface VariableExtension extends ExtensionPoint {

	public boolean isValidForCorrelation(Problem<?> problem, Solution solution, Variable variable);
	
	public boolean isShown(Problem<?> problem, Solution solution, Variable variable);

	public String getDisplayName(Problem<?> problem, Solution solution, Variable variable);
	
	public String getValueForCorrelation(Problem<?> problem, Solution solution, Variable variable);
}
