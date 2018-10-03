package thiagodnf.nautilus.plugin.zdt3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.zdt3.objective.F0Objective;
import thiagodnf.nautilus.plugin.zdt3.objective.F1Objective;
import thiagodnf.nautilus.plugin.zdt3.problem.ZDT3Problem;

public class ZDT3Plugin extends AbstractPlugin {

	@Override
	public ObjectiveAdapter getObjectiveAdapter() {
		
		ObjectiveAdapter adapter = new ObjectiveAdapter();
		
		adapter.add(new F0Objective());
		adapter.add(new F1Objective());
		
		return adapter;
	}
	
	@Override
	public String getProblemName() {
		return "ZDT3 Problem";
	}
	
	@Override
	public Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException {
		return new ZDT3Problem(instance, objectives);
	}
	
	@Override
	public List<String> getCrossoverNames(){
		return Arrays.asList("SBXCrossover");
	}
	
	@Override
	public List<String> getMutationNames(){
		return Arrays.asList("PolynomialMutation");
	}
}
