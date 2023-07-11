package mil.decea.mentorpgapi.domain.agenda;

public enum CICLO {
    UNICO("Único"),
    DIARIO("Diário"),
    SEMANAL("Semanal"),
    MENSAL("Mensal"),
    ANUAL("Anual");

    private final String titulo;

    CICLO(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
