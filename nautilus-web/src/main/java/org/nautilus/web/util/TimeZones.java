package org.nautilus.web.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimeZones {
    
    public enum OffsetBase {
        GMT, UTC
    }
    
    private static class TimeZoneComparator implements Comparator<ZoneId> {

        @Override
        public int compare(ZoneId zoneId1, ZoneId zoneId2) {
            
            LocalDateTime now = LocalDateTime.now();

            ZoneOffset offset1 = now.atZone(zoneId1).getOffset();
            ZoneOffset offset2 = now.atZone(zoneId2).getOffset();

            return offset1.compareTo(offset2);
        }
    }
    
    public static final List<String> getAvailableTimeZones() {
        return getAvailableTimeZones(OffsetBase.GMT);
    }
    
    public static final List<String> getAvailableTimeZones(OffsetBase base) {
        
        LocalDateTime now = LocalDateTime.now();
        
        return ZoneId.getAvailableZoneIds().stream()
              .map(ZoneId::of)
              .sorted(new TimeZoneComparator())
              .map(id -> String.format( "(%s%s) %s", base, getOffset(now, id), id.getId()))
              .collect(Collectors.toList());
    }

    private static String getOffset(LocalDateTime dateTime, ZoneId id) {
        return dateTime
          .atZone(id)
          .getOffset()
          .getId()
          .replace("Z", "+00:00");
    }
    
}
