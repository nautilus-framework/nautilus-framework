package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GenerateEffectOfMultiplePrincipalComponents {

	/** A pre-defined threshold cut. Default is 0.95 */
	protected double TC = 0.95;
	
	public Map<Integer, List<Integer>> execute(List<PC> pcs) {

		double threshold = 0.0;
		
		List<PC> selectedPcs = new ArrayList<>();

		for (int i = 0; i < pcs.size(); i++) {

			if (threshold < TC) {
				selectedPcs.add(pcs.get(i));
			}

			threshold += pcs.get(i).getEigen().getNormalizedEigenvalue();
		}

		System.out.println("------------------------------------------------------");
		System.out.println("Filtered Principal Components based on TC");
		System.out.println("------------------------------------------------------");

		for (PC pc : selectedPcs) {
			System.out.println(pc);
		}
		
		Map<Integer, List<Integer>> selected = new HashMap<>();

		for (int i = 0; i < selectedPcs.size(); i++) {

			PC pc = selectedPcs.get(i);

			if (!selected.containsKey(pc.getI())) {
				selected.put(pc.getI(), new ArrayList<>());
			}
			
			if (i == 0) {
				// For the first principal component, we choose both objectives contributing the
				// most positive and most negative objectives.
				selected.get(pc.getI()).addAll(pc.getMostPositiveAndMostNegativeObjectives());
			} else {

				double eigenvalue = pc.getEigen().getEigenvalue();

				// If yes and also if the cumulative contribution of eigenvalues is less than
				// TC, we consider various cases.
				if (eigenvalue > 0.1) {			
					
					// If all elements of the eigenvector are positive, we only choose the objective
					// corresponding to the highest element.
					if (pc.isAllElementsOfTheEigenvectorArePositive()) {
						selected.get(pc.getI()).addAll(pc.getMostPositiveIndexes());
					} else if (pc.isAllElementsOfTheEigenvectorAreNegative()) {
						// If all elements of the eigenvector are negative, we choose all objectives.
						selected.get(pc.getI()).addAll(pc.getAllObjectives());
					} else { // Otherwise,

						// if the value of the highest positive element (p) is less than the absolute
						// value of the most-negative element (n), we consider two different scenarios.

						double p = pc.getMostPositiveValue();
						double n = pc.getMostNegativeValue();

						if (p < Math.abs(n)) {

							// If p >= 0.9|n|, we choose two objectives corresponding to p and n.
							if (p >= 0.9 * Math.abs(n)) {
								selected.get(pc.getI()).addAll(pc.getMostNegativeIndexes());
								selected.get(pc.getI()).addAll(pc.getMostPositiveIndexes());
							}else {
								// On the other hand, if p < 0.9|n|, we only choose the objective corresponding to n.
								selected.get(pc.getI()).addAll(pc.getMostNegativeIndexes());
							}
						}else if(p > Math.abs(n)) {
							
							// Similarly, if p > |n|, then we consider two other scenarios. If
							
							// If p >= 0.8|n|, we choose both objectives corresponding to p and n.
							if (p >= 0.8 * Math.abs(n)) {
								selected.get(pc.getI()).addAll(pc.getMostNegativeIndexes());
								selected.get(pc.getI()).addAll(pc.getMostPositiveIndexes());
							}else {
								// On the other hand, if p < 0.8|n|, we only choose the objective corresponding
								// to p.
								selected.get(pc.getI()).addAll(pc.getMostPositiveIndexes());
							}
						}
					}
				} else {
					// If not, we only choose the objective corresponding to the highest absolute
					// element in the eigenvector.
					selected.get(pc.getI()).addAll(Arrays.asList(pc.getTheHighestAbsoluteElement()));
				}
			}
		}
		
		System.out.println("------------------------------------------------------");
		System.out.println("Temporary Principal Components:");
		System.out.println("------------------------------------------------------");
		
		for (Entry<Integer, List<Integer>> entry : selected.entrySet()) {
			System.out.println("PCA" + entry.getKey() + ": " + entry.getValue());
		}
		
		return selected;
	}
}
