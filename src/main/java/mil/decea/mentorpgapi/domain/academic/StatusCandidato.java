package mil.decea.mentorpgapi.domain.academic;

public enum StatusCandidato {

    PREENCHIMENTO("Sem Orientador","O candidato está ainda na fase de inscrição e análise da ficha/documentação e critérios mínimos"),
    DEFININDO_ORIENTADOR("Buscando Orientador","O candidato atende aos critérios para inscrição e já está em acompanhamento pela coordenação para atribuição de orientador"),
    ACEITO("Aceito", "O candidato foi aceito por algum orientador e aguarda a designação pelo CONDIR para matrícula no respectivo curso"),
    ACEITO_CONDICIONAL("Aceito Condicional", "O candidato foi aceito por algum orientador mas possui alguma condição que precisará´ser atendida antes do ato de matrícula e ainda aguarda a designação pelo CONDIR"),
    MATRICULA("Pronto para Matrícula","O candidato foi designado e atende a todas as condições para matrícula no respectivo curso"),
    NAOACEITO("Não Aceito","Não houve aceitação por parte de nenhuns dos orientadores consultados pela coordenação"),
    NAODESIGANADO("Não Designado","O CONDIR rejeitou/não designou o candidato para realização do curso"),
    EXCLUIDO("Excluído do Processo","Situações em que o candidato é excluído do processo de admissão e que não seja por falta de orientador ou por ter sido rejeitado pelo CONDIR");

    private final String nome;
    private final String descricao;

    StatusCandidato(String nome, String descricao) {
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
