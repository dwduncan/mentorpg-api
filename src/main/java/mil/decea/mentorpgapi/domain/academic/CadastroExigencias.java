package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "cadastroexigencias", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class CadastroExigencias extends SequenceIdEntity implements Comparable<CadastroExigencias> {

    @Column
    private String exigencia;
    @Column(columnDefinition = "TEXT")
    private String descricao;

    private int indice;
    private boolean aplicavelEspecializacao;
    private boolean aplicavelMestrado;
    private boolean aplicavelDoutorado;
    private boolean controlarPercentualExecucao;

    @Override
    public int compareTo(CadastroExigencias o) {
        return indice - o.indice;
    }

    @Override
    public String toString() {
        return exigencia;
    }

    @Override
    public String getEntityDescriptor() {
        return exigencia;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
