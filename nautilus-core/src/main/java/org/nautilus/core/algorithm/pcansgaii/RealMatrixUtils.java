package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.uma.jmetal.solution.Solution;

public class RealMatrixUtils {

	public static RealMatrix getRealMatrix(List<? extends Solution<?>> population) {

		int numberOfObjectives = population.get(0).getNumberOfObjectives();

		double[][] data = new double[numberOfObjectives][population.size()];

		for (int i = 0; i < numberOfObjectives; i++) {

			for (int j = 0; j < population.size(); j++) {
				data[i][j] = population.get(j).getObjective(i);
			}
		}
		
		return MatrixUtils.createRealMatrix(data);
	}
	
	public static RealMatrix getCovarianceMatrix(RealMatrix x) {

		int rows = x.getRowDimension();

		RealMatrix v = MatrixUtils.createRealMatrix(rows, rows);

		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < rows; j++) {

				RealMatrix xi = x.getRowMatrix(i);
				RealMatrix xjT = x.getRowMatrix(j).transpose();

				double value = xi.multiply(xjT).getEntry(0, 0);

				value = value / (rows - 1);

				v.setEntry(i, j, value);
			}
		}

		return v;
	}
	
	public static RealMatrix getStandardizedMatrix(RealMatrix x) {

		DescriptiveStatistics stats = new DescriptiveStatistics();

		for (int i = 0; i < x.getRowDimension(); i++) {

			for (int j = 0; j < x.getColumnDimension(); j++) {
				stats.addValue(x.getEntry(i, j));
			}
		}

		double mean = stats.getMean();
		double sd = stats.getStandardDeviation();

		RealMatrix s = MatrixUtils.createRealMatrix(x.getRowDimension(), x.getColumnDimension());

		for (int i = 0; i < x.getRowDimension(); i++) {

			for (int j = 0; j < x.getColumnDimension(); j++) {
				s.setEntry(i, j, ((x.getEntry(i, j) - mean) / sd));
			}
		}

		return s;
	}
	
	public static RealMatrix getCorrelationMatrix(RealMatrix v) {

		int rows = v.getRowDimension();

		RealMatrix r = MatrixUtils.createRealMatrix(rows, rows);

		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < rows; j++) {

				double vij = v.getEntry(i, j);
				double vii = v.getEntry(i, i);
				double vjj = v.getEntry(j, j);

				r.setEntry(i, j, (vij) / (Math.sqrt(vii * vjj)));
			}
		}

		return r;
	}
	
	public static List<Eigen> getEigen(RealMatrix m) {

		EigenDecomposition dec = new EigenDecomposition(m);

		List<Eigen> eigen = new ArrayList<>();

		double[] eigenvalues = dec.getRealEigenvalues();

		double sum = Arrays.stream(eigenvalues).sum();

		for (int i = 0; i < eigenvalues.length; i++) {
			eigen.add(new Eigen(i, sum, eigenvalues[i], dec.getEigenvector(i)));
		}

		return eigen;
	}
	
	public static RealMatrix getEigenvectors(RealMatrix m) {

		RealMatrix s = MatrixUtils.createRealMatrix(m.getRowDimension(), m.getColumnDimension());

		EigenDecomposition dec = new EigenDecomposition(m);

		for (int i = 0; i < m.getRowDimension(); i++) {
			s.setRowVector(i, dec.getEigenvector(i));
		}

		return s;
	}
	
	public static double[][] toDoubleMatrix(RealMatrix m) {

		double[][] matrix = new double[m.getRowDimension()][m.getColumnDimension()];

		for (int i = 0; i < m.getRowDimension(); i++) {

			for (int j = 0; j < m.getColumnDimension(); j++) {
				matrix[i][j] = m.getEntry(i, j);
			}
		}

		return matrix;
	}
}
