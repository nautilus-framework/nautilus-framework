package thiagodnf.nautilus.plugin.extension.selection;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;

@Extension
public class BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension implements SelectionExtension {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SelectionOperator<List<? extends Solution<?>>, ? extends Solution<?>> getSelection() {
		return new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	}
	
	@Override
	public String getName() {
		return "Binary Tournament With Ranking And Crowding Distance Selection";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public boolean supports(ProblemExtension extension) {
		
		if (extension == null || extension.supports() == null) {
			return false;
		}

		return true;
	}		
}
