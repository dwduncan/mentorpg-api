package mil.decea.mentorpgapi.domain.agenda;

import mil.decea.mentorpgapi.domain.academic.Aluno;

import java.time.LocalDate;
import java.util.Objects;

public class AulaAluno{

    private final Aluno aluno;
    private final Aulas aula;
    private final LocalDate inicioAula;
    private final LocalDate terminoAula;

    public AulaAluno(Aluno aluno, Aulas aula, LocalDate inicioAula, LocalDate terminoAula) {
        this.aluno = aluno;
        this.aula = aula;
        this.inicioAula = inicioAula;
        this.terminoAula = terminoAula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Aulas getAula() {
        return aula;
    }

    public LocalDate getInicioAula() {
        return inicioAula;
    }

    public LocalDate getTerminoAula() {
        return terminoAula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AulaAluno)) return false;
        AulaAluno aulaAluno = (AulaAluno) o;
        return aluno.equals(aulaAluno.aluno) && aula.equals(aulaAluno.aula) && Objects.equals(inicioAula, aulaAluno.inicioAula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, aula, inicioAula);
    }
}
