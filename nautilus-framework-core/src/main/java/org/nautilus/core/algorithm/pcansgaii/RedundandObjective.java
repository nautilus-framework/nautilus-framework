package org.nautilus.core.algorithm.pcansgaii;

public class RedundandObjective {
	
	protected int i;
	
	protected int j;
	
	protected int k;
	
	protected boolean isNegatively;
	
	public RedundandObjective(int i, int j, int k, boolean isNegatively) {
		this.i = i;
		this.j = j;
		this.k = k;
		this.isNegatively = isNegatively;
	}
	
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("f").append(i).append(" and ").append("f").append(j);

		if (isNegatively) {
			builder.append(" have identical negative correlation with ");
		} else {
			builder.append(" have identical positive correlation with ");
		}

		builder.append("f").append(k).append(" and a positive correlation among themselves");

		return builder.toString();
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}	
}


