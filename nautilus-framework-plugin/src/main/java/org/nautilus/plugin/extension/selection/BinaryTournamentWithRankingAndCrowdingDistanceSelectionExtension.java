package org.nautilus.plugin.extension.selection;

import java.util.List;

import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension extends AbstractSelectionExtension{

	@Override
	public SelectionOperator<List<? extends Solution<?>>, ? extends Solution<?>> getSelection() {
		return new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	}
	
	@Override
	public String getName() {
		return "Binary Tournament With Ranking And Crowding Distance Selection";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return null;
	}		
}
