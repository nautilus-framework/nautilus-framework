package org.nautilus.plugin.toy.extension.indicator;

import java.util.List;

import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.IndicatorExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.toy.encoding.indicator.HypervolumeApprox;
import org.pf4j.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

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
