package mil.decea.mentorpgapi.domain.externaldtaio;

/**
 * Força Aérea Brasileira - (FAB)
 * Instituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * PPGAO-MENTOR
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 25/03/2021
 * Time: 13:30
 */
public enum StatusDoc {

    NOVO(true,"Nenhum arquivo carregado"),
    INSERIDO(true,"Carregado"),
    RECUSADO(true,"O arquivo possui problemas que o impede de ser aceito"),
    EMANALISE(false,"Documento sob an\u00E1lise"),
    APROVADO(false,"Aprovado");

    private boolean habilitarUpload;
    private String mensagem;

    StatusDoc(boolean habilitarUpload, String mensagem) {
        this.habilitarUpload = habilitarUpload;
        this.mensagem = mensagem;
    }


    public String getMensagem() {
        return mensagem;
    }

    public boolean isHabilitarUpload() {
        return habilitarUpload;
    }
}
