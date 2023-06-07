package mil.decea.mentorpgapi.domain.user.validation;

import mil.decea.mentorpgapi.domain.DTOValidator;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.CpfConstraint;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidator implements DTOValidator<UserRecord> {
    @Override
    public void validate(UserRecord dto) {

        if (!CpfConstraint.isValidCpf(dto.cpf())) throw new MentorValidationException("Cpf inválido");

    }
}
