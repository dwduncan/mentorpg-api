package mil.decea.mentorpgapi.domain.changewatch.trackdefiners;


import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TrackChange {
    String[] onlyWithFieldsName() default {};
}
