package mil.decea.mentorpgapi.domain.administrativo;

public enum CategoriaOrgao {

    NAODEF("N/A","Não Aplicável",""),
    ODG("ODG","Órgão de Direção Geral (Estado-Maior)","Órgão representado pelo Estado-Maior da Força Singular"),
    ODSA("ODSA","Órgão de Direção Setorial e/ou Assessoria (Comandos-Gerais, Diretorias-Gerais, Departamentos, Secretarias)","Órgão, representado pelos Comandos-Gerais, Diretorias-Gerais, Departamentos e Secretarias da Força Singular, encarregado de planejar, executar, coordenar e controlar as atividades setoriais inerentes às suas  atribuições, e em conformidade com as decisões e diretrizes do Comandante da Força."),
    OAF("OAF","Organização Militar de Atividade-Fim (Grupos, Esquadrões, Esquadrilhas, Batalhões, etc)","Órgão que realiza as funções de preparo e de emprego operativo dos recursos destinados à missão principal nos níveis estratégico e tático do campo militar."),
    OAM("OAM","Organização Militar de Atividade-Meio (Institutos, Parques, Centros, Academias, Hospitais, Laboratórios, etc)",
            "Órgão que realiza as funções de apoio à atividade de instrução e adestramento ou de apoio às operações");


    String sigla;
    String nome;
    String descricao;

    CategoriaOrgao(String sigla, String nome, String descricao) {
        this.sigla = sigla;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String toString(){return sigla;}
}
