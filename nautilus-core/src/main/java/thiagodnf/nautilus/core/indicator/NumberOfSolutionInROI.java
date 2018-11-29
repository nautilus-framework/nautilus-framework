package thiagodnf.nautilus.core.indicator;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;
import thiagodnf.nautilus.core.rmetric.RMetric;

public class NumberOfSolutionInROI extends GenericIndicator<PointSolution> {

	private static final long serialVersionUID = 6046908284567336401L;
	
	protected RMetric rMetric;
		
	public NumberOfSolutionInROI(PointSolution zr, double delta) {
		
		this.rMetric = new RMetric(zr, delta);
	}
	
	public Double evaluate(List<PointSolution> approximation) {
		
		List<PointSolution> S = PointSolutionUtils.copy(approximation);
		
		// Step 1
		S = rMetric.prescreeningProcedure(S);
		
		// Step 2
		PointSolution zp = rMetric.pivotPointIdentification(S);
		
		// Step 3
		S = rMetric.trimmingProcedure(zp, S);

		return (double) S.size();
	}

	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}
}
