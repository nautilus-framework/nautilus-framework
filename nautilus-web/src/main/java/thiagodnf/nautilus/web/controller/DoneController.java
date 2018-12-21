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
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.factory.IndicatorFactory;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/done/{executionId:.+}")
public class DoneController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("")
	public String show(Model model, 
			@PathVariable("executionId") String executionId){
		
		Execution execution = executionService.findById(executionId);
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
//		Front referenceFront = new ArrayFront(paretoFrontFile);
//		FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
//
//		Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
//		Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
//		List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

		Map<String, Double> map = new HashMap<>();

		for (IndicatorExtension extension : pluginService.getIndicatorFactory(pluginId).getExtensions()) {
			map.put(extension.getName(), extension.getIndicator(null).evaluate(null));
		}
		
		
//		String outputString = "\n";
//		outputString += "Hypervolume (N) : "+ new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
		
		
		
		
		
//		
//		List<Execution> executions = new ArrayList<>();
//
//		for (String executionId : executionIds) {
//			executions.add(executionService.findById(executionId));
//		}
//
//		List<List<Solution<?>>> paretoFronts = new ArrayList<>();
//
//		for (Execution execution : executions) {
//			paretoFronts.add(executionService.toJMetalSolutions(execution, useAllObjectives));
//		}
//		
//		
//		
//		List<List<PointSolution>> virtualParetoFronts = new LinkedList<>();
//		
//		// SPL
//		PointSolution zr = PointSolutionUtils.createSolution(1.0, 0.0, 1.0, 1.0, 1.0, 0.0);
//		
////		PointSolution zr = PointSolutionUtils.createSolution(0.0, -1.0, 0.0, -1.0, 0.0, 0.0);
////		PointSolution zr = PointSolutionUtils.createSolution(-0.9, -0.3);
////		PointSolution zr = PointSolutionUtils.createSolution(-0.1, -0.9);
//	    
//	    double delta = 0.1;
//	    
//	    RMetric rMetric = new RMetric(zr, delta);
//	    
//		for (int i = 0; i < paretoFronts.size(); i++) {
//			
//			List<PointSolution> ps = PointSolutionUtils.convert(paretoFronts.get(i));
//			
//			virtualParetoFronts.add(rMetric.execute(ps));
//		}
//		
//		System.out.println(virtualParetoFronts);
//		
////		Front virtualNadirPoint = new ArrayFront(Arrays.asList(PointSolutionUtils.createSolution(-3.0,-3.0, -3.0, -3.0, -3.0, -3.0)));
//		
//		List<PointSolution> nadirPoint = new ArrayList<>();
//		
//		nadirPoint.add(PointSolutionUtils.createSolution(2.0, 2.0, 2.0, 2.0, 2.0, -2.0));
//		nadirPoint.add(PointSolutionUtils.createSolution(2.0, 2.0, 2.0, 2.0, -2.0, 2.0));
//		nadirPoint.add(PointSolutionUtils.createSolution(2.0, 2.0, 2.0, -2.0, 2.0, 2.0));
//		nadirPoint.add(PointSolutionUtils.createSolution(2.0, 2.0, -2.0, 2.0, 2.0, 2.0));
//		nadirPoint.add(PointSolutionUtils.createSolution(2.0, -2.0, 2.0, 2.0, 2.0, 2.0));
//		nadirPoint.add(PointSolutionUtils.createSolution(-2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
//		
//		Front virtualNadirPoint = new ArrayFront(nadirPoint);
//		
//		
//		FrontNormalizer virtualFrontNormalizer = new FrontNormalizer(virtualNadirPoint);
//		
//		
//		
//		
//		// Generate the pfknown
//		
//		List<Solution<?>> pfknown = SolutionListUtils.getNondominatedSolutions(paretoFronts);
//
//			
//		Front referenceFront = new ArrayFront(pfknown);
//		
//		
//		List<AbstractObjective> objectives = null;
//
//		if (useAllObjectives) {
//			objectives = pluginService.getObjectiveExtension(pluginId).getObjectives(problemId);
//		} else {
//			objectives = pluginService.getObjectivesByIds(pluginId, problemId, executions.get(0).getParameters().getObjectiveKeys());
//		}
//		
//		double[] minValues = new double[objectives.size()];
//		double[] maxValues = new double[objectives.size()];
//		
//		for (int i = 0; i < objectives.size(); i++) {
//			
//			minValues[i] = objectives.get(i).getMinimumValue();
//			maxValues[i] = objectives.get(i).getMaximumValue();
//		}
//		
//	    FrontNormalizer frontNormalizer = new FrontNormalizer(minValues, maxValues) ;
//	    
//	    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
//	    
//	    RowItem header = new RowItem("Algorithm");
//	    
//	    List<QualityIndicator<?,?>> indicators = new ArrayList<>();
//	    
//	    QualityIndicatorFactory qaFactory = pluginService.getQualityIndicatorFactory(pluginId);
//	    
//	    for(String qualityIndicator : qualityIndicators) {
//	    	
//	    	indicators.add(qaFactory.getQualityIndicator(qualityIndicator, normalizedReferenceFront));
//	    	
//	    	header.getValues().add(qualityIndicator);
//		}
//	    
//	    
//	    header.getValues().add("R-HV");
//	    header.getValues().add("R-IGD");
//	    header.getValues().add("R-SPREAD");
//	    header.getValues().add("# Solutions");
//	    header.getValues().add("# Solutions in ROI");
//	    
//	    
//	    
//	    
//	    
//	    
//	 // Trim the pareto-front values
//		
//	List<PointSolution> paretoFront = FrontUtils.convertFrontToSolutionList(referenceFront);
//	
//	PointSolution zp = rMetric.pivotPointIdentification(paretoFront);
//	
//	List<PointSolution> trimmedParetoFront = rMetric.trimmingProcedure(zp, paretoFront);
//	    
//	    
//	    
//	    
//	    
//	    List<RowItem> rows = new ArrayList<>();
//	    
//		for (int i = 0; i < paretoFronts.size(); i++) {
//
//			List<? extends org.uma.jmetal.solution.Solution<?>> solutions = paretoFronts.get(i);
//
//			Front normalizedFront = frontNormalizer.normalize(new ArrayFront(solutions));
//
//			List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
//
//			RowItem item = new RowItem(executions.get(i).getTitle());
//
//			for (QualityIndicator indicator : indicators) {
//				item.getValues().add(indicator.evaluate(normalizedPopulation));
//			}
//			
//			List<? extends org.uma.jmetal.solution.Solution<?>> virtualSolutions = virtualParetoFronts.get(i);
//			
//			Front virtualNormalizedFront = virtualFrontNormalizer.normalize(new ArrayFront(virtualSolutions));
//			
//			
//			List<PointSolution> virtualNormalizedPopulation = FrontUtils.convertFrontToSolutionList(virtualNormalizedFront);
//			
//			item.getValues().add(new RHypervolume(zr, delta).evaluate(virtualNormalizedPopulation));
//			
//			Front normalizedTrimmedReferenceFront = virtualFrontNormalizer.normalize(new ArrayFront(trimmedParetoFront));
//			item.getValues().add(new RInvertedGenerationalDistance(zr, delta, normalizedTrimmedReferenceFront).evaluate(virtualNormalizedPopulation));
//			
//			item.getValues().add(new RSpread(zr, delta, normalizedTrimmedReferenceFront).evaluate(virtualNormalizedPopulation));
//			
//			
//			item.getValues().add(paretoFronts.get(i).size());
//			
//			List<PointSolution> ps = PointSolutionUtils.convert(paretoFronts.get(i));
//			
//			item.getValues().add(new NumberOfSolutionInROI(zr, delta).evaluate(ps));
//			
//
//			rows.add(item);
//		}
//	    
//	    model.addAttribute("header", header);
//	    model.addAttribute("rows", rows);
		
		model.addAttribute("map", map);
		model.addAttribute("execution", execution);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
//	   
		return "done";
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public List<org.uma.jmetal.solution.Solution<?>> convert(Problem problem, List<Solution> solutions) {
//
//		List<org.uma.jmetal.solution.Solution<?>> initialPopulation = new ArrayList<>();
//
//		for (thiagodnf.nautilus.core.model.Solution sol : solutions) {
//			
//			org.uma.jmetal.solution.Solution<?> s = Converter.toJMetalSolution(problem, sol);
//			
//			problem.evaluate(s);
//			
//			initialPopulation.add(s);
//		}
//
//		return initialPopulation;
//	}
	
//	@GetMapping("/{executionId}")
//	public String view(Model model, 
//			@PathVariable("executionId") String executionId) throws IOException {
//		
//		Execution execution = executionService.findById(executionId);
//		Parameters parameters = execution.getParameters();
//		
//		String pluginId = parameters.getPluginId();
//		String problemId = parameters.getProblemId();
//		
//		ObjectiveExtension extension = pluginService.getObjectiveExtension(pluginId);
//		
//		List<AbstractObjective> allObjectives = extension.getObjectives(problemId);
//		
//		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
//		
//		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
//		
//		InstanceData data = problemExtension.readInstanceData(instance);
//		
//		Problem problem = problemExtension.createProblem(data, allObjectives);
//		
//		//List<Solution> solutions = SolutionListUtils.removeRepeated(execution.getSolutions());
//		
//		List<Solution> solutions = execution.getSolutions();
//		// Calculate
//		
//		List<org.uma.jmetal.solution.Solution<?>> jMetalSolutions = convert(problem, solutions);
//		
//		
//		List<PointSolution> referencePoints = new ArrayList<>();
//		
////		referencePoints.add(PointSolutionUtils.createSolution(-0.0, -0.0, -0.0, -0.0, -5.0, -0.0, -0.0, -0.0, -0.0, -0.5, -0.0, -0.0, -0.0, -0.0, -0.0));
////		
//		
//		referencePoints.add(PointSolutionUtils.createSolution(1.0, 0.0, 1.0, 1.0, 1.0, 0.0));
//		
//		
//		
//		double sum = 0.0;
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
//		double[] minimumValues = new double[allObjectives.size()];
//		double[] maximumValues = new double[allObjectives.size()];
//		
//		for (int i = 0; i < allObjectives.size(); i++) {
//			minimumValues[i] = allObjectives.get(i).getMinimumValue();
//			maximumValues[i] = allObjectives.get(i).getMaximumValue();
//		}
//
//		FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues) ;
//	    Front referenceFront = new ArrayFront(1, allObjectives.size());
//
//	    for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
//			
//			Point point = new ArrayPoint(allObjectives.size());
//			
//			for (int j = 0; j < allObjectives.size(); j++) {
//				point.setValue(j, allObjectives.get(j).getMinimumValue());
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
//	    
//	    Map<Integer, Double> variables = new HashMap<>();
//	    
////		List<Integer> variablesToFound = Arrays.asList(5, 10);
//	    List<Integer> variablesToFound = Arrays.asList(66, 67);
//	    
//		for (thiagodnf.nautilus.core.model.Solution s : execution.getSolutions()) {
//
//			List<String> vars = s.getVariablesValueAsList();
//
//			for (int variable : variablesToFound) {
//
//				if (!variables.containsKey(variable)) {
//					variables.put(variable, 0.0);
//				}
//
//				int total = 0;
//
//				for (String var : vars) {
//
//					if (var.equalsIgnoreCase(String.valueOf(variable))) {
//						total++;
//					}
//				}
//
//				variables.put(variable, variables.get(variable) + total);
//			}
//		}
//	    
//	    model.addAttribute("variables", variables);
//		model.addAttribute("objectives", allObjectives);
//		model.addAttribute("numberofsolutions", new ByVariablesDuplicatesRemover().execute(execution.getSolutions()).size());
//		model.addAttribute("execution", execution);
//		model.addAttribute("hypervolume", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("hypervolumeApprox", new HypervolumeApprox<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("gd", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("igd", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("epsilon", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("spread", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//		model.addAttribute("euclideanDistance", average);
//		
//		return "done";
//	}
}
