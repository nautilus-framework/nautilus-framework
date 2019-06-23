package thiagodnf.nautilus.plugin.spl.extension.problem;

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
import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.objective.AliveMutantsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.CostObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NewSimilarityObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.NumberOfProductsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UncoveredPairsObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnimportantFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.encoding.objective.UnselectedFeaturesObjective;
import thiagodnf.nautilus.plugin.spl.encoding.problem.SPLProblem;

@Extension
public class SPLProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new SPLProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "SPL Problem";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return BinarySolution.class;
	}

	@Override
	public List<AbstractObjective> getObjectives() {
		
		List<AbstractObjective> objectives = new ArrayList<>();

		objectives.add(new NumberOfProductsObjective());
		objectives.add(new AliveMutantsObjective());
		objectives.add(new UncoveredPairsObjective());
//		objectives.add(new SimilarityObjective());
		objectives.add(new NewSimilarityObjective());
		objectives.add(new CostObjective());
		objectives.add(new UnselectedFeaturesObjective());
		objectives.add(new UnimportantFeaturesObjective());
		
		return objectives;
	}

	@Override
	public Instance getInstance(Path path) {
		return new NewTXTInstanceData(path);
	}
	
	@Override
    public List<String> getVariablesAsList(Instance instance, Solution<?> solution) {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }
}
