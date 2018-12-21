package thiagodnf.nautilus.plugin.extension.indicator;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.IndicatorExtension;

@Extension
public class PISAHypervolumeExtension implements IndicatorExtension {

	@Override
	public List<String> getProblemIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "PISA Hypervolume";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public QualityIndicator<List<Solution<?>>, Double> getIndicator(Front referenceParetoFront) {
		return new PISAHypervolume<Solution<?>>(referenceParetoFront);
	}
}
