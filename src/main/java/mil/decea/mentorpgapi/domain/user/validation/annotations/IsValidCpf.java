package mil.decea.mentorpgapi.domain.user.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

@Documented
@Constraint(validatedBy = CpfConstraint.class)
@Target( { METHOD, FIELD, PARAMETER,ANNOTATION_TYPE, CONSTRUCTOR, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidCpf {
    String message() default "Cpf inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
