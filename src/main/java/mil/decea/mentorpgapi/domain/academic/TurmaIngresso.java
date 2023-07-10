package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "turmaingresso", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TurmaIngresso extends IdEntity implements Comparable<TurmaIngresso> {

    private static String formato = ";99@99/99/9999@99/99/9999;";
    public static final int anoInicialIngressos = 2002;
    public static final int anoFinalIngressos = 2070;

    private String registradoPor;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime atualizadoEm;

    @Id
    @Min(value = anoInicialIngressos, message = "O ano da turma ano n\u00E3o pode ser inferior a 2002")
    @Max(value = anoFinalIngressos, message = "O ano da turma ano n\u00E3o pode ser superior a 2070")
    private Integer anoDeIngresso;
    @Column(columnDefinition = "DATE")
    private LocalDate dataAberturaInscricao;
    @Column(columnDefinition = "DATE")
    private LocalDate dataEncerramentoInscricao;

    @Column(length = 765)
    private String prazosDoutorado = "";
    @Column(length = 765)
    private String prazosMestrado = "";;

    @Column(columnDefinition = "DATE")
    private LocalDate inicioDasAulas;

    public TurmaIngresso(int ano) {
        setPeriodosDefault(ano);
    }

    public Long getId() {
        return anoDeIngresso != null ? anoDeIngresso.longValue() : null;
    }

    public void setId(Long id) {
        if (id != null){
            anoDeIngresso = id.intValue();
        }else{
            anoDeIngresso = null;
        }
    }

    public void setPeriodosDefault(int anoDeIngresso){
        this.anoDeIngresso = anoDeIngresso;
        dataAberturaInscricao = LocalDate.of(anoDeIngresso-2, 9, 1);
        dataEncerramentoInscricao = LocalDate.of(anoDeIngresso - 1, 1, 31);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurmaIngresso)) return false;

        TurmaIngresso that = (TurmaIngresso) o;

        return Objects.equals(anoDeIngresso, that.anoDeIngresso);
    }

    @Override
    public int hashCode() {
        return anoDeIngresso != null ? anoDeIngresso.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TurmaIngresso " +anoDeIngresso;
    }

    @Override
    public int compareTo(TurmaIngresso o) {
        return o.anoDeIngresso - anoDeIngresso;
    }

    public String getPrazosDoutorado() {
        if (prazosDoutorado == null) prazosDoutorado = "";
        return prazosDoutorado;
    }

    public String getPrazosMestrado() {
        if (prazosMestrado == null) prazosMestrado = "";
        return prazosMestrado;
    }

    public void resetarPrazos(){
        setPrazosMestrado("");
        setPrazosDoutorado("");
    }

    public void setPrazosDoutorado(String prazosDoutorado) {
        this.prazosDoutorado = prazosDoutorado;
    }

    public void setPrazosMestrado(String prazosMestrado) {
        this.prazosMestrado = prazosMestrado;
    }
}
