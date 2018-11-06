package thiagodnf.nautilus.plugin.provider.algorithm;

import java.util.Arrays;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.RNSGAII;
import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class RNSGAIIProvider implements AlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		
//		PointSolution p1 = PointSolutionUtils.createSolution(-0.0, -0.5, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.5, -0.0);
		
		PointSolution p1 = PointSolutionUtils.createSolution(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

		builder.setReferencePoints(Arrays.asList(p1));
		builder.setEpsilon(0.0001);
		
		return new RNSGAII(builder);
	}

	@Override
	public String toString() {
		return "R-NSGA-II";
	}
}
