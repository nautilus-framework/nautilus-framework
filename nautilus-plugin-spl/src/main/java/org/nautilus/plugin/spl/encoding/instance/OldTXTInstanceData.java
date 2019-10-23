package org.nautilus.plugin.spl.encoding.instance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nautilus.core.gui.Tab;
import org.nautilus.core.gui.TableTabContent;
import org.nautilus.core.model.Instance;
import org.nautilus.core.util.InstanceReader;
import org.nautilus.core.util.SimilarityUtils;

import com.google.common.base.Preconditions;

public class OldTXTInstanceData extends Instance {

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
	
	private double[] importance;
	
	private double sumOfImportance;
	
	private double[] productCost;
	
	private double[] productImportance;

	public OldTXTInstanceData(Path path) {

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
		this.importance = reader.getDoubleVerticalArray(numberOfFeatures);
		
		this.sumOfCosts = 0.0;
		this.sumOfImportance = 0.0;
		this.productCost = new double[getNumberOfProducts()];
		this.productImportance = new double[getNumberOfProducts()];
		
		this.similarity = calculateSimilarity(numberOfProducts);
		
		for (int i = 0; i < getNumberOfProducts(); i++) {

			this.productCost[i] = calculateProductCost(i);
			this.productImportance[i] = calculateProductImportance(i);

			this.sumOfCosts += productCost[i];
			this.sumOfImportance += productImportance[i];
		}
	}
	
	protected double[][] calculateSimilarity(int size) {

		Preconditions.checkArgument(size >= 0, "The size should not be >= 0");

		double[][] similarity = new double[size][size];

		for (int i = 0; i < size; i++) {

			List<String> features1 = getProduct(i);

			for (int j = i; j < size; j++) {

				if (i != j) {

					List<String> features2 = getProduct(j);

					similarity[i][j] = SimilarityUtils.getJaccardIndex(features1, features2);
					similarity[j][i] = similarity[i][j];
				}
			}
		}

		return similarity;
	}
	
	protected double calculateProductCost(int productIndex) {

		Preconditions.checkArgument(productIndex >= 0, "The productIndex should be >= 0");
		Preconditions.checkArgument(productIndex < getNumberOfProducts(), "The productIndex should be < the number of products");
		
		double sum = 0.0;

		for (int i = 0; i < getNumberOfFeatures(); i++) {

			if (getFeaturesProducts()[i][productIndex] == 1) {

				sum += getCosts()[i];
			}
		}

		return sum;
	}
	
	protected double calculateProductImportance(int productIndex) {

		Preconditions.checkArgument(productIndex >= 0, "The productIndex should be >= 0");
		Preconditions.checkArgument(productIndex < getNumberOfProducts(), "The productIndex should be < the number of products");
		
		double sum = 0.0;

		for (int i = 0; i < getNumberOfFeatures(); i++) {

			if (getFeaturesProducts()[i][productIndex] == 1) {

				sum += getImportance()[i];
			}
		}

		return sum;
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
		return this.productCost[productIndex];
	}
	
	public double getProductImportance(int productIndex) {
		return this.productImportance[productIndex];
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

	public double getSumOfCosts() {
		return sumOfCosts;
	}
	
	public void setSumOfCosts(double sumOfCosts) {
		this.sumOfCosts = sumOfCosts;
	}

	public double[] getCosts() {
		return costs;
	}

	public void setCosts(double[] costs) {
		this.costs = costs;
	}

	public double[] getImportance() {
		return importance;
	}

	public void setImportance(double[] importance) {
		this.importance = importance;
	}

	public double getSumOfImportance() {
		return sumOfImportance;
	}

	public void setSumOfImportance(double sumOfImportance) {
		this.sumOfImportance = sumOfImportance;
	}
	
	@Override
    public List<Tab> getTabs(Instance data) {

        NewTXTInstanceData c = (NewTXTInstanceData) data;

        List<Tab> tabs = new ArrayList<>();

        tabs.add(getFeaturesTab(c));
        tabs.add(getProductsTab(c));
        tabs.add(getPairwiseCoverageTab(c));
        tabs.add(getMutationCoverageTab(c));

        return tabs;
    }

    protected Tab getFeaturesTab(NewTXTInstanceData data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Feature", "Cost", "Importance"));

        List<String> features = data.getFeatures();

        for (int i = 0; i < data.getNumberOfFeatures(); i++) {
            table.getRows().add(Arrays.asList(
                    features.get(i),
                    "" + data.getCosts().get(i),
                    "" + data.getImportances().get(i)
            ));
        }

        return new Tab("Features", table);
    }

    protected Tab getProductsTab(NewTXTInstanceData data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Feature", "Cost", "Importance"));

        for (int i = 0; i < data.getNumberOfProducts(); i++) {
            table.getRows().add(Arrays.asList("Product " + i, 
                data.getProduct(i).toString(), 
                "" + data.getProductCost(i),
                "" + data.getProductImportance(i)
            ));
        }

        return new Tab("Products", table);
    }
    
    protected Tab getPairwiseCoverageTab(NewTXTInstanceData data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Covered", "Uncovered", "# of Uncovered"));

        for (int i = 0; i < data.getNumberOfProducts(); i++) {

            List<String> covered = new ArrayList<>();
            List<String> unCovered = new ArrayList<>();

            for (int j = 0; j < data.getNumberOfPairs(); j++) {
                if (data.getPairs(i).contains(j)) {
                    covered.add(String.valueOf(j));
                } else {
                    unCovered.add(String.valueOf(j));
                }
            }

            table.getRows().add(Arrays.asList("Product " + i, covered.toString(), unCovered.toString(), ""+unCovered.size()));
        }

        return new Tab("Pairwise Coverate", table);
    }
    
    protected Tab getMutationCoverageTab(NewTXTInstanceData data) {

        TableTabContent table = new TableTabContent(Arrays.asList("Product Id", "Dead", "Alive", "# of Alive"));

        for (int i = 0; i < data.getNumberOfProducts(); i++) {

            List<String> covered = new ArrayList<>();
            List<String> unCovered = new ArrayList<>();

            for (int j = 0; j < data.getNumberOfMutants(); j++) {
                if (data.getMutants(i).contains(j)) {
                    covered.add(String.valueOf(j));
                } else {
                    unCovered.add(String.valueOf(j));
                }
            }

            table.getRows().add(Arrays.asList("Product " + i, covered.toString(), unCovered.toString(), ""+unCovered.size()));
        }

        return new Tab("Mutation Coverage", table);
    }
}
