package mil.decea.mentorpgapi.domain.user.dto;

import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public record UserBasicDTO(
                        Long id,
                        @IsValidCpf String cpf,
                        String nomeguerra,
                        String nomecompleto,
                        String posto,
                        String quadro
        ) {

        public UserBasicDTO(User user){
                this(user.getId(), user.getCpf(), user.getNomeGuerra(), user.getNomeCompleto(), user.getPosto().name(), user.getQuadro());
        }
}
