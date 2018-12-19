package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

public interface MutationExtension extends ExtensionPoint {

	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex);
	
	public String getName();
	
	public String getId();
	
	public boolean supports(ProblemExtension extension);
}
