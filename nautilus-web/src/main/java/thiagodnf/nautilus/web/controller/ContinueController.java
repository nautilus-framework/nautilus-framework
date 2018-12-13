package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.Correlation.CorrelationItem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.problem.AbstractProblem;
import thiagodnf.nautilus.core.ranking.RankingComparator;
import thiagodnf.nautilus.core.ranking.RankingItem;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.CorrelationService;
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
	
	@Autowired
	private CorrelationService correlationService;
	
	@GetMapping("/continue/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);

		Parameters parameters = execution.getParameters();
		
		Settings settings = execution.getSettings();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
//		List<? extends Solution<?>> solutions = execution.getSolutions();
		List<GenericSolution> solutions = execution.getSolutions();
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
//		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, Arrays.asList("alive-mutants", "cost"));
		
		
//		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
		
//		if (objectives.size() != 1) {
//			solutions = normalizer.normalize(objectives, solutions);
//		}
		
		//List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
//		InstanceData data = pluginService.getProblemExtension(pluginId, problemId).readInstanceData(instance);
		InstanceData data = null;
		
//		AbstractProblem problem = pluginService.getProblemExtension(pluginId, problemId).createProblem(data, objectives);
		AbstractProblem problem = null;
		
		Correlation correlation = pluginService.getCorrelationers().get(settings.getCorrelation());
		
//		List<CorrelationItem> correlationItems = correlationService.correlateVariables(problem, data, objectives, solutions);
		List<CorrelationItem> correlationItems = Arrays.asList();
		
		for(CorrelationItem correlationItem : correlationItems) {
			System.out.println(correlationItem);
		}
		
		
		
		model.addAttribute("objectives", objectives);
		//model.addAttribute("correlationsForObjectives", correlationService.correlateObjectives(correlation, objectives, solutions));
		model.addAttribute("correlationsForVariables", correlationItems);
		
		
		System.out.println("--------");

		List<CorrelationItem> normalized = correlationService.normalize(correlationItems, objectives);
		//model.addAttribute("correlationsForVariables", normalized);
		
		for(CorrelationItem correlationItem : normalized) {
			System.out.println(correlationItem);
		}
		System.out.println("--------");
		
		
		
		
		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
		
//		if (objectives.size() != 1) {
//			solutions = normalizer.normalize(objectives, solutions);
//		}
		
		List<GenericSolution> selectedSolutions = new ArrayList<>();
		
		// Step 1: Separate the selected solutions
		for(GenericSolution sol : execution.getSolutions()) {
			
			if(sol.getAttributes().get("selected") != null) {
				selectedSolutions.add(sol);
			}
		}
		
		double r[] = new double[objectives.size()];
		
		// First option
		
//		for(Solution s : selectedSolutions) {
//		
//			System.out.println(s);
//			
//			for (int i = 0; i < s.getVariables().size(); i++) {
//
//				Variable v = s.getVariables().get(i);
//	
//				double feedback = v.getUserFeeback();
//				
//				String value = v.getValue();
//				
//				if (s.getType().equalsIgnoreCase(BinarySolution.class.getName())) {
//					value = String.valueOf(i);
//					
//					if(v.getValue().equalsIgnoreCase("false")) {
//						continue;
//					}
//				}
//				
//				CorrelationItem item = null;
//				
//				for (CorrelationItem c : correlationItems) {
//					if(c.getName().equalsIgnoreCase(value)) {
//						item = c;
//						break;
//					}
//				}
//				
//				for (int j = 0; j < r.length; j++) {
//
//					double distance = 0.0;
//					double minDistance = item.getValues().get(j);
//					
//					if (feedback == 0) {
//						distance = minDistance;
//					} else if (feedback > 0) {
//						distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
//					} else {
//						distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
//					}
//					
//					if (Double.isNaN(distance)) {
//						distance = minDistance;
//					}
//					
//					
//					r[j] += distance;
//				}
//				
//				System.out.println(Arrays.toString(r));
//
//			}
//		}
		
		
		for(GenericSolution s : selectedSolutions) {
			
			System.out.println(s);
			
			for (int i = 0; i < s.getNumberOfVariables(); i++) {

				//Variable v = s.getVariables().get(i);
				Variable v = new Variable();
	
				double feedback = v.getUserFeeback();
				
				String value = v.getValue();
				
//				if (s.getType().equalsIgnoreCase(BinarySolution.class.getName())) {
//					value = String.valueOf(i);
//					
//					if(v.getValue().equalsIgnoreCase("false")) {
//						continue;
//					}
//				}
				
				CorrelationItem item = null;
				
				for (CorrelationItem c : normalized) {
					if(c.getName().equalsIgnoreCase(value)) {
						item = c;
						break;
					}
				}
				
				for (int j = 0; j < r.length; j++) {

					double distance = 0.0;
					double minDistance = item.getValues().get(j);
					
					if (feedback == 0) {
						distance = minDistance;
					} else if (feedback > 0) {
						distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
					} else {
						distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
					}
					
					if (Double.isNaN(distance)) {
						distance = minDistance;
					}
					
					
					r[j] += distance;
				}
				
				//

			}
		}
		
		
		
		
		System.out.println(Arrays.toString(r));
		
		
		
		
		
		List<RankingItem> items = new ArrayList<>();

		for (int i = 0; i < r.length; i++) {
			items.add(new RankingItem(objectives.get(i), r[i]));
		}

		Collections.sort(items, new RankingComparator(false));

		double sum = items.stream().map(e -> e.getValue()).reduce(Double::sum).get();
		double average = (double) sum / (double) items.size();

		List<RankingItem> selectedItems = items.stream()
				.filter(e -> e.getValue() >= average)
				.collect(Collectors.toList());

		Parameters nextParameters = execution.getParameters();

		List<String> selectedObjectiveIds = selectedItems.stream()
				.map(e -> e.getId())
				.collect(Collectors.toList());

		nextParameters.setObjectiveKeys(Arrays.asList("alive-mutants", "cost"));
//		nextParameters.setObjectiveKeys(Arrays.asList("number-of-2s", "number-of-4s"));
		nextParameters.setLastExecutionId(executionId);
		
		model.addAttribute("rankingItems", items);
		model.addAttribute("rankingSelectedItems", selectedItems);
		model.addAttribute("execution", execution);
		
		return "continue";
	}
}
