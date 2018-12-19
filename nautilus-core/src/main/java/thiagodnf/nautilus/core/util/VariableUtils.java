package thiagodnf.nautilus.core.util;

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
	
	public static Object clone(Object object) {

		if (object instanceof BinarySet) {
			return ((BinarySet) object).clone();
		} else if (object instanceof Integer) {
			return new Integer(((Integer) object));
		} else if (object instanceof Double) {
			return new Double(((Double) object));
		} else if (object instanceof String) {
			return new String(((String) object));
		} else {
			return object;
		}
	}
}
