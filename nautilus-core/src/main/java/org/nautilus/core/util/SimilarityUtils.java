package org.nautilus.core.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

public class SimilarityUtils {
	
	public static Double getJaccardIndex(final List<String> left, final List<String> right) {
		
		Preconditions.checkNotNull(left, "String left must not be null");
		Preconditions.checkNotNull(right, "String right must not be null");
		
		final int leftLength = left.size();
		final int rightLength = right.size();

		if (leftLength == 0 || rightLength == 0) {
			return 0d;
		}

		final Set<Object> leftSet = new HashSet<>();
        
        for (int i = 0; i < leftLength; i++) {
            leftSet.add(left.get(i));
        }
        
        final Set<Object> rightSet = new HashSet<>();
        
        for (int i = 0; i < rightLength; i++) {
            rightSet.add(right.get(i));
        }
        
        final Set<Object> unionSet = new HashSet<>(leftSet);
        
        unionSet.addAll(rightSet);
        
        final int intersectionSize = leftSet.size() + rightSet.size() - unionSet.size();
        
        return 1.0d * intersectionSize / unionSet.size();
	}
	
	public static Double getJaccardIndexFromInteger(final List<Integer> left, final List<Integer> right) {
		
		Preconditions.checkNotNull(left, "String left must not be null");
		Preconditions.checkNotNull(right, "String right must not be null");
		
		final int leftLength = left.size();
		final int rightLength = right.size();

		if (leftLength == 0 || rightLength == 0) {
			return 0d;
		}

		final Set<Integer> leftSet = new HashSet<>();
        
        for (int i = 0; i < leftLength; i++) {
            leftSet.add(left.get(i));
        }
        
        final Set<Integer> rightSet = new HashSet<>();
        
        for (int i = 0; i < rightLength; i++) {
            rightSet.add(right.get(i));
        }
        
        final Set<Integer> unionSet = new HashSet<>(leftSet);
        
        unionSet.addAll(rightSet);
        
        final int intersectionSize = leftSet.size() + rightSet.size() - unionSet.size();
        
        return 1.0d * intersectionSize / unionSet.size();
	}

	public static Integer getHammingDistance(final List<String> left, final List<String> right) {
		
		Preconditions.checkNotNull(left, "String left must not be null");
		Preconditions.checkNotNull(right, "String right must not be null");
		Preconditions.checkArgument(left.size() == right.size(), "Strings set must have the same length");

		int distance = 0;

		for (int i = 0; i < left.size(); i++) {
			
			if (!left.get(i).equalsIgnoreCase(right.get(i))) {
				distance++;
			}
		}

		return distance;
	}
}
