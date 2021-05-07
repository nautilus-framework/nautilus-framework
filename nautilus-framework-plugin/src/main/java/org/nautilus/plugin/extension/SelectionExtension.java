package org.nautilus.plugin.extension;

import java.util.List;

import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;

public interface SelectionExtension extends ExtensionPoint {

	public SelectionOperator<List<? extends Solution<?>>, ? extends Solution<?>> getSelection();
	
	public Class<? extends Solution<?>> supports();
}
