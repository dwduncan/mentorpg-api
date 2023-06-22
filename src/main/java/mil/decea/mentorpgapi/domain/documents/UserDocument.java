package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.IgnoreTrackChange;
import mil.decea.mentorpgapi.domain.changewatch.TrackChange;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.MinioStorage;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Table(name = "userdocs", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
@TrackChange(recordClass = UserDocumentRecord.class)
public class UserDocument extends ExternalDataEntity<UserDocument, UserDocumentRecord> implements MinioStorage<UserDocument> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @NotForRecordField
    @IgnoreTrackChange
    private User user;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ObjectForRecordField
    private DocumentType tipoDocumentacao;
    @Enumerated(EnumType.ORDINAL)
    private StatusDoc statusDocumento = StatusDoc.NOVO;
    @Column(columnDefinition = "TEXT")
    private String motivoRecusa;
    private boolean obrigatorio;
    private Long idExigencia;

    @NotForRecordField
    public void updateValues(UserDocumentRecord rec) {
        this.setTipoDocumentacao(new DocumentType(rec.tipoDocumentacao()));
        this.setPreviousFileName(rec.previousFileName());
        this.setMotivoRecusa(rec.motivoRecusa());
        this.setIdExigencia(rec.idExigencia());
        this.setObrigatorio(rec.obrigatorio());
        this.setStatusDocumento(rec.statusDocumento());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setFormato(rec.formato());
        this.setTamanho(rec.tamanho());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
    }
    @NotForRecordField
    public UserDocument(UserDocumentRecord rec) {
        updateValues(rec);
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
        return user.getId() + "/type" + getTipoDocumentacao().getId() + "/" + previousFileName;
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
        this.setDataHoraUpload(previousEntity.getDataHoraUpload());
        this.setBase64Data(previousEntity.getBase64Data());
        this.setArquivoUrl(previousEntity.getArquivoUrl());
    }

    @Override
    public String getEntityDescriptor() {
        return user.getEntityDescriptor() + " - " + getTipoDocumentacao().getTipo()
                + " - " + getNomeArquivo();
    }
}
