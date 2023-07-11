package mil.decea.mentorpgapi.domain.planejamentoestrategico;

public enum VinculoTeses {

    CONTINUACAO("Continuação","O projeto proposto é continuação direta do trabalho selecionado. " +
            "Seja confrontando, melhorando ou aprofundando o projeto/linha de pesquisa."),
    FORTE_RELACAO("Forte","O projeto proposto tem muitos assuntos/métodos/abordagens/conceitos/etc que complemetam, interagem, abordam, " +
            "refutam ou corroboram com os argumentos e resultados do trabalho selecionado."),
    MEDIA_RELACAO("Médio","O projeto proposto compartilha uma quantidade razoável de conceitos ou possui um conceito muito importante relacionado ao trabalho selecionado."),
    FRACA_RELACAO("Fraco","O projeto proposto possui alguns conceitos ou tópicos que também podem ser encontrados no trabalho selecionado, " +
            "mas abordam/empregam/utilizam para fins/objetivos distintos.");

    private final String nome;
    private final String descricao;

    VinculoTeses(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
