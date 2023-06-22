package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;
import mil.decea.mentorpgapi.domain.daoservices.minio.MinioStorage;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractExternalData<T extends AbstractExternalData<T, Z>, Z extends IdentifiedRecord> implements ExternalData<T,Z> {

    @Column(columnDefinition = "TEXT")
    protected String formato;
    @Column(columnDefinition = "TEXT")
    protected String nomeArquivo;
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime dataHoraUpload;
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
    public AbstractExternalData<T,Z> getChangingObject() {
        return this;
    }

//@MethodDefaultValue(fieldName = "bucket",defaultValue = "\"userdocuments\"")

}
