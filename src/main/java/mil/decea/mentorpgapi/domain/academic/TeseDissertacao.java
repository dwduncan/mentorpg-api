package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;

import java.util.ArrayList;
import java.util.List;


@Table(name = "tesesdissertacoes", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeseDissertacao extends AbstractPublicacao<TeseDissertacao>{

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    protected Aluno aluno;

    @OneToMany(fetch = FetchType.LAZY)
    protected List<Autoria<TeseDissertacao>> autorias;

    protected Boolean reservada = false;

    public TeseDissertacao(Aluno aluno) {
        this.aluno = aluno;
        String t = aluno.getPesquisaAluno().getTitulo();
        setTitulo(t != null && !t.isBlank() ? t : "INFORMAR TITULO");
        setTipoPublicacao(aluno.getPosGraduacao() == PosGraduacao.MESTRADO ? TipoPublicacao.DISSERTACAO : TipoPublicacao.TESE);
    }

    public TeseDissertacao(Long idTeseDissertacao, String titulo, String palavrasChaves) {
        setId(idTeseDissertacao);
        setTitulo(titulo);
        setPalavrasChave(palavrasChaves);
    }

    @PrePersist
    @PreUpdate
    public void preSalvar(){
        if (!isPossuiTitulo()) setTitulo("DESCONHECIDO");
        if (getTipoPublicacao() == null) setTipoPublicacao(aluno.getPosGraduacao() == PosGraduacao.MESTRADO ? TipoPublicacao.DISSERTACAO : TipoPublicacao.TESE);
        if (isReservada()) {
            setFormato(null);
            setNomeArquivo(null);
        }
        buildAutoriasList();
    }

    public void buildAutoriasList(){

        if (autorias == null) autorias = new ArrayList<>();
        else autorias.clear();

        autorias.add(new Autoria<>(aluno.getUser(), this, true));
        if (aluno.getPesquisaAluno().getCoorientador() != null){
            autorias.add(new Autoria<>(aluno.getPesquisaAluno().getCoorientador().getUser(), this));
        }
        if (aluno.getPesquisaAluno().getOrientador() != null){
            autorias.add(new Autoria<>(aluno.getPesquisaAluno().getOrientador().getUser(), this));
        }
    }

    @Override
    public List<Autoria<TeseDissertacao>> getAutores() {
        if (autorias == null || autorias.isEmpty()) buildAutoriasList();
        return autorias;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Boolean getReservada() {
        if (reservada == null) reservada = false;
        return reservada;
    }

    public Boolean isReservada() {
        if (reservada == null) reservada = false;
        return reservada;
    }

    public void setReservada(Boolean reservada) {
        if (reservada == null) reservada = false;
        this.reservada = reservada;
    }

    @Override
    public String getRotuloNomeDoArquivo() {
        if (isReservada()) return "DOCUMENTO RESERVADO - NÃO INSERIDO";
        return super.getRotuloNomeDoArquivo();
    }

    @Override
    public String getEntityDescriptor() {
        return getAluno().getUser() + " " + getTipoPublicacao().name();
    }

    @Override
    public TrackedEntity getParentObject() {
        return getAluno();
    }

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"tesesdissertacoes\"")
    public String getBucket() {
        return "tesesdissertacoes";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "\"aluno_\" + obj.getAluno().getId() +\"/\"+obj.getTipoDocumentacao().name()+\"/\"+obj.getNomeArquivo()")
    public String getStorageDestinationPath() {
        return "aluno_" + getAluno().getId() + "/" + getTipoPublicacao().name() + "/" + getNomeArquivo();
    }


    @Override
    public TeseDissertacao getExternalData() {
        return this;
    }

    @NotForRecordField
    @Override
    public String getPreviousStorageDestinationPath() {
        return "aluno_" + getAluno().getId() + "/" + getTipoPublicacao().name() + "/" + previousFileName;
    }
}
