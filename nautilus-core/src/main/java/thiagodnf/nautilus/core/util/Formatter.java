package thiagodnf.nautilus.core.util;

import java.text.DecimalFormat;

public class Formatter {

	public String format(double value) {

		DecimalFormat df2 = new DecimalFormat(".####");

		return df2.format(value);
	}
}
