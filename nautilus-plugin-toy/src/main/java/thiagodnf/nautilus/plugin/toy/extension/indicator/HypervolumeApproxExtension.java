package thiagodnf.nautilus.plugin.toy.extension.indicator;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.toy.encoding.indicator.HypervolumeApprox;

@Extension
public class HypervolumeApproxExtension implements IndicatorExtension {

	@Override
	public QualityIndicator<List<Solution<?>>, Double> getIndicator(Front referenceParetoFront) {
		return new HypervolumeApprox<>(referenceParetoFront);
	}

	@Override
	public boolean supports(ProblemExtension extension) {
		return true;
	}

	@Override
	public String getName() {
		return "Hypervolume Approx";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
}