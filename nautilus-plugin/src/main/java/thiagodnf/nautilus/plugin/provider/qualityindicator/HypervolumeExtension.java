package thiagodnf.nautilus.plugin.provider.qualityindicator;

import org.pf4j.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.plugin.extension.QualityIndicatorExtension;

@Extension
public class HypervolumeExtension extends QualityIndicatorExtension {

	@Override
	public String getName() {
		return "Hypervolume";
	}

	@Override
	public QualityIndicator<?, ?> create(Front referenceParetoFront) {
		return new PISAHypervolume<>(referenceParetoFront);
	}
}
