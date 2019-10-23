package org.nautilus.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.util.binarySet.BinarySet;

public class VariableUtils {

	public static List<String> getVariableAsList(Object variable) {

		if (variable instanceof BinarySet) {
			return getVariableAsList((BinarySet) variable);
		} else if (variable instanceof Integer) {
			return Arrays.asList(String.valueOf(variable));
		} else if (variable instanceof Double) {
			return Arrays.asList(String.valueOf(variable));
		}

		throw new RuntimeException("The kind of variable is not recognized");
	}
	
	public static List<String> getVariableAsList(BinarySet binarySet) {

		List<String> variables = new ArrayList<>();

		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

			if (binarySet.get(i)) {
				variables.add(String.valueOf(i));
			}
		}

		return variables;
	}
}
