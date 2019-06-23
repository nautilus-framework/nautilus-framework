package thiagodnf.nautilus.plugin.nrp.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.problem.AbstractProblemExtension;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.ImportanceObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.NumberOfRequirementsObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.ProfitObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.objective.TasksObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.problem.NRPProblem;

@Extension
public class NRPProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new NRPProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "NRP Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return BinarySolution.class;
	}

	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfRequirementsObjective());
		objectives.add(new CostObjective());
		objectives.add(new ProfitObjective());
		objectives.add(new ImportanceObjective());
		objectives.add(new TasksObjective());

		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new TXTInstance(path);
	}
	
	@Override
    public List<String> getVariablesAsList(Instance instance, Solution<?> solution) {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }
}
