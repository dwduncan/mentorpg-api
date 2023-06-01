package mil.decea.mentorpgapi.domain;

import com.fasterxml.jackson.databind.util.StdConverter;

public class EnumConverterImpl<T extends EnumConverter<?>> extends StdConverter<String, T> {
    @Override
    public T convert(String value) {
        return new T().convert(value);
    }
}
