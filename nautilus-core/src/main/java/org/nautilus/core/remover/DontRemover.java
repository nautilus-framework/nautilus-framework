package org.nautilus.core.remover;

import org.nautilus.core.encoding.NSolution;

public class DontRemover extends AbstractRemover {

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {
		return false;
	}
}
