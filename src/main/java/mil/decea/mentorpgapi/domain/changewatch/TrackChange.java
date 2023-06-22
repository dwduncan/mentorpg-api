package mil.decea.mentorpgapi.domain.changewatch;


import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TrackChange {

    Class<? extends Record> recordClass() default EmptyRecord.class;

}
