package mil.decea.mentorpgapi.domain.user.dto;

import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public record UserDTO(
        @IsValidCpf String cpf
) {
}
