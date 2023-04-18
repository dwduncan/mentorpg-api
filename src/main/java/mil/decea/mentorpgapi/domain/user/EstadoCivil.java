package mil.decea.mentorpgapi.domain.user;

public enum EstadoCivil {

    SOL("Solteiro(a)"),
    CAS("Casado(a)"),
    SEP("Separado(a)"),
    DIV("Divorciado(a)"),
    VIU("Viúvo(a)");

    private String nome;

    EstadoCivil(String nome) {


        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
