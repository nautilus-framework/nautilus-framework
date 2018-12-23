package thiagodnf.nautilus.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;

import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.repository.ExecutionRepository.ExecutionSimplified;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.ParetoFrontService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/done/{executionId:.+}")
public class DoneController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ParetoFrontService paretoFrontService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("")
	public String show(Model model, 
			@PathVariable("executionId") String executionId){
		
		Execution execution = executionService.findById(executionId);
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		String filename = parameters.getFilename();
		
		String objectives = parameters
				.getObjectiveIds()
				.stream()
				.sorted()
				.reduce((a,b) -> a + "-" + b).get();
		
		String paretoFrontName = paretoFrontService.getParetoFrontName(problemId, filename, objectives);

		List<ExecutionSimplified> paretoFronts = executionService.findByName(pluginId, problemId, paretoFrontName);

		if (paretoFronts.isEmpty()) {
			return "redirect/execution/" + executionId;
		} else if (paretoFronts.size() > 1) {
			return "redirect/execution/" + executionId;
		}

		Execution pareto = executionService.findById(paretoFronts.get(0).getId());
		
		Front paretoFront = new ArrayFront(pareto.getSolutions());
		Front executionFront = new ArrayFront(execution.getSolutions());
		
		FrontNormalizer frontNormalizer = new FrontNormalizer(paretoFront);
		
		Front normalizedParetoFront = frontNormalizer.normalize(paretoFront);
		Front normalizedExecutionFront = frontNormalizer.normalize(executionFront);
		
		List normalizedExecution = FrontUtils.convertFrontToSolutionList(normalizedExecutionFront);

		Map<String, Double> map = new HashMap<>();

		for (IndicatorExtension extension : pluginService.getIndicatorFactory(pluginId).getExtensions()) {
			map.put(extension.getName(), extension.getIndicator(normalizedParetoFront).evaluate(normalizedExecution));
		}
		
		model.addAttribute("indicators", map);
		model.addAttribute("execution", execution);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
	   
		return "done";
	}
}
