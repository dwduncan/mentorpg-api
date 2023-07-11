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
 * Time: 11:58
 */
public enum StatusSolicitacao {

    NOVO("Rascunho"),
    SUBMETIDA("Submetido"),
    EM_ANALISE("Em análise"),
    COM_PENDENCIAS("Corrigir Pendências"),
    CONFORMIDADE("Conformidade"),
    AUTORIZADO("Autorizado"),
    NAOCONFORMIDADE("Não Conformidade"),
    PENDENCIACOORIGIDA("Retificada");

    private final String status;

    StatusSolicitacao(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
