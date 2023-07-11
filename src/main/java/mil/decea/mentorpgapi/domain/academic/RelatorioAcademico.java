package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.util.ArrayList;
import java.util.List;

@Table(name = "relatoriosacademicos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RelatorioAcademico extends SequenceIdEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Aluno aluno;

    @OrderBy
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "relatorioAcademico")
    private List<Bibliografia> bibliografias ;

    @OrderBy
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "relatorioAcademico")
    private List<CumprimentoExigencia> cumprimentoExigencias;

    @OrderBy
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "relatorio")
    private List<OrientacaoAluno> orientacoesAluno;

    @Column(columnDefinition = "TEXT")
    private String contextualizacao;
    private boolean contextualizacaoAprovada;

    @Column(columnDefinition = "TEXT")
    private String qualProblema;
    private boolean qualProblemaAprovado;

    @Column(columnDefinition = "TEXT")
    private String porqueEhProblema;
    private boolean porqueEhProblemaAprovado;

    @Column(columnDefinition = "TEXT")
    private String oQueFazer;
    private boolean oqueFazerAprovado;

    @Column(columnDefinition = "TEXT")
    private String contribuicaoOperacional;
    private boolean contribuicaoOperacionalAprovado;

    @Column(columnDefinition = "TEXT")
    private String contribuicaoAcademica;
    private boolean contribuicaoAcademicaAprovado;

    public List<Bibliografia> getBibliografias() {
        if (bibliografias == null) bibliografias = new ArrayList<>();
        return bibliografias;
    }

    @Override
    public String getEntityDescriptor() {
        return null;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }

    public void adicionarCumprimentoExigencia(CumprimentoExigencia cumprimentoExigencia){

        if (cumprimentoExigencia.getCadastroExigencia() == null) throw new NullPointerException("O objeto da classe CumprimentoExigencia não pode ter o CadastroExigencias null!!!");

        cumprimentoExigencia.setRelatorioAcademico(this);
        getCumprimentoExigencias().add(cumprimentoExigencia);
    }

    public List<CumprimentoExigencia> getCumprimentoExigencias() {
        if (cumprimentoExigencias == null) cumprimentoExigencias = new ArrayList<>();
        return cumprimentoExigencias;
    }

    public List<OrientacaoAluno> getOrientacoesAluno() {
        if (orientacoesAluno == null) orientacoesAluno = new ArrayList<>();
        return orientacoesAluno;
    }

    public void adicionarOrientacaoAluno(OrientacaoAluno o){
        o.setRelatorio(this);
        getOrientacoesAluno().add(o);
    }

}
