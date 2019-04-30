package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class NewSimilarityObjective extends AbstractObjective {
	
	protected double sum;
	
	protected double count;
	
	protected List<Integer> selectedProducts;
	
	protected Map<String, Integer> indexes;
	
	protected double[] counts;
	
	@Override
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		this.sum = 0.0;
		this.count = 0.0;
		this.indexes = new HashMap<>();
		this.selectedProducts = new ArrayList<>();
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		indexes = instance.getIndexesForOptionalFeatures();
	
		this.counts = new double[indexes.size()];
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		
		this.selectedProducts.add(i);
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		List<String> optionalFeatures = instance.getProductWithoutMandatoryFeatures(i);
		
		for (String optionalFeature : optionalFeatures) {

			int j = indexes.get(optionalFeature);
			
			this.counts[j] += 1;
		}
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		if (selectedProducts.size() == 0) {
			return 0.0;
		}

		double max = Arrays.stream(counts).max().getAsDouble();

		if (max == 1.0) {
			return 0.0;
		}

		return (double) max / (double) selectedProducts.size();
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
