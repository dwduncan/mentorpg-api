package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.*;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.PreviousValueMessage;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.RecordFieldName;
import mil.decea.mentorpgapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Table(name = "areadepesquisa", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AreaDePesquisa extends SequenceIdEntity implements Comparable<AreaDePesquisa> {

    @NotNull(message = "Obrigatório informar a sigla")
    @Column(nullable = false)
    private String sigla;
    @Column(columnDefinition = "TEXT")
    private String nomeExtenso;

    @Column(columnDefinition = "TEXT")
    private String conceito;

    @OrderBy("nome asc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "areaDePesquisa")
    private List<LinhaDePesquisa> linhasDePesquisa;

    private boolean encerrado;

    @ManyToOne(fetch = FetchType.LAZY)
    private User coordenadorDaArea;

    @Embedded
    @RecordFieldName("imagemAreaDePesquisa")
    @PreviousValueMessage("Imagem alterada")
    private EmbeddedImage imagemAreaDePesquisa;

    public List<LinhaDePesquisa> getLinhasDePesquisa() {
        if (linhasDePesquisa == null) linhasDePesquisa = new ArrayList<>();
        return linhasDePesquisa;
    }

    public String getNomeOrientador(){
        return getCoordenadorDaArea() != null ? getCoordenadorDaArea().getNomeQualificado() : "";
    }

    /**
     * para não ter que excluir
     * @return
     */
    public boolean isEncerrado() {
        return encerrado;
    }

    public void setEncerrado(boolean encerrado) {
        this.encerrado = encerrado;
    }


    public String getSiglaENome(){
        return "(" + sigla + ") " + nomeExtenso;
    }

    public User getCoordenadorDaArea() {
        return coordenadorDaArea;
    }

    public void setCoordenadorDaArea(User coordenadorDaArea) {
        this.coordenadorDaArea = coordenadorDaArea;
    }

    public EmbeddedImage getImageAreaDePesquisa() {
        if (imagemAreaDePesquisa == null){
            imagemAreaDePesquisa = new EmbeddedImage();
        }
        return imagemAreaDePesquisa;
    }

    public boolean isImagemPresente(){
        return getImageAreaDePesquisa().getNomeArquivo() != null && !getImageAreaDePesquisa().getNomeArquivo().isEmpty();
    }

    @Override
    public int compareTo(AreaDePesquisa o) {
        return getSigla().compareToIgnoreCase(o.getSigla());
    }

    @Override
    public String getEntityDescriptor() {
        return "(" + sigla + ") " + nomeExtenso;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }

}
