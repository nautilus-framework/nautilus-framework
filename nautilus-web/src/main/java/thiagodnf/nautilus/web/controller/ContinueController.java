package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.core.colorize.Colorize;
import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.Correlation.CorrelationItem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.problem.AbstractProblem;
import thiagodnf.nautilus.core.solution.BinarySolution;
import thiagodnf.nautilus.plugin.extension.VariableExtension;
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
		
		List<Solution> solutions = execution.getSolutions();
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		
//		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
		
//		if (objectives.size() != 1) {
//			solutions = normalizer.normalize(objectives, solutions);
//		}
		
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
		InstanceData data = pluginService.getProblemExtension(pluginId, problemId).readInstanceData(instance); 
		
		AbstractProblem problem = pluginService.getProblemExtension(pluginId, problemId).createProblem(data, objectives);
		
		Correlation correlation = pluginService.getCorrelationers().get(settings.getCorrelation());
		
		List<CorrelationItem> correlationItems = correlationService.correlateVariables(problem, data, objectives, solutions);
		
		for(CorrelationItem correlationItem : correlationItems) {
			System.out.println(correlationItem);
		}
		
		
		
		model.addAttribute("objectives", objectives);
		model.addAttribute("correlationsForObjectives", correlationService.correlateObjectives(correlation, objectives, solutions));
		model.addAttribute("correlationsForVariables", correlationItems);
		
		
		System.out.println("--------");

		List<CorrelationItem> normalized = correlationService.normalize(correlationItems);
		model.addAttribute("correlationsForVariables", normalized);
		
		for(CorrelationItem correlationItem : normalized) {
			System.out.println(correlationItem);
		}
		System.out.println("--------");
		
		
		
		
		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
		
		if (objectives.size() != 1) {
			solutions = normalizer.normalize(objectives, solutions);
		}
		
		List<Solution> selectedSolutions = new ArrayList<>();
		
		// Step 1: Separate the selected solutions
		for(Solution sol : execution.getSolutions()) {
			if(sol.getProperties().containsKey("selected")) {
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
		
		
		for(Solution s : selectedSolutions) {
			
			System.out.println(s);
			
			for (int i = 0; i < s.getVariables().size(); i++) {

				Variable v = s.getVariables().get(i);
	
				double feedback = v.getUserFeeback();
				
				String value = v.getValue();
				
				if (s.getType().equalsIgnoreCase(BinarySolution.class.getName())) {
					value = String.valueOf(i);
					
					if(v.getValue().equalsIgnoreCase("false")) {
						continue;
					}
				}
				
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//model.addAttribute("plugin", plugin);
		
		
		
		
		
		
		
//		// Step 2: Invert the objectives values
//		for (Solution sol : selectedSolutions) {
//
//			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
//				
//				sol.setObjective(i, 1.0 - sol.getObjective(i));
//			}
//		}
//		
//		// Step 3: Normalize the objectives between [1,10]
//		for (Solution sol : selectedSolutions) {
//			
//			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
//				
//				sol.setObjective(i, Normalizer.normalize(sol.getObjective(i), 1, 10, 0, 1));
//			}
//		}
//		
//		// Step 4: Apply the user feedback
//		for (Solution sol : selectedSolutions) {
//
//			double feedback = 0.0;
//
//			if (sol.getProperties().containsKey("feedback")) {
//				
//				feedback = Double.valueOf(sol.getProperties().get("feedback"));
//			}
//
//			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
//				
//				sol.setObjective(i, sol.getObjective(i) + feedback * sol.getObjective(i));
//			}
//		}
//		
//		double[] ranking = new double[objectiveKeys.size()];
//		
//		// Step 5: Sum the objective values
//		for (Solution s : selectedSolutions) {
//
//			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
//				
//				ranking[i] += s.getObjectives().get(i);
//			}
//		}
//		
		Map<String, Double> rankingMap = new HashMap<>();
//
//		for (int i = 0; i < ranking.length; i++) {
//			rankingMap.put(objectiveKeys.get(i), ranking[i]);
//		}
//
//		rankingMap = sort(rankingMap);
//		
//		
//		double sum = rankingMap.values().stream().reduce(Double::sum).get();
//		double average = (double) sum / (double) objectiveKeys.size();
//		
		Map<String, Double> selectedMap = new HashMap<>();
//		
//		for(Entry<String, Double> entry : rankingMap.entrySet()) {
//			
//			double value = entry.getValue();
//			
//			if(value >= average) {
//				selectedMap.put(entry.getKey(), entry.getValue());
//			}
//		}
//		
//		if(selectedMap.isEmpty()) {
//			selectedMap.putAll(rankingMap);
//		}
//		
//		selectedMap = sort(selectedMap);
//		
//		
//		List<String> selectedObjectives = selectedMap
//				.keySet()
//				.stream()
//				.collect(Collectors.toList());
//		
////		Parameters nextParameters =  execution.getParameters();
////		
////		nextParameters.setObjectiveKeys(selectedObjectives);
////		nextParameters.setLastExecutionId(executionId);
//		
//		
//		model.addAttribute("selectedMap", selectedMap);
		model.addAttribute("execution", execution);
		
		
		
		
		
		
		
		
		
//		PearsonCorrelation p = new PearsonCorrelation();
//		
//		
//		if (objectives.size() != 1) {
//			solutions = pluginService.getNormalizers().get(settings.getNormalize()).normalize(objectives, solutions);
//		}
//		
//		
//		int index = 1;
//
//		Map<String, Integer> map = new HashMap<>();
//
//		List<CorrelationItem> values = new ArrayList<>();
//		
//		for (Solution s : solutions) {
//
//			CorrelationItem item = new CorrelationItem();//List<String> valid = new ArrayList<>();
//
//			for (Variable v : s.getVariables()) {
//
//				if (extension.isValidForCorrelation(problem, s, v)) {
//
//					String value = extension.getValueForCorrelation(problem, s, v);
//
//					item.getVariables().add(value);
//					
//					if (!map.containsKey(value)) {
//						map.put(value, index++);
//					}
//				}
//			}
//			
//			item.setObjectives(s.getObjectives());
//
//			values.add(item);
//		}
//		
//		System.out.println(map);
//		
//		System.out.println(values);
//		
//		System.out.println("-----");
		
		
		
		
		
		//System.out.println(solutions);
		
		// Descomentar
		
//		List<Correlation> correlations = p.calculate(map, values, objectiveKeys.size(), solutions);
//		
//		
		//double[] r = new double[objectives.size()];
//		
//		for(Solution s : selectedSolutions) {
//			
//			System.out.println(s);
//			
//			for (Variable v : s.getVariables()) {
//
//				if (extension.isValidForCorrelation(problem, s, v)) {
//
//					String value = extension.getValueForCorrelation(problem, s, v);
//					
//					double feedback = v.getUserFeeback();
//					
//					for (Correlation c : correlations) {
//	
//						if (c.getVariable().equalsIgnoreCase(value)) {
//	
//							for (int i = 0; i < r.length; i++) {
//	
//								double distance = 0.0;
//								double minDistance = c.getValues().get(i);
//								
//								if (feedback == 0) {
//									distance = minDistance;
//								} else if (feedback > 0) {
//									distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
//								} else {
//									distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
//								}
//								
//								if (Double.isNaN(distance)) {
//									distance = minDistance;
//								}
//								
//								
//								r[i] += distance;
//								//System.out.println(r[i]);
//	//							r[i] += c.getValues().get(i) + c.getValues().get(i) * v.getUserFeeback();
//							}
//							
//							System.out.println(Arrays.toString(r));
//						}
//					}
//				}
//			}
//			
//			
//		}
		
		System.out.println(Arrays.toString(r));
		
		for (int i = 0; i < r.length; i++) {
			rankingMap.put(objectiveKeys.get(i), r[i]);
		}
		
		rankingMap = sort(rankingMap);
		
//		
		double sum = rankingMap.values().stream().reduce(Double::sum).get();
		double average = (double) sum / (double) objectiveKeys.size();
//		
		
		double epsilon = 0.002;
		double lastValue = 0.0;
		
		
		for(Entry<String, Double> entry : rankingMap.entrySet()) {
			
			double value = entry.getValue();
			
			if(value >= average) {
				selectedMap.put(entry.getKey(), entry.getValue());
			}
		}
//		
		if(selectedMap.isEmpty()) {
			selectedMap.putAll(rankingMap);
		}
		
		selectedMap = sort(selectedMap);
		
		
		List<String> selectedObjectives = selectedMap
				.keySet()
				.stream()
				.collect(Collectors.toList());
		
		
		Parameters nextParameters =  execution.getParameters();
		
		nextParameters.setObjectiveKeys(selectedObjectives);
		nextParameters.setLastExecutionId(executionId);
		
		
		model.addAttribute("selectedMap", selectedMap);
		
		
		
		//model.addAttribute("correlations", correlations);
		model.addAttribute("objectiveKeys", objectiveKeys);
		
		model.addAttribute("rankingMap", rankingMap);
		
		return "continue";
	}
	
	protected Map<String, Double> sort(Map<String, Double> ranking) {
		 return ranking.entrySet().stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
	
	protected Map<String, Double> sortDescedent(Map<String, Double> ranking) {
		 return ranking.entrySet().stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}
	
	protected double[] mean(double[] array, double size) {
		
		if (size == 0) {
			return array;
		}

		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = (array[i] / size);
		}

		return newArray;
	}
	
	protected double[] meanInverse(double[] array, double size) {
		
		if (size == 0) {
			return array;
		}

		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = 1.0 - (array[i] / size);
		}

		return newArray;
	}
	
}
