package mil.decea.mentorpgapi.domain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdConverter;
import mil.decea.mentorpgapi.domain.user.EstadoCivil;

import java.util.Arrays;

public interface EnumConverter<T extends Enum<T>>  {
    T convert(String s);

}
