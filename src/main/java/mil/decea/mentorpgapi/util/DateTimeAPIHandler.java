package mil.decea.mentorpgapi.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class DateTimeAPIHandler {
    private static final List<TemporalStringConverter<?>> allPatterns = List.of(
            new TemporalStringConverter<LocalDate>("dd/MM/yyyy",Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$").pattern(), new LocalDateConverter()),
            new TemporalStringConverter<LocalDate>("dd/MM/yy",Pattern.compile("^\\d{2}/\\d{2}/\\d{2}$").pattern(), new LocalDateConverter()),
            new TemporalStringConverter<LocalTime>("HH:mm",Pattern.compile("^\\d{2}:\\d{2}$").pattern(), new LocalTimeConverter()),
            new TemporalStringConverter<LocalTime>("HH:mm:ss",Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").pattern(), new LocalTimeConverter()),
            new TemporalStringConverter<LocalDateTime>("dd/MM/yyyy HH:mm",Pattern.compile("^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$").pattern(), new LocalDateTimeConverter()),
            new TemporalStringConverter<LocalDateTime>("dd/MM/yy HH:mm",Pattern.compile("^\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}$").pattern(), new LocalDateTimeConverter()),
            new TemporalStringConverter<LocalDateTime>("dd/MM/yy HH:mm:ss",Pattern.compile("^\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}$").pattern(), new LocalDateTimeConverter()),
            new TemporalStringConverter<LocalDateTime>("dd/MM/yyyy HH:mm:ss",Pattern.compile("^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$").pattern(), new LocalDateTimeConverter()));
    public static String converter(LocalDateTime localDateTime){
        if (localDateTime == null) return "";
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }


    public static String converter(LocalDate localDate){
        if (localDate == null) return "";
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static <T extends Temporal> T converterStringDate(String date){
        if (date == null || date.isBlank()) return null;
        return (T) allPatterns.stream().map(tsc -> tsc.convert(date)).filter(Objects::nonNull).findAny().orElse(null);
    }

    static class LocalDateConverter implements CustomConverter<LocalDate>{
        @Override
        public LocalDate convert(String temporal, String temporalFormat) {
            return LocalDate.parse(temporal,DateTimeFormatter.ofPattern(temporalFormat));
        }
    }
    static class LocalTimeConverter implements CustomConverter<LocalTime>{
        @Override
        public LocalTime convert(String temporal, String temporalFormat) {
            return LocalTime.parse(temporal,DateTimeFormatter.ofPattern(temporalFormat));
        }
    }

    static class LocalDateTimeConverter implements CustomConverter<LocalDateTime>{
        @Override
        public LocalDateTime convert(String temporal, String temporalFormat) {
            return LocalDateTime.parse(temporal,DateTimeFormatter.ofPattern(temporalFormat));
        }
    }

    static class TemporalStringConverter<T extends Temporal>{

        String temporalFormat;
        String pattern;
        CustomConverter<T> converter;

        public TemporalStringConverter(String temporalFormat, String pattern, CustomConverter<T> converter) {
            this.temporalFormat = temporalFormat;
            this.pattern = pattern;
            this.converter = converter;
        }

        public T convert(String temporal){

            if (temporal.matches(pattern)){
                return converter.convert(temporal, temporalFormat);
            }

            return null;
        }
    }

    private interface CustomConverter<T extends Temporal> {
        T convert(String temporal, String temporalFormat);
    }

    public static void main(String... a){
        String d = "15/11/2023 10:45";
        LocalDateTime ld = converterStringDate(d);
        System.out.println(ld);
    }


}