package org.nautilus.plugin.nrp.extension.problem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.extension.problem.AbstractProblemExtension;
import org.nautilus.plugin.nrp.encoding.instance.TXTInstance;
import org.nautilus.plugin.nrp.encoding.objective.CostObjective;
import org.nautilus.plugin.nrp.encoding.objective.ImportanceObjective;
import org.nautilus.plugin.nrp.encoding.objective.NumberOfRequirementsObjective;
import org.nautilus.plugin.nrp.encoding.objective.ProfitObjective;
import org.nautilus.plugin.nrp.encoding.objective.TasksObjective;
import org.nautilus.plugin.nrp.encoding.problem.NRPProblem;
import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

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
       
	    
	    TXTInstance data = (TXTInstance) instance;
        
        NBinarySolution sol = (NBinarySolution) solution;
        
        BinarySet binarySet = sol.getVariableValue(0);
        
        List<String> variables = new ArrayList<>();
        
        for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

            if (binarySet.get(i)) {
                variables.add("Requirement #"+i+": "+data.getRequirement(i));
            }
        }
        
        return variables;
	    
    }
}
