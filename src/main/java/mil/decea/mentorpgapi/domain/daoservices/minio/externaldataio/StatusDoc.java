package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.InnerValueChange;

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

@InnerValueChange("getRotulo()")
public enum StatusDoc {

    NOVO(true,"Nenhum arquivo carregado","Rascunho"),
    INSERIDO(true,"Carregado","Enviado"),
    RECUSADO(true,"O arquivo possui problemas que o impede de ser aceito","Recusado"),
    EMANALISE(false,"Documento sob an\u00E1lise","Em análise"),
    APROVADO(false,"Aprovado","Aprovado");

    private boolean habilitarUpload;
    private String mensagem;
    private String rotulo;

    StatusDoc(boolean habilitarUpload, String mensagem, String rotulo) {
        this.habilitarUpload = habilitarUpload;
        this.mensagem = mensagem;
        this.rotulo = rotulo;
    }


    public String getMensagem() {
        return mensagem;
    }

    public boolean isHabilitarUpload() {
        return habilitarUpload;
    }

    public String getRotulo() {
        return rotulo;
    }
}
