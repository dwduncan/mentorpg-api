package mil.decea.mentorpgapi.domain.user;

import jakarta.validation.constraints.NotBlank;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public record LoginRecord(@IsValidCpf String cpf, @NotBlank(message = "Informe a senha") String senha) {
}
