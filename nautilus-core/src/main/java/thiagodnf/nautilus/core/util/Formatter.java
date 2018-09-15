package thiagodnf.nautilus.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Formatter {

	public static String date(Date date) {

		LocalDateTime d = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE, dd MMM yyyy HH:mm:ss");

		return d.format(formatter);
	}
}
