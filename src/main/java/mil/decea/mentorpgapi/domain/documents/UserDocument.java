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
    @NotForRecordField
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
        this.setTipoDocumentacao(new DocumentType(rec.tipoDocumentacao()));
        this.setMotivoRecusa(rec.motivoRecusa());
        this.setObrigatorio(rec.obrigatorio());
        this.setIdExigencia(rec.idExigencia());
        this.setStatusDocumento(rec.statusDocumento());
        if (this.id != null) this.setId(rec.id());
        this.setAtivo(rec.ativo());
        this.setBase64Data(rec.base64Data());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setTamanho(rec.tamanho());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setArquivoUrl(rec.arquivoUrl());
    }
    @NotForRecordField
    public UserDocument(UserDocumentRecord rec) {
        setUserDocument(rec);
    }


}
