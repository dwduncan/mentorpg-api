package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.time.LocalDate;
@Table(name = "cumprimentoexigencias", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class CumprimentoExigencia extends SequenceIdEntity implements Comparable<CumprimentoExigencia> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private RelatorioAcademico relatorioAcademico;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private CadastroExigencias cadastroExigencia;

    @Column(columnDefinition = "DATE")
    private LocalDate atendidaEm;

    @Column(columnDefinition = "DATE")
    private LocalDate prazoInicio;

    @Column(columnDefinition = "DATE")
    private LocalDate prazoTermino;

    @Column(columnDefinition = "DATE")
    private LocalDate dataEstimadaTermino;

    @Max(100)
    @Min(0)
    private int totalExecutado;

    @Override
    public int compareTo(CumprimentoExigencia o) {
        return cadastroExigencia.compareTo(o.cadastroExigencia);
    }

    @Override
    public String getEntityDescriptor() {
        return cadastroExigencia.getExigencia();
    }

    @Override
    public TrackedEntity getParentObject() {
        return relatorioAcademico;
    }
}
