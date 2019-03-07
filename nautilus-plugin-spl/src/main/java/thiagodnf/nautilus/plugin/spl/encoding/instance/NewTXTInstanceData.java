package thiagodnf.nautilus.plugin.spl.encoding.instance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
//import static org.valid4j.Assertive.require;

//import static org.hamcrest.Matchers.greaterThanOrEqualTo;
//import static org.hamcrest.Matchers.lessThan;
import static org.jvalidation.Assertive.require;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.util.InstanceReader;
import thiagodnf.nautilus.core.util.SimilarityUtils;

public class NewTXTInstanceData extends InstanceData implements AbstractTXTInstanceData {
	
	protected int numberOfProducts;
	
	protected int numberOfFeatures;
	
	protected int numberOfMutants;
	
	protected int numberOfPairs;
	
	protected List<String> features;
	
	protected List<String> mandatoryFeatures;
	
	protected List<List<Integer>> products;
	
	protected List<List<Integer>> mutants;
	
	protected List<List<Integer>> pairs;
	
	protected List<Double> costs;
	
	protected List<Double> importances;
	
	protected List<Double> productCosts;
	
	protected List<Double> productImportances;
	
	protected double sumOfCosts;
	
	protected double sumOfImportances;
	
	protected double[][] similarity;
	
	public NewTXTInstanceData(Path path) {

//		require(path, not(nullValue()));
//		require(Files.exists(path), equalTo(true));
		
		this.products = new ArrayList<>();
		this.mutants = new ArrayList<>();
		this.pairs = new ArrayList<>();
		this.productCosts = new ArrayList<>();
		this.productImportances = new ArrayList<>();
		
		InstanceReader reader = new InstanceReader(path, " ");
		
		reader.ignoreLine();
		this.numberOfProducts = reader.readIntegerValue();
		
		reader.ignoreLine();
		this.numberOfFeatures = reader.readIntegerValue();
		
		reader.ignoreLine();
		this.numberOfMutants = reader.readIntegerValue();
		
		reader.ignoreLine();
		this.numberOfPairs = reader.readIntegerValue();
		
		reader.ignoreLine();
		this.features = reader.readStringValues();
		
		reader.ignoreLine();
		this.mandatoryFeatures = reader.readStringValues();
		
		reader.ignoreLine();
		for (int i = 0; i < getNumberOfProducts(); i++) {
			products.add(reader.readIntegerValues());
		}
		
		reader.ignoreLine();
		for (int i = 0; i < getNumberOfProducts(); i++) {
			this.mutants.add(reader.readIntegerValues());
		}
		
		reader.ignoreLine();
		for (int i = 0; i < getNumberOfProducts(); i++) {
			this.pairs.add(reader.readIntegerValues());
		}
		
		reader.ignoreLine();
		this.costs = reader.readDoubleValues();
		
		reader.ignoreLine();
		this.importances = reader.readDoubleValues();
		
		// Calculate some data because we want performance

		for (int i = 0; i < getNumberOfProducts(); i++) {

			productCosts.add(calculateProductCost(i));
			productImportances.add(calculateProductImportance(i));

			this.sumOfCosts += productCosts.get(i);
			this.sumOfImportances += productImportances.get(i);
		}
		
		this.similarity = calculateSimilarity(numberOfProducts);
	}
	
	@Override
	public int getNumberOfMutants() {
		return this.numberOfMutants;
	}

	@Override
	public int getNumberOfProducts() {
		return this.numberOfProducts;
	}

	@Override
	public int getNumberOfFeatures() {
		return this.numberOfFeatures;
	}

	@Override
	public int getNumberOfPairs() {
		return this.numberOfPairs;
	}

	@Override
	public List<String> getFeatures() {
		return this.features;
	}	
	
	@Override
	public List<Integer> getProduct(int index) {
		return products.get(index);
	}
	
	public List<String> getProductWithFeatures(int index) {

		List<String> features = new ArrayList<>();

		for (Integer featureId : getProduct(index)) {
			features.add(getFeatures().get(featureId));
		}

		return features;
	}
	
	protected double calculateProductCost(int index) {

		double sum = 0.0;

		for (Integer featureId : getProduct(index)) {
			sum += costs.get(featureId);
		}

		return sum;
	}
	
	protected double calculateProductImportance(int index) {

		double sum = 0.0;

		for (Integer featureId : getProduct(index)) {
			sum += importances.get(featureId);
		}

		return sum;
	}
	
	protected double[][] calculateSimilarity(int size) {

		double[][] similarity = new double[size][size];

		for (int i = 0; i < size; i++) {

			List<Integer> features1 = this.getProduct(i);

			for (int j = i; j < size; j++) {

				if (i != j) {

					List<Integer> features2 = this.getProduct(j);

					similarity[i][j] = SimilarityUtils.getJaccardIndexFromInteger(features1, features2);
					similarity[j][i] = similarity[i][j];
				}
			}
		}

		return similarity;
	}

	@Override
	public double getProductCost(int index) {
		return this.productCosts.get(index);
	}

	@Override
	public double getSumOfCosts() {
		return this.sumOfCosts;
	}

	@Override
	public double getProductImportance(int index) {
		return this.productImportances.get(index);
	}

	@Override
	public double getSumOfImportance() {
		return this.sumOfImportances;
	}
	
	@Override
	public double getSimilarity(int i, int j) {
		
//		require(i).isInteger().between(0, getNumberOfProducts() - 1);
//		require(j).isInteger().between(0, getNumberOfProducts() - 1);

		return this.similarity[i][j];
	}

	@Override
	public List<Integer> getMutants(int index) {
		return this.mutants.get(index);
	}

	@Override
	public List<Integer> getPairs(int index) {
		return this.pairs.get(index);
	}
}
