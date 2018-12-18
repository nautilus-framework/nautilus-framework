package thiagodnf.nautilus.core.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class BruteForceSearch<S extends Solution<?>> implements Algorithm<List<S>>, AlgorithmListener{

	private static final long serialVersionUID = -6449424521348976254L;

	protected SolutionListEvaluator<S> evaluator;
	
	private OnProgressListener onProgressListener;
	
	protected List<S> population;
	
	protected Problem<S> problem;
	
	/**
	 * Constructor 
	 * 
	 * @param builder The algorithm's builder
	 */
	public BruteForceSearch(Builder builder) {
		this.population = new ArrayList<>();
		this.problem = builder.getProblem();
		this.evaluator = new SequentialSolutionListEvaluator<S>();
	}
	
	@Override
	public void run() {

		updateProgress(0.0);
		
		if (problem instanceof IntegerProblem) {
			population = createSolutionsForIntegerProblem();
		} else {
			throw new RuntimeException("The problem should be an Integer one");
		}
		
		updateProgress(50.0);
		
		population = evaluatePopulation(population);
		
		updateProgress(100.0);
	}
	
	protected List<S> evaluatePopulation(List<S> population) {
		
		population = evaluator.evaluate(population, problem);

		return population;
	}
	
	public List<S> createSolutionsForIntegerProblem() {
		
		if (problem.getNumberOfVariables() > 10) {
			throw new RuntimeException("The problem should have at most 10 variables");
		}

		if (problem.getNumberOfObjectives() > 5) {
			throw new RuntimeException("The problem should have at most 5 objectives");
		}

		List<S> population = new ArrayList<>();
		
		String[] numbers = generateForInteger(problem.getNumberOfVariables(), problem.getNumberOfObjectives());

		for (String number : numbers) {

			IntegerSolution s = (IntegerSolution) problem.createSolution();

			String[] chars = number.trim().split("");

			for (int i = 0; i < chars.length; i++) {
				s.setVariableValue(i, ((int) chars[i].charAt(0)) - 96);
			}

			population.add((S) s);
		}
		
		return population;
	}
	
	protected void updateProgress(double value) {
		
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(value);
		}
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

	@Override
	public String getName() {
		return "Brute-force Search";
	}

	@Override
	public String getDescription() {
		return "Brute-force Search";
	}

	@Override
	public List<S> getResult() {
		return SolutionListUtils.getNondominatedSolutions(population);
	}
	
	protected static String[] generateForInteger(int size, int max) {

		String[] database = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o" };
		
		String[] selected = new String[max];
		
		for (int i = 0; i < max; i++) {
			selected[i] = database[i];
		}
		
		return getAllLists(selected, size);
	}

	public static String[] getAllLists(String[] elements, int lengthOfList)
	{
	    //initialize our returned list with the number of elements calculated above
	    String[] allLists = new String[(int)Math.pow(elements.length, lengthOfList)];

	    //lists of length 1 are just the original elements
	    if(lengthOfList == 1) return elements; 
	    else {
	        //the recursion--get all lists of length 3, length 2, all the way up to 1
	        String[] allSublists = getAllLists(elements, lengthOfList - 1);

	        //append the sublists to each element
	        int arrayIndex = 0;

	        for(int i = 0; i < elements.length; i++){
	            for(int j = 0; j < allSublists.length; j++){
	                //add the newly appended combination to the list
	                allLists[arrayIndex] = elements[i] + allSublists[j];
	                arrayIndex++;
	            }
	        }
	        return allLists;
	    }
	}

	public static void main(String[] args){
	    
		String[] result = generateForInteger(10, 3);
        
		System.out.println(Arrays.toString(result));
//		for(int j=0; j<result.length; j++){
//            System.out.println(result[j]);
//        }
	}
}
