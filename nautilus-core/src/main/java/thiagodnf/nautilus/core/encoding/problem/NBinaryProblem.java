package thiagodnf.nautilus.core.encoding.problem;

import java.util.List;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public abstract class NBinaryProblem extends NProblem<BinarySolution> implements BinaryProblem {

	private static final long serialVersionUID = 8418817221014037519L;

	protected List<Integer> bitsPerVariable;
	
	public NBinaryProblem(InstanceData data, List<AbstractObjective> objectives) {
		super(data, objectives);
	}

	@Override
	public int getNumberOfBits(int index) {
		return bitsPerVariable.get(index);
	}

	@Override
	public int getTotalNumberOfBits() {

		int count = 0;
		
		for (int i = 0; i < this.getNumberOfVariables(); i++) {
			count += this.getNumberOfBits(i);
		}

		return count;
	}
	
	public List<Integer> getBitsPerVariable() {
		return bitsPerVariable;
	}

	public void setBitsPerVariable(List<Integer> bitsPerVariable) {
		this.bitsPerVariable = bitsPerVariable;
	}

	@Override
	public void evaluate(BinarySolution solution) {

		BinarySet binarySet = solution.getVariableValue(0) ;
		
		for (int i = 0; i < objectives.size(); i++) {
			objectives.get(i).beforeProcess(instance);
		}
		
		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
			
			if (binarySet.get(i)) {
				
				for (int j = 0; j < objectives.size(); j++) {
					objectives.get(j).process(instance, solution, i);
				}
			}
		}
		
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i).evaluate(instance, solution));
		}
	}
	
	@Override
	public BinarySolution createSolution() {
		return new NBinarySolution(
			getNumberOfObjectives(), 
			getNumberOfVariables(),
			getBitsPerVariable()
		);
	}
}
