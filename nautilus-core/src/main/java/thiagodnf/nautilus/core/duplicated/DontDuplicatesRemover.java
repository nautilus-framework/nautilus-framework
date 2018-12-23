package thiagodnf.nautilus.core.duplicated;

import thiagodnf.nautilus.core.encoding.NSolution;

public class DontDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "Don't Remove";
	}

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {
		return false;
	}
}
