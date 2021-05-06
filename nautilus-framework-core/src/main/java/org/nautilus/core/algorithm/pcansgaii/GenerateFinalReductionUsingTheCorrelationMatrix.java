package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealMatrix;

public class GenerateFinalReductionUsingTheCorrelationMatrix {

	/** A pre-defined threshold cut. Default is 0.95 */
	protected double TC = 0.95;
	
	public List<Integer> execute(RealMatrix r, List<PC> pcs, Map<Integer, List<Integer>> selected) {

		List<Integer> selectedObjectives = new ArrayList<>();
		
		for (Entry<Integer, List<Integer>> entry : selected.entrySet()) {

			for (Integer i : entry.getValue()) {

				if (!selectedObjectives.contains(i)) {
					selectedObjectives.add(i);
				}
			}
		}
		
		selectedObjectives = selectedObjectives.stream().sorted().collect(Collectors.toList());
		
		System.out.println("------------------------------------------------------");
		System.out.println("Temporary Selected Indexes:");
		System.out.println("------------------------------------------------------");
		System.out.println(selectedObjectives);
		
		// we then return to a reduced correlation matrix (only columns and rows
		// corresponding to non-redundant objectives) and investigate if there still
		// exists a set of objectives having identical positive or negative correlation
		// coefficients with other objectives and having a positive correlation among
		// themselves.
		
		int[] indices = selectedObjectives.stream().mapToInt(x -> x).toArray();
		
		RealMatrix redR = r.getSubMatrix(indices, indices);
		
		System.out.println("------------------------------------------------------");
		System.out.println("Reduced Correlation Matrix");
		System.out.println("------------------------------------------------------");
		
		//PrintUtils.matrix(redR);
		
		List<RedundandObjective> redundantObjectives = new GenerateRedundantObjectives().execute(redR);
		
		while(!redundantObjectives.isEmpty()) {
			
			RedundandObjective o = redundantObjectives.get(0);
			
			int obj1 = o.getI();
			int obj2 = o.getJ();
			
			int pcForObj1 = findPC(selected, selectedObjectives.get(obj1));
			int pcForObj2 = findPC(selected, selectedObjectives.get(obj2));
			
			if (pcForObj1 < pcForObj2) {
				selectedObjectives.remove(obj2);
			} else if (pcForObj1 > pcForObj2) {
				selectedObjectives.remove(obj1);
			} else {
				
				PC pcObj = pcs.get(pcForObj1);

				double contributionObj1 = pcObj.getEigen().getEigenvector().getEntry(obj1);
				double contributionObj2 = pcObj.getEigen().getEigenvector().getEntry(obj2);

				if (contributionObj1 > contributionObj2) {
					selectedObjectives.remove(obj2);
				} else {
					selectedObjectives.remove(obj1);
				}
				
				System.out.println("Igual");
			}
			
			indices = selectedObjectives.stream().mapToInt(x -> x).toArray();
			
			redR = r.getSubMatrix(indices, indices);
			
			redundantObjectives = new GenerateRedundantObjectives().execute(redR);
		}
		
		return selectedObjectives;
	}
	
	protected int findPC(Map<Integer, List<Integer>> selected, int objIndex) {

		for (Entry<Integer, List<Integer>> entry : selected.entrySet()) {

			if (entry.getValue().contains(objIndex)) {
				return entry.getKey();
			}
		}

		return -1;
	}
}
