package thiagodnf.nautilus.plugin.spl.encoding.objective;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class NumberOfProductsObjective extends AbstractObjective {
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		BinarySolution solution = (BinarySolution) sol;
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		BinarySet binarySet = solution.getVariableValue(0) ;
		
//		Old Code. Before We ran over all bit inside the bitset to get
//		the number of bits set as true. Now, you don't need to do that. 
//		You need just to get the cardinality of it.
//		
//		int numbersOfProducts = 0;
//
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//			if (binarySet.get(i)) {
//				numbersOfProducts++;
//			}
//		}
		
		return (double) binarySet.cardinality() / (double) instance.getNumberOfProducts();
	}
	
	@Override
	public String getName() {
		return "Number of Products";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
