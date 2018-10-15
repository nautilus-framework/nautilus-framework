package thiagodnf.nautilus.core.ranking;

import java.util.Comparator;

public class RankingComparator implements Comparator<RankingItem>{

	private boolean isAscending;
	
	/**
	 * Constructor. As default this comparator is an ascending one.
	 * If you want to sort by descending, {@link #RankingComparator(boolean)}
	 */
	public RankingComparator() {
		this(true);
	}
	
	/**
	 * Constructor
	 * 
	 * @param isAscending Define if it will be sorted by ascending or descending order
	 */
	public RankingComparator(boolean isAscending ) {
		this.isAscending = isAscending;
	}
	
	public boolean isAscending() {
		return isAscending;
	}

	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int compare(RankingItem o1, RankingItem o2) {
		
		if(isAscending) {
			return Double.compare(o1.getValue(), o2.getValue());
		}
		
		return Double.compare(o2.getValue(), o1.getValue());
	}
}
