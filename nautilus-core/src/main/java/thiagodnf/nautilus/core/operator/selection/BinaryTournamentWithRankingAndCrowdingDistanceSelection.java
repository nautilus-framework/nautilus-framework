package thiagodnf.nautilus.core.operator.selection;

import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.SolutionUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

public class BinaryTournamentWithRankingAndCrowdingDistanceSelection<S extends Solution<?>> extends Selection<S> {

	private static final long serialVersionUID = 1255624003699494462L;

	private Comparator<S> comparator;

	private final int numberOfTournaments;

	/** 
	 * Constructor 
	 * 
	 */
	public BinaryTournamentWithRankingAndCrowdingDistanceSelection() {
		this.numberOfTournaments = 2;
		this.comparator = new RankingAndCrowdingDistanceComparator<S>();
	}

	@Override
	/** Execute() method */
	public S execute(List<S> solutionList) {
		if (null == solutionList) {
			throw new JMetalException("The solution list is null");
		} else if (solutionList.isEmpty()) {
			throw new JMetalException("The solution list is empty");
		}

		S result;
		if (solutionList.size() == 1) {
			result = solutionList.get(0);
		} else {
			result = SolutionListUtils.selectNRandomDifferentSolutions(1, solutionList).get(0);
			int count = 1; // at least 2 solutions are compared
			do {
				S candidate = SolutionListUtils.selectNRandomDifferentSolutions(1, solutionList).get(0);
				result = SolutionUtils.getBestSolution(result, candidate, comparator);
			} while (++count < this.numberOfTournaments);
		}

		return result;
	}

	@Override
	public String getName() {
		return "Binary Tournament With Ranking And Crowding Distance";
	}

}
