package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.planejamentoestrategico.OdsaInteressada;
import mil.decea.mentorpgapi.domain.planejamentoestrategico.PropostaOperacional;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "alunos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aluno extends Pesquisador implements Comparable<Aluno>{

    @NotNull
    @ManyToOne
    protected TurmaIngresso turma;

    @Column(nullable = false)
    @Enumerated
    private StatusAluno statusAluno = StatusAluno.VERIFICAR;

    @Enumerated(EnumType.STRING)
    private PosGraduacao posGraduacao;

    @Column(columnDefinition = "DATE")
    private LocalDate dataDefesa;

    @Column(columnDefinition = "DATE")
    private LocalDate prazoEstendido;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "aluno")
    private Candidato candidato;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "aluno")
    private PesquisaAluno pesquisaAluno;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "aluno")
    private RelatorioAcademico relatorioAcademico;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "aluno")
    private TeseDissertacao teseDissertacao;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "aluno")
    private PropostaOperacional propostaOperacional;

    private boolean teseInserida = false;

    @OrderBy
    @OneToMany(mappedBy = "aluno")
    private List<MatriculaEmDisciplina> matriculasEmDisciplinas;

    @OrderBy(value = "prioridade, ordem")
    @OneToMany(mappedBy = "aluno", fetch = FetchType.LAZY)
    private List<OdsaInteressada> odsaInteressadas;

    @Column
    private String omAtual;

    @Column
    private String bcaDesignacao;

    @Column
    private String bcaClassificacao;

    @Column
    private String omOrigem;

    @Column
    private String odsaOrigem;

    @Column
    private String omDestino;

    @Column
    private String odsaDestino;
    @Override
    public String getEntityDescriptor() {
        return "Aluno " + user.getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }


    public void setTeseDissertacao(TeseDissertacao teseDissertacao) {
        if (teseDissertacao != null) teseDissertacao.setAluno(this);
        this.teseDissertacao = teseDissertacao;
    }

    public List<OdsaInteressada> getOdsaInteressadas() {
        if (odsaInteressadas == null) odsaInteressadas = new ArrayList<>();
        return odsaInteressadas;
    }

    public PropostaOperacional getPropostaOperacional() {

        if (propostaOperacional == null){
            propostaOperacional = new PropostaOperacional(this);
        }

        return propostaOperacional;
    }

    public PesquisaAluno getPesquisaAluno() {
        if (pesquisaAluno == null) {
            pesquisaAluno = new PesquisaAluno();
            pesquisaAluno.setAluno(this);
        }
        return pesquisaAluno;
    }

    public RelatorioAcademico getRelatorioAcademico() {
        if (relatorioAcademico == null) {
            relatorioAcademico = new RelatorioAcademico();
            relatorioAcademico.setAluno(this);
        }
        return relatorioAcademico;
    }

    public void setPropostaOperacional(PropostaOperacional proposta) {
        this.propostaOperacional = proposta;
        if (propostaOperacional != null) propostaOperacional.setAluno(this);
    }

    public int getAnoTermino(){
        if (prazoEstendido != null) return prazoEstendido.getYear();
        int at = getPosGraduacao() == PosGraduacao.MESTRADO ? 1 : 3;
        return getTurma().getAnoDeIngresso() + at;
    }

    public int getAnoTerminoSemExtensao(){
        int at = getPosGraduacao() == PosGraduacao.MESTRADO ? 1 : 3;
        return getTurma().getAnoDeIngresso() + at;
    }

    public boolean isUltimoAno(){
        return LocalDate.now().getYear() >= getAnoTermino();
    }

    public boolean isUltimoAno(int ano){
        return ano >= getAnoTermino();
    }

    public boolean isUltimoAnoSemExtensao(int ano){
        return ano == getAnoTerminoSemExtensao();
    }

    public String getNomeOrientador(){
        return getPesquisaAluno().getOrientador() != null ? getPesquisaAluno().getOrientador().getUser().getNomeQualificado() : "";
    }

    public String getNomeCoorientador(){
        return getPesquisaAluno().getCoorientador() != null ? getPesquisaAluno().getCoorientador().getUser().getNomeQualificado() : "";
    }

    public String getTemaAprovadoIngresso(){
        if (getCandidato() != null){
            return getCandidato().getPropostaPesquisa().getTema();
        }
        return "PROPOSTA PREMILINAR NÃO CADASTRADA";
    }

    public void setTemaAprovadoIngresso(String tema){
        if (getCandidato() != null){
            getCandidato().getPropostaPesquisa().setTema(tema);
        }
    }

    public String getTituloAprovadoIngresso(){
        if (getCandidato() != null){
            return getCandidato().getPropostaPesquisa().getTitulo();
        }
        return "PROPOSTA PREMILINAR NÃO CADASTRADA";
    }

    public void setTituloAprovadoIngresso(String titulo){
        if (getCandidato() != null){
            getCandidato().getPropostaPesquisa().setTitulo(titulo);
        }
    }

    @Override
    public int compareTo(Aluno o) {
        int dif = getUser().getForcaSingular().getPrioridade() - o.getUser().getForcaSingular().getPrioridade();
        return dif == 0 ? getUser().compareTo(o.getUser()) : dif;
    }

    public String getTitulo(){
        return getPesquisaAluno().getTitulo() != null && !getPesquisaAluno().getTitulo().isBlank() ? getPesquisaAluno().getTitulo() : "NÃO INFORMADO";
    }

    public void setTitulo(String titulo){
        if (titulo == null || titulo.isBlank()) titulo = "DESCONHECIDO";
        getPesquisaAluno().setTitulo(titulo);
        getTeseDissertacao().setTitulo(titulo);
    }

    public void validarConflitoTemporalDisciplina(MatriculaEmDisciplina disciplinaCursada){
        for(MatriculaEmDisciplina dc : getMatriculasEmDisciplinas()){
            if (dc.getDisciplina().equals(disciplinaCursada.getDisciplina()) && !dc.equals(disciplinaCursada)){
                if (disciplinaCursada.getInicio().compareTo(dc.getTermino()) < 0 &&
                        disciplinaCursada.getTermino().compareTo(dc.getInicio()) > 0){
                    throw new MentorValidationException("Não é possível cursar uma mesma disciplina em um mesmo período de tempo. Não é permitido sobreposição de períodos.");
                }
            }
        }
    }

}
