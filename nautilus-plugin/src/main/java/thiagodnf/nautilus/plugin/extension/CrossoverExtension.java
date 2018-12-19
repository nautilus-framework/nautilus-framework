package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

public interface CrossoverExtension extends ExtensionPoint {

	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex);
	
	public boolean supports(ProblemExtension extension);
		
	public String getName();
	
	public String getId();
}
