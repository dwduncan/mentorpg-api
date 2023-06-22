package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.changewatch.InnerValueChange;

import java.util.Arrays;
@InnerValueChange("getNome()")
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

    public EstadoCivil convert(String s) {
        return Arrays.stream(EstadoCivil.values()).filter(p->nome.equalsIgnoreCase(s) || p.name().equalsIgnoreCase(s)).findAny().orElse(null);
    }

}
