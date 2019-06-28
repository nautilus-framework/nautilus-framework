package thiagodnf.nautilus.core.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

public class Formatter {
	
	public static String fileSize(long size) {
		return FileUtils.byteCountToDisplaySize(size);
	}
	
	public static String date(long l, String timeZone) {
		return date(new Date(l), timeZone);
	}

	public static String date(Date date, String timeZone) {

	    ZoneId zoneId = ZoneId.of(timeZone.split(" ")[1]);
	    
	    ZonedDateTime d = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return d.format(formatter);
	}
	
	public static String interval(long interval) {

		final long hr = TimeUnit.MILLISECONDS.toHours(interval);
		final long min = TimeUnit.MILLISECONDS.toMinutes(interval - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(interval - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(interval - TimeUnit.HOURS.toMillis(hr)- TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));

		return String.format("%02d hours, %02d minutes, %02d seconds", hr, min, sec, ms);
	}
}
