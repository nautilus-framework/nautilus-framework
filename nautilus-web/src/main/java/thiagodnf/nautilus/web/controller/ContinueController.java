package thiagodnf.nautilus.web.controller;

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

import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Solution;
import thiagodnf.nautilus.web.service.ExecutionService;

@Controller
public class ContinueController {
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("/continue/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		Parameters parameters = execution.getParameters();
		
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		List<Solution> selectedSolutions = new ArrayList<>();
		
		// Step 1: Separate the selected solutions
		for(Solution sol : execution.getSolutions()) {
			
			if(sol.getProperties().containsKey("selected")) {
				
				selectedSolutions.add(sol);
			}
		}
		
		// Step 2: Invert the objectives values
		for (Solution sol : selectedSolutions) {

			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
				
				sol.setObjective(i, 1.0 - sol.getObjective(i));
			}
		}
		
		// Step 3: Normalize the objectives between [1,10]
		for (Solution sol : selectedSolutions) {
			
			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
				
				sol.setObjective(i, Normalizer.normalize(sol.getObjective(i), 1, 10, 0, 1));
			}
		}
		
		// Step 4: Apply the user feedback
		for (Solution sol : selectedSolutions) {

			double feedback = 0.0;

			if (sol.getProperties().containsKey("feedback")) {
				
				feedback = Double.valueOf(sol.getProperties().get("feedback"));
			}

			for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
				
				sol.setObjective(i, sol.getObjective(i) + feedback * sol.getObjective(i));
			}
		}
		
		double[] ranking = new double[objectiveKeys.size()];
		
		// Step 5: Sum the objective values
		for (Solution s : selectedSolutions) {

			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
				
				ranking[i] += s.getObjectives().get(i);
			}
		}
		
		model.addAttribute("ranking", Arrays.toString(ranking));
		
		
		Map<String, Double> rankingMap = new HashMap<>();

		for (int i = 0; i < ranking.length; i++) {
			rankingMap.put(objectiveKeys.get(i), ranking[i]);
		}

		rankingMap = sort(rankingMap);
		
		
		double sum = rankingMap.values().stream().reduce(Double::sum).get();
		double average = (double) sum / (double) objectiveKeys.size();
		
		Map<String, Double> selectedMap = new HashMap<>();
		
		for(Entry<String, Double> entry : rankingMap.entrySet()) {
			
			double value = entry.getValue();
			
			if(value >= average) {
				selectedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
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
		
		model.addAttribute("rankingMap", rankingMap);
		model.addAttribute("selectedMap", selectedMap);
		model.addAttribute("execution", execution);
		
		return "continue";
	}
	
	protected Map<String, Double> sort(Map<String, Double> ranking) {
		 return ranking.entrySet().stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
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
