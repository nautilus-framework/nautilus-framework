package org.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.plugin.extension.SelectionExtension;
import org.nautilus.plugin.extension.selection.BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;

public class SelectionFactory {
	
	private List<SelectionExtension> extensions = new ArrayList<>();
	
	public SelectionFactory() {
		extensions.add(new BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension());
	}

	public List<SelectionExtension> getExtensions() {
		return extensions;
	}

	public SelectionOperator<List<? extends Solution<?>>, ? extends Solution<?>> getSelection(String selectionId) {

		for (SelectionExtension extension : getExtensions()) {

			if (selectionId.equalsIgnoreCase(extension.getId())) {
				return extension.getSelection();
			}
		}

		return null;
	}
}
