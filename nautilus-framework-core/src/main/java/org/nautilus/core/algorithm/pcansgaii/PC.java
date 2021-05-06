package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealVector;

public class PC {
	
	protected int i;
	
	protected Eigen eigen;
	
	public PC(int i, Eigen eigen) {
		this.i = i;
		this.eigen = eigen;
	}
	
	public Eigen getEigen() {
		return this.eigen;
	}
	
	public int getTheHighestAbsoluteElement() {

		int maxIndex = -1;
		double maxValue = Double.NEGATIVE_INFINITY;

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

			double value = Math.abs(eigenvector.getEntry(i));

			if (value > maxValue) {
				maxValue = value;
				maxIndex = i;
			}
		}

		return maxIndex;
	}
	
	public List<Integer> getMostNegativeIndexes() {
		
		double minValue = getMostNegativeValue();

		List<Integer> indexes = new ArrayList<>();

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

//			if (Math.abs(eigenvector.getEntry(i) - minValue) <= 0.00001) {
			if (eigenvector.getEntry(i) == minValue) {
				indexes.add(i);
			}
		}

		return indexes;
	}
	
	public List<Integer> getMostPositiveIndexes() {

		double maxValue = getMostPositiveValue();

		List<Integer> indexes = new ArrayList<>();

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

//			if (Math.abs(eigenvector.getEntry(i) - maxValue) <= 0.00001) {
			if (eigenvector.getEntry(i) == maxValue) {
				indexes.add(i);
			}
		}

		return indexes;
	}
	
	public double getMostPositiveValue() {

		double maxValue = Double.NEGATIVE_INFINITY;

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

			double value = eigenvector.getEntry(i);

			if (value > maxValue) {
				maxValue = value;
			}
		}

		return maxValue;
	}
	
	public double getMostNegativeValue() {

		double minValue = Double.POSITIVE_INFINITY;

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

			double value = eigenvector.getEntry(i);

			if (value < minValue) {
				minValue = value;
			}
		}

		return minValue;
	}
	
//	public List<Integer> getTheMostImportantConflictingObjectives(){
//		return Arrays.asList(getMostNegativeIndex(), getMostPositiveIndex());
//	}
	
	public List<Integer> getMostPositiveAndMostNegativeObjectives(){
	
		List<Integer> objectives = new ArrayList<>();

		objectives.addAll(getMostNegativeIndexes());
		objectives.addAll(getMostPositiveIndexes());
		
		return objectives.stream().sorted().collect(Collectors.toList());
	}
	
	public List<Integer> getAllObjectives(){
		return this.getMostPositiveAndMostNegativeObjectives();
	}
	
	public String toString() {
		return "PCA" + i + ": " + getAllObjectives()
			.stream()
			.map(e -> "f" + e)
			.collect(Collectors.toList());
	}

	public boolean isAllElementsOfTheEigenvectorArePositive() {

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

			if (eigenvector.getEntry(i) < 0) {
				return false;
			}
		}

		return true;
	}
	
	public boolean isAllElementsOfTheEigenvectorAreNegative() {

		RealVector eigenvector = eigen.getEigenvector();

		for (int i = 0; i < eigenvector.getDimension(); i++) {

			if (eigenvector.getEntry(i) >= 0) {
				return false;
			}
		}

		return true;
	}

	public int getI() {
		return this.i;
	}	
}
