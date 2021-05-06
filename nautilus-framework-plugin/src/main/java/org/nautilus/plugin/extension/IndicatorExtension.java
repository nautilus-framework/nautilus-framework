package org.nautilus.plugin.extension;

import java.util.List;

import org.nautilus.plugin.annotations.Extension;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

public interface IndicatorExtension extends Extension {

	public QualityIndicator<List<Solution<?>>, Double> getIndicator(Front referenceParetoFront);
	
	public boolean supports(ProblemExtension extension);
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
