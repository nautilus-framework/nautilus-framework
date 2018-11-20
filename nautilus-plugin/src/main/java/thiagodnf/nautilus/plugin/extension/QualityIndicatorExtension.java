package thiagodnf.nautilus.plugin.extension;

import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.util.front.Front;

public abstract class QualityIndicatorExtension implements Extension {

	public abstract QualityIndicator<?, ?> create(Front referenceParetoFront);
	
	public String toString() {
		return getName();
	}
}
