package thiagodnf.nautilus.plugin.mip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.distance.JaccardDistance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.mip.objective.MaximumNumberObjective;
import thiagodnf.nautilus.plugin.mip.problem.MaximumIntegerProblem;

public class MIPPlugin extends AbstractPlugin {

	@Override
	public Map<String, List<AbstractObjective>> getObjectives() {
		
		Map<String, List<AbstractObjective>> groups = new HashMap<>();
		
		groups.put("Odd Numbers", Arrays.asList(
			new MaximumNumberObjective(1),
			new MaximumNumberObjective(3),
			new MaximumNumberObjective(5),
			new MaximumNumberObjective(7),
			new MaximumNumberObjective(9)
		));
		
		groups.put("Even Numbers", Arrays.asList(
			new MaximumNumberObjective(2),
			new MaximumNumberObjective(4),
			new MaximumNumberObjective(6),
			new MaximumNumberObjective(8),
			new MaximumNumberObjective(10)
		));
		
		return groups;
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
	public List<String> getCrossoverNames(){
		return Arrays.asList("IntegerSBXCrossover");
	}
	
	@Override
	public List<String> getMutationNames(){
		return Arrays.asList("IntegerPolynomialMutation");
	}
	
	@Override
	public double getSimilarityDistance(List<String> variables1, List<String> variables2) {
		return JaccardDistance.calculate(variables1, variables2);
	}
}
