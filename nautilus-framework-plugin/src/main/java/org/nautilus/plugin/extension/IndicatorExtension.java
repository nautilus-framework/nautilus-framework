package org.nautilus.plugin.extension;

import java.util.List;

import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

public interface IndicatorExtension extends ExtensionPoint {

	public QualityIndicator<List<Solution<?>>, Double> getIndicator(Front referenceParetoFront);
	
	public boolean supports(ProblemExtension extension);
}
