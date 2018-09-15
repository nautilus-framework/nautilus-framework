package thiagodnf.nautilus.plugin.zdt1.problem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.solution.DoubleSolution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ZDT1Problem extends ZDT1 {

	private static final long serialVersionUID = -3013657610039035283L;

	private List<AbstractObjective> objectives;

	public ZDT1Problem(Path instance, List<AbstractObjective> objectives) throws IOException  {
		super(getNumberOfVariables(instance));
		this.objectives = objectives;
	}

	private static Integer getNumberOfVariables(Path instance) throws IOException  {
		return Integer.valueOf(FileUtils.readFileToString(instance.toFile()));
	}

	@Override
	public void evaluate(DoubleSolution solution) {

		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(solution));
		}
	}
}
