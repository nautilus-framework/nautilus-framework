package thiagodnf.nautilus.core.indicator;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.rmetric.RMetric;

public class RInvertedGenerationalDistance extends InvertedGenerationalDistance<PointSolution> {

	private static final long serialVersionUID = 6292740869882877883L;
	
	protected RMetric rMetric;

	public RInvertedGenerationalDistance(PointSolution zr, double delta, Front referenceParetoFront){
		super(referenceParetoFront);
		
		this.rMetric = new RMetric(zr, delta);
	}
	
	@Override
	public Double evaluate(List<PointSolution> approximation) {
		
		Preconditions.checkNotNull(approximation, "The pareto front approximation is null");

		List<PointSolution> paretoFront = FrontUtils.convertFrontToSolutionList(referenceParetoFront);
		
		PointSolution zp = rMetric.pivotPointIdentification(paretoFront);
		
		List<PointSolution> trimmedParetoFront = rMetric.trimmingProcedure(zp, paretoFront);
		
		return invertedGenerationalDistance(new ArrayFront(approximation), new ArrayFront(trimmedParetoFront));
	}
	
}
