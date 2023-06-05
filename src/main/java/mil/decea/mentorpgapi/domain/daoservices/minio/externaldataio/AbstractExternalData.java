package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;

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
    protected LocalDateTime dataHoraUpload;
    @Transient
    protected String arquivoUrl = "";
    @Transient
    protected String base64Data;



    /*@NotForRecordField
    public String getSuffix(){
        if (getNomeArquivo() != null && getNomeArquivo().contains(".")){
            return getNomeArquivo().split("\\.")[1];
        }
        return null;
    }*/

}
