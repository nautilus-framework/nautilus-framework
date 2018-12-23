package thiagodnf.nautilus.plugin.spl.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.util.InstanceReader;
import thiagodnf.nautilus.core.util.SimilarityUtils;

public class TXTInstanceData extends InstanceData {

	private int numberOfProducts;

	private int numberOfFeatures;

	private String[] features;

	private int[][] featuresProducts;

	private int numberOfMutants;

	private int[][] mutantsProducts;

	private int numberOfPairs;

	private int[][] pairsProducts;

	private double[] costs;

	private double sumOfCosts;

	private double[][] similarity;
	
	private double[][] similarityAmongOptionalFeatures;
	
	private String[] mandatoryFeatures;

	public TXTInstanceData(Path path) {

		Preconditions.checkNotNull(path, "The path should not be null");
		Preconditions.checkArgument(Files.exists(path), "The path does not exists");

		InstanceReader reader = new InstanceReader(path);

		reader.setSeparator(" ");

		this.numberOfProducts = reader.getInteger();
		this.numberOfFeatures = reader.getInteger();
		this.features = reader.getStringVerticalArray(numberOfFeatures);
		this.featuresProducts = reader.getIntegerMatrix(numberOfFeatures);
		reader.readLine(); // Ignore Comment
		this.numberOfMutants = reader.getInteger();
		this.mutantsProducts = reader.getIntegerMatrix(numberOfMutants);
		reader.readLine(); // Ignore Comment
		this.numberOfPairs = reader.getInteger();
		this.pairsProducts = reader.getIntegerMatrix(numberOfPairs);
		reader.readLine(); // Ignore Comment
		this.costs = reader.getDoubleVerticalArray(numberOfFeatures);
		reader.readLine(); // Ignore Comment
		this.mandatoryFeatures = reader.getStringHorizontalArray();
		
		this.similarity = calculateSimilarity(numberOfProducts);
		this.similarityAmongOptionalFeatures = calculateSimilarityAmongOptionalFeatures(numberOfProducts);

		for (int i = 0; i < getNumberOfProducts(); i++) {
			this.sumOfCosts += getProductCost(i);
		}
	}
	
	public double[][] calculateSimilarityAmongOptionalFeatures(int size) {

		Preconditions.checkArgument(size >= 0, "The size should not be >= 0");
		
		double[][] similarity = new double[size][size];

		for (int i = 0; i < size; i++) {

			for (int j = i; j < size; j++) {

				if (i != j) {
					
					List<String> features1 = getProduct(i);
					List<String> features2 = getProduct(j);

					for (String mandatoryFeature : mandatoryFeatures) {
						features1.remove(mandatoryFeature);
						features2.remove(mandatoryFeature);
					}

					similarity[i][j] = SimilarityUtils.getJaccardIndex(features1, features2);
					similarity[j][i] = similarity[i][j];
				}
			}
		}

		return similarity;
	}

	public double[][] calculateSimilarity(int size) {

		Preconditions.checkArgument(size >= 0, "The size should not be >= 0");

		double[][] similarity = new double[size][size];

		for (int i = 0; i < size; i++) {

			for (int j = i; j < size; j++) {

				if (i != j) {
					
					List<String> features1 = getProduct(i);
					List<String> features2 = getProduct(j);
					
					similarity[i][j] = SimilarityUtils.getJaccardIndex(features1, features2);
					similarity[j][i] = similarity[i][j];
				}
			}
		}

		return similarity;
	}

	public int getNumberOfProducts() {
		return numberOfProducts;
	}

	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}

	public int getNumberOfFeatures() {
		return numberOfFeatures;
	}

	public void setNumberOfFeatures(int numberOfFeatures) {
		this.numberOfFeatures = numberOfFeatures;
	}

	public String[] getFeatures() {
		return features;
	}

	public void setFeatures(String[] features) {
		this.features = features;
	}

	public int[][] getFeaturesProducts() {
		return featuresProducts;
	}

	public void setFeaturesProducts(int[][] featuresProducts) {
		this.featuresProducts = featuresProducts;
	}

	public int getNumberOfMutants() {
		return numberOfMutants;
	}

	public void setNumberOfMutants(int numberOfMutants) {
		this.numberOfMutants = numberOfMutants;
	}

	public int[][] getMutantsProducts() {
		return mutantsProducts;
	}

	public void setMutantsProducts(int[][] mutantsProducts) {
		this.mutantsProducts = mutantsProducts;
	}

	public int getNumberOfPairs() {
		return numberOfPairs;
	}

	public void setNumberOfPairs(int numberOfPairs) {
		this.numberOfPairs = numberOfPairs;
	}

	public int[][] getPairsProducts() {
		return pairsProducts;
	}

	public void setPairsProducts(int[][] pairsProducts) {
		this.pairsProducts = pairsProducts;
	}

	public double getProductCost(int productIndex) {

		double sum = 0.0;

		for (int i = 0; i < getNumberOfFeatures(); i++) {

			if (getFeaturesProducts()[i][productIndex] == 1) {

				sum += getCost()[i];
			}
		}

		return sum;
	}

	public List<String> getProduct(int productIndex) {

		List<String> features = new ArrayList<>();

		for (int i = 0; i < getNumberOfFeatures(); i++) {

			if (getFeaturesProducts()[i][productIndex] == 1) {
				features.add(getFeatures()[i]);
			}
		}

		return features;
	}

	public void setSimilarity(double[][] similarity) {
		this.similarity = similarity;
	}

	public double getSimilarity(int i, int j) {
		return getSimilarity()[i][j];
	}
	
	public double[][] getSimilarity() {
		return this.similarity;
	}

	public double[] getCost() {
		return costs;
	}

	public void setCost(double[] cost) {
		this.costs = cost;
	}

	public double getSumOfCosts() {
		return sumOfCosts;
	}
	
	public String[] getMandatoryFeatures() {
		return mandatoryFeatures;
	}
	
	
	
	public double[][] getSimilarityAmongOptionalFeatures() {
		return similarityAmongOptionalFeatures;
	}
	
	public double getSimilarityAmongOptionalFeatures(int i, int j) {
		return getSimilarityAmongOptionalFeatures()[i][j];
	}

	public void setSimilarityAmongOptionalFeatures(double[][] similarityAmongOptionalFeatures) {
		this.similarityAmongOptionalFeatures = similarityAmongOptionalFeatures;
	}

	public void setMandatoryFeatures(String[] mandatoryFeatures) {
		this.mandatoryFeatures = mandatoryFeatures;
	}

	public void setSumOfCosts(double sumOfCosts) {
		this.sumOfCosts = sumOfCosts;
	}
}
