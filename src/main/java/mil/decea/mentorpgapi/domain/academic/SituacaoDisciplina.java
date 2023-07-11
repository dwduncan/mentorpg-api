package mil.decea.mentorpgapi.domain.academic;

public enum SituacaoDisciplina {

    PLANEJADO("Planejado"),
    MATRICULADO("Matriculado"),
    OUVINTE("Ouvinte"),
    APROVADO("Aprovado"),
    CANCELADO("Cancelado"),
    REPROVADO("Reprovado");

    private String situacao;

    SituacaoDisciplina(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }

}
