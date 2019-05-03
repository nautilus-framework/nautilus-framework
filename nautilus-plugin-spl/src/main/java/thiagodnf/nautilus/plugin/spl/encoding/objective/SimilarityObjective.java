package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class SimilarityObjective extends AbstractObjective {
	
	protected double sum;
	
	protected double count;
	
	protected List<Integer> selectedProducts;
	
//	@Override
//	public double calculate(InstanceData instanceData, Solution<?> sol) {
//
//		return 0.0;
//		
//		BinarySolution solution = (BinarySolution) sol;
//		
//		BinarySet binarySet = solution.getVariableValue(0) ;
//		
//		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
//		
//		double sum = 0.0;
//		double count = 0.0;
//		
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//			
//			if (binarySet.get(i)) {
//                
//				for (int j = i; j < binarySet.getBinarySetLength(); j++) {
//                    
//					if (binarySet.get(j)) {
//                       
//						if (i != j) {
//                            sum += instance.getSimilarity(i, j);
//                            count++;
//                        }
//                    }
//                }
//            }
//        }
//		
//		if (count == 0.0) {
//			return 1.0;
//		}
//
//		return (double) sum / (double) count;
//	}
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
		this.count = 0.0;
		this.selectedProducts = new ArrayList<>();
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		this.selectedProducts.add(i);
		
//		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
//		
//		BinarySolution solution = (BinarySolution) sol;
//		
//		BinarySet binarySet = solution.getVariableValue(0) ;
//		
//		for (int j = i; j < binarySet.getBinarySetLength(); j++) {
//            
//			if (binarySet.get(j)) {
//               
//				if (i != j) {
//                    sum += instance.getSimilarity(i, j);
//                    count++;
//                }
//            }
//        }
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {

		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		for (int i = 0; i < selectedProducts.size(); i++) {

			for (int j = i; j < selectedProducts.size(); j++) {

				if (i != j) {
					
					int prodI = selectedProducts.get(i);
					int prodJ = selectedProducts.get(j);
					
					sum += instance.getSimilarity(prodI, prodJ);
					count++;
				}
			}
		}
		
		if (count == 0.0) {
			return 1.0;
		}

		return (double) sum / (double) count;
	}
	
	@Override
	public String getName() {
		return "Similarity";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
