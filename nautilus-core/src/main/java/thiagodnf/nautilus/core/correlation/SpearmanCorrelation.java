package thiagodnf.nautilus.core.correlation;

import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class SpearmanCorrelation extends Correlation {

	@Override
	public String getName() {
		return "Spearman's Correlation";
	}

	@Override
	public List<String> correlatVariables(List<AbstractObjective> objectives, List<Solution> solutions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		
		SpearmansCorrelation correlation = new SpearmansCorrelation();
		
		return correlation.correlation(x, y);
	}

}
