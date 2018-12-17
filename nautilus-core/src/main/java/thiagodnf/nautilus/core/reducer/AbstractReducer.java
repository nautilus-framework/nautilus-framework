package thiagodnf.nautilus.core.reducer;

import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractReducer {

	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract String getName() ;
}
