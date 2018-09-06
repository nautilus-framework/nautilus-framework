package thiagodnf.nautilus.core.util;

public class Normalizer {

	public static double normalize(double value, double a, double b, double min, double max) {
		return a + (((value - min) * (b - a)) / (max - min));
	}

	public static double normalize(double value, double min, double max) {
		return normalize(value, 0, 1, min, max);
	}

}
