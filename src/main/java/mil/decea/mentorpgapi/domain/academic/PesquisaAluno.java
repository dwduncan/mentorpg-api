package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "pesquisaAluno", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PesquisaAluno extends Pesquisa{

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pesquisaAluno")
    private Aluno aluno;

    @Override
    public String getEntityDescriptor() {
        return "Programa de Pesquisa " + aluno.getUser().getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return aluno.getUser();
    }
}
