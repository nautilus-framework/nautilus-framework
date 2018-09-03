package thiagodnf.nautilus.plugin.mip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.mip.objective.MinimumNumberObjective;
import thiagodnf.nautilus.plugin.mip.problem.MinimumIntegerProblem;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public class MIPPlugin extends AbstractPlugin {

	@Override
	public List<AbstractObjective> getObjectives() {
		return Arrays.asList(
			new MinimumNumberObjective(1),
			new MinimumNumberObjective(2),
			new MinimumNumberObjective(3),
			new MinimumNumberObjective(4),
			new MinimumNumberObjective(5),
			new MinimumNumberObjective(6),
			new MinimumNumberObjective(7),
			new MinimumNumberObjective(8),
			new MinimumNumberObjective(9),
			new MinimumNumberObjective(10)		
		);
	}

	@Override
	public String getProblemKey() {
		return "minimum-integer-problem";
	}

	@Override
	public String getProblemName() {
		return "Minimum Integer Problem";
	}
	
	@Override
	public Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException {
		return new MinimumIntegerProblem(instance, objectives);
	}
}
