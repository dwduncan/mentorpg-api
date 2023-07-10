package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "propostapremilinar", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PropostaPremilinar extends Pesquisa {

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "propostaPesquisa")
    private  Candidato candidato;

    @Column(columnDefinition = "TEXT")
    private String contextualizacao;

    @Column(columnDefinition = "TEXT")
    private String problemaPesquisa;

    @Column(columnDefinition = "TEXT")
    private String objetivoPesquisa;

    @Column(columnDefinition = "TEXT")
    private String informacoesComplementares;

    @Column(columnDefinition = "TEXT")
    private String aplicacaoConhecimentos;

    @Column(columnDefinition = "TEXT")
    private String areaDeInteresse;

    @Column(columnDefinition = "TEXT")
    private String sugestaoOrientacao;

    @Override
    public String getEntityDescriptor() {
        return "Proposta premilinar" + candidato.getUser().getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return candidato;
    }
}
