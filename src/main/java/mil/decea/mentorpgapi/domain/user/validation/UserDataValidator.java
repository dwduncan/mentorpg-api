package mil.decea.mentorpgapi.domain.user.validation;

import mil.decea.mentorpgapi.domain.daoservices.DTOValidator;
import mil.decea.mentorpgapi.domain.user.UserRecord_old;
import mil.decea.mentorpgapi.domain.user.validation.annotations.CpfConstraint;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidator implements DTOValidator<UserRecord_old> {
    @Override
    public void validate(UserRecord_old dto) {

        if (!CpfConstraint.isValidCpf(dto.cpf())) throw new MentorValidationException("Cpf inválido");

    }
}
