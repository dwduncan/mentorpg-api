package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.NotForRecordField;
import mil.decea.mentorpgapi.domain.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Table(name = "userdocs", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserDocument extends ExternalDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
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
    public void setUserDocument(UserDocumentRecord rec) {
        if (this.id == null) this.setId(rec.id());
        this.setTipoDocumentacao(new DocumentType(rec.tipoDocumentacao()));
        this.setObrigatorio(rec.obrigatorio());
        this.setStatusDocumento(rec.statusDocumento());
        this.setMotivoRecusa(rec.motivoRecusa());
        this.setUser(rec.user());
        this.setIdExigencia(rec.idExigencia());
        this.setAtivo(rec.ativo());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setFormato(rec.formato());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setArquivoUrl(rec.arquivoUrl());
        this.setBase64Data(rec.base64Data());
    }
    @NotForRecordField
    public UserDocument(UserDocumentRecord rec) {
        setUserDocument(rec);
    }


}
