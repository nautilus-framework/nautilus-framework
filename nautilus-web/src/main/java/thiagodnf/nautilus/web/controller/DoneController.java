package thiagodnf.nautilus.web.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;
import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/done")
public class DoneController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<org.uma.jmetal.solution.Solution<?>> convert(Problem problem, List<Solution> solutions) {

		List<org.uma.jmetal.solution.Solution<?>> initialPopulation = new ArrayList<>();

		for (thiagodnf.nautilus.core.model.Solution sol : solutions) {
			
			org.uma.jmetal.solution.Solution<?> s = Converter.toJMetalSolutionWithOutObjectives(problem, sol);
			
			problem.evaluate(s);
			
			initialPopulation.add(s);
		}

		return initialPopulation;
	}
	
//	@GetMapping("/{executionId}")
//	public String view(Model model, @PathVariable("executionId") String executionId) throws IOException {
//		
//		Execution execution = executionService.findById(executionId);
//		Parameters parameters = execution.getParameters();
//		Settings settings = execution.getSettings();
//		
//		String pluginId = parameters.getPluginId();
//		String problemId = parameters.getProblemId();
//		
//		ObjectiveExtension extension = pluginService.getObjectiveExtension(pluginId);
//		
//		List<AbstractObjective> objectives = extension.getObjectives(problemId);
//		
//		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
//		
//		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
//		
//		InstanceData data = problemExtension.readInstanceData(instance);
//		
//		Problem problem = problemExtension.createProblem(data, objectives);
//		
//		// Calculate
//		
//		List<org.uma.jmetal.solution.Solution<?>> jMetalSolutions = convert(problem, execution.getSolutions());
//		
//		
//		
////		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
////		
////		if (objectives.size() != 1) {
////			solutions = normalizer.normalize(objectives, solutions);
////		}
//		
//		List<PointSolution> referencePoints = new ArrayList<>();
//		
//		
//		referencePoints.add(PointSolutionUtils.createSolution(-0.0, -0.5, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.5, -0.0));
//		
//		
//		
//		double sum = 0.0;
//		
//		System.out.println("========");
//		
//		for(org.uma.jmetal.solution.Solution<?> sol : jMetalSolutions) {
//			
//			List<Double> p1 = Converter.toDoubleList(sol.getObjectives());
//			
//			for(PointSolution rp : referencePoints) {
//				
//				List<Double> p2 = Converter.toDoubleList(rp.getObjectives());
//				
//				sum += EuclideanDistance.calculate(p1, p2);
//			}
//		}
//		
//		double average = (double) sum / ((double) jMetalSolutions.size() * (double) referencePoints.size());
//		
//		
//		
//		
//		System.out.println(average);
//		
//		
////		
////
////		Parameters parameters = execution.getParameters();
////		
////		String problemKey = parameters.getProblemId();
////	
////		AbstractPlugin plugin = pluginService.getPlugin(problemKey);
////		
////		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
////		
//		double[] minimumValues = new double[objectives.size()];
//		double[] maximumValues = new double[objectives.size()];
//		
//		for (int i = 0; i < objectives.size(); i++) {
//			minimumValues[i] = objectives.get(i).getMinimumValue();
//			maximumValues[i] = objectives.get(i).getMaximumValue();
//		}
//		
////		//Path instance = fileService.getInstancesFile(problemKey, parameters.getFilename());
////    	
////		Problem problem = plugin.getProblem(instance, objectives);
////		
////		List<? extends Solution<?>> solutions = Converter.toJMetalSolutions(problem, execution.getSolutions());
////		
////		//
////		
//	    FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues) ;
////
//	    Front referenceFront = new ArrayFront(1, objectives.size());
////	    
//		for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
//			
//			Point point = new ArrayPoint(objectives.size());
//			
//			for (int j = 0; j < objectives.size(); j++) {
//				point.setValue(j, objectives.get(j).getMinimumValue());
//			}
//
//			referenceFront.setPoint(i, point);
//		}
////	    
//	    Front normalizedReferenceFront = frontNormalizer.normalize(new ArrayFront(referenceFront)) ;
////	    
//	    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(jMetalSolutions)) ;
////	    
//	    List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront) ;	
////	
////	    
//	    double number2 = 0.0;
//	    double number9 = 0.0;
//	    double numberOfVariables = 0.0;
//	    
//	    for(thiagodnf.nautilus.core.model.Solution s : execution.getSolutions()) {
//	    	
//	    	
//	    	
//	    	for(Variable v : s.getVariables()) {
//	    		
//	    		if(v.getValue().equalsIgnoreCase("2")) {
//	    			number2++;
//	    		}
//	    		if(v.getValue().equalsIgnoreCase("9")) {
//	    			number9++;
//	    		}
//	    		
//	    		numberOfVariables++;
//	    	}
//	    }
////	    
//	    number2 = (double) number2 / (double) numberOfVariables;
//	    number9 = (double) number9 / (double) numberOfVariables;
//	    
////	    number3 = (double) number3 / (double) variables;
////	    number4 = (double) number4 / (double) variables;
////	    
////	   
//	    model.addAttribute("number2", number2);
//	    model.addAttribute("number9", number9);
////	    model.addAttribute("number3", number3);
////	    model.addAttribute("number4", number4);
////	    
////	   
////	    
////	    //
////	    
////	    model.addAttribute("objectives", objectives);
////		model.addAttribute("plugin", plugin);
////		model.addAttribute("execution", execution);
////		
////		
////		
////		model.addAttribute("epsilon", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
////		
////		model.addAttribute("euclideanDistance", EuclideanDistance.calculate(normalizedReferenceFront, normalizedFront));
//		
//		model.addAttribute("objectives", objectives);
//		model.addAttribute("numberofsolutions", execution.getSolutions().size());
//		model.addAttribute("execution", execution);
//		model.addAttribute("hypervolume", new HypervolumeApprox<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("gd", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("igd", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("epsilon", 0.0);
//		model.addAttribute("spread", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("euclideanDistance", average);
//		
//		
//		
//		
//		return "done";
//	}
	
	@GetMapping("/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) throws IOException {
		
		Execution execution = executionService.findById(executionId);
		Parameters parameters = execution.getParameters();
		Settings settings = execution.getSettings();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		ObjectiveExtension extension = pluginService.getObjectiveExtension(pluginId);
		
		List<AbstractObjective> objectives = extension.getObjectives(problemId);
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
		
		InstanceData data = problemExtension.readInstanceData(instance);
		
		Problem problem = problemExtension.createProblem(data, objectives);
		
		
		List<Solution> solutions = SolutionListUtils.removeRepeated(execution.getSolutions());
		
		// Calculate
		
		List<org.uma.jmetal.solution.Solution<?>> jMetalSolutions = convert(problem, solutions);
		
		
		
//		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
//		
//		if (objectives.size() != 1) {
//			solutions = normalizer.normalize(objectives, solutions);
//		}
		
		List<PointSolution> referencePoints = new ArrayList<>();
		
		
		referencePoints.add(PointSolutionUtils.createSolution(1.0, 0.0, 1.0, 1.0, 1.0, 0.0));
		
		
		
		double sum = 0.0;
		
		System.out.println("========");
		
		for(org.uma.jmetal.solution.Solution<?> sol : jMetalSolutions) {
			
			List<Double> p1 = Converter.toDoubleList(sol.getObjectives());
			
			for(PointSolution rp : referencePoints) {
				
				List<Double> p2 = Converter.toDoubleList(rp.getObjectives());
				
				sum += EuclideanDistance.calculate(p1, p2);
			}
		}
		
		double average = (double) sum / ((double) jMetalSolutions.size() * (double) referencePoints.size());
		
		
		
		System.out.println(average);
		
		// Product 66 and product 67
		
		// Feature: DB (11) and Repository (12)
//		
//
//		Parameters parameters = execution.getParameters();
//		
//		String problemKey = parameters.getProblemId();
//	
//		AbstractPlugin plugin = pluginService.getPlugin(problemKey);
//		
//		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
//		
		double[] minimumValues = new double[objectives.size()];
		double[] maximumValues = new double[objectives.size()];
		
		for (int i = 0; i < objectives.size(); i++) {
			minimumValues[i] = objectives.get(i).getMinimumValue();
			maximumValues[i] = objectives.get(i).getMaximumValue();
		}
		
//		//Path instance = fileService.getInstancesFile(problemKey, parameters.getFilename());
//    	
//		Problem problem = plugin.getProblem(instance, objectives);
//		
//		List<? extends Solution<?>> solutions = Converter.toJMetalSolutions(problem, execution.getSolutions());
//		
//		//
//		
	    FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues) ;
//
	    Front referenceFront = new ArrayFront(1, objectives.size());
//	    
		for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
			
			Point point = new ArrayPoint(objectives.size());
			
			for (int j = 0; j < objectives.size(); j++) {
				point.setValue(j, objectives.get(j).getMinimumValue());
			}

			referenceFront.setPoint(i, point);
		}
//	    
	    Front normalizedReferenceFront = frontNormalizer.normalize(new ArrayFront(referenceFront)) ;
//	    
	    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(jMetalSolutions)) ;
//	    
	    List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront) ;	
//	
//	    
	    
	    Map<Integer, Double> variables = new HashMap<>();
	    
		List<Integer> variablesToFound = Arrays.asList(66, 67, 0, 1);
	    
	    double numberOfVariables = execution.getSolutions().size();
	    
		for (thiagodnf.nautilus.core.model.Solution s : execution.getSolutions()) {

			for(int variable : variablesToFound) {
				
				if(!variables.containsKey(variable)) {
					variables.put(variable, 0.0);
				}
				
				double value = variables.get(variable);
				
				
				if (s.getVariables().get(variable).getValue().equalsIgnoreCase("true")) {
					value++;
				}
				
				variables.put(variable, value);
			}
		}
//	    
//	    number66 = (double) number66 / (double) numberOfVariables;
//	    number67 = (double) number67 / (double) numberOfVariables;
//	    both = (double) both / (double) numberOfVariables;
	    
//	    number3 = (double) number3 / (double) variables;
//	    number4 = (double) number4 / (double) variables;
//	    
//	   
	    
//	    variables.put("number2", number66);
//	    variables.put("number9", number67);
//	    variables.put("both", both);
	    
	    model.addAttribute("variables", variables);
//	    model.addAttribute("number3", number3);
//	    model.addAttribute("number4", number4);
//	    
//	   
//	    
//	    //
//	    
//	    model.addAttribute("objectives", objectives);
//		model.addAttribute("plugin", plugin);
//		model.addAttribute("execution", execution);
//		
//		
//		
//		model.addAttribute("epsilon", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		
//		model.addAttribute("euclideanDistance", EuclideanDistance.calculate(normalizedReferenceFront, normalizedFront));
		
		model.addAttribute("objectives", objectives);
		model.addAttribute("numberofsolutions", SolutionListUtils.removeRepeated(execution.getSolutions()).size());
		model.addAttribute("execution", execution);
		model.addAttribute("hypervolume", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("gd", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("igd", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("epsilon", 0.0);
		model.addAttribute("spread", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("euclideanDistance", average);
		
		
		
		
		return "done";
	}
}
