package mil.decea.mentorpgapi.etc.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public record FirstAdminRecord(@IsValidCpf String cpf,
                               @NotBlank(message = "Informe o nome completo") String nomeCompleto,
                               @NotBlank(message = "Informe o nome de guerra") String nomeGuerra,
                               String email,
                               String celular,
                               @Size(min = 8, message = "A senha deve ter no mínimo 8 caractéres") String senha,
                               String confirmeSenha) {}
