package mil.decea.mentorpgapi.domain.user.validation;

import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import mil.decea.mentorpgapi.domain.DTOValidator;
import mil.decea.mentorpgapi.domain.user.dto.UserDTO;
import mil.decea.mentorpgapi.domain.user.validation.annotations.CpfConstraint;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidator implements DTOValidator<UserDTO> {
    @Override
    public void validate(UserDTO dto) {

        if (!CpfConstraint.isValidCpf(dto.cpf())) throw new MentorValidationException("Cpf inválido");

    }
}
