package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class GenerateEigen {

	public List<Eigen> execute(RealMatrix r) {

		List<Eigen> eigen = new ArrayList<>();
		
		EigenDecomposition dec = new EigenDecomposition(r);

		double[] eigenvalues = dec.getRealEigenvalues();

		double sum = Arrays.stream(eigenvalues).sum();

		for (int i = 0; i < eigenvalues.length; i++) {
			eigen.add(new Eigen(i, sum, eigenvalues[i], dec.getEigenvector(i)));
		}
		
		Collections.sort(eigen);
		
		System.out.println("------------------------------------------------------");
		System.out.println("Eigenvalues:");
		System.out.println("------------------------------------------------------");
		//PrintUtils.eigenvalues(eigen);
		
		System.out.println("------------------------------------------------------");
		System.out.println("Eigenvectors:");
		System.out.println("------------------------------------------------------");
		//PrintUtils.eigenvectors("Model-1: RRT", eigen);

		return eigen;
	}
}
