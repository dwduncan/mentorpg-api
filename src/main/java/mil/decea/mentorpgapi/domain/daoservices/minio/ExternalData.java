package mil.decea.mentorpgapi.domain.daoservices.minio;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.changewatch.ChangeWatcher;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;

import java.io.Serializable;

public interface ExternalData<T extends ExternalData, Z extends IdentifiedRecord> extends ChangeWatcher<ExternalData<T, Z>,Z> {
    String getFormato();

    String getNomeArquivo();

    java.time.LocalDateTime getDataHoraUpload();

    String getArquivoUrl();

    String getBase64Data();

    String getPreviousFileName();

    void setFormato(String formato);

    void setNomeArquivo(String nomeArquivo);

    void setDataHoraUpload(java.time.LocalDateTime dataHoraUpload);

    void setArquivoUrl(String arquivoUrl);

    void setBase64Data(String base64Data);

    @NotForRecordField
    default String[] getFileNamePrefixSuffix(){
        if (getNomeArquivo() != null && getNomeArquivo().contains(".")){
            return getNomeArquivo().split("\\.");
        }
        return null;
    };

    void copyFields(T previousEntity);


}
