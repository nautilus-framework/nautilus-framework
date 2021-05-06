package org.nautilus.core.algorithm.pcansgaii;

import org.apache.commons.math3.linear.RealVector;

public class Eigen implements Comparable<Eigen>{

	protected int id;
	
	protected double eigenvalue;

	protected double sum;
	
	protected RealVector eigenvector;

	public Eigen(int id, double sum, double eigenvalue, RealVector eigenvector) {
		this.id = id;
		this.sum = sum;
		this.eigenvalue = eigenvalue;
		this.eigenvector = eigenvector;
	}

	public double getEigenvalue() {
		return eigenvalue;
	}
	
	public double getNormalizedEigenvalue() {
		return eigenvalue / sum;
	}

	public RealVector getEigenvector() {
		return eigenvector;
	}

	public int getId() {
		return id;
	}

	public double getSum() {
		return sum;
	}

	@Override
	public int compareTo(Eigen eigen) {

		if (getEigenvalue() > eigen.getEigenvalue()) {
			return -1;
		} else if (getEigenvalue() < eigen.getEigenvalue()) {
			return 1;
		} else {
			return 0;
		}
	}
}
