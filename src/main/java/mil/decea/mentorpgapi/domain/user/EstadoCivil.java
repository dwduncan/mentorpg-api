package mil.decea.mentorpgapi.domain.user;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import mil.decea.mentorpgapi.domain.EnumConverter;

import java.util.Arrays;

public enum EstadoCivil implements EnumConverter<EstadoCivil> {

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

    @Override
    public EstadoCivil convert(String s) {
        return Arrays.stream(EstadoCivil.values()).filter(p->nome.equalsIgnoreCase(s) || p.name().equalsIgnoreCase(s)).findAny().orElse(null);
    }

}
