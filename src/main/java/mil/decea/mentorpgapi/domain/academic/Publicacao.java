package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.user.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



@Table(name = "publicacoes", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Publicacao extends AbstractPublicacao<Publicacao>  {

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "publicacao", orphanRemoval = true)
    private List<Autoria<Publicacao>> autores;

    public String getCreditosFormatado(){
        return new DecimalFormat("0.00").format(getCreditos());
    }

    public void adicinarAutor(User autor){
        if (!getAutores().stream().map(Autoria::getUsuario).toList().contains(autor)){
            getAutores().add(new Autoria<>(autor,this));
        }
    }

    public void removerAutor(User autor){
        Autoria<Publicacao> a = new Autoria<Publicacao>(autor,this);
        getAutores().remove(a);
    }

    @Override
    public List<Autoria<Publicacao>> getAutores() {
        if (autores == null) autores = new ArrayList<>();
        return autores;
    }

    @Override
    public String getEntityDescriptor() {
        return getTipoPublicacao().name() + " " + getTitulo();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"publicacoes\"")
    public String getBucket() {
        return "publicacoes";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "getTipoPublicacao().name() +\"/\"+getId()+\"/publication.pdf\"")
    public String getStorageDestinationPath() {
        return getTipoPublicacao().name() + "/" + getId() + "/publication.pdf";
    }


    @Override
    public Publicacao getExternalData() {
        return this;
    }

    @NotForRecordField
    @Override
    public String getPreviousStorageDestinationPath() {
        return getStorageDestinationPath();
    }
}
