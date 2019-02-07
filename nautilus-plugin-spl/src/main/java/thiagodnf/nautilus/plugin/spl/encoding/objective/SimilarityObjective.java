package thiagodnf.nautilus.plugin.spl.encoding.objective;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class SimilarityObjective extends AbstractObjective {
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		BinarySolution solution = (BinarySolution) sol;
		
		BinarySet binarySet = solution.getVariableValue(0) ;
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		double sum = 0.0;
		double count = 0.0;
		
		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
			
			if (binarySet.get(i)) {
                
				for (int j = i; j < binarySet.getBinarySetLength(); j++) {
                    
					if (binarySet.get(j)) {
                       
						if (i != j) {
                            sum += instance.getSimilarity(i, j);
                            count++;
                        }
                    }
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
