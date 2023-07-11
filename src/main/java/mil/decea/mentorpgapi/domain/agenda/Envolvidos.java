package mil.decea.mentorpgapi.domain.agenda;

public enum Envolvidos {

    INDIVIDUAL("Individual"),
    COORDENACAO("Coordenação"),
    ALUNOS("Alunos"),
    TODOS("Todos");

    private final String titulo;

    Envolvidos(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
