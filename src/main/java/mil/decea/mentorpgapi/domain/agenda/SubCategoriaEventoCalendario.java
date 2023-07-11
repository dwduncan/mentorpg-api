package mil.decea.mentorpgapi.domain.agenda;

public enum SubCategoriaEventoCalendario {
    DISP_MEDICA(GeradorEventos.CATEGORIA_PESSOAL, "Dispensa Médica", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    DISP_RECOPENSA(GeradorEventos.CATEGORIA_PESSOAL, "Dispensa como Recompensa", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    ESCALA_SVC(GeradorEventos.CATEGORIA_PESSOAL + GeradorEventos.CATEGORIA_ADM_EXTERNA, "Escala de Serviço", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    MISSAO_ORGANICA(GeradorEventos.CATEGORIA_PESSOAL + GeradorEventos.CATEGORIA_ADM_ORGANICA, "Missão a serviço do PPGAO", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    FERIAS(GeradorEventos.CATEGORIA_PESSOAL, "Férias", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    LUTO(GeradorEventos.CATEGORIA_PESSOAL, "Luto", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    NUPCIAS(GeradorEventos.CATEGORIA_PESSOAL, "Núpcias", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    CURSOS(GeradorEventos.CATEGORIA_PESSOAL, "Cursos/Aulas Interesse Particular", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    OUTROS(GeradorEventos.CATEGORIA_PESSOAL, "Outros", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    LIC_MATPAT(GeradorEventos.CATEGORIA_PESSOAL, "Licença Maternidade/Paternidade", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    ADM_RELACIONADO(GeradorEventos.CATEGORIA_ADM_ORGANICA, "Administrativo", ()->new EventoAdministrativo(), Envolvidos.COORDENACAO, Envolvidos.ALUNOS, Envolvidos.TODOS),
    ADM_NAORELACIONADO(GeradorEventos.CATEGORIA_ADM_EXTERNA, "Atendimento Outras Organizações", ()->new EventoAdministrativo(), Envolvidos.COORDENACAO, Envolvidos.ALUNOS, Envolvidos.TODOS),
    INFORMATIVO(GeradorEventos.CATEGORIA_ADM_ORGANICA, "Informativo/Divulgação", ()->new EventoAdministrativo(), Envolvidos.COORDENACAO, Envolvidos.ALUNOS, Envolvidos.TODOS),
    ACADEMICO(GeradorEventos.CATEGORIA_ACADEMICO, "Evento Acadêmico", ()->new EventoAdministrativo(), Envolvidos.COORDENACAO, Envolvidos.ALUNOS, Envolvidos.TODOS),
    DOCENCIA(GeradorEventos.CATEGORIA_PESSOAL, "Ministrar Aulas/Cursos", ()->new EventoPessoal(), Envolvidos.INDIVIDUAL),
    AULA(GeradorEventos.CATEGORIA_AULA, "Aula", ()->new Aulas(), Envolvidos.INDIVIDUAL);

    private final int categoria;
    private final String subCategoria;
    private final Envolvidos[] envolvidos;
    private final InstanciadorNovoEvento novaInstanciaGeradorEventos;

    SubCategoriaEventoCalendario(int categoria, String subCategoria, InstanciadorNovoEvento novaInstanciaGeradorEventos, Envolvidos... envolvidos) {
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.novaInstanciaGeradorEventos = novaInstanciaGeradorEventos;
        this.envolvidos = envolvidos;
        //this.novaInstanciaGeradorEventos.setSubCategoriaEvento(this);
        //this.novaInstanciaGeradorEventos.setTitulo(this.getSubCategoria());
    }

    public boolean isDaCategoria(int _categoria) {
        return (categoria & _categoria) != 0;
    }

    public int getCategoria() {
        return categoria;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public Envolvidos[] getEnvolvidos() {
        return envolvidos;
    }

    public InstanciadorNovoEvento getNovaInstanciaGeradorEventos() {
        return novaInstanciaGeradorEventos;
    }

    @Override
    public String toString() {
        return subCategoria;
    }
}
