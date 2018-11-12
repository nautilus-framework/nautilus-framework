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
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;
import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.duplicated.ByVariablesDuplicatesRemover;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.util.HypervolumeApprox;

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
			
			org.uma.jmetal.solution.Solution<?> s = Converter.toJMetalSolution(problem, sol);
			
			problem.evaluate(s);
			
			initialPopulation.add(s);
		}

		return initialPopulation;
	}
	
	@GetMapping("/{executionId}")
	public String view(Model model, 
			@PathVariable("executionId") String executionId) throws IOException {
		
		Execution execution = executionService.findById(executionId);
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		ObjectiveExtension extension = pluginService.getObjectiveExtension(pluginId);
		
		List<AbstractObjective> allObjectives = extension.getObjectives(problemId);
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
		
		InstanceData data = problemExtension.readInstanceData(instance);
		
		Problem problem = problemExtension.createProblem(data, allObjectives);
		
		//List<Solution> solutions = SolutionListUtils.removeRepeated(execution.getSolutions());
		
		List<Solution> solutions = execution.getSolutions();
		// Calculate
		
		List<org.uma.jmetal.solution.Solution<?>> jMetalSolutions = convert(problem, solutions);
		
		
		List<PointSolution> referencePoints = new ArrayList<>();
		
//		referencePoints.add(PointSolutionUtils.createSolution(-0.0, -0.0, -0.0, -0.0, -5.0, -0.0, -0.0, -0.0, -0.0, -0.5, -0.0, -0.0, -0.0, -0.0, -0.0));
//		
		
		referencePoints.add(PointSolutionUtils.createSolution(1.0, 0.0, 1.0, 1.0, 1.0, 0.0));
		
		
		
		double sum = 0.0;
		
		for(org.uma.jmetal.solution.Solution<?> sol : jMetalSolutions) {
			
			List<Double> p1 = Converter.toDoubleList(sol.getObjectives());
			
			for(PointSolution rp : referencePoints) {
				
				List<Double> p2 = Converter.toDoubleList(rp.getObjectives());
				
				sum += EuclideanDistance.calculate(p1, p2);
			}
		}
		
		double average = (double) sum / ((double) jMetalSolutions.size() * (double) referencePoints.size());
		
		
		
		double[] minimumValues = new double[allObjectives.size()];
		double[] maximumValues = new double[allObjectives.size()];
		
		for (int i = 0; i < allObjectives.size(); i++) {
			minimumValues[i] = allObjectives.get(i).getMinimumValue();
			maximumValues[i] = allObjectives.get(i).getMaximumValue();
		}

		FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues) ;
	    Front referenceFront = new ArrayFront(1, allObjectives.size());

	    for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
			
			Point point = new ArrayPoint(allObjectives.size());
			
			for (int j = 0; j < allObjectives.size(); j++) {
				point.setValue(j, allObjectives.get(j).getMinimumValue());
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
	    
//		List<Integer> variablesToFound = Arrays.asList(5, 10);
	    List<Integer> variablesToFound = Arrays.asList(66, 67);
	    
		for (thiagodnf.nautilus.core.model.Solution s : execution.getSolutions()) {

			List<String> vars = s.getVariablesValueAsList();

			for (int variable : variablesToFound) {

				if (!variables.containsKey(variable)) {
					variables.put(variable, 0.0);
				}

				int total = 0;

				for (String var : vars) {

					if (var.equalsIgnoreCase(String.valueOf(variable))) {
						total++;
					}
				}

				variables.put(variable, variables.get(variable) + total);
			}
		}
	    
	    model.addAttribute("variables", variables);
		model.addAttribute("objectives", allObjectives);
		model.addAttribute("numberofsolutions", new ByVariablesDuplicatesRemover().execute(execution.getSolutions()).size());
		model.addAttribute("execution", execution);
		model.addAttribute("hypervolume", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("hypervolumeApprox", new HypervolumeApprox<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("gd", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("igd", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("epsilon", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("spread", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
		model.addAttribute("euclideanDistance", average);
		
		return "done";
	}
}
