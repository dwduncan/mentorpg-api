package mil.decea.mentorpgapi.util.datageneration;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({CONSTRUCTOR, TYPE, FIELD, METHOD})
public @interface OptionalsRecordField {
}