package org.nautilus.plugin.toy.extension.problem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.solution.NIntegerSolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.extension.problem.AbstractProblemExtension;
import org.nautilus.plugin.toy.encoding.instance.TXTInstance;
import org.nautilus.plugin.toy.encoding.objective.NumberOfObjective;
import org.nautilus.plugin.toy.encoding.problem.ToyProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;

public class ToyProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new ToyProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "Toy Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return IntegerSolution.class;
	}

	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfObjective(1));
		objectives.add(new NumberOfObjective(2));
		objectives.add(new NumberOfObjective(3));
		objectives.add(new NumberOfObjective(4));
		objectives.add(new NumberOfObjective(5));
		objectives.add(new NumberOfObjective(6));

		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new TXTInstance(path);
	}

    @Override
    public List<String> getVariablesAsList(Instance instance, Solution<?> solution) {

        NIntegerSolution sol = (NIntegerSolution) solution;

        List<String> variables = new ArrayList<>();

        for (int i = 0; i < sol.getNumberOfVariables(); i++) {
            variables.add(String.valueOf(sol.getVariableValue(i)));
        }

        return variables;
    }
    
    @Override
    public List<Path> getInstancePaths() {

        List<Path> allInstances = new ArrayList<>();

        allInstances.add(getInstanceFolder().resolve("1-to-020-w-005.txt"));
        allInstances.add(getInstanceFolder().resolve("1-to-100-w-100.txt"));
        allInstances.add(getInstanceFolder().resolve("1-to-100-w-010.txt"));

        return allInstances;
    }
    
    private Path getInstanceFolder() {
        
        return Paths.get("nautilus-framework")
                .resolve("nautilus-plugin-toy")
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("instances");
    }
}
