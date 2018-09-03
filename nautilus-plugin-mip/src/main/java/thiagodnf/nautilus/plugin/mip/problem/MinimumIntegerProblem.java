package thiagodnf.nautilus.plugin.mip.problem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public class MinimumIntegerProblem extends AbstractIntegerProblem {

	private static final long serialVersionUID = 5347625751550400087L;

	private int lowerBound = 1;
	
	private int upperBound = 10;
	
	private List<AbstractObjective> objectives;

	public MinimumIntegerProblem(Path path, List<AbstractObjective> objectives) throws IOException {
		
		this.objectives = objectives;

		String content = FileUtils.readFileToString(path.toFile());
		
		int size = Integer.valueOf(content);
		
		// JMetal's Settings
		setNumberOfVariables(size);
		setNumberOfObjectives(objectives.size());
		setName(MinimumIntegerProblem.class.getSimpleName());
		
		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lowerBound);
			upperLimit.add(upperBound);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(IntegerSolution solution) {

		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(solution));
		}
	}

}
