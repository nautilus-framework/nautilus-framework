package thiagodnf.nautilus.plugin.mip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.mip.objective.MaximumNumberObjective;
import thiagodnf.nautilus.plugin.mip.problem.MaximumIntegerProblem;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;

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
	public String getDescription() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<p><b>Goal</b></p>");
		buffer.append("<p>Optimize the number of integers inside the array</p>");
		buffer.append("<p><b># of Objectives</b></p>");
		buffer.append("<p>10 objectives</p>");
		buffer.append("<p><b>Example</b></p>");
		buffer.append("<p>[1, 2, 1, 3, 3, 1, 4, 5, 1, 1]</p>");
		buffer.append("<p>The instance file contains the array size</p>");
		buffer.append("<p>The range for every array's position is [1, 20] </p>");
		
		return buffer.toString();
	}
}
