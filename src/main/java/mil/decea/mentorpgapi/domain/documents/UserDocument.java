package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.AppendFieldLabelDescriptor;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.IgnoreTrackChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedElementCollection;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.util.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.domain.user.User;

@Table(name = "userdocs", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
@TrackOnlySelectedFields({"motivoRecusa", "statusDocumento", "nomeArquivo"})
public class UserDocument extends ExternalDataEntity implements TrackedElementCollection<UserDocument> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @NotForRecordField
    @IgnoreTrackChange
    private User user;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ObjectForRecordField
    @IgnoreTrackChange
    private DocumentType tipoDocumentacao;
    @Enumerated(EnumType.ORDINAL)
    @AppendFieldLabelDescriptor(value = "getFieldLabelDescriptor",valueIsMethod = true)
    private StatusDoc statusDocumento = StatusDoc.NOVO;
    @Column(columnDefinition = "TEXT")
    @AppendFieldLabelDescriptor(value = "getFieldLabelDescriptor",valueIsMethod = true)
    private String motivoRecusa;
    private boolean obrigatorio;
    private Long idExigencia;


    @NotForRecordField
    public UserDocument(User user) {
        this.user = user;
    }

    @MethodDefaultValue(fieldName = "userId",defaultValue = "obj.getUser().getId()")
    public Long getUserId(){
        return getUser().getId();
    }

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"userdocuments\"")
    public String getBucket() {
        return "userdocuments";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "\"usr_\" + obj.getUser().getId() +\"/type_\"+obj.getTipoDocumentacao().getId()+\"/\"+obj.getNomeArquivo()")
    public String getStorageDestinationPath() {
        return "usr_" + getUser().getId() + "/type_" + getTipoDocumentacao().getId() + "/" + getNomeArquivo();
    }
    @Override
    @NotForRecordField
    public String getPreviousStorageDestinationPath() {
        return user.getId() + "/type_" + getTipoDocumentacao().getId() + "/" + previousFileName;
    }
    @Override
    public UserDocument getExternalData() {
        return this;
    }


    @Override
    public void copyFields(UserDocument previousEntity) {

        this.setTipoDocumentacao(previousEntity.getTipoDocumentacao());
        this.setPreviousFileName(previousEntity.getPreviousFileName());
        this.setMotivoRecusa(previousEntity.getMotivoRecusa());
        this.setIdExigencia(previousEntity.getIdExigencia());
        this.setObrigatorio(previousEntity.isObrigatorio());
        this.setStatusDocumento(previousEntity.getStatusDocumento());
        this.setId(previousEntity.getId());
        this.setAtivo(previousEntity.isAtivo());
        this.setNomeArquivo(previousEntity.getNomeArquivo());
        this.setFormato(previousEntity.getFormato());
        this.setTamanho(previousEntity.getTamanho());
        this.setLastUpdate(previousEntity.getLastUpdate());
        this.setBase64Data(previousEntity.getBase64Data());
        this.setArquivoUrl(previousEntity.getArquivoUrl());
    }

    @Override
    public String getEntityDescriptor() {
        return " arquivo: " + getNomeArquivo() + " tipo: " + getTipoDocumentacao().getTipo();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }

    public String getFieldLabelDescriptor() {
        return getTipoDocumentacao().getTipo();
    }

}
