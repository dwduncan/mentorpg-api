package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.EnumConverter;

import java.util.Arrays;

/**
 * Força Aérea Brasileira - (FAB)
 * Instituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * PPGAO-MENTOR
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 10/04/2021
 * Time: 14:59
 */
public enum Titulacao implements EnumConverter<Titulacao> {

    SENHOR("Senhor", "Sr.", "Sra."),
    PROF("Professor", "Prof.", "Profa."),
    PROFM("Professor Mestre", "Prof. M.e", "Profa M.a"),
    PROFD("Professor Doutor", "Prof. D.r", "Profa. D.ra."),
    MESTRE("Mestre", "M.e", "M.a"),
    DOUTOR("Doutor", "D.r", "D.ra"),
    REITOR("Reitor", "Reitor", "Reitora");

    private final String titulo;
    private final String sigla;
    private final String siglaFem;

    Titulacao(String titulo, String sigla, String siglafEM) {
        this.titulo = titulo;
        this.sigla = sigla;
        this.siglaFem = siglafEM;
    }

    @Override
    public String toString() {
        return titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSigla(Sexo sexo) {
        return sexo == Sexo.MASCULINO ? sigla : siglaFem;
    }

    public String getSigla() {
        return sigla;
    }

    public String getSiglaFem() {
        return siglaFem;
    }

    public String getSiglas(){
        return getSigla() + " / " + getSiglaFem();
    }

    @Override
    public Titulacao convert(String s) {
        return Arrays.stream(Titulacao.values()).filter(p->sigla.equalsIgnoreCase(s) || titulo.equalsIgnoreCase(s)
                || p.name().equalsIgnoreCase(s) || siglaFem.equalsIgnoreCase(s)).findAny().orElse(null);
    }
}
