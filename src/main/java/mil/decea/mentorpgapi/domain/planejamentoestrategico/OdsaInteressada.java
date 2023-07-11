package mil.decea.mentorpgapi.domain.planejamentoestrategico;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.academic.Aluno;

@Table(name = "odsainteressadas", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OdsaInteressada extends SequenceIdEntity implements Comparable<OdsaInteressada>{

    @ManyToOne(fetch = FetchType.LAZY)
    private Aluno aluno;

    @NotBlank
    private String odsa = "";
    private String om;
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    private int prioridade = 1;
    private boolean selecionada;

    int ordem;


    public OdsaInteressada(Aluno aluno) {
        this.odsa = aluno.getOdsaOrigem();
        this.aluno = aluno;
    }

    @Override
    public int compareTo(OdsaInteressada oi) {
        int comp = prioridade - oi.prioridade;
        return comp == 0 ? getOrdem() - oi.getOrdem() : comp;
    }

    public String getOm() {
        return om;
    }

    public void setOm(String om) {
        this.om = om != null ? om.toUpperCase().trim() : null;
    }

    @Override
    public String getEntityDescriptor() {
        return odsa;
    }

    @Override
    public TrackedEntity getParentObject() {
        return aluno;
    }
}
