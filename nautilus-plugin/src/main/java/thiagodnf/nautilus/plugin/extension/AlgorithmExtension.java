package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.reduction.AbstractReduction;

public interface AlgorithmExtension extends ExtensionPoint {

	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder);
	
	public AbstractReduction getReduction() ;
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
