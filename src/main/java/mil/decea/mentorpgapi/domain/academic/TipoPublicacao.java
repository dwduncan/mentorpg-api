package mil.decea.mentorpgapi.domain.academic;

/**
 * Força Aérea Brasileira - (FAB)
 * Instituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * PPGAO-MENTOR
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 20/03/2021
 * Time: 17:19
 */
public enum TipoPublicacao {

    ARTIGOPERIODICO("Artigo em periódico"),
    ARTIGOCONGRESSO("Artigo em congresso"),
    DISSERTACAO("Dissertação"),
    TTC("TTC"),
    TESE("Tese"),
    POSTER("Poster");

    private final String tipo;

    TipoPublicacao(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

}
