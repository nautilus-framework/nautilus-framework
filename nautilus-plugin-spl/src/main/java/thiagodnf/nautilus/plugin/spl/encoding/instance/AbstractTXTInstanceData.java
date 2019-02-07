package thiagodnf.nautilus.plugin.spl.encoding.instance;

import java.util.List;

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
}
