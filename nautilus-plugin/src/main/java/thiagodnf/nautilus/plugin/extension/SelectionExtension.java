package thiagodnf.nautilus.plugin.extension;

import java.util.List;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;

public interface SelectionExtension extends ExtensionPoint {

	public SelectionOperator<List<? extends Solution<?>>, ? extends Solution<?>> getSelection();
	
	public boolean supports(ProblemExtension extension);
	
	public String getName();
	
	public String getId();
}
