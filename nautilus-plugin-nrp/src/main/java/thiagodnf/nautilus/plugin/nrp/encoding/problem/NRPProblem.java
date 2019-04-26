package thiagodnf.nautilus.plugin.nrp.encoding.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTFileInstance;

public class NRPProblem extends NBinaryProblem {

	private static final long serialVersionUID = 1233594822179588853L;

	public NRPProblem(InstanceData instance, List<AbstractObjective> objectives) {
		super(instance, objectives);
		
		setNumberOfVariables(1);
		
		List<Integer> bitsPerVariable = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			bitsPerVariable.add(((TXTFileInstance) getInstance()).getNumberOfRequirements());
		}
		
		setBitsPerVariable(bitsPerVariable);
	}
	
	@Override
	public void evaluate(BinarySolution solution) {
		
		// Change if it is invalid

		BinarySet binarySet = (BinarySet) solution.getVariableValue(0);

		if (binarySet.isEmpty()) {

			int pos = JMetalRandom.getInstance().nextInt(0, binarySet.getBinarySetLength() - 1);

			binarySet.set(pos, true);
		}

		super.evaluate(solution);
	}
}
