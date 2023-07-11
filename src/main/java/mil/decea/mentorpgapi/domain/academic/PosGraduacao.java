package mil.decea.mentorpgapi.domain.academic;

public enum PosGraduacao {

    ESPECIALIZACAO("Especialização","E"),
    MESTRADO("Mestrado","M"),
    DOUTORADO("Doutorado","D"),
    DOUTORADODIRETO("Doutorado Direto","D");

    private final String nome;
    private final String sigla;


    PosGraduacao(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }
}
