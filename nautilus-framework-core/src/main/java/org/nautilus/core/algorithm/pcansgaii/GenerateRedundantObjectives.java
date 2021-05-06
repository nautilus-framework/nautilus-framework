package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class GenerateRedundantObjectives {

	public List<RedundandObjective> execute(RealMatrix r) {

		List<RedundandObjective> redundantObjectives = new ArrayList<>();
		
		for (int i = 0; i < r.getRowDimension(); i++) {

			for (int j = i; j < r.getColumnDimension(); j++) {

				if (i != j) {

					for (int k = 0; k < r.getColumnDimension(); k++) {

						if (i != k && j != k) {

							if (areNegativelyCorrelated(r, i, k) && areNegativelyCorrelated(r, j, k)) {

								if (arePositivelyCorrelated(r, i, j)) {
									redundantObjectives.add(new RedundandObjective(i,j,k, true));
								}
							} else if (arePositivelyCorrelated(r, i, k) && arePositivelyCorrelated(r, j, k)) {

								if (arePositivelyCorrelated(r, i, j)) {
									redundantObjectives.add(new RedundandObjective(i,j,k ,false));
								}
							}
						}
					}
				}
			}
		}

		System.out.println("------------------------------------------------------");
		System.out.println("Possible Redundant Objectives");
		System.out.println("------------------------------------------------------");

		for (RedundandObjective redundandObjective : redundantObjectives) {
			System.out.println(redundandObjective);
		}

		return redundantObjectives;
	}
	
	public boolean arePositivelyCorrelated(RealMatrix r, int i, int j) {

		if (r.getEntry(i, j) >= 0) {
			return true;
		}

		return false;
	}

	public boolean areNegativelyCorrelated(RealMatrix r, int i, int j) {

		if (r.getEntry(i, j) < 0) {
			return true;
		}

		return false;
	}
}
