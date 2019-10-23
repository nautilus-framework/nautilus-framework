package thiagodnf.nautilus.plugin.zdt.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.problem.AbstractProblemExtension;
import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.plugin.zdt.encoding.instance.TXTInstanceData;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F1Objective;
import thiagodnf.nautilus.plugin.zdt.encoding.objective.F2ForZDT3Objective;
import thiagodnf.nautilus.plugin.zdt.encoding.problem.ZDT3Problem;

@Extension
public class ZDT3ProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new ZDT3Problem(data, objectives);
	}

	@Override
	public String getName() {
		return "ZDT3 Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return DoubleSolution.class;
	}
	
	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new F1Objective());
		objectives.add(new F2ForZDT3Objective());

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
