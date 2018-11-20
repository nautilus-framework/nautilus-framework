package thiagodnf.nautilus.plugin.provider.qualityindicator;

import org.pf4j.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.plugin.extension.QualityIndicatorExtension;

@Extension
public class SpreadExtension extends QualityIndicatorExtension {

	@Override
	public String getName() {
		return "Spread";
	}

	@Override
	public QualityIndicator<?, ?> create(Front referenceParetoFront) {
		return new Spread<>(referenceParetoFront);
	}
}
