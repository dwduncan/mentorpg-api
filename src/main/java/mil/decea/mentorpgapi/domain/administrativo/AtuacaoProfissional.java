package mil.decea.mentorpgapi.domain.administrativo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.academic.Candidato;

import java.time.LocalDate;

@SuppressWarnings("unused")
@Table(name = "atuacaoprofissional", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AtuacaoProfissional extends SequenceIdEntity {


    private String instituicao;
    @Column(columnDefinition = "DATE")
    private LocalDate inicio;
    @Column(columnDefinition = "DATE")
    private LocalDate termino;
    @Column(columnDefinition = "TEXT")
    private String atividade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Candidato candidato;

    protected int ordem;

    @Override
    public String getEntityDescriptor() {
        return null;
    }

    @Override
    public TrackedEntity getParentObject() {
        return candidato.getParentObject();
    }
}
