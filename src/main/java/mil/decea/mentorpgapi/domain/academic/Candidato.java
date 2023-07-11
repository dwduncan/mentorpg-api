package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.administrativo.AtuacaoProfissional;
import mil.decea.mentorpgapi.domain.administrativo.IdiomaAdapter;
import mil.decea.mentorpgapi.domain.administrativo.OcorrenciaCandidato;
import mil.decea.mentorpgapi.domain.administrativo.UnarioWrapper;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import mil.decea.mentorpgapi.domain.planejamentoestrategico.PropostaOperacional;
import mil.decea.mentorpgapi.domain.user.EstadoCivil;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "candidatos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Candidato extends Pesquisador implements Comparable<Candidato>{


    @ManyToOne(fetch = FetchType.LAZY)
    private Aluno aluno;

    @Override
    public String getEntityDescriptor() {
        return "Candidato " + user.getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }

    @OneToOne(fetch = FetchType.LAZY)
    private PropostaPremilinar propostaPesquisa;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "candidato")
    private PropostaOperacional propostaOperacional;

    @ManyToOne(fetch = FetchType.LAZY)
    private TurmaIngresso turma;

    @Enumerated(EnumType.STRING)
    private PosGraduacao posGraduacao;
    /*
        @OrderColumn(name = "ordem")*/
    @OrderBy("prioridade ASC")
    @OneToMany(mappedBy = "candidato")
    private List<ConsultaOrientador> orientadoresConsultados = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dataHoraCienteRegulamentacao;

    @OneToMany(mappedBy = "candidato")
    private List<MatriculaEmDisciplina> disciplinaJaCursadas = new ArrayList<>();

    @OneToMany(mappedBy = "candidato")
    private List<AtuacaoProfissional> atuacoesProfissionais = new ArrayList<>();

    @OneToMany(mappedBy = "candidato")
    private Set<OcorrenciaCandidato> despachos = new HashSet<>();

    @Column
    private String odsa;
    @Column
    private String omOrigem;

    @Column(columnDefinition = "TEXT")
    private String pendenciasACorrigir;

    @Column
    private String chaveAutenticacao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao = StatusSolicitacao.NOVO;

    @Enumerated(EnumType.STRING)
    private StatusCandidato statusCandidato = StatusCandidato.PREENCHIMENTO;


    private Integer idiomaIngles;
    private Integer idiomaEspanhol;
    private Integer idiomaFrances;
    private Integer idiomaOutro;

    private String outroIdioma;

    @Column
    private String nomePai;
    @Column
    private String nomeMae;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;
    @Column(columnDefinition = "DATE")
    private LocalDate dataExpedicaoRG;

    @Column
    private String maiorTitulacaoObtida;
    @Column
    private String paisMaiorTitulacaoObtida;
    @Column
    private String instituicaoMaiorTitulacaoObtida;
    @Column
    private String areaMaiorTitulacaoObtida;
    private String anoMaiorTitulacaoObtida;

    private boolean maiorTitulacaoMesmaGraduacao;

    private String titulacaoObtida;
    private String paisGraduacao;
    private String instituicaoGraduacao;
    private String areaGraduacao;
    private String anoGraduacao;
    @Column(columnDefinition = "TEXT")
    private String informacoesAdicionaisRelevantes;

    private Boolean fatoresCondicionantesAtendidos;
    private Boolean naoExibirNoModoApresentacao;
    private Boolean aprovadoNoConselho;

    @Column(columnDefinition = "TEXT")
    private String fatoresCondicionantes;


    public StatusCandidato getStatusCandidato() {
        if (statusCandidato == null) statusCandidato = StatusCandidato.PREENCHIMENTO;
        return statusCandidato;
    }


    public PropostaOperacional getPropostaOperacional() {

        if (propostaOperacional == null){
            propostaOperacional = new PropostaOperacional(this);
        }

        return propostaOperacional;
    }

    public void setPrograma(ProgramaPosGraduacao programa){
        if (programa != null
                && programa.getFatoresCondicionantes() != null
                && getFatoresCondicionantes().isBlank()){
            fatoresCondicionantes = programa.getFatoresCondicionantes();
        }
        getPropostaPesquisa().setPrograma(programa);
    }

    public void migrarInscricaoAntiga(Candidato antigo){
        setLattes(antigo.getLattes());
        setOrcid(antigo.getOrcid());
        setIdiomaIngles(antigo.getIdiomaIngles());
        setIdiomaEspanhol(antigo.getIdiomaEspanhol());
        setIdiomaFrances(antigo.getIdiomaFrances());
        setIdiomaOutro(antigo.getIdiomaOutro());
        setOutroIdioma(antigo.getOutroIdioma());
        setNomePai(antigo.getNomePai());
        setNomeMae(antigo.getNomeMae());
        setEstadoCivil(EstadoCivil.valueOf(antigo.getEstadoCivil().name()));
        setDataExpedicaoRG(antigo.getDataExpedicaoRG());
        setTitulacaoObtida(antigo.getTitulacaoObtida());
        setPaisGraduacao(antigo.getPaisGraduacao());
        setInstituicaoGraduacao(antigo.getInstituicaoGraduacao());
        setAreaGraduacao(antigo.getAreaGraduacao());
        setAnoGraduacao(antigo.getAnoGraduacao());
        setInformacoesAdicionaisRelevantes(antigo.getInformacoesAdicionaisRelevantes());
        disciplinaJaCursadas = antigo.getDisciplinaJaCursadas();
        atuacoesProfissionais = antigo.getAtuacoesProfissionais();
    }

    public void importarDadosAluno(Aluno _aluno){

        setLattes(_aluno.getLattes());
        setOrcid(_aluno.getOrcid());
        Candidato antigo = _aluno.getCandidato();
        if (antigo != null) {
            setIdiomaIngles(antigo.getIdiomaIngles());
            setIdiomaEspanhol(antigo.getIdiomaEspanhol());
            setIdiomaFrances(antigo.getIdiomaFrances());
            setIdiomaOutro(antigo.getIdiomaOutro());
            setOutroIdioma(antigo.getOutroIdioma());
            setNomePai(antigo.getNomePai());
            setNomeMae(antigo.getNomeMae());
            setEstadoCivil(EstadoCivil.valueOf(antigo.getEstadoCivil().name()));
            setDataExpedicaoRG(antigo.getDataExpedicaoRG());
            setTitulacaoObtida(antigo.getTitulacaoObtida());
            setPaisGraduacao(antigo.getPaisGraduacao());
            setInstituicaoGraduacao(antigo.getInstituicaoGraduacao());
            setAreaGraduacao(antigo.getAreaGraduacao());
            setAnoGraduacao(antigo.getAnoGraduacao());
            setInformacoesAdicionaisRelevantes(antigo.getInformacoesAdicionaisRelevantes());
            disciplinaJaCursadas = antigo.getDisciplinaJaCursadas();
            atuacoesProfissionais = antigo.getAtuacoesProfissionais();
        }
    }

    public Aluno criarCadastrarDeAluno(){

        if (getPropostaPesquisa().getOrientador() == null){
            throw new IllegalStateException("Somente após atribuir um orientador, um candidato poderá se tornar aluno.");
        }

        if (aluno == null){
            aluno = new Aluno();
            aluno.getPesquisaAluno().setAreaConcentracao(getPropostaPesquisa().getAreaConcentracao());
            aluno.getPesquisaAluno().setAreaPesquisa(getPropostaPesquisa().getAreaPesquisa());
            aluno.setUser(getUser());
            aluno.setCandidato(this);
            aluno.setStatusAluno(StatusAluno.EM_CURSO);
            aluno.getPesquisaAluno().setCoorientador(getPropostaPesquisa().getCoorientador());
            aluno.getPesquisaAluno().setLinhaDePesquisa(getPropostaPesquisa().getLinhaDePesquisa());
            aluno.setOdsaOrigem(getOdsa());
            aluno.setOmOrigem(getOmOrigem());
            aluno.setOrcid(this.getOrcid());
            aluno.getPesquisaAluno().setOrientador(getPropostaPesquisa().getOrientador());
            aluno.getPesquisaAluno().setPrograma(getPropostaPesquisa().getPrograma());
            aluno.setPosGraduacao(getPosGraduacao());
            aluno.setTurma(getTurma());
            aluno.setLattes(getLattes());
            //aluno.setPropostaOperacional(getPropostaOperacional().clone());
        }else{
            throw new IllegalStateException("Este candidato já foi definido como aluno. Não é permitido defini-lo novamente.");
        }
        return aluno;
    }


    public String getTodasPendencias(){
        StringBuilder pends = new StringBuilder(getPendenciasACorrigir().trim());
        for(UserDocument doc : getUser().getDocuments()){
            if (doc.getStatusDocumento() == StatusDoc.RECUSADO){
                String sinal = pends.toString().trim().endsWith(".") || pends.toString().trim().endsWith(";")
                        || pends.toString().trim().endsWith("!") || pends.toString().trim().equals("") ? " " : "; ";
                pends.append(sinal).append(doc.getTipoDocumentacao().getTipo()).append(" foi recusado pelo seguinte motivo: ").append(doc.getMotivoRecusa());
            }
        }
        return pends.toString().trim();
    }


    public IdiomaAdapter getIngles(){
        return new IdiomaAdapter(new UnarioWrapper() {
            @Override
            public String getNomeCampo() {
                return "Inglês";
            }

            @Override
            public Integer getValorUnario() {
                return getIdiomaIngles();
            }

            @Override
            public void setValorUnario(Integer valor) {
                setIdiomaIngles(valor);
            }
        });
    }

    public IdiomaAdapter getEspanhol(){
        return new IdiomaAdapter(new UnarioWrapper() {
            @Override
            public String getNomeCampo() {
                return "Espanhol";
            }

            @Override
            public Integer getValorUnario() {
                return getIdiomaEspanhol();
            }

            @Override
            public void setValorUnario(Integer valor) {
                setIdiomaEspanhol(valor);
            }
        });
    }

    public IdiomaAdapter getFrances(){
        return new IdiomaAdapter(new UnarioWrapper() {
            @Override
            public String getNomeCampo() {
                return "Francês";
            }

            @Override
            public Integer getValorUnario() {
                return getIdiomaFrances();
            }

            @Override
            public void setValorUnario(Integer valor) {
                setIdiomaFrances(valor);
            }
        });
    }

    public IdiomaAdapter getOutro(){
        return new IdiomaAdapter(new UnarioWrapper() {
            @Override
            public String getNomeCampo() {
                return getOutroIdioma() !=null && !getOutroIdioma().isBlank() ? getOutroIdioma() : "Outro";
            }

            @Override
            public Integer getValorUnario() {
                return getIdiomaOutro();
            }

            @Override
            public void setValorUnario(Integer valor) {
                setIdiomaOutro(valor);
            }
        });
    }

    public ConsultaOrientador getConsultaPorId(Long id){
        return id != null ? getOrientadoresConsultados().stream().filter(c->c.getId().equals(id)).findAny().orElse(null) : null;
    }

    public boolean isPossuiPendencias(){
        return !getPendenciasACorrigir().isBlank() || isPossuiDocumentoRecusado();
    }

    public boolean isPossuiDocumentoRecusado(){
        return getUser().getDocuments().stream().anyMatch(d->d.getStatusDocumento() == StatusDoc.RECUSADO);
    }

    public boolean isPossuiAlgumDocumentoNaoAprovado(){
        return getUser().getDocuments().stream().anyMatch(d->d.getStatusDocumento() != StatusDoc.APROVADO);
    }

    public void preValidarStatusInscricao(){

        if (getStatusSolicitacao() == StatusSolicitacao.CONFORMIDADE){
            if (!getPendenciasACorrigir().isBlank()){
                throw new MentorValidationException("Verifique o real estado de conformidade da Ficha de Inscrição, pois foi informado que ela " +
                        "está em conformidade enquanto ainda há pendências a corrigir");
            }

            if (isPossuiDocumentoRecusado()){
                throw new MentorValidationException("Verifique o real estado de conformidade da Ficha de Inscrição, pois foi informado que ela " +
                        "está em conformidade enquanto ainda há documentos que foram recusados.");
            }else if (isPossuiAlgumDocumentoNaoAprovado()){
                throw new MentorValidationException("Verifique o real estado de conformidade da Ficha de Inscrição, pois foi informado que ela " +
                        "está em conformidade enquanto ainda há documentos que não foram aprovados.");
            }
        }

        if (getStatusSolicitacao() == StatusSolicitacao.COM_PENDENCIAS){
            if (!isPossuiDocumentoRecusado() && getPendenciasACorrigir().isBlank()) {
                throw new MentorValidationException("Verifique o real estado de conformidade da Ficha de Inscrição, pois foi informado que ela " +
                        "está com pendências porém não há documentos recusados nem foi informado pendências a corrigir");
            }
        }

    }

    public ConsultaOrientador getConsultaAoOrientador(Professor professor){
        return getOrientadoresConsultados().stream().filter(c->c.getOrientador().equals(professor)).findAny().orElse(null);
    }

    public ConsultaOrientador criarConsultaOrientador(Professor professor){

        if (professor != null){
            ConsultaOrientador consultaOrientador = getConsultaAoOrientador(professor);
            if (consultaOrientador == null){
                consultaOrientador = new ConsultaOrientador();
                consultaOrientador.setOrientador(professor);
                consultaOrientador.setCandidato(this);
                consultaOrientador.setOrdem(getOrientadoresConsultados().size());
                getOrientadoresConsultados().add(consultaOrientador);
                return consultaOrientador;
            }
        }
        return null;
    }

    @Override
    public int compareTo(@NotNull Candidato candidato) {
        if (getUser().getForcaSingular() == candidato.getUser().getForcaSingular()){
            return getUser().compareTo(candidato.getUser());
        }
        return getUser().getForcaSingular().getPrioridade() - candidato.getUser().getForcaSingular().getPrioridade();
    }
}
