package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reducer.AbstractReducer;
import thiagodnf.nautilus.core.reducer.AbstractReducer.RankingItem;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class ContinueController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("/continue/{executionId}")
	public String continueExecution(Model model, 
			@PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);
		
		Parameters parameters = execution.getParameters();
		Settings settings = execution.getSettings();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		String filename = parameters.getFilename();
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, filename);
		InstanceDataExtension instanceDataExtension = pluginService.getInstanceDataExtension(pluginId, problemId);
		InstanceData data = instanceDataExtension.getInstanceData(instance);
		
		AbstractNormalize normalizer = new ByMaxAndMinValuesNormalize();
		AbstractReducer reducer = pluginService.getReducers().get(settings.getReducerId());
		
		List<AbstractObjective> allObjectives = pluginService.getObjectiveExtension(pluginId, problemId).getObjectives();
		List<AbstractObjective> selectedObjectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveIds());
		
		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
		Problem<?> problem = problemExtension.getProblem(data, selectedObjectives);
		
		List<NSolution<?>> solutions = execution.getSolutions();
		List<NSolution<?>> normalizedSolutions = normalizer.normalize(selectedObjectives, solutions);
		
		List<RankingItem> rankingItems = reducer.execute(problem, data, allObjectives, selectedObjectives, normalizedSolutions);
		
		Parameters nextParameters = execution.getParameters();
		
		nextParameters.getObjectiveIds().clear();
		
		for (RankingItem item : rankingItems) {

			if (item.selected) {
				nextParameters.getObjectiveIds().add(item.objectiveId);
			}
		}

		nextParameters.setLastExecutionId(executionId);
		
		model.addAttribute("execution", execution);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("rankings", rankingItems);
		
		return "continue";
	}
}
