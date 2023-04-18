package mil.decea.mentorpgapi.domain.user.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CpfConstraint.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidCpf {
    String message() default "Cpf inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
