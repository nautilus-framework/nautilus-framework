package thiagodnf.nautilus.web.util;

import java.io.IOException;

import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;

public class RNSGAIIRunner extends AbstractAlgorithmRunner {
	  /**
	   * @param args Command line arguments.
	   * @throws JMetalException
	 * @throws IOException 
	   */
	  public static void main(String[] args) throws JMetalException, IOException {
	   
		  
		  
//		  Parameters parameters = new Parameters();
//			
//			parameters.setFilename("050.txt");
//			parameters.setCrossoverDistribution(20.0);
//			parameters.setCrossoverName("IntegerSBXCrossover");
//			parameters.setCrossoverProbability(0.9);
//			
//			parameters.setMutationDistribution(20.0);
//			parameters.setMutationName("IntegerPolynomialMutation");
//			parameters.setMutationProbability(0.005);
//			
//			parameters.setPopulationSize(100);
//			parameters.setMaxEvaluations(500000);
//			parameters.setProblemKey(new MIPPlugin().getProblemKey());
//		  
//		  
//		  
//		  
//		  
//		  Problem<DoubleSolution> problem;
//	    Algorithm<List<DoubleSolution>> algorithm;
//	    CrossoverOperator<DoubleSolution> crossover;
//	    MutationOperator<DoubleSolution> mutation;
//	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
//	    String referenceParetoFront = "" ;
//
//	    String problemName ;
//	    if (args.length == 1) {
//	      problemName = args[0];
//	    } else if (args.length == 2) {
//	      problemName = args[0] ;
//	      referenceParetoFront = args[1] ;
//	    } else {
//	      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
//	      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf" ;
//	    }
//
//	    problem =new ZDT1();//  ProblemUtils.<DoubleSolution> loadProblem(problemName);//Tanaka();//
//
//	    double crossoverProbability = 0.9 ;
//	    double crossoverDistributionIndex = 20.0 ;
//	    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
//
//	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
//	    double mutationDistributionIndex = 20.0 ;
//	    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
//
//	    selection = new BinaryTournamentSelection<DoubleSolution>(
//	        new RankingAndCrowdingDistanceComparator<DoubleSolution>());
//
//	    List<Double> referencePoint = new ArrayList<>() ;
//
//	    /*referencePoint.add(0.0) ;
//	    referencePoint.add(1.0) ;
//	    referencePoint.add(1.0) ;
//	    referencePoint.add(0.0) ;
//	    referencePoint.add(0.5) ;
//	    referencePoint.add(0.5) ;
//	    referencePoint.add(0.2) ;
//	    referencePoint.add(0.8) ;
//	    referencePoint.add(0.8) ;
//	    referencePoint.add(0.2) ;*/
//	    //Example fig 2 paper Deb
////	    referencePoint.add(0.2) ;
////	    referencePoint.add(0.4) ;
////	    referencePoint.add(0.8) ;
////	    referencePoint.add(0.4) ;
//	    
//	    referencePoint.add(0.2) ;
//	    referencePoint.add(0.4) ;
//	    
//	    //Example fig 3 paper Deb
////	    referencePoint.add(0.1) ;
////	    referencePoint.add(0.6) ;
////
////	    referencePoint.add(0.3) ;
////	    referencePoint.add(0.6) ;
////
////	    referencePoint.add(0.5) ;
////	    referencePoint.add(0.2) ;
////
////	    referencePoint.add(0.7) ;
////	    referencePoint.add(0.2) ;
////
////	    referencePoint.add(0.9) ;
////	    referencePoint.add(0.0) ;
//	    /*referencePoint.add(0.1) ;
//	    referencePoint.add(1.0) ;
//	    referencePoint.add(1.0) ;
//	    referencePoint.add(0.0) ;
//
//	    referencePoint.add(0.5) ;
//	    referencePoint.add(0.8);
//	    referencePoint.add(0.8) ;
//	    referencePoint.add(0.6) ;*/
//	    //referencePoint.add(0.0) ;
//	    //referencePoint.add(1.0);
//
//	    //referencePoint.add(1.0) ;
//	    //referencePoint.add(0.6);
//	    //referencePoint.add(0.4) ;
//	    //referencePoint.add(0.8);
//
//	    double epsilon= 0.0001;
//
//
//	    algorithm = new RNSGAIIBuilder<DoubleSolution>(problem, crossover, mutation, referencePoint,epsilon)
//	        .setSelectionOperator(selection)
//	        .setMaxEvaluations(parameters.getMaxEvaluations())
//	        .setPopulationSize(parameters.getPopulationSize())
//	        .build() ;
//
//	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
//	        .execute() ;
//
//	    List<DoubleSolution> population = algorithm.getResult() ;
//	    long computingTime = algorithmRunner.getComputingTime() ;
//
//	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
//
//	    
//	    List<thiagodnf.nautilus.core.model.Solution> solutions = Converter.toSolutions(population);
//	    
//	    
//	    
////	    Execution execution = new Execution();
////		
////		execution.setId("75BIksd100VARIAVEIS");
////		execution.setParameters(parameters);
////		execution.setSolutions(solutions);
////		execution.setExecutionTime(algorithmRunner.getComputingTime());
////		execution.getSettings().setNormalize(new ByMaxAndMinValuesNormalize().getKey());
////		
////		FileUtils.writeStringToFile(new File("output.json"), execution.toString());
////	    
//		printFinalSolutionSet(population);
////	    printFinalSolutionSet(population);
////	    if (!referenceParetoFront.equals("")) {
////	      printQualityIndicators(population, referenceParetoFront) ;
////	    }
	  }
}
