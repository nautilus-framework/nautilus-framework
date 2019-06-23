package thiagodnf.nautilus.plugin.zdt.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.gui.Tab;
import thiagodnf.nautilus.core.gui.TableTabContent;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.problem.AbstractProblemExtension;
import thiagodnf.nautilus.plugin.zdt.encoding.instance.TXTInstanceData;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F1Objective;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F2ForZDT1Objective;
import thiagodnf.nautilus.plugin.zdt.encoding.problem.ZDT1Problem;

@Extension
public class ZDT1ProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new ZDT1Problem(data, objectives);
	}

	@Override
	public String getName() {
		return "ZDT1 Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return DoubleSolution.class;
	}

	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new F1Objective());
		objectives.add(new F2ForZDT1Objective());

		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new TXTInstanceData(path);
	}
	
	@Override
    public List<String> getVariablesAsList(Instance instance, Solution<?> solution) {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }
}
