package thiagodnf.nautilus.plugin.spl.encoding.instance;

import java.util.List;
import java.util.Map;

public interface AbstractTXTInstanceData {

	public int getNumberOfProducts();
	
	public int getNumberOfFeatures();
	
	public int getNumberOfMutants();
	
	public int getNumberOfPairs();
	
	public List<String> getFeatures();
	
	public List<Integer> getProduct(int index);
	
	public double getProductCost(int index);
	
	public double getProductImportance(int index);

	public double getSumOfCosts();

	public double getSumOfImportance();

	public double getSimilarity(int i, int j);
	
	public List<Integer> getMutants(int index);
	
	public List<Integer> getPairs(int index);
	
	public List<String> getProductWithoutMandatoryFeatures(int index);
	
	public List<String> getOptionalFeatures();
	
	public Map<String, Integer> getIndexesForOptionalFeatures();
}
