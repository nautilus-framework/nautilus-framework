package thiagodnf.nautilus.plugin.factory;

import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.plugin.extension.QualityIndicatorExtension;
import thiagodnf.nautilus.plugin.provider.qualityindicator.HypervolumeExtension;
import thiagodnf.nautilus.plugin.provider.qualityindicator.SpreadExtension;

public class QualityIndicatorFactory extends Factory {

	public QualityIndicatorFactory() {
		addExtension(new SpreadExtension());
		addExtension(new HypervolumeExtension());
	}

	public QualityIndicator<?, ?> getQualityIndicator(String key, Front referenceParetoFront) {
		return ((QualityIndicatorExtension)getExtension(key)).create(referenceParetoFront);
	}
}
