package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.web.model.Execution;
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
		
		Execution execution = executionService.findExecutionById(executionId);
		
		String problemId = execution.getProblemId();
		String filename = execution.getInstance();
		
//		Path instance = fileService.getInstance(problemId, filename);
//		InstanceExtension instanceDataExtension = pluginService.getInstanceDataExtension(pluginId, problemId);
//		Instance data = instanceDataExtension.getInstanceData(instance);
//		
//		AbstractNormalize normalizer = new ByParetoFrontValuesNormalize();
//		AbstractReduction reduction = pluginService.getReducers().get(settings.getReducerId());
//		
//		List<AbstractObjective> selectedObjectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveIds());
//		
//		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
//		NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(data, selectedObjectives);
//		
//		List<NSolution<?>> solutions = execution.getSolutions();
//		List<NSolution<?>> normalizedSolutions = normalizer.normalize(selectedObjectives, solutions);
//		
//		List<RankingItem> rankingItems = reduction.execute(problem, normalizedSolutions, null);
//		
//		Parameters nextParameters = execution.getParameters();
//		
//		nextParameters.getObjectiveIds().clear();
//		
//		for (RankingItem item : rankingItems) {
//
//			if (item.isSelected()) {
//				nextParameters.getObjectiveIds().add(item.getObjectiveId());
//			}
//		}
//
//		nextParameters.setLastExecutionId(executionId);
//		
//		model.addAttribute("execution", execution);
//		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
//		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
//		model.addAttribute("rankings", rankingItems);
		
		return "continue";
	}
}
