package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.user.User;

import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Mensagem<E extends SequenceIdEntity> extends SequenceIdEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User usuarioEmissor;
    @Column(columnDefinition = "TEXT")
    private String mensagem;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime enviadoEm;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime vistoEm;

    public abstract E getReceptor();

    public abstract void setReceptor(E receptor);


}