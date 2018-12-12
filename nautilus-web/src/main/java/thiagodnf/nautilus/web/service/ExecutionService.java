package thiagodnf.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.duplicated.ByVariablesDuplicatesRemover;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.exception.ExecutionNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.repository.ExecutionRepository;
import thiagodnf.nautilus.web.repository.ExecutionRepository.IdsAndDatesOnly;

@Service
public class ExecutionService {
	
	@Autowired
	private ExecutionRepository executionRepository;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	public Execution save(Execution execution) {
		return this.executionRepository.save(execution);
	}
	
	public Execution findById(String executionId) {
		return this.executionRepository.findById(executionId)
				.orElseThrow(ExecutionNotFoundException::new);
	}

	public List<Execution> findAll(String problemKey) {
		return this.executionRepository.findAll();
	}

	public List<IdsAndDatesOnly> findByProblemId(String problemId) {
		return this.executionRepository.findByParametersProblemId(problemId);
	}
	
	public void deleteById(String executionId) {
		executionRepository.deleteById(executionId);
	}
	
	public boolean existsById(String executionId) {
		return executionRepository.existsById(executionId);
	}

	public List<IdsAndDatesOnly> findByPluginIdAndProblemId(String pluginId, String problemId) {
		return this.executionRepository.findByParametersPluginIdAndParametersProblemId(pluginId, problemId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Solution<?>> toJMetalSolutions(Execution execution, boolean useAllObjectives){
		
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();

		List<AbstractObjective> objectives = null;

		if (useAllObjectives) {
			objectives = pluginService.getObjectiveExtension(pluginId, problemId).getObjectives();
		} else {
			objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		}

		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());

		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);

//		InstanceData data = problemExtension.readInstanceData(instance);
		InstanceData data = null;

		@SuppressWarnings("rawtypes")
		Problem problem = problemExtension.getProblem(data, objectives);
		
		List<Solution<?>> population = new ArrayList<>();
		
		//List<thiagodnf.nautilus.core.model.Solution> solutions = new ByVariablesDuplicatesRemover().execute(execution.getSolutions());
		
//		for (thiagodnf.nautilus.core.model.Solution sol : solutions) {
//			
//			Solution<?> s = Converter.toJMetalSolutionWithOutObjectives(problem, sol);
//			
//			problem.evaluate(s);
//			
//			population.add(s);
//		}

		return population;
		

		//return (List<Solution<?>>) Converter.toJMetalSolutions(problem, execution.getSolutions());
	}
}
