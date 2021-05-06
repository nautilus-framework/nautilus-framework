package org.nautilus.core.algorithm.pcansgaii;

import java.util.ArrayList;
import java.util.List;

public class GenerateEigenvalueAnalysis {

	public List<PC> execute(List<Eigen> eigens) {

		List<PC> pcs = new ArrayList<>();

		for (int i = 0; i < eigens.size(); i++) {
			pcs.add(new PC(i + 1, eigens.get(i)));
		}

		System.out.println("------------------------------------------------------");
		System.out.println("Principal Components");
		System.out.println("------------------------------------------------------");

		for (PC pc : pcs) {
			System.out.println(pc);
		}

		return pcs;
	}
}
