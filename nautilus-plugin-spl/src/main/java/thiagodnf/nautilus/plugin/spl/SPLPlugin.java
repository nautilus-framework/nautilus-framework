package thiagodnf.nautilus.plugin.spl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.distance.JaccardDistance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.spl.objective.NumberOfProducts;
import thiagodnf.nautilus.plugin.spl.problem.SoftwareProductLineProblem;

public class SPLPlugin extends AbstractPlugin {

	@Override
	public ObjectiveAdapter getObjectiveAdapter() {
		
		ObjectiveAdapter adapter = new ObjectiveAdapter();
		
		adapter.add(new NumberOfProducts());
		
		return adapter;
	}

	@Override
	public String getProblemName() {
		return "Maximum Integer Problem";
	}
	
	@Override
	public Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException {
		return new SoftwareProductLineProblem(instance, objectives);
	}
	
	@Override
	public double getSimilarityDistance(List<String> variables1, List<String> variables2) {
		return JaccardDistance.calculate(variables1, variables2);
	}
}
