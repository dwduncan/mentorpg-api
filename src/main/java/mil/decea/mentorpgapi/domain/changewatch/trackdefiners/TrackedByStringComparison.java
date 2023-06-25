package mil.decea.mentorpgapi.domain.changewatch.trackdefiners;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Utilizado quando para determinar que Objetos mais complexos não terão cada campo/atributo de
 * entidade comparados. Toda a mudança é comparado exclusivamente através do método toString()
 * que deverá ser sobre-escrito na classe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TrackedByStringComparison {

    String recordFieldToCompare() default "";
}
