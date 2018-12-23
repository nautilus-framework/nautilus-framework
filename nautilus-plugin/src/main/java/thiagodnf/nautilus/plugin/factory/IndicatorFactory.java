package thiagodnf.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.extension.indicator.IGDExtension;
import thiagodnf.nautilus.plugin.extension.indicator.PISAHypervolumeExtension;
import thiagodnf.nautilus.plugin.extension.indicator.SpreadExtension;

public class IndicatorFactory {

	private List<IndicatorExtension> extensions = new ArrayList<>();

	public IndicatorFactory() {
		getExtensions().add(new PISAHypervolumeExtension());
		getExtensions().add(new SpreadExtension());
		getExtensions().add(new IGDExtension());
	}

	public List<IndicatorExtension> getExtensions() {
		return extensions;
	}

	public QualityIndicator<List<Solution<?>>, Double> getIndicator(String name, Front referenceParetoFront) {

		for (IndicatorExtension extension : getExtensions()) {

			if (name.equalsIgnoreCase(extension.getId())) {
				return extension.getIndicator(referenceParetoFront);
			}
		}

		return null;
	}
}
