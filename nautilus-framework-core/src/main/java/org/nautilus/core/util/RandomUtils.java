package org.nautilus.core.util;

import java.util.List;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.base.Preconditions;

public class RandomUtils {

	protected static final JMetalRandom random = JMetalRandom.getInstance();

	public static int randInt(int lowerBound, int upperBound) {
		return random.nextInt(lowerBound, upperBound);
	}

	public static double randDouble(double lowerBound, double upperBound) {
		return random.nextDouble(lowerBound, upperBound);
	}

	public static double randDouble() {
		return random.nextDouble();
	}
	
	public static <T> T randValue(List<T> list) {
		
		Preconditions.checkNotNull(list, "The list should not be null");
		Preconditions.checkArgument(!list.isEmpty(), "The list should not be empty");

		int pos = RandomUtils.randInt(0, list.size() - 1);

		return list.get(pos);
	}
}
