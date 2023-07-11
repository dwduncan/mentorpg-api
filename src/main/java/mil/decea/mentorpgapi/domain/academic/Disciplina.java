package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.agenda.Aulas;

import java.util.ArrayList;
import java.util.List;

@Table(name = "disciplinas", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Disciplina extends SequenceIdEntity implements Comparable<Disciplina> {

    @NotNull
    @NotBlank
    @Column
    private String sigla;

    @NotNull
    @NotBlank
    @Column
    private String nome;

    @Column
    private String instituicao = "ITA";

    private double creditoMaximo;

    @Column(columnDefinition = "TEXT")
    private String horasSemanais;

    @Column(columnDefinition = "TEXT")
    private String requisitoRecomendado;

    @Column(columnDefinition = "TEXT")
    private String requisitoExigido;

    @Column(columnDefinition = "TEXT")
    private String ementa;

    @Column(columnDefinition = "TEXT")
    private String professor = "";

    @Column(columnDefinition = "TEXT")
    private String bibliografia;

    @NotNull(message = "Informe a qual programa a disciplina está vinculado")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramaPosGraduacao programaPosGraduacao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "disciplina")
    private List<Aulas> aulas;

    public void setSigla(String sigla) {
        if (sigla != null) sigla = sigla.toUpperCase().replaceAll("\\s","");
        this.sigla = sigla;
    }

    public List<Aulas> getAulas() {
        if (aulas == null) aulas = new ArrayList<>();
        return aulas;
    }

    public String getSiglaENome(){
        return sigla + " - " + nome;
    }

    public String getSiglaPrograma(){
        return getProgramaPosGraduacao() != null ? getProgramaPosGraduacao().getSigla() : "";
    }

    @Override
    public int compareTo(Disciplina o) {
        return sigla.compareToIgnoreCase(o.sigla);
    }

    @Override
    public String getEntityDescriptor() {
        return "Disciplina " + getNome();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
