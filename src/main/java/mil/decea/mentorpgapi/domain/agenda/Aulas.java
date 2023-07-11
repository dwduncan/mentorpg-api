package mil.decea.mentorpgapi.domain.agenda;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.academic.Disciplina;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Table(name = "aulas", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aulas extends AbstractGeradorEventos<Aulas> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Disciplina disciplina;

    @Override
    public Aulas getGeradorEventos() {
        return this;
    }

    @Override
    public boolean isCadastravelPeloUsuario() {
        return false;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
        setTitulo(disciplina.getSigla());
        setDescricao(disciplina.getNome());
    }

    @Override
    public int calcularValorCompostoDiasSemana(List<DayOfWeek> diasDaSemana) {
        setDiasSemana(super.calcularValorCompostoDiasSemana(diasDaSemana));
        return getDiasSemana();
    }

    @Override
    public String getTitulo() {
        return disciplina.getSigla();
    }


    public String getDescricaoDiasSemana(){
        StringBuilder sb = new StringBuilder("");
        Locale pt_BR = new Locale("pt","BR");
        for(DayOfWeek dow : Periodicidade.mapaDiasSemana.keySet()){
            if ((Periodicidade.mapaDiasSemana.get(dow) & getDiasSemana()) != 0) {
                if (!sb.toString().isBlank()) sb.append(", ");
                sb.append(dow.getDisplayName(TextStyle.SHORT, pt_BR).toUpperCase());
            }
        }
        return sb.toString();
    }

    @Override
    public SubCategoriaEventoCalendario getSubCategoriaEvento() {
        return SubCategoriaEventoCalendario.AULA;
    }


    @Override
    public String getEntityDescriptor() {
        return "Aula " + getDisciplina().getSigla();
    }

    @Override
    public TrackedEntity getParentObject() {
        return disciplina;
    }
}
