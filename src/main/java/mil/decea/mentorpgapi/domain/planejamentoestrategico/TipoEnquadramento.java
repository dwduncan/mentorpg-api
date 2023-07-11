package mil.decea.mentorpgapi.domain.planejamentoestrategico;

/**
 *  JAMAIS APAGUE OU ALTERE A ORDEM DOS ELEMENTOS!!! A ORDEM DELES É A CHAVE PRIMÁRIA PARA AS CONFIGURAÇÕES SALVAS NO BD
 */
public enum TipoEnquadramento {

    ACAOINSTITUCIONAL("Ação institucional","A"),
    CAPACIDADE("Capacidade","CP"),
    INOVACAO("Tecnologia e Inovação","I"),//Contribuição científico-tecnológica e de inovação
    EIXOESTARTEGICO("Eixo estratégico","EE"),
    POSSIBILIDADE_ATUACAO("Possibilidade de atuação","PA"),
    TAREFA("Tarefa","T");

    private final String nome;
    private final String prefixo;

    /**
     *  JAMAIS APAGUE OU ALTERE A ORDEM DOS ELEMENTOS!!! A ORDEM DELES É A CHAVE PRIMÁRIA PARA AS CONFIGURAÇÕES SALVAS NO BD
     */
    TipoEnquadramento(String nome, String prefixo) {
        this.nome = nome;
        this.prefixo = prefixo;
    }

    public String getNome() {
        return nome;
    }

    public String getPrefixo() {
        return prefixo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
