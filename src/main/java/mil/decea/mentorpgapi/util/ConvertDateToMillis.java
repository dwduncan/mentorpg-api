package mil.decea.mentorpgapi.util;

import java.time.*;

public class ConvertDateToMillis {

    public static Long converter(LocalDateTime localDateTime){
        if (localDateTime == null) return null;
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }


    public static Long converter(LocalDate localDate){
        if (localDate == null) return null;
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

}
