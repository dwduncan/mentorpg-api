package mil.decea.mentorpgapi.domain.academic;

public enum StatusConsultaOrientador {

    NAO_CONSULTADO("Não consultado"),
    ENVIADO("Consulta enviada"),
    ANALISANDO("Mais tempo para analisar"),
    ALTERACAO_TEMA("Solicitou alteração de tema"),
    RECUSADO("Recusado"),
    ACEITO("Aceito"),
    DESCONSIDERAR("Desconsiderar");

    private final String situacao;

    StatusConsultaOrientador(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }
}
