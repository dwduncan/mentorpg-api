package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.ExternalDataRecord;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;
import mil.decea.mentorpgapi.domain.user.UserImageRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractExternalData implements ExternalData {

    @Column(columnDefinition = "TEXT")
    protected String formato;
    @Column(columnDefinition = "TEXT")
    protected String nomeArquivo;
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime lastUpdate;
    protected long tamanho;
    @Transient
    protected String arquivoUrl = "";
    @Transient
    protected String base64Data;
    @Transient
    protected String previousFileName;


    @Override
    public void setNomeArquivo(String nomeArquivo) {
        if (previousFileName == null) previousFileName = getNomeArquivo();
        this.nomeArquivo = nomeArquivo;
    }

    @Override
    @NotForRecordField
    public void updateValues(ExternalDataRecord rec) {
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setLastUpdate(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setTamanho(rec.tamanho());
    }

//@MethodDefaultValue(fieldName = "bucket",defaultValue = "\"userdocuments\"")

}
