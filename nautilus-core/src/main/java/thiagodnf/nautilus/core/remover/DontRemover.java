package thiagodnf.nautilus.core.remover;

import thiagodnf.nautilus.core.encoding.NSolution;

public class DontRemover extends AbstractRemover {

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {
		return false;
	}
}
