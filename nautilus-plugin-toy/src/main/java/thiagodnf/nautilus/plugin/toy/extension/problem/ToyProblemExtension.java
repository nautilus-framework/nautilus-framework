package thiagodnf.nautilus.plugin.toy.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.problem.AbstractProblemExtension;
import thiagodnf.nautilus.plugin.gui.Tab;
import thiagodnf.nautilus.plugin.gui.TableTabContent;
import thiagodnf.nautilus.plugin.toy.encoding.instance.TXTInstance;
import thiagodnf.nautilus.plugin.toy.encoding.objective.NumberOfObjective;
import thiagodnf.nautilus.plugin.toy.encoding.problem.ToyProblem;

@Extension
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

		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new TXTInstance(path);
	}
	
	@Override
	public List<Tab> getTabs(Instance data) {
		return Arrays.asList(getContentTab(data));
	}

	protected Tab getContentTab(Instance data) {

		TXTInstance d = (TXTInstance) data;

		TableTabContent table = new TableTabContent("Key", "Value");

		table.addRow("Lower Bound", d.getLowerBound());
		table.addRow("Upper Bound", d.getUpperBound());
		table.addRow("Number of Variables", d.getNumberOfVariables());

		return new Tab("Content", table);
	}
}
