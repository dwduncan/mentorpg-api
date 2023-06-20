package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractExternalData<T extends ExternalData> implements ExternalData<T> {

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



}
