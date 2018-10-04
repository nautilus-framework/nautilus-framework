package thiagodnf.nautilus.plugin.mip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.adapter.OperatorAdapter;
import thiagodnf.nautilus.core.distance.JaccardDistance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.operator.crossover.SBXForIntegerCrossover;
import thiagodnf.nautilus.core.operator.mutation.PolynomialForIntegerMutation;
import thiagodnf.nautilus.core.operator.selection.BinaryTournamentWithRankingAndCrowdingDistanceSelection;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.core.plugin.PluginBinding;
import thiagodnf.nautilus.plugin.mip.objective.MaximumNumberObjective;
import thiagodnf.nautilus.plugin.mip.problem.MaximumIntegerProblem;

public class MIPPlugin extends AbstractPlugin {

	@Override
	public ObjectiveAdapter getObjectiveAdapter() {

		ObjectiveAdapter adapter = new ObjectiveAdapter();

		adapter.add("Odd Numbers", new MaximumNumberObjective(1));
		adapter.add("Odd Numbers", new MaximumNumberObjective(3));
		adapter.add("Odd Numbers", new MaximumNumberObjective(5));
		adapter.add("Odd Numbers", new MaximumNumberObjective(7));
		adapter.add("Odd Numbers", new MaximumNumberObjective(9));

		adapter.add("Even Numbers", new MaximumNumberObjective(2));
		adapter.add("Even Numbers", new MaximumNumberObjective(4));
		adapter.add("Even Numbers", new MaximumNumberObjective(6));
		adapter.add("Even Numbers", new MaximumNumberObjective(8));
		adapter.add("Even Numbers", new MaximumNumberObjective(10));

		return adapter;
	}
	
	@Override
	public OperatorAdapter getOperatorAdapter() {
		
		OperatorAdapter adapter = new OperatorAdapter();
		
		adapter.addSelection(new BinaryTournamentWithRankingAndCrowdingDistanceSelection<IntegerSolution>());
		
		adapter.addCrossover(new SBXForIntegerCrossover());
		
		adapter.addMutation(new PolynomialForIntegerMutation());
		
		return adapter;
	}
	
	@Override
	public String getProblemName() {
		return "Maximum Integer Problem";
	}

	@Override
	public Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException {
		return new MaximumIntegerProblem(instance, objectives);
	}
	
	@Override
	public double getSimilarityDistance(List<String> variables1, List<String> variables2) {
		return JaccardDistance.calculate(variables1, variables2);
	}
}
